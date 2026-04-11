package com.weichat.common.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class WxGroupInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String roomId;
    private Long corpid;
    private Long createUserId;
    private Integer total;
    private Long noticeSendervid;
    private Long flag;
    private Long createTime;
    private String noticeContent;
    private String nickname;
    private Integer newFlag;
    private Long noticeTime;
    private String managers;
    private Date updateTime;
}
