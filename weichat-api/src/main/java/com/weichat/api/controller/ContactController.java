package com.weichat.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.entity.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/contact")
public class ContactController {

    @Autowired
    private WxWorkApiClient client;

    @PostMapping("/getExternalContacts")
    public ApiResult getExternalContacts(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/GetExternalContacts", params));
    }

    @PostMapping("/getInnerContacts")
    public ApiResult getInnerContacts(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/GetInnerContacts", params));
    }

    @PostMapping("/getUserInfoByVids")
    public ApiResult getUserInfoByVids(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/GetUserInfoByVids", params));
    }

    @PostMapping("/agreeUser")
    public ApiResult agreeUser(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/AgreeUser", params));
    }

    @PostMapping("/delContact")
    public ApiResult delContact(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/DelContact", params));
    }

    @PostMapping("/getApplyUserList")
    public ApiResult getApplyUserList(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/GetApplyUserList", params));
    }

    @PostMapping("/getManagedConversations")
    public ApiResult getManagedConversations(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/getManagedConversations", params));
    }

    @PostMapping("/getSessionList")
    public ApiResult getSessionList(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/GetSessionList", params));
    }

    @PostMapping("/updateSetMyUserInfo")
    public ApiResult updateSetMyUserInfo(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/UpdateSetMyUserInfo", params));
    }

    @PostMapping("/setColleagueRemark")
    public ApiResult setColleagueRemark(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/SetColleagueRemark", params));
    }

    @PostMapping("/getColleagueRemarkList")
    public ApiResult getColleagueRemarkList(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/GetColleaRemarkList", params));
    }

    @PostMapping("/setTop")
    public ApiResult setTop(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/SetTop", params));
    }

    @PostMapping("/setShield")
    public ApiResult setShield(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/SetShield", params));
    }

    @PostMapping("/setFold")
    public ApiResult setFold(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/SetTold", params));
    }

    @PostMapping("/setMark")
    public ApiResult setMark(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/SetMark", params));
    }

    @PostMapping("/addBlack")
    public ApiResult addBlack(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/AddSubContactBlack", params));
    }

    @PostMapping("/searchContact")
    public ApiResult searchContact(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/SearchContact", params));
    }

    @PostMapping("/addSearch")
    public ApiResult addSearch(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/AddSearch", params));
    }

    @PostMapping("/addSearchWxwork")
    public ApiResult addSearchWxwork(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/AddSearchWxwork", params));
    }

    @PostMapping("/addBusinessCard")
    public ApiResult addBusinessCard(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/AddBusinessCard", params));
    }

    @PostMapping("/addWxUser")
    public ApiResult addWxUser(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/addWxUser", params));
    }

    @PostMapping("/operCustomer")
    public ApiResult operCustomer(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/OperCustomer", params));
    }

    @PostMapping("/addShareUser")
    public ApiResult addShareUser(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/AddShareUser", params));
    }

    @PostMapping("/getCircleIds")
    public ApiResult getCircleIds(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/getCircleIds", params));
    }

    @PostMapping("/getHuLianInnerContacts")
    public ApiResult getHuLianInnerContacts(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/GetHuLianInnerContacts", params));
    }

    @PostMapping("/getGroupIds")
    public ApiResult getGroupIds(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/getGroupIds", params));
    }

    @PostMapping("/getGroupChangeData")
    public ApiResult getGroupChangeData(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/getGroupChangeData", params));
    }
}
