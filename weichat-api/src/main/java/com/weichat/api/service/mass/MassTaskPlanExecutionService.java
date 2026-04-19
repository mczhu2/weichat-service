package com.weichat.api.service.mass;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.weichat.api.vo.request.mass.MassTaskCreateRequest;
import com.weichat.api.vo.request.mass.MassTaskPayload;
import com.weichat.api.vo.request.mass.MassTaskPlanCreateRequest;
import com.weichat.common.entity.MassTask;
import com.weichat.common.entity.MassTaskDetail;
import com.weichat.common.entity.MassTaskPlan;
import com.weichat.common.entity.WxFriendInfo;
import com.weichat.common.entity.WxGroupInfo;
import com.weichat.common.enums.MassTaskDetailSendStatusEnum;
import com.weichat.common.enums.MassTaskDetailSentFlagEnum;
import com.weichat.common.enums.MassTaskPlanScheduleTypeEnum;
import com.weichat.common.enums.MassTaskPlanStatusEnum;
import com.weichat.common.enums.MassTaskReceiverTypeEnum;
import com.weichat.common.service.MassTaskPlanService;
import com.weichat.common.service.MassTaskService;
import com.weichat.common.service.WxFriendInfoService;
import com.weichat.common.service.WxGroupInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 群发计划执行服务。
 * 负责三件事：
 * 1. 校验并保存计划；
 * 2. 根据周期规则计算 nextTriggerTime；
 * 3. 到点后将计划物化成 mass_task 与 mass_task_detail。
 */
@Slf4j
@Service
public class MassTaskPlanExecutionService {

    private static final int DEFAULT_RATE_PER_MINUTE = 10;
    private static final String DEFAULT_TIMEZONE = "Asia/Shanghai";
    private static final DateTimeFormatter SLOT_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Autowired
    private MassTaskPlanService massTaskPlanService;

    @Autowired
    private MassTaskService massTaskService;

    @Autowired
    private MassTaskRequestValidator massTaskRequestValidator;

    @Autowired
    private WxFriendInfoService wxFriendInfoService;

    @Autowired
    private WxGroupInfoService wxGroupInfoService;

    /**
     * 校验计划创建请求。
     * 这里在原有任务校验基础上，补充周期规则、时间窗口和窗口容量校验。
     */
    public List<String> validatePlanCreateRequest(MassTaskPlanCreateRequest request) {
        List<String> errors = new ArrayList<>(
                massTaskRequestValidator.validateCreateRequest(toMassTaskCreateRequest(request))
        );
        if (request == null) {
            return errors;
        }
        if (!MassTaskPlanScheduleTypeEnum.contains(request.getScheduleType())) {
            errors.add("scheduleType is invalid");
        }
        if (request.getEffectiveStartAt() == null) {
            errors.add("effectiveStartAt is required");
        }
        if (!StringUtils.hasText(request.getWindowStartTime())) {
            errors.add("windowStartTime is required");
        }
        if (!StringUtils.hasText(request.getWindowEndTime())) {
            errors.add("windowEndTime is required");
        }
        LocalTime windowStart = parseTime(request.getWindowStartTime(), "windowStartTime", errors);
        LocalTime windowEnd = parseTime(request.getWindowEndTime(), "windowEndTime", errors);
        if (windowStart != null && windowEnd != null && !windowStart.isBefore(windowEnd)) {
            errors.add("windowStartTime must be earlier than windowEndTime");
        }
        if (request.getEffectiveStartAt() != null
                && request.getEffectiveEndAt() != null
                && !request.getEffectiveEndAt().isAfter(request.getEffectiveStartAt())) {
            errors.add("effectiveEndAt must be later than effectiveStartAt");
        }

        int ratePerMinute = normalizeRatePerMinute(request.getRatePerMinute());
        if (ratePerMinute <= 0) {
            errors.add("ratePerMinute must be greater than 0");
        }

        validateScheduleRule(request, errors);
        if (!errors.isEmpty()) {
            return errors;
        }

        MassTaskPlan plan = buildPlanEntity(request);
        validateFirstExecutionWindow(plan, errors);
        if (!errors.isEmpty()) {
            return errors;
        }

        LocalDateTime firstTriggerTime = resolveNextTriggerTime(plan, plan.getEffectiveStartAt().minusSeconds(1));
        if (firstTriggerTime == null) {
            errors.add("no valid execution time can be calculated from the schedule and window");
            return errors;
        }

        int receiverCount = request.getReceiverIds() == null ? 0 : request.getReceiverIds().size();
        if (receiverCount > calculateWindowCapacity(firstTriggerTime, request.getWindowEndTime(), ratePerMinute)) {
            errors.add("receiverIds exceed the configured window capacity for the first execution");
        }
        return errors;
    }

    /**
     * 首个可执行日如果就是 effectiveStartAt 当天，则要求 effectiveStartAt 仍在当天发送窗口内。
     * 否则计划会被静默顺延到下一次窗口，用户容易误以为“已经到期但没有执行”。
     */
    private void validateFirstExecutionWindow(MassTaskPlan plan, List<String> errors) {
        if (plan == null || plan.getEffectiveStartAt() == null || errors == null || !errors.isEmpty()) {
            return;
        }
        LocalDate firstDate = plan.getEffectiveStartAt().toLocalDate();
        if (!matchesSchedule(plan, firstDate)) {
            return;
        }
        if (buildCandidateTriggerTime(plan, firstDate) == null) {
            errors.add("effectiveStartAt is later than windowEndTime on the first executable day");
        }
    }

    @Transactional
    public Long createPlan(MassTaskPlanCreateRequest request) {
        List<String> errors = validatePlanCreateRequest(request);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join("; ", errors));
        }

        MassTaskPlan plan = buildPlanEntity(request);
        plan.setNextTriggerTime(resolveNextTriggerTime(plan, plan.getEffectiveStartAt().minusSeconds(1)));
        plan.setPlanStatus(MassTaskPlanStatusEnum.ENABLED.getCode());
        return massTaskPlanService.createPlan(plan);
    }

    public List<MassTaskPlan> getPlanList(int offset, int limit) {
        return massTaskPlanService.getPlanList(offset, limit);
    }

    public MassTaskPlan getPlanById(Long planId) {
        return massTaskPlanService.getPlanById(planId);
    }

    public void updatePlanStatus(Long planId, Integer status) {
        if (!MassTaskPlanStatusEnum.contains(status)) {
            throw new IllegalArgumentException("plan status is invalid");
        }
        MassTaskPlan existing = massTaskPlanService.getPlanById(planId);
        if (existing == null) {
            throw new IllegalArgumentException("mass task plan not found");
        }
        MassTaskPlan update = new MassTaskPlan();
        update.setId(planId);
        update.setPlanStatus(status);
        massTaskPlanService.updateById(update);
    }

    public void processDuePlans(int limit) {
        List<MassTaskPlan> duePlans = massTaskPlanService.getDuePlans(limit);
        if (CollectionUtils.isEmpty(duePlans)) {
            return;
        }
        for (MassTaskPlan plan : duePlans) {
            try {
                materializePlan(plan);
            } catch (Exception e) {
                log.error("materialize mass task plan failed, planId={}", plan.getId(), e);
            }
        }
    }

    @Transactional
    protected void materializePlan(MassTaskPlan plan) {
        if (plan == null || !MassTaskPlanStatusEnum.ENABLED.getCode().equals(plan.getPlanStatus())) {
            return;
        }
        if (plan.getNextTriggerTime() == null) {
            return;
        }

        // scheduleSlot 用于防止同一计划槽位被重复生成执行任务。
        String scheduleSlot = SLOT_FORMATTER.format(plan.getNextTriggerTime());
        if (massTaskService.countByPlanIdAndScheduleSlot(plan.getId(), scheduleSlot) > 0) {
            advancePlan(plan);
            return;
        }

        MassTask task = buildTaskFromPlan(plan, scheduleSlot);
        List<MassTaskDetail> details = buildDetailsFromPlan(plan, task.getSendTime());
        massTaskService.createMassTask(task, details);
        advancePlan(plan);
    }

    private void advancePlan(MassTaskPlan plan) {
        LocalDateTime nextTriggerTime = resolveNextTriggerTime(plan, plan.getNextTriggerTime());
        MassTaskPlan update = new MassTaskPlan();
        update.setId(plan.getId());
        update.setLastTriggerTime(plan.getNextTriggerTime());
        if (nextTriggerTime == null) {
            update.setPlanStatus(MassTaskPlanStatusEnum.FINISHED.getCode());
        } else {
            update.setNextTriggerTime(nextTriggerTime);
        }
        massTaskPlanService.updateById(update);
    }

    private MassTask buildTaskFromPlan(MassTaskPlan plan, String scheduleSlot) {
        MassTask task = new MassTask();
        task.setTaskName(buildTaskName(plan, scheduleSlot));
        task.setTaskType(plan.getTaskType());
        task.setMsgType(plan.getMsgType());
        task.setPayloadVersion(StringUtils.hasText(plan.getPayloadVersion()) ? plan.getPayloadVersion() : "v1");
        task.setPayloadJson(plan.getPayloadJson());
        task.setTemplateId(plan.getTemplateId());
        task.setCreator(plan.getCreator());
        task.setRemark(plan.getRemark());
        task.setSendTime(plan.getNextTriggerTime());
        task.setPlanId(plan.getId());
        task.setScheduleSlot(scheduleSlot);

        MassTaskPayload payload = parsePayload(plan.getPayloadJson());
        if (payload != null && StringUtils.hasText(payload.getContent())) {
            task.setContent(payload.getContent());
        }
        return task;
    }

    private List<MassTaskDetail> buildDetailsFromPlan(MassTaskPlan plan, LocalDateTime sendStartTime) {
        List<Long> receiverIds = parseReceiverIds(plan.getReceiverIdsJson());
        List<MassTaskDetail> details = MassTaskReceiverTypeEnum.EXTERNAL_CONTACT.getCode().equals(plan.getReceiverType())
                ? buildUserDetails(receiverIds)
                : buildGroupDetails(receiverIds);
        // 将接收人均匀铺到当天窗口内，后续发送任务只按 planned_send_time 取数。
        applyPlannedSendTime(details, sendStartTime, plan.getWindowEndTime(), normalizeRatePerMinute(plan.getRatePerMinute()));
        return details;
    }

    private List<MassTaskDetail> buildUserDetails(List<Long> userIds) {
        List<MassTaskDetail> details = new ArrayList<>();
        for (Long userId : userIds) {
            MassTaskDetail detail = new MassTaskDetail();
            detail.setReceiverType(MassTaskReceiverTypeEnum.EXTERNAL_CONTACT.getCode());
            detail.setReceiverId(userId);
            detail.setIsSent(MassTaskDetailSentFlagEnum.UNSENT.getCode());
            detail.setSendStatus(MassTaskDetailSendStatusEnum.UNSUCCESSFUL.getCode());
            WxFriendInfo friendInfo = wxFriendInfoService.selectByPrimaryKey(userId);
            if (friendInfo == null) {
                detail.setReceiverName("未知用户");
            } else if (StringUtils.hasText(friendInfo.getRealname())) {
                detail.setReceiverName(friendInfo.getRealname());
            } else if (StringUtils.hasText(friendInfo.getNickname())) {
                detail.setReceiverName(friendInfo.getNickname());
            } else {
                detail.setReceiverName("未知用户");
            }
            details.add(detail);
        }
        return details;
    }

    private List<MassTaskDetail> buildGroupDetails(List<Long> groupIds) {
        List<MassTaskDetail> details = new ArrayList<>();
        for (Long groupId : groupIds) {
            MassTaskDetail detail = new MassTaskDetail();
            detail.setReceiverType(MassTaskReceiverTypeEnum.GROUP_CHAT.getCode());
            detail.setReceiverId(groupId);
            detail.setIsSent(MassTaskDetailSentFlagEnum.UNSENT.getCode());
            detail.setSendStatus(MassTaskDetailSendStatusEnum.UNSUCCESSFUL.getCode());
            WxGroupInfo groupInfo = wxGroupInfoService.selectByPrimaryKey(groupId);
            detail.setReceiverName(groupInfo == null ? "未知群聊" : groupInfo.getNickname());
            details.add(detail);
        }
        return details;
    }

    private void applyPlannedSendTime(List<MassTaskDetail> details,
                                      LocalDateTime firstSendTime,
                                      String windowEndTimeText,
                                      int ratePerMinute) {
        if (CollectionUtils.isEmpty(details)) {
            return;
        }
        LocalTime windowEnd = LocalTime.parse(windowEndTimeText);
        LocalDateTime windowEndAt = firstSendTime.toLocalDate().atTime(windowEnd);
        long totalMinutes = ChronoUnit.MINUTES.between(firstSendTime, windowEndAt);
        if (totalMinutes <= 0) {
            throw new IllegalArgumentException("invalid window capacity");
        }
        long capacity = totalMinutes * ratePerMinute;
        if (capacity < details.size()) {
            throw new IllegalArgumentException("window capacity is smaller than receiver count");
        }
        // 每分钟最多发送 ratePerMinute 条，因此第 i 条落在 i / ratePerMinute 分钟后。
        for (int i = 0; i < details.size(); i++) {
            details.get(i).setPlannedSendTime(firstSendTime.plusMinutes(i / ratePerMinute));
        }
    }

    private int calculateWindowCapacity(LocalDateTime firstSendTime, String windowEndTimeText, int ratePerMinute) {
        LocalTime windowEnd = LocalTime.parse(windowEndTimeText);
        LocalDateTime windowEndAt = firstSendTime.toLocalDate().atTime(windowEnd);
        long totalMinutes = ChronoUnit.MINUTES.between(firstSendTime, windowEndAt);
        if (totalMinutes <= 0) {
            return 0;
        }
        long capacity = totalMinutes * ratePerMinute;
        return capacity > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) capacity;
    }

    private MassTaskPlan buildPlanEntity(MassTaskPlanCreateRequest request) {
        MassTaskPlan plan = new MassTaskPlan();
        plan.setPlanName(StringUtils.hasText(request.getPlanName())
                ? request.getPlanName().trim()
                : "群发计划-" + System.currentTimeMillis());
        plan.setTaskType(request.getTaskType());
        plan.setReceiverType(request.getReceiverType());
        plan.setReceiverIdsJson(JSON.toJSONString(request.getReceiverIds()));
        plan.setMsgType(request.getMsgType());
        plan.setPayloadVersion("v1");
        plan.setPayloadJson(request.getPayload() == null ? null : JSON.toJSONString(request.getPayload()));
        plan.setTemplateId(request.getTemplateId());
        plan.setCreator(request.getCreator());
        plan.setRemark(request.getRemark());
        plan.setScheduleType(request.getScheduleType());
        plan.setScheduleRuleJson(buildScheduleRuleJson(request));
        plan.setEffectiveStartAt(request.getEffectiveStartAt());
        plan.setEffectiveEndAt(request.getEffectiveEndAt());
        plan.setWindowStartTime(request.getWindowStartTime());
        plan.setWindowEndTime(request.getWindowEndTime());
        plan.setRatePerMinute(normalizeRatePerMinute(request.getRatePerMinute()));
        plan.setTimezone(StringUtils.hasText(request.getTimezone()) ? request.getTimezone() : DEFAULT_TIMEZONE);
        return plan;
    }

    private void validateScheduleRule(MassTaskPlanCreateRequest request, List<String> errors) {
        if (request == null || request.getScheduleType() == null) {
            return;
        }
        switch (request.getScheduleType()) {
            case 1:
            case 2:
                break;
            case 3:
                if (CollectionUtils.isEmpty(request.getWeekdays())) {
                    errors.add("weekdays is required when scheduleType=weekly");
                    return;
                }
                for (Integer weekday : request.getWeekdays()) {
                    if (weekday == null || weekday < 1 || weekday > 7) {
                        errors.add("weekdays must contain values between 1 and 7");
                        return;
                    }
                }
                break;
            case 4:
                if (CollectionUtils.isEmpty(request.getMonthDays())) {
                    errors.add("monthDays is required when scheduleType=monthly");
                    return;
                }
                for (Integer monthDay : request.getMonthDays()) {
                    if (monthDay == null || (monthDay != -1 && (monthDay < 1 || monthDay > 31))) {
                        errors.add("monthDays must contain values between 1 and 31, or -1");
                        return;
                    }
                }
                break;
            default:
                break;
        }
    }

    private String buildScheduleRuleJson(MassTaskPlanCreateRequest request) {
        JSONObject json = new JSONObject();
        if (!CollectionUtils.isEmpty(request.getWeekdays())) {
            json.put("weekdays", request.getWeekdays());
        }
        if (!CollectionUtils.isEmpty(request.getMonthDays())) {
            json.put("monthDays", request.getMonthDays());
        }
        return json.isEmpty() ? null : json.toJSONString();
    }

    private LocalDateTime resolveNextTriggerTime(MassTaskPlan plan, LocalDateTime previousTriggerTime) {
        if (plan == null || plan.getEffectiveStartAt() == null) {
            return null;
        }

        LocalDate startDate = maxDate(plan.getEffectiveStartAt().toLocalDate(), previousTriggerTime.toLocalDate());
        // 向后滚动查找下一个命中的日期，范围先限制在 400 天内，避免无界循环。
        for (int offset = 0; offset < 400; offset++) {
            LocalDate candidateDate = startDate.plusDays(offset);
            if (!matchesSchedule(plan, candidateDate)) {
                continue;
            }
            LocalDateTime candidate = buildCandidateTriggerTime(plan, candidateDate);
            if (candidate == null || !candidate.isAfter(previousTriggerTime)) {
                continue;
            }
            if (plan.getEffectiveEndAt() != null && candidate.isAfter(plan.getEffectiveEndAt())) {
                return null;
            }
            return candidate;
        }
        return null;
    }

    private LocalDateTime buildCandidateTriggerTime(MassTaskPlan plan, LocalDate candidateDate) {
        LocalTime windowStart = LocalTime.parse(plan.getWindowStartTime());
        LocalTime windowEnd = LocalTime.parse(plan.getWindowEndTime());
        LocalDateTime candidate = candidateDate.atTime(windowStart);
        if (candidateDate.equals(plan.getEffectiveStartAt().toLocalDate())
                && plan.getEffectiveStartAt().isAfter(candidate)) {
            candidate = plan.getEffectiveStartAt();
        }
        return candidate.toLocalTime().isBefore(windowEnd) ? candidate : null;
    }

    private boolean matchesSchedule(MassTaskPlan plan, LocalDate candidateDate) {
        switch (plan.getScheduleType()) {
            case 1:
                return candidateDate.equals(plan.getEffectiveStartAt().toLocalDate());
            case 2:
                return true;
            case 3:
                // weekly: 规则里直接使用 1-7，对应 Monday-Sunday。
                return parseIntegerList(plan.getScheduleRuleJson(), "weekdays")
                        .contains(candidateDate.getDayOfWeek().getValue());
            case 4:
                List<Integer> monthDays = parseIntegerList(plan.getScheduleRuleJson(), "monthDays");
                if (monthDays.contains(candidateDate.getDayOfMonth())) {
                    return true;
                }
                // -1 代表每月最后一天。
                return monthDays.contains(-1)
                        && candidateDate.getDayOfMonth() == YearMonth.from(candidateDate).lengthOfMonth();
            default:
                return false;
        }
    }

    private List<Integer> parseIntegerList(String jsonText, String key) {
        if (!StringUtils.hasText(jsonText)) {
            return Collections.emptyList();
        }
        JSONObject json = JSON.parseObject(jsonText);
        JSONArray array = json == null ? null : json.getJSONArray(key);
        return array == null ? Collections.<Integer>emptyList() : array.toJavaList(Integer.class);
    }

    private List<Long> parseReceiverIds(String jsonText) {
        if (!StringUtils.hasText(jsonText)) {
            return Collections.emptyList();
        }
        return JSON.parseArray(jsonText, Long.class);
    }

    private MassTaskPayload parsePayload(String payloadJson) {
        if (!StringUtils.hasText(payloadJson)) {
            return null;
        }
        return JSON.parseObject(payloadJson, MassTaskPayload.class);
    }

    private MassTaskCreateRequest toMassTaskCreateRequest(MassTaskPlanCreateRequest request) {
        if (request == null) {
            return null;
        }
        MassTaskCreateRequest createRequest = new MassTaskCreateRequest();
        createRequest.setTaskName(request.getPlanName());
        createRequest.setTaskType(request.getTaskType());
        createRequest.setReceiverType(request.getReceiverType());
        createRequest.setReceiverIds(request.getReceiverIds());
        createRequest.setMsgType(request.getMsgType());
        createRequest.setCreator(request.getCreator());
        createRequest.setRemark(request.getRemark());
        createRequest.setTemplateId(request.getTemplateId());
        createRequest.setPayload(request.getPayload());
        createRequest.setSendTime(request.getEffectiveStartAt());
        return createRequest;
    }

    private LocalTime parseTime(String value, String fieldName, List<String> errors) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        try {
            return LocalTime.parse(value);
        } catch (DateTimeParseException ex) {
            errors.add(fieldName + " must use HH:mm:ss format");
            return null;
        }
    }

    private int normalizeRatePerMinute(Integer ratePerMinute) {
        return ratePerMinute == null ? DEFAULT_RATE_PER_MINUTE : ratePerMinute;
    }

    private LocalDate maxDate(LocalDate first, LocalDate second) {
        return first.isAfter(second) ? first : second;
    }

    private String buildTaskName(MassTaskPlan plan, String scheduleSlot) {
        return plan.getPlanName() + "-" + scheduleSlot;
    }
}
