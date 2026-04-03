package com.weichat.api.vo.response.contact;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
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
    private Long userId;

    @ApiModelProperty(value = "用户名", example = "wxid_xxx")
    @JSONField(name = "acctid")
    private String acctId;

    @ApiModelProperty(value = "昵称", example = "张三")
    @JSONField(name = "nickname", alternateNames = {"name"})
    private String nickname;

    @ApiModelProperty(value = "备注名", example = "张三-客户")
    @JSONField(name = "remarks", alternateNames = {"remark"})
    private String remarks;

    @ApiModelProperty(value = "头像URL", example = "https://example.com/avatar.jpg")
    @JSONField(name = "avatar", alternateNames = {"headImg"})
    private String avatar;

    @ApiModelProperty(value = "排序值", example = "100000000")
    @JSONField(name = "disp_order", alternateNames = {"dispOrder"})
    private Long dispOrder;

    @ApiModelProperty(value = "性别(0:未知, 1:男, 2:女)", example = "1")
    private Integer sex;

    @ApiModelProperty(value = "手机号", example = "13800138000")
    private String mobile;

    @ApiModelProperty(value = "邮箱", example = "test@example.com")
    private String email;

    @ApiModelProperty(value = "企业名称", example = "测试企业")
    @JSONField(name = "corp_name", alternateNames = {"corpName"})
    private String corpName;

    @ApiModelProperty(value = "企业ID", example = "1970324998039140")
    @JSONField(name = "corpid", alternateNames = {"corp_id", "corpId"})
    private Long corpId;

    @ApiModelProperty(value = "职位", example = "工程师")
    private String position;

    @ApiModelProperty(value = "部门名称", example = "微盟行政内测")
    @JSONField(name = "party_name", alternateNames = {"partyName"})
    private String partyName;

    @ApiModelProperty(value = "部门ID", example = "1688853293326024")
    @JSONField(name = "partyid", alternateNames = {"partyId"})
    private Long partyId;

    @ApiModelProperty(value = "父级部门ID", example = "1688853293326024")
    @JSONField(name = "parentid", alternateNames = {"parentId"})
    private Long parentId;

    @ApiModelProperty(value = "是否部门(0:否, 1:是)", example = "1")
    @JSONField(name = "is_Department", alternateNames = {"isDepartment"})
    private Integer isDepartment;

    @ApiModelProperty(value = "节点类型", example = "0")
    private Integer type;

    @ApiModelProperty(value = "联系人类型(1:内部, 2:外部)", example = "2")
    @JSONField(name = "state", alternateNames = {"contactType"})
    private Integer state;

    @ApiModelProperty(value = "标签ID列表")
    @JSONField(name = "labelid", alternateNames = {"labelIds"})
    private List<String> labelIds;

    @ApiModelProperty(value = "描述信息", example = "重要客户")
    @JSONField(name = "description", alternateNames = {"desc", "des"})
    private String description;

    @ApiModelProperty(value = "unionId", example = "ozynqsqC7yU4vcGRIYbl8cryOypE")
    @JSONField(name = "unionid")
    private String unionId;

    @ApiModelProperty(value = "创建时间", example = "1768975066")
    @JSONField(name = "create_time", alternateNames = {"createTime"})
    private Long createTime;

    @ApiModelProperty(value = "添加客户时间", example = "1768975066")
    @JSONField(name = "add_customer_time", alternateNames = {"addCustomerTime"})
    private Long addCustomerTime;

    @ApiModelProperty(value = "企业备注", example = "测试企业备注")
    @JSONField(name = "company_remark", alternateNames = {"companyRemark"})
    private String companyRemark;

    @ApiModelProperty(value = "来源类型", example = "51")
    private Integer source;

    @ApiModelProperty(value = "英文名", example = "Tom")
    @JSONField(name = "english_name", alternateNames = {"englishName"})
    private String englishName;

    @ApiModelProperty(value = "真实姓名", example = "张三")
    @JSONField(name = "realname")
    private String realName;

    @ApiModelProperty(value = "真实备注", example = "重要客户")
    @JSONField(name = "real_remarks", alternateNames = {"realRemarks"})
    private String realRemarks;

    @ApiModelProperty(value = "openid", example = "orFrbsjn3TUv6HTzWlZqNQ54VOUM")
    private String openid;

    @ApiModelProperty(value = "ticket", example = "114A2A26E50DE4BC6351E5E2481F1FE6A544AC2A656CE72D2AC8474F8479A79D")
    private String ticket;

    @ApiModelProperty(value = "联系人状态", example = "9")
    private Integer status;

    @ApiModelProperty(value = "分页游标", example = "8596253")
    private Long seq;

    @ApiModelProperty(value = "申请创建时间", example = "1743647253")
    @JSONField(name = "apply_create_time", alternateNames = {"applyCreateTime"})
    private Long applyCreateTime;

    @ApiModelProperty(value = "申请更新时间", example = "1743647570")
    @JSONField(name = "apply_update_time", alternateNames = {"applyUpdateTime"})
    private Long applyUpdateTime;

    @ApiModelProperty(value = "时间戳", example = "1683268963")
    private Long timestamp;

    @ApiModelProperty(value = "自定义属性列表")
    @JSONField(name = "SelfAttrInfo", alternateNames = {"selfAttrInfo"})
    private List<JSONObject> selfAttrInfo = new ArrayList<>();

    @ApiModelProperty(value = "条目标识", example = "2")
    @JSONField(name = "item_flag", alternateNames = {"ItemFlag", "itemFlag"})
    private Integer itemFlag;

    @ApiModelProperty(value = "来源信息")
    @JSONField(name = "source_info", alternateNames = {"sourceInfo"})
    private JSONObject sourceInfo;

    @ApiModelProperty(value = "备注手机号列表")
    @JSONField(name = "remark_phone", alternateNames = {"remarkPhones"})
    private List<String> remarkPhones;
}
