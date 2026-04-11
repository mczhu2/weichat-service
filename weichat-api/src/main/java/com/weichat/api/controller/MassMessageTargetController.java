package com.weichat.api.controller;

import com.weichat.api.entity.ApiResult;
import com.weichat.common.entity.WxGroupInfo;
import com.weichat.common.entity.WxUserInfo;
import com.weichat.common.service.WxGroupInfoService;
import com.weichat.common.service.WxUserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "Mass Message Target")
@RestController
@RequestMapping({"/api/v1/mass-message-targets", "/api/v1/query"})
public class MassMessageTargetController {

    @Autowired
    private WxUserInfoService wxUserInfoService;

    @Autowired
    private WxGroupInfoService wxGroupInfoService;

    @ApiOperation("List external contacts for mass messaging")
    @GetMapping("/external-contacts")
    public ApiResult<List<WxUserInfo>> listExternalContacts(
            @RequestParam(required = false) String uuid,
            @RequestParam(required = false) List<Long> corpIds,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize) {

        int safePageNum = Math.max(pageNum, 1);
        int safePageSize = Math.max(pageSize, 1);
        int offset = (safePageNum - 1) * safePageSize;

        String normalizedUuid = normalize(uuid);
        List<Long> normalizedCorpIds = corpIds == null || corpIds.isEmpty() ? null : corpIds;
        List<WxUserInfo> contacts = wxUserInfoService.selectByFiltersWithPaging(
                normalizedUuid,
                normalizedCorpIds,
                offset,
                safePageSize
        );

        return ApiResult.success(contacts);
    }

    @ApiOperation("List groups for mass messaging")
    @GetMapping("/groups")
    public ApiResult<List<WxGroupInfo>> listGroups(
            @RequestParam(required = false) String uuid,
            @RequestParam Long corpId,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize) {

        int safePageNum = Math.max(pageNum, 1);
        int safePageSize = Math.max(pageSize, 1);
        int offset = (safePageNum - 1) * safePageSize;

        List<WxGroupInfo> groups = wxGroupInfoService.selectByFiltersWithPaging(
                corpId,
                normalize(uuid),
                offset,
                safePageSize
        );

        return ApiResult.success(groups);
    }

    private String normalize(String value) {
        return hasText(value) ? value.trim() : null;
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
