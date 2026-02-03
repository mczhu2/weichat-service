package com.weichat.common.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 微信用户信息实体类
 */
@Data
public class WxUserInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 企业微信企业ID
     */
    private Long corpid;

    /**
     * 用户唯一标识
     */
    private String unionid;

    /**
     * 创建时间戳
     */
    private Long createTime;

    /**
     * 管理员ID
     */
    private Long adminVid;

    /**
     * 性别：1-男，2-女，0-未知
     */
    private Integer sex;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 企业全称
     */
    private String corpFullName;

    /**
     * 账号ID
     */
    private String acctid;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 企业名称
     */
    private String corpName;

    /**
     * 英文名
     */
    private String englishName;

    /**
     * 真实姓名
     */
    private String realname;

    /**
     * 视频号ID
     */
    private Long vid;

    /**
     * 邮箱
     */
    private String mail;

    /**
     * 所有者名称
     */
    private String ownername;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 职位
     */
    private String position;

    /**
     * 企业描述
     */
    private String corpDesc;

    /**
     * 企业LOGO
     */
    private String corpLogo;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 注册设备uuid
     */
    private String uuid;
}
