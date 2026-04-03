package com.weichat.api.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.entity.ApiResult;
import com.weichat.api.vo.request.BaseRequest;
import com.weichat.api.vo.request.contact.*;
import com.weichat.api.vo.response.contact.ContactInfo;
import com.weichat.api.vo.response.contact.ContactListResponse;
import com.weichat.api.vo.response.contact.SessionInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 联系人管理Controller
 *
 * @author weichat
 */
@Api(tags = "联系人管理")
@RestController
@RequestMapping("/api/v1/contact")
public class ContactController {

    @Autowired
    private WxWorkApiClient client;

    @ApiOperation("获取外部联系人列表")
    @PostMapping("/getExternalContacts")
    public ApiResult<ContactListResponse> getExternalContacts(@RequestBody GetExternalContactsRequest request) {
        return ApiResult.from(client.post("/wxwork/GetExternalContacts", toJson(request)), ContactListResponse.class);
    }

    @ApiOperation("获取内部联系人列表")
    @PostMapping("/getInnerContacts")
    public ApiResult<ContactListResponse> getInnerContacts(@RequestBody GetInnerContactsRequest request) {
        return ApiResult.from(client.post("/wxwork/GetInnerContacts", toJson(request)), ContactListResponse.class);
    }

    @ApiOperation("根据VID获取用户信息")
    @PostMapping("/getUserInfoByVids")
    public ApiResult<List<ContactInfo>> getUserInfoByVids(@RequestBody VidsRequest request) {
        return ApiResult.fromList(client.post("/wxwork/GetUserInfoByVids", toJson(request)), ContactInfo.class);
    }

    @ApiOperation("同意好友申请")
    @PostMapping("/agreeUser")
    public ApiResult<Void> agreeUser(@RequestBody VidRequest request) {
        return ApiResult.from(client.post("/wxwork/AgreeUser", toJson(request)));
    }

    @ApiOperation("删除联系人")
    @PostMapping("/delContact")
    public ApiResult<Void> delContact(@RequestBody VidRequest request) {
        return ApiResult.from(client.post("/wxwork/DelContact", toJson(request)));
    }

    @ApiOperation("获取好友申请列表")
    @PostMapping("/getApplyUserList")
    public ApiResult<List<ContactInfo>> getApplyUserList(@RequestBody BaseRequest request) {
        return ApiResult.fromList(client.post("/wxwork/GetApplyUserList", toJson(request)), ContactInfo.class);
    }

    @ApiOperation("获取托管会话列表")
    @PostMapping("/getManagedConversations")
    public ApiResult<List<SessionInfo>> getManagedConversations(@RequestBody BaseRequest request) {
        return ApiResult.fromList(client.post("/wxwork/getManagedConversations", toJson(request)), SessionInfo.class);
    }

    @ApiOperation("获取会话列表")
    @PostMapping("/getSessionList")
    public ApiResult<List<SessionInfo>> getSessionList(@RequestBody BaseRequest request) {
        return ApiResult.fromList(client.post("/wxwork/GetSessionList", toJson(request)), SessionInfo.class);
    }

    @ApiOperation("更新我的用户信息设置")
    @PostMapping("/updateSetMyUserInfo")
    public ApiResult<Void> updateSetMyUserInfo(@RequestBody UpdateSetMyUserInfoRequest request) {
        return ApiResult.from(client.post("/wxwork/UpdateSetMyUserInfo", toJson(request)));
    }

    @ApiOperation("设置同事备注")
    @PostMapping("/setColleagueRemark")
    public ApiResult<Void> setColleagueRemark(@RequestBody SetColleagueRemarkRequest request) {
        return ApiResult.from(client.post("/wxwork/SetColleagueRemark", toJson(request)));
    }

    @ApiOperation("获取同事备注列表")
    @PostMapping("/getColleagueRemarkList")
    public ApiResult<List<ContactInfo>> getColleagueRemarkList(@RequestBody BaseRequest request) {
        return ApiResult.fromList(client.post("/wxwork/GetColleaRemarkList", toJson(request)), ContactInfo.class);
    }

    @ApiOperation("设置会话置顶")
    @PostMapping("/setTop")
    public ApiResult<Void> setTop(@RequestBody SessionSettingRequest request) {
        return ApiResult.from(client.post("/wxwork/SetTop", toJson(request)));
    }

    @ApiOperation("设置消息免打扰")
    @PostMapping("/setShield")
    public ApiResult<Void> setShield(@RequestBody SessionSettingRequest request) {
        return ApiResult.from(client.post("/wxwork/SetShield", toJson(request)));
    }

    @ApiOperation("设置会话折叠")
    @PostMapping("/setFold")
    public ApiResult<Void> setFold(@RequestBody SessionSettingRequest request) {
        return ApiResult.from(client.post("/wxwork/SetTold", toJson(request)));
    }

    @ApiOperation("设置会话标记")
    @PostMapping("/setMark")
    public ApiResult<Void> setMark(@RequestBody SessionSettingRequest request) {
        return ApiResult.from(client.post("/wxwork/SetMark", toJson(request)));
    }

    @ApiOperation("添加黑名单")
    @PostMapping("/addBlack")
    public ApiResult<Void> addBlack(@RequestBody VidRequest request) {
        return ApiResult.from(client.post("/wxwork/AddSubContactBlack", toJson(request)));
    }

    @ApiOperation("搜索联系人")
    @PostMapping("/searchContact")
    public ApiResult<List<ContactInfo>> searchContact(@RequestBody SearchContactRequest request) {
        return ApiResult.fromList(client.post("/wxwork/SearchContact", toJson(request)), ContactInfo.class);
    }

    @ApiOperation("搜索添加好友")
    @PostMapping("/addSearch")
    public ApiResult<Void> addSearch(@RequestBody AddSearchRequest request) {
        return ApiResult.from(client.post("/wxwork/AddSearch", toJson(request)));
    }

    @ApiOperation("企微搜索添加好友")
    @PostMapping("/addSearchWxwork")
    public ApiResult<Void> addSearchWxwork(@RequestBody AddSearchRequest request) {
        return ApiResult.from(client.post("/wxwork/AddSearchWxwork", toJson(request)));
    }

    @ApiOperation("添加名片好友")
    @PostMapping("/addBusinessCard")
    public ApiResult<Void> addBusinessCard(@RequestBody AddBusinessCardRequest request) {
        return ApiResult.from(client.post("/wxwork/AddBusinessCard", toJson(request)));
    }

    @ApiOperation("添加微信用户")
    @PostMapping("/addWxUser")
    public ApiResult<Void> addWxUser(@RequestBody AddBusinessCardRequest request) {
        return ApiResult.from(client.post("/wxwork/addWxUser", toJson(request)));
    }

    @ApiOperation("操作客户")
    @PostMapping("/operCustomer")
    public ApiResult<Void> operCustomer(@RequestBody OperCustomerRequest request) {
        return ApiResult.from(client.post("/wxwork/OperCustomer", toJson(request)));
    }

    @ApiOperation("添加分享用户")
    @PostMapping("/addShareUser")
    public ApiResult<Void> addShareUser(@RequestBody VidRequest request) {
        return ApiResult.from(client.post("/wxwork/AddShareUser", toJson(request)));
    }

    @ApiOperation("获取圈子ID列表")
    @PostMapping("/getCircleIds")
    public ApiResult<Void> getCircleIds(@RequestBody BaseRequest request) {
        return ApiResult.from(client.post("/wxwork/getCircleIds", toJson(request)));
    }

    @ApiOperation("获取互联内部联系人")
    @PostMapping("/getHuLianInnerContacts")
    public ApiResult<ContactListResponse> getHuLianInnerContacts(@RequestBody BaseRequest request) {
        return ApiResult.from(client.post("/wxwork/GetHuLianInnerContacts", toJson(request)), ContactListResponse.class);
    }

    @ApiOperation("获取群组ID列表")
    @PostMapping("/getGroupIds")
    public ApiResult<Void> getGroupIds(@RequestBody BaseRequest request) {
        return ApiResult.from(client.post("/wxwork/getGroupIds", toJson(request)));
    }

    @ApiOperation("获取群组变更数据")
    @PostMapping("/getGroupChangeData")
    public ApiResult<Void> getGroupChangeData(@RequestBody BaseRequest request) {
        return ApiResult.from(client.post("/wxwork/getGroupChangeData", toJson(request)));
    }

    /**
     * 将请求对象转换为JSONObject
     */
    private JSONObject toJson(Object request) {
        return (JSONObject) JSON.toJSON(request);
    }
}
