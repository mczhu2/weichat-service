package com.weichat.common.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 微信群信息实体类
 */
@Data
public class WxGroupInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 群ID
     */
    private String roomId;

    /**
     * 创建者ID
     */
    private Long createUserId;

    /**
     * 群成员总数
     */
    private Integer total;

    /**
     * 通知发送者ID
     */
    private Long noticeSendervid;

    /**
     * 标记
     */
    private Long flag;

    /**
     * 创建时间戳
     */
    private Long createTime;

    /**
     * 通知内容
     */
    private String noticeContent;

    /**
     * 群昵称
     */
    private String nickname;

    /**
     * 新标记
     */
    private Integer newFlag;

    /**
     * 通知时间
     */
    private Long noticeTime;

    /**
     * 管理员列表，JSON格式
     */
    private String managers;

    /**
     * 更新时间
     */
    private Date updateTime;

}