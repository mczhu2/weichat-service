package com.weichat.api.service;

import com.alibaba.fastjson.JSONObject;
import com.weichat.api.client.WxWorkApiClient;
import com.weichat.api.entity.ApiResult;
import com.weichat.api.service.RemoteMediaDownloadService.RemoteMediaResource;
import com.weichat.api.vo.response.bigfile.AuthKeyInfo;
import com.weichat.api.vo.response.bigfile.BigAuthKeyResponse;
import com.weichat.api.vo.response.bigfile.BigFileUploadResponse;
import com.weichat.api.vo.response.bigfile.ImageDimension;
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

        // 3. 获取AuthKey和FileKey
        AuthKeyInfo authKeyInfo = getAuthKey(uuid);

        // 4. 上传大文件（不传宽高）
        BigFileUploadResponse response = uploadBigFile(
                uuid,
                authKeyInfo.getAuthKey(),
                authKeyInfo.getFileKey(),
                imageResource.getBytes(),
                imageResource.getFilename(),
                imageResource.getContentType()
        );

        // 5. 补充计算的宽高信息
        response.setWidth(dimension.getWidth());
        response.setHeight(dimension.getHeight());

        return response;
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

        // 2. 获取AuthKey和FileKey
        AuthKeyInfo authKeyInfo = getAuthKey(uuid);

        // 3. 上传大文件
        return uploadBigFile(
                uuid,
                authKeyInfo.getAuthKey(),
                authKeyInfo.getFileKey(),
                videoResource.getBytes(),
                videoResource.getFilename(),
                videoResource.getContentType()
        );
    }

    /**
     * 获取大文件上传AuthKey和FileKey
     *
     * @param uuid 设备UUID
     * @return AuthKey和FileKey信息
     */
    private AuthKeyInfo getAuthKey(String uuid) {
        logger.debug("获取AuthKey, uuid: {}", uuid);

        JSONObject request = new JSONObject();
        request.put("uuid", uuid);

        JSONObject response = wxWorkApiClient.post("/wxwork/GetBigAuthkey", request);

        if (response == null) {
            logger.error("获取AuthKey响应为空");
            throw new IllegalStateException("获取AuthKey响应为空");
        }

        // 检查响应码（支持 code 和 errcode）
        Integer code = response.getInteger("code");
        if (code == null) {
            code = response.getInteger("errcode");
        }

        if (code == null || code != 0) {
            String errorMsg = response.getString("msg");
            if (errorMsg == null) {
                errorMsg = response.getString("errmsg");
            }
            logger.error("获取AuthKey失败, code: {}, msg: {}", code, errorMsg);
            throw new IllegalStateException("获取AuthKey失败: " + errorMsg);
        }

        // 获取 auth_key（下划线格式）
        String authKey = response.getString("auth_key");
        String filekey = response.getString("filekey");
        if (!StringUtils.hasText(authKey)) {
            // 尝试驼峰格式
            authKey = response.getString("AuthKey");
        }
        if (!StringUtils.hasText(authKey)) {
            // 尝试从 data 中获取
            JSONObject data = response.getJSONObject("data");
            if (data != null) {
                authKey = data.getString("auth_key");
                if (!StringUtils.hasText(authKey)) {
                    authKey = data.getString("AuthKey");
                }
                filekey = data.getString("filekey");
            }
        }

        if (!StringUtils.hasText(authKey)) {
            logger.error("AuthKey为空");
            throw new IllegalStateException("AuthKey为空");
        }

        logger.debug("获取filekey: {},AuthKey成功: {}", filekey, authKey );
        return new AuthKeyInfo(authKey, filekey);
    }

    /**
     * 上传大文件
     *
     * @param uuid        设备UUID
     * @param authKey     AuthKey
     * @param fileKey     FileKey
     * @param fileBytes   文件字节数组
     * @param filename    文件名
     * @param contentType 内容类型
     * @return 上传响应
     */
    private BigFileUploadResponse uploadBigFile(String uuid,
                                                String authKey,
                                                String fileKey,
                                                byte[] fileBytes,
                                                String filename,
                                                String contentType) {
        logger.debug("上传大文件, uuid: {}, filename: {}, size: {}", uuid, filename, fileBytes.length);

        // 构建额外参数（只传递 authkey 和 filekey）
        JSONObject extraParams = new JSONObject();
        extraParams.put("authkey", authKey);
        extraParams.put("filekey", fileKey);

        // 调用上传接口
        JSONObject response = wxWorkApiClient.postMultipart(
                "/wxwork/BigUploadFile",
                uuid,
                "file",
                filename,
                contentType,
                fileBytes,
                extraParams
        );

        if (response == null) {
            logger.error("大文件上传响应为空");
            throw new IllegalStateException("大文件上传响应为空");
        }

        // 检查响应码（支持 code 和 errcode）
        Integer code = response.getInteger("code");
        if (code == null) {
            code = response.getInteger("errcode");
        }

        if (code == null || code != 0) {
            String errorMsg = response.getString("msg");
            if (errorMsg == null) {
                errorMsg = response.getString("errmsg");
            }
            logger.error("大文件上传失败, code: {}, msg: {}", code, errorMsg);
            throw new IllegalStateException("大文件上传失败: " + errorMsg);
        }

        // 解析响应数据（支持下划线和驼峰格式）
        BigFileUploadResponse uploadResponse = new BigFileUploadResponse();

        // 数据可能在 data 字段中，也可能直接在响应中
        JSONObject data = response.getJSONObject("data");
        JSONObject source = data != null ? data : response;

        uploadResponse.setFileid(source.getString("file_id"));

        // aes_key 或 aesKey
        String aesKey = source.getString("aes_key");
        if (!StringUtils.hasText(aesKey)) {
            aesKey = source.getString("aesKey");
        }
        uploadResponse.setAesKey(aesKey);

        uploadResponse.setMd5(source.getString("md5"));
        uploadResponse.setSize(source.getInteger("file_size"));

        if (!StringUtils.hasText(uploadResponse.getFileid())) {
            logger.error("上传响应中fileid为空");
            throw new IllegalStateException("上传响应中fileid为空");
        }

        logger.info("大文件上传成功, fileid: {}, size: {}", uploadResponse.getFileid(), uploadResponse.getSize());
        return uploadResponse;
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
}
