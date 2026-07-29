package com.weichat.common.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 企微回调路由在线状态巡检目标。
 * <p>一条记录代表一个存在回调路由、且按 userId 最新绑定关系筛选后的企微运行实例。</p>
 */
@Data
public class WxCallbackRouteMonitorTarget implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 企微运行实例 UUID。
     */
    private String uuid;

    /**
     * 企微用户 ID。
     */
    private Long userId;

    /**
     * 用户昵称，优先用于告警展示。
     */
    private String nickname;

    /**
     * 用户真实姓名，昵称为空时用于告警展示。
     */
    private String realname;

    /**
     * 账号 ID，昵称和真实姓名为空时用于告警展示。
     */
    private String acctid;
}
