package com.weichat.common.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 微信好友信息实体类
 */
@Data
public class WxFriendInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 用户唯一标识
     */
    private String unionid;

    /**
     * 创建时间戳
     */
    private Long createTime;

    /**
     * 性别：1-男，2-女，0-未知
     */
    private Integer sex;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 公司备注
     */
    private String companyRemark;

    /**
     * 账号ID
     */
    private String acctid;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 英文名
     */
    private String englishName;

    /**
     * 备注手机号，JSON数组格式
     */
    private String remarkPhone;

    /**
     * 真实姓名
     */
    private String realname;

    /**
     * 真实备注
     */
    private String realRemarks;

    /**
     * 好友ID
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
     * 企业ID
     */
    private Long corpId;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 序列，用于同步
     */
    private Long seq;

    /**
     * 状态：0-正常，1-已删除，2-黑名单等
     */
    private Integer status;

    /**
     * 更新时间
     */
    private Date updateTime;

}