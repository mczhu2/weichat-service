package com.weichat.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.entity.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/group")
public class GroupController {

    @Autowired
    private WxWorkApiClient client;

    @PostMapping("/getChatroomMembers")
    public ApiResult getChatroomMembers(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/GetChatroomMembers", params));
    }

    @PostMapping("/getRoomUserList")
    public ApiResult getRoomUserList(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/GetRoomUserList", params));
    }

    @PostMapping("/getSessionRoomList")
    public ApiResult getSessionRoomList(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/GetSessionRoomList", params));
    }

    @PostMapping("/agreeAddRoom")
    public ApiResult agreeAddRoom(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/AgreeAddRoom", params));
    }

    @PostMapping("/agreeCardAddRoom")
    public ApiResult agreeCardAddRoom(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/AgreeCardAddRoom", params));
    }

    @PostMapping("/createRoomInner")
    public ApiResult createRoomInner(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/CreateRoomNei", params));
    }

    @PostMapping("/createRoomWx")
    public ApiResult createRoomWx(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/CreateRoomWx", params));
    }

    @PostMapping("/sendNotice")
    public ApiResult sendNotice(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/SendNotice", params));
    }

    @PostMapping("/updateRoomName")
    public ApiResult updateRoomName(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/UpdateRoomName", params));
    }

    @PostMapping("/invitationToRoomLink")
    public ApiResult invitationToRoomLink(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/InvitationToRoomLink", params));
    }

    @PostMapping("/invitationToRoom")
    public ApiResult invitationToRoom(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/InvitationToRoom", params));
    }

    @PostMapping("/delRoomUsers")
    public ApiResult delRoomUsers(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/DelRoomUsers", params));
    }

    @PostMapping("/setMyRoomName")
    public ApiResult setMyRoomName(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/SetMyRoomName", params));
    }

    @PostMapping("/transferOwner")
    public ApiResult transferOwner(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/TransferChatroomOwner", params));
    }

    @PostMapping("/dissolutionRoom")
    public ApiResult dissolutionRoom(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/DissolutionRoom", params));
    }

    @PostMapping("/wxRoomInvite")
    public ApiResult wxRoomInvite(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/WxRoomInvite", params));
    }

    @PostMapping("/getWelcomeMsgList")
    public ApiResult getWelcomeMsgList(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/GetWelcomeMsgList", params));
    }

    @PostMapping("/addWelcomeMsg")
    public ApiResult addWelcomeMsg(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/AddWelcomeMsg", params));
    }

    @PostMapping("/setWelcomeMsg")
    public ApiResult setWelcomeMsg(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/SetWelcomeMsg", params));
    }

    @PostMapping("/delWelcomeMsg")
    public ApiResult delWelcomeMsg(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/DelWelcomeMsg", params));
    }

    @PostMapping("/addRoomFriends")
    public ApiResult addRoomFriends(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/AddRoomFriends", params));
    }

    @PostMapping("/outRoom")
    public ApiResult outRoom(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/OutRoomReq", params));
    }

    @PostMapping("/getRoomDesUserList")
    public ApiResult getRoomDesUserList(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/GetRoomDesUserList", params));
    }

    @PostMapping("/getRoomInfo")
    public ApiResult getRoomInfo(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/GetRoomInfo", params));
    }

    @PostMapping("/wxRoomHeader")
    public ApiResult wxRoomHeader(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/WxRoomHeader", params));
    }

    @PostMapping("/roomnameRemark")
    public ApiResult roomnameRemark(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/RoomnameRemarkReq", params));
    }

    @PostMapping("/disableRenameChatroom")
    public ApiResult disableRenameChatroom(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/DisableRenameChatroom", params));
    }

    @PostMapping("/setChatroomInvite")
    public ApiResult setChatroomInvite(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/SetChatroomInvite", params));
    }

    @PostMapping("/delRoomAdmins")
    public ApiResult delRoomAdmins(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/DelRoomAdmins", params));
    }

    @PostMapping("/addRoomAdmins")
    public ApiResult addRoomAdmins(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/AddRoomAdmins", params));
    }

    @PostMapping("/queryAntiSpamRule")
    public ApiResult queryAntiSpamRule(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/queryCRMAntiSpamRule", params));
    }

    @PostMapping("/setRoomAnti")
    public ApiResult setRoomAnti(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/setRoomAnti", params));
    }

    @PostMapping("/getRoomBlackList")
    public ApiResult getRoomBlackList(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/getRoomBlackList", params));
    }

    @PostMapping("/addOrSubRoomBlackList")
    public ApiResult addOrSubRoomBlackList(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/addOrSubRoomBlackList", params));
    }

    @PostMapping("/setRoomManagement")
    public ApiResult setRoomManagement(@RequestBody JSONObject params) {
        return ApiResult.from(client.post("/wxwork/setRoomManagement", params));
    }
}
