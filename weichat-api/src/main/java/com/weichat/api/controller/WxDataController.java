package com.weichat.api.controller;

import com.weichat.common.entity.WxFriendInfo;
import com.weichat.common.entity.WxGroupInfo;
import com.weichat.common.entity.WxGroupMember;
import com.weichat.common.entity.WxMessageInfo;
import com.weichat.common.entity.WxUserInfo;
import com.weichat.common.service.WxFriendInfoService;
import com.weichat.common.service.WxGroupInfoService;
import com.weichat.common.service.WxGroupMemberService;
import com.weichat.common.service.WxMessageInfoService;
import com.weichat.common.service.WxUserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信数据处理Controller
 * 提供对五张表的数据新增接口
 */
@Api(tags = "数据管理")
@RestController
@RequestMapping("/api/v1/wx")
public class WxDataController {

    @Autowired
    private WxUserInfoService wxUserInfoService;

    @Autowired
    private WxMessageInfoService wxMessageInfoService;

    @Autowired
    private WxGroupInfoService wxGroupInfoService;

    @Autowired
    private WxGroupMemberService wxGroupMemberService;

    @Autowired
    private WxFriendInfoService wxFriendInfoService;

    @ApiOperation("新增微信用户信息")
    @PostMapping("/user")
    public ResponseEntity<String> addWxUser(@RequestBody WxUserInfo wxUserInfo) {
        try {
            int result = wxUserInfoService.insert(wxUserInfo);
            if (result > 0) {
                return ResponseEntity.ok("微信用户信息新增成功");
            } else {
                return ResponseEntity.badRequest().body("微信用户信息新增失败");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("微信用户信息新增失败: " + e.getMessage());
        }
    }

    @ApiOperation("新增微信消息信息")
    @PostMapping("/message")
    public ResponseEntity<String> addWxMessage(@RequestBody WxMessageInfo wxMessageInfo) {
        try {
            int result = wxMessageInfoService.insert(wxMessageInfo);
            if (result > 0) {
                return ResponseEntity.ok("微信消息信息新增成功");
            } else {
                return ResponseEntity.badRequest().body("微信消息信息新增失败");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("微信消息信息新增失败: " + e.getMessage());
        }
    }

    @ApiOperation("新增微信群信息")
    @PostMapping("/group")
    public ResponseEntity<String> addWxGroup(@RequestBody WxGroupInfo wxGroupInfo) {
        try {
            int result = wxGroupInfoService.insert(wxGroupInfo);
            if (result > 0) {
                return ResponseEntity.ok("微信群信息新增成功");
            } else {
                return ResponseEntity.badRequest().body("微信群信息新增失败");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("微信群信息新增失败: " + e.getMessage());
        }
    }

    @ApiOperation("新增微信群成员信息")
    @PostMapping("/group-member")
    public ResponseEntity<String> addWxGroupMember(@RequestBody WxGroupMember wxGroupMember) {
        try {
            int result = wxGroupMemberService.insert(wxGroupMember);
            if (result > 0) {
                return ResponseEntity.ok("微信群成员信息新增成功");
            } else {
                return ResponseEntity.badRequest().body("微信群成员信息新增失败");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("微信群成员信息新增失败: " + e.getMessage());
        }
    }

    @ApiOperation("新增微信好友信息")
    @PostMapping("/friend")
    public ResponseEntity<String> addWxFriend(@RequestBody WxFriendInfo wxFriendInfo) {
        try {
            int result = wxFriendInfoService.insert(wxFriendInfo);
            if (result > 0) {
                return ResponseEntity.ok("微信好友信息新增成功");
            } else {
                return ResponseEntity.badRequest().body("微信好友信息新增失败");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("微信好友信息新增失败: " + e.getMessage());
        }
    }

    @ApiOperation("查询所有微信消息")
    @GetMapping("/messages")
    public ResponseEntity<String> getAllWxMessages() {
        try {
            List<WxMessageInfo> messageList = wxMessageInfoService.selectAll();
            if (messageList != null && !messageList.isEmpty()) {
                return ResponseEntity.ok("查询成功，共找到 " + messageList.size() + " 条消息");
            } else {
                return ResponseEntity.badRequest().body("未找到消息");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("查询消息失败: " + e.getMessage());
        }
    }
}
