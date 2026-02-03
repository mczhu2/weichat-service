package com.weichat.common.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 回调任务实体类
 * 用于存储回调请求参数和处理状态
 */
@Data
public class WxCallbackTask implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 运行实例ID
     */
    private String uuid;

    /**
     * 回调消息内容，JSON格式
     */
    private String jsonContent;

    /**
     * 消息类型
     */
    private String type;

    /**
     * 处理状态：0-待处理，1-处理中，2-处理成功，3-处理失败
     */
    private Integer status;

    /**
     * 重试次数
     */
    private Integer retryCount;

    /**
     * 最大重试次数
     */
    private Integer maxRetryCount;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 处理时间
     */
    private Date processTime;

    /**
     * 任务状态常量
     */
    public static final Integer STATUS_PENDING = (Integer) 0;
    public static final Integer STATUS_PROCESSING = (Integer) 1;
    public static final Integer STATUS_SUCCESS = (Integer) 2;
    public static final Integer STATUS_FAILED = (Integer) 3;
}
