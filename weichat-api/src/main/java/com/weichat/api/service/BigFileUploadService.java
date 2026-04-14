package com.weichat.api.service;

import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.entity.ApiResult;
import com.weichat.api.service.RemoteMediaDownloadService.RemoteMediaResource;
import com.weichat.api.vo.response.bigfile.BigAuthKeyResponse;
import com.weichat.api.vo.response.bigfile.BigFileUploadResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * 大文件上传服务
 * <p>
 * 用于朋友圈图片、视频等大文件的上传，包括：
 * <ul>
 *   <li>获取大文件上传AuthKey</li>
 *   <li>上传大文件到企业微信</li>
 *   <li>自动计算图片宽高</li>
 * </ul>
 * </p>
 *
 * @author weichat
 * @since 1.0
 */
@Service
public class BigFileUploadService {

    private static final Logger logger = LoggerFactory.getLogger(BigFileUploadService.class);

    @Autowired
    private WxWorkApiClient wxWorkApiClient;

    @Autowired
    private RemoteMediaDownloadService remoteMediaDownloadService;

    /**
     * 通过URL上传图片（大文件方式）
     * <p>
     * 自动下载图片、计算宽高、获取AuthKey并上传。
     * </p>
     *
     * @param uuid     设备UUID
     * @param imageUrl 图片URL
     * @return 上传响应
     */
    public BigFileUploadResponse uploadImageByUrl(String uuid, String imageUrl) {
        logger.info("开始上传图片, uuid: {}, url: {}", uuid, imageUrl);

        // 1. 下载图片
        RemoteMediaResource imageResource = remoteMediaDownloadService.download(
                imageUrl,
                null,
                "image/png",
                "pyq-image"
        );

        // 2. 计算图片宽高
        ImageDimension dimension = calculateImageDimension(imageResource.getBytes());
        logger.debug("图片尺寸: {}x{}", dimension.getWidth(), dimension.getHeight());

        // 3. 获取AuthKey
        String authKey = getAuthKey(uuid);

        // 4. 上传大文件
        return uploadBigFile(
                uuid,
                authKey,
                imageResource.getBytes(),
                imageResource.getFilename(),
                imageResource.getContentType(),
                dimension.getWidth(),
                dimension.getHeight(),
                null
        );
    }

    /**
     * 通过URL上传视频（大文件方式）
     * <p>
     * 自动下载视频、获取AuthKey并上传。
     * </p>
     *
     * @param uuid     设备UUID
     * @param videoUrl 视频URL
     * @return 上传响应
     */
    public BigFileUploadResponse uploadVideoByUrl(String uuid, String videoUrl) {
        logger.info("开始上传视频, uuid: {}, url: {}", uuid, videoUrl);

        // 1. 下载视频
        RemoteMediaResource videoResource = remoteMediaDownloadService.download(
                videoUrl,
                null,
                "video/mp4",
                "pyq-video"
        );

        // 2. 获取AuthKey
        String authKey = getAuthKey(uuid);

        // 3. 上传大文件
        return uploadBigFile(
                uuid,
                authKey,
                videoResource.getBytes(),
                videoResource.getFilename(),
                videoResource.getContentType(),
                null,
                null,
                null
        );
    }

    /**
     * 获取大文件上传AuthKey
     *
     * @param uuid 设备UUID
     * @return AuthKey
     */
    private String getAuthKey(String uuid) {
        logger.debug("获取AuthKey, uuid: {}", uuid);

        JSONObject request = new JSONObject();
        request.put("uuid", uuid);

        JSONObject response = wxWorkApiClient.post("/wxwork/GetBigAuthkey", request);

        ApiResult<BigAuthKeyResponse> result = ApiResult.from(response, BigAuthKeyResponse.class);
        if (result == null || result.getCode() != 0) {
            String errorMsg = result != null ? result.getMsg() : "获取AuthKey失败";
            logger.error("获取AuthKey失败: {}", errorMsg);
            throw new IllegalStateException("获取AuthKey失败: " + errorMsg);
        }

        if (result.getData() == null || !StringUtils.hasText(result.getData().getAuthKey())) {
            logger.error("AuthKey为空");
            throw new IllegalStateException("AuthKey为空");
        }

        String authKey = result.getData().getAuthKey();
        logger.debug("获取AuthKey成功: {}", authKey);
        return authKey;
    }

    /**
     * 上传大文件
     *
     * @param uuid        设备UUID
     * @param authKey     AuthKey
     * @param fileBytes   文件字节数组
     * @param filename    文件名
     * @param contentType 内容类型
     * @param width       图片宽度（图片类型必填）
     * @param height      图片高度（图片类型必填）
     * @param videoLen    视频时长（视频类型选填）
     * @return 上传响应
     */
    private BigFileUploadResponse uploadBigFile(String uuid,
                                                String authKey,
                                                byte[] fileBytes,
                                                String filename,
                                                String contentType,
                                                Integer width,
                                                Integer height,
                                                Integer videoLen) {
        logger.debug("上传大文件, uuid: {}, filename: {}, size: {}", uuid, filename, fileBytes.length);

        // 构建请求参数
        JSONObject request = new JSONObject();
        request.put("uuid", uuid);
        request.put("authKey", authKey);
        if (width != null) {
            request.put("width", width);
        }
        if (height != null) {
            request.put("height", height);
        }
        if (videoLen != null) {
            request.put("videoLen", videoLen);
        }

        // 调用上传接口
        JSONObject response = wxWorkApiClient.postMultipart(
                "/wxwork/BigUploadFile",
                uuid,
                "file",
                filename,
                contentType,
                fileBytes,
                request
        );

        ApiResult<BigFileUploadResponse> result = ApiResult.from(response, BigFileUploadResponse.class);
        if (result == null || result.getCode() != 0) {
            String errorMsg = result != null ? result.getMsg() : "大文件上传失败";
            logger.error("大文件上传失败: {}", errorMsg);
            throw new IllegalStateException("大文件上传失败: " + errorMsg);
        }

        if (result.getData() == null) {
            logger.error("上传响应数据为空");
            throw new IllegalStateException("上传响应数据为空");
        }

        logger.info("大文件上传成功, fileid: {}, size: {}", result.getData().getFileid(), result.getData().getSize());
        return result.getData();
    }

    /**
     * 计算图片尺寸
     *
     * @param imageBytes 图片字节数组
     * @return 图片尺寸
     */
    private ImageDimension calculateImageDimension(byte[] imageBytes) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes)) {
            BufferedImage image = ImageIO.read(inputStream);
            if (image == null) {
                logger.warn("无法读取图片，使用默认尺寸");
                return new ImageDimension(0, 0);
            }
            return new ImageDimension(image.getWidth(), image.getHeight());
        } catch (IOException e) {
            logger.error("计算图片尺寸失败", e);
            return new ImageDimension(0, 0);
        }
    }

    /**
     * 图片尺寸内部类
     */
    private static class ImageDimension {
        private final int width;
        private final int height;

        public ImageDimension(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }
}
