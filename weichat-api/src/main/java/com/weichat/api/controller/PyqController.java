package com.weichat.api.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.entity.ApiResult;
import com.weichat.api.enums.PyqFileType;
import com.weichat.api.service.BigFileUploadService;
import com.weichat.api.vo.request.moment.PostPyqV2Request;
import com.weichat.api.vo.response.bigfile.BigFileUploadResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 朋友圈发布Controller
 * <p>
 * 提供朋友圈发布功能，支持多种内容类型：
 * <ul>
 *   <li>纯文字朋友圈</li>
 *   <li>图片朋友圈（支持多图）</li>
 *   <li>视频朋友圈（视频+封面图）</li>
 *   <li>链接朋友圈（链接+封面图）</li>
 * </ul>
 * </p>
 *
 * @author weichat
 * @since 1.0
 */
@Api(tags = "朋友圈发布")
@RestController
@RequestMapping("/wxwork")
public class PyqController {

    private static final Logger logger = LoggerFactory.getLogger(PyqController.class);

    @Autowired
    private WxWorkApiClient wxWorkApiClient;

    @Autowired
    private BigFileUploadService bigFileUploadService;

    /**
     * 发布朋友圈
     * <p>
     * 支持的内容类型组合：
     * <ul>
     *   <li>纯文字：content + 空file_content</li>
     *   <li>图片：content + file_content[type=1, type=1, ...]（可多张）</li>
     *   <li>视频：content + file_content[type=3, type=2]（封面图+视频，顺序固定）</li>
     *   <li>链接：content + file_content[type=1] + link_info（封面图+链接信息）</li>
     * </ul>
     * </p>
     *
     * @param request 朋友圈发布请求
     * @return 发布结果
     */
    @ApiOperation(value = "发布朋友圈", notes = "支持文字、图片、视频、链接等多种类型的朋友圈发布")
    @PostMapping(value = "/PostPyq", consumes = "application/json", produces = "application/json")
    public ApiResult<JSONObject> postPyq(@RequestBody PostPyqV2Request request) {
        logger.info("发布朋友圈请求, uuid: {}, content: {}, fileCount: {}, hasLink: {}",
                request.getUuid(),
                request.getContent(),
                request.getFileContent() != null ? request.getFileContent().size() : 0,
                request.getLinkInfo() != null);

        // 参数校验
        validateRequest(request);

        // 处理文件上传到CDN
        if (request.getFileContent() != null && !request.getFileContent().isEmpty()) {
            processFileUploads(request);
        }

        // 转换为JSON并调用企业微信API
        JSONObject requestJson = (JSONObject) JSON.toJSON(request);
        JSONObject response = wxWorkApiClient.post("/wxwork/PostPyq", requestJson);

        logger.info("朋友圈发布响应: {}", response);

        return ApiResult.from(response);
    }

    /**
     * 处理文件上传（使用大文件上传接口）
     * <p>
     * 所有图片和视频都通过大文件上传接口处理：
     * <ul>
     *   <li>1. 调用 /wxwork/GetBigAuthkey 获取 AuthKey</li>
     *   <li>2. 调用 /wxwork/BigUploadFile 上传文件</li>
     *   <li>3. 图片自动计算宽高</li>
     *   <li>4. 将返回的信息填充到请求对象中</li>
     * </ul>
     * </p>
     *
     * @param request 朋友圈发布请求
     */
    private void processFileUploads(PostPyqV2Request request) {
        for (PostPyqV2Request.FileContent file : request.getFileContent()) {
            // 如果已经有fileid，说明已经上传过，跳过
            if (StringUtils.hasText(file.getFileid())) {
                logger.debug("文件已有fileid，跳过上传: {}", file.getFileid());
                continue;
            }

            // 检查是否有url
            if (!StringUtils.hasText(file.getUrl())) {
                throw new IllegalArgumentException("文件必须提供url字段");
            }

            // 获取文件类型枚举
            PyqFileType fileType = PyqFileType.fromCode(file.getType());
            if (fileType == null) {
                throw new IllegalArgumentException("不支持的文件类型: " + file.getType());
            }

            BigFileUploadResponse uploadResponse;
            try {
                // 所有类型都使用大文件上传接口
                if (fileType.isImageType()) {
                    // 图片或视频封面图（自动计算宽高）
                    uploadResponse = bigFileUploadService.uploadImageByUrl(request.getUuid(), file.getUrl());
                } else if (fileType.isVideoType()) {
                    // 视频
                    uploadResponse = bigFileUploadService.uploadVideoByUrl(request.getUuid(), file.getUrl());
                } else {
                    throw new IllegalArgumentException("不支持的文件类型: " + fileType.getDescription());
                }

                // 填充大文件上传返回的信息
                fillBigFileInfo(file, uploadResponse);

                logger.info("大文件上传成功, type: {}, fileid: {}, size: {}",
                        fileType.getDescription(), file.getFileid(), file.getSize());

            } catch (Exception e) {
                logger.error("大文件上传失败, type: {}, url: {}", fileType.getDescription(), file.getUrl(), e);
                throw new IllegalStateException("大文件上传失败: " + e.getMessage(), e);
            }
        }
    }

    /**
     * 填充大文件上传返回的信息到文件对象
     *
     * @param file           文件内容对象
     * @param uploadResponse 大文件上传响应
     */
    private void fillBigFileInfo(PostPyqV2Request.FileContent file, BigFileUploadResponse uploadResponse) {
        file.setFileid(uploadResponse.getFileid());
        file.setMd5(uploadResponse.getMd5());
        file.setSize(uploadResponse.getSize());

        if (uploadResponse.getWidth() != null) {
            file.setWidth(uploadResponse.getWidth());
        }
        if (uploadResponse.getHeight() != null) {
            file.setHeight(uploadResponse.getHeight());
        }
        if (uploadResponse.getVideoLen() != null) {
            file.setVideoLen(uploadResponse.getVideoLen());
        }
    }

    /**
     * 校验请求参数
     *
     * @param request 请求对象
     * @throws IllegalArgumentException 参数不合法时抛出
     */
    private void validateRequest(PostPyqV2Request request) {
        if (request.getUuid() == null || request.getUuid().trim().isEmpty()) {
            throw new IllegalArgumentException("uuid不能为空");
        }

        // 校验文件内容类型组合
        if (request.getFileContent() != null && !request.getFileContent().isEmpty()) {
            boolean hasImage = false;
            boolean hasVideo = false;
            boolean hasVideoCover = false;

            for (PostPyqV2Request.FileContent file : request.getFileContent()) {
                if (file.getType() == null) {
                    throw new IllegalArgumentException("文件类型type不能为空");
                }

                // 获取文件类型枚举
                PyqFileType fileType = PyqFileType.fromCode(file.getType());
                if (fileType == null) {
                    throw new IllegalArgumentException("不支持的文件类型: " + file.getType());
                }

                // 统计各类型文件
                switch (fileType) {
                    case IMAGE:
                        hasImage = true;
                        break;
                    case VIDEO:
                        hasVideo = true;
                        break;
                    case VIDEO_COVER:
                        hasVideoCover = true;
                        break;
                }

                // 校验必填字段（如果没有fileid，则必须有url）
                if (!StringUtils.hasText(file.getFileid())) {
                    if (!StringUtils.hasText(file.getUrl())) {
                        throw new IllegalArgumentException("文件必须提供url或fileid");
                    }
                }
            }

            // 校验类型组合规则
            if (hasImage && (hasVideo || hasVideoCover)) {
                throw new IllegalArgumentException("图片类型不能与视频类型共存");
            }

            if (hasVideo && !hasVideoCover) {
                throw new IllegalArgumentException("发送视频朋友圈必须包含封面图");
            }

            if (hasVideoCover && !hasVideo) {
                throw new IllegalArgumentException("视频封面图必须与视频一起使用");
            }
        }

        // 校验链接信息
        if (request.getLinkInfo() != null) {
            if (request.getLinkInfo().getTitle() == null || request.getLinkInfo().getTitle().trim().isEmpty()) {
                throw new IllegalArgumentException("链接标题title不能为空");
            }
            if (request.getLinkInfo().getContentUrl() == null || request.getLinkInfo().getContentUrl().trim().isEmpty()) {
                throw new IllegalArgumentException("链接URL contentUrl不能为空");
            }

            // 链接朋友圈必须有封面图
            if (request.getFileContent() == null || request.getFileContent().isEmpty()) {
                throw new IllegalArgumentException("链接朋友圈必须包含封面图");
            }
        }

        logger.debug("请求参数校验通过");
    }
}
