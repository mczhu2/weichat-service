package com.weichat.common.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 微信群成员信息实体类
 */
@Data
public class WxGroupMember implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 群ID，关联wx_group_info表
     */
    private String roomId;

    /**
     * 唯一标识
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
     * 账号ID
     */
    private String acctid;

    /**
     * 加入场景
     */
    private Integer joinScene;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 英文名
     */
    private String englishName;

    /**
     * 真实姓名
     */
    private String realname;

    /**
     * 群备注
     */
    private String roomNotes;

    /**
     * 加入时间戳
     */
    private Long jointime;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 群昵称
     */
    private String roomNickname;

    /**
     * 职位
     */
    private String position;

    /**
     * 群成员列表
     */
    private List<String> memberList;

    /**
     * 用户ID
     */
    private Long uin;

    /**
     * 邀请者ID
     */
    private Long inviteUserId;

    /**
     * 企业ID
     */
    private Long corpId;

    /**
     * 更新时间
     */
    private Date updateTime;

}