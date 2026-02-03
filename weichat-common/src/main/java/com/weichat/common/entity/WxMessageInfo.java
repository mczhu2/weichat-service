package com.weichat.common.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 微信消息信息实体类
 */
@Data
public class WxMessageInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 消息ID
     */
    private Long msgId;
    
    /**
     * 消息ID（用于接收请求中的messageId字段）
     */
    private String messageId;

    /**
     * 消息类型：0-文本，2-文本，78-小程序，101-图片，104-表情
     */
    private Integer msgtype;

    /**
     * 消息标记
     */
    private Long flag;

    /**
     * 接收人ID
     */
    private Long receiver;

    /**
     * 发送人ID
     */
    private Long sender;

    /**
     * 发送人名称
     */
    private String senderName;

    /**
     * 是否为群聊：0-否，1-是
     */
    private Integer isRoom;

    /**
     * 群会话ID，群消息时存储room_conversation_id
     */
    private String roomId;

    /**
     * 服务器ID
     */
    private Long serverId;

    /**
     * 发送时间戳
     */
    private Long sendTime;

    /**
     * 参考ID
     */
    private Long referid;

    /**
     * 应用信息
     */
    private String appInfo;

    /**
     * 已读人数
     */
    private Integer readuinscount;

    /* 文本消息字段 */

    /**
     * 客服ID，只有客服消息接收才会有值否则为0
     */
    private Long kfId;

    /**
     * 文本消息内容
     */
    private String content;

    /**
     * 是否同步
     */
    private Integer issync;

    /**
     * @列表，JSON格式
     */
    private String atList;

    /* 表情消息字段 */

    /**
     * 表情URL
     */
    private String url;

    /**
     * 宽度，表情/图片/视频
     */
    private Integer width;

    /**
     * 高度，表情/图片/视频
     */
    private Integer height;

    /**
     * 表情类型
     */
    private String emotionType;

    /* 小程序消息字段 */

    /**
     * 小程序缩略图文件ID
     */
    private String thumbFileId;

    /**
     * 小程序标题
     */
    private String title;

    /**
     * 小程序名称
     */
    private String appName;

    /**
     * 小程序缩略图MD5
     */
    private String thumbMd5;

    /**
     * 小程序页面路径
     */
    private String pagepath;

    /**
     * 小程序大小
     */
    private Integer size;

    /**
     * 小程序APPID
     */
    private String appid;

    /**
     * 小程序缩略图AES密钥
     */
    private String thumbAesKey;

    /**
     * 小程序用户名
     */
    private String username;

    /**
     * 小程序图标URL
     */
    private String weappIconUrl;

    /**
     * 小程序描述
     */
    private String desc;

    /* 图片消息字段 */

    /**
     * 图片大小
     */
    private Long fileSize;

    /**
     * 图片文件ID
     */
    private String fileId;

    /**
     * 图片CDN授权密钥
     */
    private String openimCdnAuthkey;

    /**
     * 图片AES密钥
     */
    private String aesKey;

    /**
     * 图片MD5值
     */
    private String md5;

    /* 公共字段 */

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}