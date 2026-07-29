package com.weichat.common.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 企微员工业务系统回调路由。
 * <p>第一版以 uuid 作为员工运行实例身份，保存该实例对应的下游业务系统回调地址。</p>
 */
@Data
public class WxCallbackRoute implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键 ID。
     */
    private Long id;

    /**
     * 企微运行实例 UUID。
     */
    private String uuid;

    /**
     * 业务系统回调 URL。
     */
    private String callbackUrl;

    /**
     * 创建时间。
     */
    private Date createTime;

    /**
     * 更新时间。
     */
    private Date updateTime;
}
