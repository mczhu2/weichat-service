package com.weichat.common.service;

import com.weichat.common.entity.WxMessageInfo;

import java.util.List;

/**
 * 微信消息信息Service接口
 */
public interface WxMessageInfoService {
    /**
     * 新增微信消息信息
     * @param wxMessageInfo 微信消息信息
     * @return 影响行数
     */
    int insert(WxMessageInfo wxMessageInfo);

    /**
     * 根据ID查询微信消息信息
     * @param id 主键ID
     * @return 微信消息信息
     */
    WxMessageInfo selectByPrimaryKey(Long id);

    /**
     * 根据消息ID查询微信消息信息
     * @param msgId 消息ID
     * @return 微信消息信息
     */
    WxMessageInfo selectByMsgId(Long msgId);

    /**
     * 查询所有微信消息信息
     * @return 微信消息信息列表
     */
    List<WxMessageInfo> selectAll();

    /**
     * 根据ID更新微信消息信息
     * @param wxMessageInfo 微信消息信息
     * @return 影响行数
     */
    int updateByPrimaryKey(WxMessageInfo wxMessageInfo);

    /**
     * 根据ID删除微信消息信息
     * @param id 主键ID
     * @return 影响行数
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 根据接收人ID查询微信消息信息
     * @param receiver 接收人ID
     * @return 微信消息信息列表
     */
    List<WxMessageInfo> selectByReceiver(Long receiver);

    /**
     * 根据发送人ID查询微信消息信息
     * @param sender 发送人ID
     * @return 微信消息信息列表
     */
    List<WxMessageInfo> selectBySender(Long sender);

    /**
     * 根据消息类型查询微信消息信息
     * @param msgtype 消息类型
     * @return 微信消息信息列表
     */
    List<WxMessageInfo> selectByMsgtype(Integer msgtype);
    
    /**
     * 根据消息ID查询微信消息信息（字符串类型）
     * @param messageId 消息ID
     * @return 微信消息信息
     */
    WxMessageInfo selectByMessageId(String messageId);

    /**
     * 批量插入微信消息信息
     * @param list 微信消息信息列表
     * @return 影响行数
     */
    int batchInsert(List<WxMessageInfo> list);

    /**
     * 根据发送时间范围查询微信消息信息
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 微信消息信息列表
     */
    List<WxMessageInfo> selectBySendTimeRange(Long startTime, Long endTime);
}
