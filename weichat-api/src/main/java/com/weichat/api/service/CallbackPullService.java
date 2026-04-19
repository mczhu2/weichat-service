package com.weichat.api.service;

import com.weichat.api.entity.ApiResult;
import com.weichat.api.vo.request.callback.CallbackPullRequest;
import com.weichat.api.vo.response.callback.CallbackPullResponse;
import com.weichat.common.entity.WxCallbackTask;
import com.weichat.common.entity.WxUserInfo;
import com.weichat.common.mapper.WxCallbackTaskMapper;
import com.weichat.common.mapper.WxUserInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 回调消息回拉服务
 *
 * @author weichat
 */
@Slf4j
@Service
public class CallbackPullService {

    @Autowired
    private WxUserInfoMapper wxUserInfoMapper;

    @Autowired
    private WxCallbackTaskMapper wxCallbackTaskMapper;

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final long FIVE_MINUTES_IN_MILLIS = TimeUnit.MINUTES.toMillis(5);

    /**
     * 获取最近5分钟内最小的回调任务ID
     *
     * @param uuid 用户UUID
     * @return 最小ID，如果没有则返回null
     */
    private Long getMinIdFromLastFiveMinutes(String uuid) {
        LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);
        Date fiveMinutesAgoDate = Date.from(fiveMinutesAgo.atZone(ZoneId.systemDefault()).toInstant());
        
        return wxCallbackTaskMapper.selectMinIdAfterTime(uuid, fiveMinutesAgoDate);
    }

    /**
     * 回拉消息记录
     *
     * @param request 回拉请求参数
     * @return 回拉响应
     */
    public ApiResult<CallbackPullResponse> pullCallbackMessages(CallbackPullRequest request) {
        String uuid = request.getUuid();
        Integer dataRangeType = request.getDataRangeType();
        Integer pageNum = request.getPageNum() != null ? request.getPageNum() : DEFAULT_PAGE_SIZE;

        // 查询用户信息
        WxUserInfo userInfo = wxUserInfoMapper.selectByUuid(uuid);
        if (userInfo == null) {
            log.warn("未找到uuid为{}的用户信息", uuid);
            return ApiResult.fail("未找到设备uuid对应的用户信息");
        }

        // 确定起始ID
        Long startId = null;
        if (dataRangeType != null && dataRangeType == 1) {
            // 增量拉取：使用用户记录的lastPulledMessageId
            startId = userInfo.getLastPulledMessageId();
            if (startId == null) {
                // 如果没有历史记录，则取最近5分钟内的最小ID
                startId = getMinIdFromLastFiveMinutes(uuid);
                log.info("增量拉取但历史游标不存在，uuid={}，将从最近5分钟的最小ID={}开始拉取", uuid, startId);
            }
        }
        // dataRangeType == 0 时，startId 为 null，会从最小ID开始拉取

        // 查询回调任务列表
        List<WxCallbackTask> tasks = wxCallbackTaskMapper.selectByUuidAndIdRange(uuid, startId, pageNum);

        // 提取jsonContent列表
        List<String> jsonContentList = tasks.stream()
                .map(WxCallbackTask::getJsonContent)
                .collect(Collectors.toList());

        // 计算当前拉取的最大ID
        Long currentMaxId = null;
        if (!tasks.isEmpty()) {
            currentMaxId = tasks.stream()
                    .map(WxCallbackTask::getId)
                    .max(Long::compareTo)
                    .orElse(null);
        }

        // 判断是否还有更多数据
        Boolean hasMore = false;
        if (currentMaxId != null) {
            // 尝试再查询一条来判断是否有更多数据
            List<WxCallbackTask> nextTasks = wxCallbackTaskMapper.selectByUuidAndIdRange(uuid, currentMaxId, 1);
            hasMore = !nextTasks.isEmpty();
        }

        // 更新用户的lastPulledMessageId
        if (currentMaxId != null) {
            wxUserInfoMapper.updateLastPulledMessageId(uuid, currentMaxId);
            log.info("更新uuid={}的lastPulledMessageId={}", uuid, currentMaxId);
        }

        // 构建响应
        CallbackPullResponse response = new CallbackPullResponse();
        response.setJsonContentList(jsonContentList);
        response.setHasMore(hasMore);
        response.setCurrentMaxId(currentMaxId);

        return ApiResult.success(response);
    }
}
