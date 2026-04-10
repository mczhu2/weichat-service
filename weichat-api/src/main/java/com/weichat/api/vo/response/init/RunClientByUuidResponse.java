package com.weichat.api.vo.response.init;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 根据UUID获取运行客户端响应
 *
 * @author weichat
 */
@Data
@ApiModel(description = "根据UUID获取运行客户端响应数据")
public class RunClientByUuidResponse {

    @ApiModelProperty(value = "实例唯一标识", example = "69af4a23bdcda397e02ce571c8c36d73")
    private String uuid;

    @ApiModelProperty(value = "登录时间戳", example = "1775802251")
    private Long logintime;

    @ApiModelProperty(value = "登录类型(0:未初始化, 1:初始化未登录, 2:已登录)", example = "2")
    private Integer loginType;

    @ApiModelProperty(value = "用户信息")
    private UserInfo user_info;

    /**
     * 用户信息
     */
    @Data
    @ApiModel(description = "用户信息")
    public static class UserInfo {
        
        @ApiModelProperty(value = "用户对象信息")
        private UserObject object;
        
        @ApiModelProperty(value = "用户ID", example = "1688854282397463")
        private Long userid;
        
        @ApiModelProperty(value = "客户端ID", example = "2c5eefc58eb4842850d04eaee483a59c")
        private String clientId;
        
        @ApiModelProperty(value = "更新时间", example = "1775802380")
        private Long updateTime;
        
        @ApiModelProperty(value = "请求URL", example = "http://47.94.7.218:9952")
        private String requrl;
        
        @ApiModelProperty(value = "是否已登录", example = "true")
        private Boolean isLogin;
    }

    /**
     * 用户对象信息
     */
    @Data
    @ApiModel(description = "用户对象信息")
    public static class UserObject {
        
        @ApiModelProperty(value = "unionid", example = "ozynqsteRcrRDmoawoni968tiXHs")
        private String unionid;
        
        @ApiModelProperty(value = "创建时间", example = "0")
        private Long create_time;
        
        @ApiModelProperty(value = "性别(1:男, 2:女)", example = "1")
        private Integer sex;
        
        @ApiModelProperty(value = "手机号", example = "15556531599")
        private String mobile;
        
        @ApiModelProperty(value = "账号ID", example = "15556531599")
        private String acctid;
        
        @ApiModelProperty(value = "企业corp_id", example = "ww33dc36c553d22b16")
        private String scorp_id;
        
        @ApiModelProperty(value = "头像URL", example = "https://wework.qpic.cn/wwpic3az/485661_Qc99ACfUTLC6L-0_1772641737/0")
        private String avatar;
        
        @ApiModelProperty(value = "企业名称", example = "测试企业")
        private String corp_name;
        
        @ApiModelProperty(value = "英文名", example = "")
        private String english_name;
        
        @ApiModelProperty(value = "真实姓名", example = "张三")
        private String realname;
        
        @ApiModelProperty(value = "用户ID", example = "1688854282397463")
        private Long user_id;
        
        @ApiModelProperty(value = "昵称", example = "张三")
        private String nickname;
        
        @ApiModelProperty(value = "职位", example = "后端")
        private String position;
        
        @ApiModelProperty(value = "企业ID", example = "1970324963104386")
        private Long corp_id;
        
        @ApiModelProperty(value = "企业全称", example = "")
        private String corp_full_name;
        
        @ApiModelProperty(value = "企业描述", example = "")
        private String corp_desc;
    }
}
