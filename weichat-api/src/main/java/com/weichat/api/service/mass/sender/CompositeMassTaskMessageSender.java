package com.weichat.api.service.mass.sender;

import com.alibaba.fastjson.JSONObject;
import com.weichat.common.entity.MassTask;
import com.weichat.common.enums.MassMessageTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class CompositeMassTaskMessageSender implements MassTaskMessageSender {

    @Autowired
    private MassTaskMessageSupport messageSupport;

    @Autowired(required = false)
    private List<MassTaskMessageSender> senderList = Collections.emptyList();

    private Map<Integer, MassTaskMessageSender> childSenderMap = Collections.emptyMap();

    /**
     * 初始化子消息类型到发送器的映射，供组合消息逐项分发时复用。
     */
    @PostConstruct
    private void initChildSenderMap() {
        Map<Integer, MassTaskMessageSender> senderMap = new HashMap<>();
        for (MassTaskMessageSender sender : senderList) {
            if (sender == this) {
                continue;
            }
            senderMap.put(sender.getMsgType(), sender);
        }
        this.childSenderMap = Collections.unmodifiableMap(senderMap);
    }

    @Override
    public Integer getMsgType() {
        return MassMessageTypeEnum.COMPOSITE.getCode();
    }

    /**
     * 按组合模板中定义的顺序逐条发送子消息。
     * 单条子消息异常会记录日志并继续后续发送；仅当全部子消息都失败时才返回失败。
     *
     * @param task            当前群发任务
     * @param receiverContext 接收者上下文
     * @return 最后一条子消息的下游响应；若没有子项则返回成功结果
     */
    @Override
    public JSONObject send(MassTask task, MassTaskReceiverContext receiverContext) {
        List<MassTaskCompositeItem> items = messageSupport.resolveCompositeItems(task, receiverContext.getReceiverName());
        if (items.isEmpty()) {
            throw new IllegalArgumentException("composite template items are empty");
        }

        JSONObject lastResult = null;
        int successCount = 0;
        String lastErrorMessage = null;
        for (int i = 0; i < items.size(); i++) {
            try {
                MassTaskCompositeItem item = items.get(i);
                Integer childMsgType = item.getMsgType();
                if (childMsgType == null || childMsgType.equals(MassMessageTypeEnum.COMPOSITE.getCode())) {
                    throw new IllegalArgumentException("composite item[" + i + "] msgType is invalid");
                }
                if (!StringUtils.hasText(item.getTemplateContent())) {
                    throw new IllegalArgumentException("composite item[" + i + "] templateContent is required");
                }

                MassTaskMessageSender sender = childSenderMap.get(childMsgType);
                if (sender == null) {
                    throw new IllegalArgumentException("unsupported composite child msgType: " + childMsgType);
                }

                lastResult = sender.send(buildChildTask(task, item), receiverContext);
                messageSupport.ensureSuccess(lastResult, "send composite item[" + i + "]");
                successCount++;
            } catch (Exception ex) {
                lastErrorMessage = ex.getMessage();
                log.error("composite item send failed, index={}, taskName={}, receiver={}",
                        i, task.getTaskName(), receiverContext.getReceiverName(), ex);
            }
        }

        if (successCount == 0) {
            throw new IllegalStateException(StringUtils.hasText(lastErrorMessage)
                    ? lastErrorMessage
                    : "all composite items failed");
        }

        return lastResult == null ? messageSupport.successResult() : lastResult;
    }

    /**
     * 基于组合子项构造一个临时子任务对象，复用已有单消息发送器能力。
     *
     * @param sourceTask 原始组合任务
     * @param item       当前子消息定义
     * @return 临时子任务
     */
    private MassTask buildChildTask(MassTask sourceTask, MassTaskCompositeItem item) {
        MassTask childTask = new MassTask();
        childTask.setTaskName(sourceTask.getTaskName());
        childTask.setTaskType(sourceTask.getTaskType());
        childTask.setCreator(sourceTask.getCreator());
        childTask.setRemark(sourceTask.getRemark());
        childTask.setPayloadVersion(sourceTask.getPayloadVersion());
        childTask.setMsgType(item.getMsgType());
        childTask.setContent(item.getTemplateContent());
        return childTask;
    }
}
