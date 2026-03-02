package com.weichat.api.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.entity.ApiResult;
import com.weichat.api.vo.request.BaseRequest;
import com.weichat.api.vo.request.group.*;
import com.weichat.api.vo.response.group.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 群管理Controller
 *
 * @author weichat
 */
@Api(tags = "群管理")
@RestController
@RequestMapping("/api/v1/group")
public class GroupController {

    @Autowired
    private WxWorkApiClient client;

    @ApiOperation("获取聊天室成员")
    @PostMapping("/getChatroomMembers")
    public ApiResult<RoomMemberListResponse> getChatroomMembers(@RequestBody RoomIdRequest request) {
        return ApiResult.from(client.post("/wxwork/GetChatroomMembers", toJson(request)), RoomMemberListResponse.class);
    }

    @ApiOperation("获取群用户列表")
    @PostMapping("/getRoomUserList")
    public ApiResult<List<RoomMemberInfo>> getRoomUserList(@RequestBody RoomIdRequest request) {
        return ApiResult.fromList(client.post("/wxwork/GetRoomUserList", toJson(request)), RoomMemberInfo.class);
    }

    @ApiOperation("获取会话群列表")
    @PostMapping("/getSessionRoomList")
    public ApiResult<List<RoomInfo>> getSessionRoomList(@RequestBody BaseRequest request) {
        return ApiResult.fromList(client.post("/wxwork/GetSessionRoomList", toJson(request)), RoomInfo.class);
    }

    @ApiOperation("同意加群申请")
    @PostMapping("/agreeAddRoom")
    public ApiResult<Void> agreeAddRoom(@RequestBody AgreeAddRoomRequest request) {
        return ApiResult.from(client.post("/wxwork/AgreeAddRoom", toJson(request)));
    }

    @ApiOperation("名片加群同意")
    @PostMapping("/agreeCardAddRoom")
    public ApiResult<Void> agreeCardAddRoom(@RequestBody AgreeAddRoomRequest request) {
        return ApiResult.from(client.post("/wxwork/AgreeCardAddRoom", toJson(request)));
    }

    @ApiOperation("创建内部群")
    @PostMapping("/createRoomInner")
    public ApiResult<CreateRoomResponse> createRoomInner(@RequestBody CreateRoomRequest request) {
        return ApiResult.from(client.post("/wxwork/CreateRoomNei", toJson(request)), CreateRoomResponse.class);
    }

    @ApiOperation("创建企微群")
    @PostMapping("/createRoomWx")
    public ApiResult<CreateRoomResponse> createRoomWx(@RequestBody CreateRoomRequest request) {
        return ApiResult.from(client.post("/wxwork/CreateRoomWx", toJson(request)), CreateRoomResponse.class);
    }

    @ApiOperation("发送群公告")
    @PostMapping("/sendNotice")
    public ApiResult<Void> sendNotice(@RequestBody SendNoticeRequest request) {
        return ApiResult.from(client.post("/wxwork/SendNotice", toJson(request)));
    }

    @ApiOperation("更新群名称")
    @PostMapping("/updateRoomName")
    public ApiResult<Void> updateRoomName(@RequestBody UpdateRoomNameRequest request) {
        return ApiResult.from(client.post("/wxwork/UpdateRoomName", toJson(request)));
    }

    @ApiOperation("邀请链接进群")
    @PostMapping("/invitationToRoomLink")
    public ApiResult<InviteLinkResponse> invitationToRoomLink(@RequestBody RoomIdRequest request) {
        return ApiResult.from(client.post("/wxwork/InvitationToRoomLink", toJson(request)), InviteLinkResponse.class);
    }

    @ApiOperation("邀请进群")
    @PostMapping("/invitationToRoom")
    public ApiResult<Void> invitationToRoom(@RequestBody RoomMemberOperRequest request) {
        return ApiResult.from(client.post("/wxwork/InvitationToRoom", toJson(request)));
    }

    @ApiOperation("删除群成员")
    @PostMapping("/delRoomUsers")
    public ApiResult<Void> delRoomUsers(@RequestBody RoomMemberOperRequest request) {
        return ApiResult.from(client.post("/wxwork/DelRoomUsers", toJson(request)));
    }

    @ApiOperation("设置我的群昵称")
    @PostMapping("/setMyRoomName")
    public ApiResult<Void> setMyRoomName(@RequestBody SetMyRoomNameRequest request) {
        return ApiResult.from(client.post("/wxwork/SetMyRoomName", toJson(request)));
    }

    @ApiOperation("转移群主")
    @PostMapping("/transferOwner")
    public ApiResult<Void> transferOwner(@RequestBody TransferOwnerRequest request) {
        return ApiResult.from(client.post("/wxwork/TransferChatroomOwner", toJson(request)));
    }

    @ApiOperation("解散群")
    @PostMapping("/dissolutionRoom")
    public ApiResult<Void> dissolutionRoom(@RequestBody RoomIdRequest request) {
        return ApiResult.from(client.post("/wxwork/DissolutionRoom", toJson(request)));
    }

    @ApiOperation("企微群邀请")
    @PostMapping("/wxRoomInvite")
    public ApiResult<Void> wxRoomInvite(@RequestBody RoomMemberOperRequest request) {
        return ApiResult.from(client.post("/wxwork/WxRoomInvite", toJson(request)));
    }

    @ApiOperation("获取欢迎语列表")
    @PostMapping("/getWelcomeMsgList")
    public ApiResult<Void> getWelcomeMsgList(@RequestBody BaseRequest request) {
        return ApiResult.from(client.post("/wxwork/GetWelcomeMsgList", toJson(request)));
    }

    @ApiOperation("添加欢迎语")
    @PostMapping("/addWelcomeMsg")
    public ApiResult<Void> addWelcomeMsg(@RequestBody SendNoticeRequest request) {
        return ApiResult.from(client.post("/wxwork/AddWelcomeMsg", toJson(request)));
    }

    @ApiOperation("设置欢迎语")
    @PostMapping("/setWelcomeMsg")
    public ApiResult<Void> setWelcomeMsg(@RequestBody SetWelcomeMsgRequest request) {
        return ApiResult.from(client.post("/wxwork/SetWelcomeMsg", toJson(request)));
    }

    @ApiOperation("删除欢迎语")
    @PostMapping("/delWelcomeMsg")
    public ApiResult<Void> delWelcomeMsg(@RequestBody DelWelcomeMsgRequest request) {
        return ApiResult.from(client.post("/wxwork/DelWelcomeMsg", toJson(request)));
    }

    @ApiOperation("添加群好友")
    @PostMapping("/addRoomFriends")
    public ApiResult<Void> addRoomFriends(@RequestBody AddRoomFriendsRequest request) {
        return ApiResult.from(client.post("/wxwork/AddRoomFriends", toJson(request)));
    }

    @ApiOperation("退出群聊")
    @PostMapping("/outRoom")
    public ApiResult<Void> outRoom(@RequestBody RoomIdRequest request) {
        return ApiResult.from(client.post("/wxwork/OutRoomReq", toJson(request)));
    }

    @ApiOperation("获取群用户描述列表")
    @PostMapping("/getRoomDesUserList")
    public ApiResult<List<RoomMemberInfo>> getRoomDesUserList(@RequestBody RoomIdRequest request) {
        return ApiResult.fromList(client.post("/wxwork/GetRoomDesUserList", toJson(request)), RoomMemberInfo.class);
    }

    @ApiOperation("获取群信息")
    @PostMapping("/getRoomInfo")
    public ApiResult<RoomInfo> getRoomInfo(@RequestBody RoomIdRequest request) {
        return ApiResult.from(client.post("/wxwork/GetRoomInfo", toJson(request)), RoomInfo.class);
    }

    @ApiOperation("设置企微群头像")
    @PostMapping("/wxRoomHeader")
    public ApiResult<Void> wxRoomHeader(@RequestBody WxRoomHeaderRequest request) {
        return ApiResult.from(client.post("/wxwork/WxRoomHeader", toJson(request)));
    }

    @ApiOperation("群名备注")
    @PostMapping("/roomnameRemark")
    public ApiResult<Void> roomnameRemark(@RequestBody RoomnameRemarkRequest request) {
        return ApiResult.from(client.post("/wxwork/RoomnameRemarkReq", toJson(request)));
    }

    @ApiOperation("禁止修改群名")
    @PostMapping("/disableRenameChatroom")
    public ApiResult<Void> disableRenameChatroom(@RequestBody RoomSettingRequest request) {
        return ApiResult.from(client.post("/wxwork/DisableRenameChatroom", toJson(request)));
    }

    @ApiOperation("设置群邀请确认")
    @PostMapping("/setChatroomInvite")
    public ApiResult<Void> setChatroomInvite(@RequestBody RoomSettingRequest request) {
        return ApiResult.from(client.post("/wxwork/SetChatroomInvite", toJson(request)));
    }

    @ApiOperation("删除群管理员")
    @PostMapping("/delRoomAdmins")
    public ApiResult<Void> delRoomAdmins(@RequestBody RoomMemberOperRequest request) {
        return ApiResult.from(client.post("/wxwork/DelRoomAdmins", toJson(request)));
    }

    @ApiOperation("添加群管理员")
    @PostMapping("/addRoomAdmins")
    public ApiResult<Void> addRoomAdmins(@RequestBody RoomMemberOperRequest request) {
        return ApiResult.from(client.post("/wxwork/AddRoomAdmins", toJson(request)));
    }

    @ApiOperation("查询反垃圾规则")
    @PostMapping("/queryAntiSpamRule")
    public ApiResult<Void> queryAntiSpamRule(@RequestBody RoomIdRequest request) {
        return ApiResult.from(client.post("/wxwork/queryCRMAntiSpamRule", toJson(request)));
    }

    @ApiOperation("设置群反垃圾")
    @PostMapping("/setRoomAnti")
    public ApiResult<Void> setRoomAnti(@RequestBody RoomSettingRequest request) {
        return ApiResult.from(client.post("/wxwork/setRoomAnti", toJson(request)));
    }

    @ApiOperation("获取群黑名单")
    @PostMapping("/getRoomBlackList")
    public ApiResult<List<RoomMemberInfo>> getRoomBlackList(@RequestBody RoomIdRequest request) {
        return ApiResult.fromList(client.post("/wxwork/getRoomBlackList", toJson(request)), RoomMemberInfo.class);
    }

    @ApiOperation("添加/移除群黑名单")
    @PostMapping("/addOrSubRoomBlackList")
    public ApiResult<Void> addOrSubRoomBlackList(@RequestBody RoomBlackListRequest request) {
        return ApiResult.from(client.post("/wxwork/addOrSubRoomBlackList", toJson(request)));
    }

    @ApiOperation("设置群管理")
    @PostMapping("/setRoomManagement")
    public ApiResult<Void> setRoomManagement(@RequestBody RoomIdRequest request) {
        return ApiResult.from(client.post("/wxwork/setRoomManagement", toJson(request)));
    }

    /**
     * 将请求对象转换为JSONObject
     */
    private JSONObject toJson(Object request) {
        return (JSONObject) JSON.toJSON(request);
    }
}
