package com.weichat.api.controller;

import com.weichat.api.entity.ApiResult;
import com.weichat.common.entity.WxFriendInfo;
import com.weichat.common.entity.WxGroupInfo;
import com.weichat.common.service.WxFriendInfoService;
import com.weichat.common.service.WxGroupInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 群发目标查询控制器，分别提供圈人和圈群的筛选接口。
 */
@Api(tags = "群发目标查询")
@RestController
@RequestMapping({"/api/v1/mass-message-targets", "/api/v1/query"})
public class MassMessageTargetController {

    @Autowired
    private WxFriendInfoService wxFriendInfoService;

    @Autowired
    private WxGroupInfoService wxGroupInfoService;

    /**
     * 分页查询圈人目标。
     * uuid 和 corpIds 都是可选条件，corpIds 支持多选筛选。
     */
    @ApiOperation("分页查询圈人目标")
    @GetMapping("/external-contacts")
    public ApiResult<List<WxFriendInfo>> listExternalContacts(
            @RequestParam(required = false) String uuid,
            @RequestParam(required = false) List<Long> corpIds,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize) {

        int safePageNum = Math.max(pageNum, 1);
        int safePageSize = Math.max(pageSize, 1);
        int offset = (safePageNum - 1) * safePageSize;

        String normalizedUuid = normalize(uuid);
        List<Long> normalizedCorpIds = corpIds == null || corpIds.isEmpty() ? null : corpIds;
        List<WxFriendInfo> contacts = wxFriendInfoService.selectExternalByFiltersWithPaging(
                normalizedUuid,
                normalizedCorpIds,
                offset,
                safePageSize
        );

        return ApiResult.success(contacts);
    }

    /**
     * 分页查询圈群目标。
     * corpIds 为必填条件，uuid 为可选条件，corpIds 支持多选筛选。
     */
    @ApiOperation("分页查询圈群目标")
    @GetMapping("/groups")
    public ApiResult<List<WxGroupInfo>> listGroups(
            @RequestParam(required = false) String uuid,
            @RequestParam List<Long> corpIds,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize) {

        int safePageNum = Math.max(pageNum, 1);
        int safePageSize = Math.max(pageSize, 1);
        int offset = (safePageNum - 1) * safePageSize;

        List<Long> normalizedCorpIds = corpIds == null || corpIds.isEmpty() ? null : corpIds;
        List<WxGroupInfo> groups = wxGroupInfoService.selectByFiltersWithPaging(
                normalizedCorpIds,
                normalize(uuid),
                offset,
                safePageSize
        );

        return ApiResult.success(groups);
    }

    /**
     * 统一处理空白字符串，避免把空串传入查询条件。
     */
    private String normalize(String value) {
        return hasText(value) ? value.trim() : null;
    }

    /**
     * 判断字符串是否包含有效文本。
     */
    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
