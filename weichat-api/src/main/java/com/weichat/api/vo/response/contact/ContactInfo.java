package com.weichat.api.vo.response.contact;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 联系人信息
 *
 * @author weichat
 */
@Data
@ApiModel(description = "联系人信息")
public class ContactInfo {

    @ApiModelProperty(value = "用户ID", example = "123456789")
    @JSONField(name = "user_id", alternateNames = {"vid"})
    private Long vid;

    @ApiModelProperty(value = "用户名", example = "wxid_xxx")
    @JSONField(name = "acctid")
    private String userName;

    @ApiModelProperty(value = "昵称", example = "张三")
    @JSONField(name = "nickname", alternateNames = {"name"})
    private String nickname;

    @ApiModelProperty(value = "备注名", example = "张三-客户")
    @JSONField(name = "remarks", alternateNames = {"remark"})
    private String remark;

    @ApiModelProperty(value = "头像URL", example = "https://example.com/avatar.jpg")
    @JSONField(name = "headImg", alternateNames = {"avatar"})
    private String headImg;

    @ApiModelProperty(value = "性别(0:未知, 1:男, 2:女)", example = "1")
    private Integer sex;

    @ApiModelProperty(value = "手机号", example = "13800138000")
    private String mobile;

    @ApiModelProperty(value = "邮箱", example = "test@example.com")
    private String email;

    @ApiModelProperty(value = "企业名称", example = "测试企业")
    @JSONField(name = "corpName", alternateNames = {"corp_name"})
    private String corpName;

    @ApiModelProperty(value = "职位", example = "工程师")
    private String position;

    @ApiModelProperty(value = "联系人类型(1:内部, 2:外部)", example = "2")
    @JSONField(name = "state")
    private Integer contactType;

    @ApiModelProperty(value = "标签ID列表")
    @JSONField(name = "labelid")
    private List<String> labelIds;

    @ApiModelProperty(value = "描述信息", example = "重要客户")
    @JSONField(name = "description", alternateNames = {"desc", "des"})
    private String desc;

    @ApiModelProperty(value = "unionid", example = "ozynqsqC7yU4vcGRIYbl8cryOypE")
    private String unionid;

    @ApiModelProperty(value = "创建时间", example = "1768975066")
    @JSONField(name = "create_time")
    private Long createTime;

    @ApiModelProperty(value = "添加客户时间", example = "1768975066")
    @JSONField(name = "add_customer_time")
    private Long addCustomerTime;

    @ApiModelProperty(value = "企业备注", example = "测试企业备注")
    @JSONField(name = "company_remark")
    private String companyRemark;

    @ApiModelProperty(value = "英文名", example = "Tom")
    @JSONField(name = "english_name")
    private String englishName;

    @ApiModelProperty(value = "真实姓名", example = "张三")
    private String realname;

    @ApiModelProperty(value = "真实备注", example = "重要客户")
    @JSONField(name = "real_remarks")
    private String realRemarks;

    @ApiModelProperty(value = "企业ID", example = "1970325134026788")
    @JSONField(name = "corp_id")
    private Long corpId;

    @ApiModelProperty(value = "openid", example = "orFrbsjn3TUv6HTzWlZqNQ54VOUM")
    private String openid;

    @ApiModelProperty(value = "ticket", example = "114A2A26E50DE4BC6351E5E2481F1FE6A544AC2A656CE72D2AC8474F8479A79D")
    private String ticket;

    @ApiModelProperty(value = "联系人状态", example = "9")
    private Integer status;

    @ApiModelProperty(value = "分页游标", example = "8596253")
    private Long seq;

    @ApiModelProperty(value = "申请创建时间", example = "1743647253")
    @JSONField(name = "apply_create_time")
    private Long applyCreateTime;

    @ApiModelProperty(value = "申请更新时间", example = "1743647570")
    @JSONField(name = "apply_update_time")
    private Long applyUpdateTime;

    @ApiModelProperty(value = "时间戳", example = "1683268963")
    private Long timestamp;

    @ApiModelProperty(value = "条目标识", example = "2")
    @JSONField(name = "item_flag", alternateNames = {"ItemFlag"})
    private Integer itemFlag;

    @ApiModelProperty(value = "来源信息")
    @JSONField(name = "source_info")
    private JSONObject sourceInfo;

    @ApiModelProperty(value = "备注手机号列表")
    @JSONField(name = "remark_phone")
    private List<String> remarkPhones;
}
