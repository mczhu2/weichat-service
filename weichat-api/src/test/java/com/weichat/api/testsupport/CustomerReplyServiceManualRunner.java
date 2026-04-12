package com.weichat.api.testsupport;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.weichat.api.ApiApplication;
import com.weichat.api.entity.ApiResult;
import com.weichat.api.service.CustomerReplyService;
import com.weichat.api.service.MessageSendService;
import com.weichat.api.vo.request.message.SendVoiceRequest;
import com.weichat.api.vo.response.cdn.CdnUploadResponse;
import com.weichat.api.vo.response.message.SendMsgResponse;
import com.weichat.common.entity.WxMessageInfo;
import com.weichat.common.entity.WxUserInfo;
import com.weichat.common.service.WxMessageInfoService;
import com.weichat.common.service.WxUserInfoService;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class CustomerReplyServiceManualRunner {

    private static final long TEST_MESSAGE_ID = 502L;
    private static final String TEST_UPLOAD_UUID = "b5593fb8bd69537ef978e5760ef9076b";
    private static final String CDN_UPLOAD_URL = "http://47.94.7.218:9952/wxwork/CdnUploadFile";
    private static final int TEST_VOICE_SECONDS = 1;
    private static final String SAMPLE_MP3_DATA_URL =
            "data:audio/mpeg;base64,"
                    + "SUQzBAAAAAAAIlRTU0UAAAAOAAADTGF2ZjYxLjcuMTAwAAAAAAAAAAAAAAD/83DAAAAAAAAAAAAASW5mbwAAAA8AAAAWAAAJsQAdHR0dKCgoKCgzMzMzPT09PT1ISEhIU1NTU1NeXl5eaWlpaWlzc3Nzfn5+fn6JiYmJlJSUlJSenp6enqmpqam0tLS0tL+/v7/KysrKytTU1NTf39/f3+rq6ur19fX19f////8AAAAATGF2YzYxLjE5AAAAAAAAAAAAAAAAJAQvAAAAAAAACbFHrf5/AAAAAAAAAAAAAAAAAP/zQMQAFGiGcBdYGAB/5KAmOmOmOkOkWpvA7oKkLZmE5tic6nWZtSChqbuO7kYhh/H8jEYpLDuBgYGLB94gBAEMuH+jdy/hjgN/DHL+4MRACYP5MEHYDP935cPggGNKbERQwgYDAgEA//NCxAkWyXKdn5poAgAAGBJhaT9yXiWSEUWGDSEGzAS0C0xbImtSABPoyJ6JiJb+FuBagVr8kR6j1Mv8cwwxNHqPX/zIvF4xLpdS//y8SRiXS6ZF4vHf8qEgaEoSBorVH23kuuuH7par//NAxAkSwFZIf94AANAFAAMAEHAwGwUDBVASMDkLIxCw3jZTJJMRYM8oCUSKMAMAhX7EXalc7stWgXVFeRu1u/8V/68r/9n79i/9n//97jeiKqP/etwUYw0q0WtJhGAKABZqQw0IGqT/80LEGREgSiRU5/RAeBN7IqK2LKcxXnkPM6nq6Po0H1P/lVVk/17Ze3YQpOPMM3ousRRud9Xa+bWA2YSpj963WekyP3k0mGoATAGwBY1zkhhB8YaEs2hNHYCMiEk2ajd+/roZ36q73fz/80DEMBIASiD05/RAVJ3ns2nZpHsbavQx44h7B6cw3/0WPTSb/WOplhRkJZYWANE4wAUAAMA2ANziEDcI54HDipM1vpNP1xXeqsvZRRUdTinp7snq9l+/q5a6c7m0s1nYuzZdiD992v/zQsRDEUBOFADv9kCKaGE/H90DPTH+ai5yQoCAATAKwDI3Z00xOFBBIZUk8sisBFlqlu30P9fI1U08OirKOypDc70sKer8ltu6l3Clza11ci7oZQoMv1+V1nxgRQTGXNSGAQAEYBUAcv/zQMRaEVhKGCLv9kBuphyEcIDhw0r15pDbVLtTt/7Pt3q3/j2e6po1zW/Smq3iu4WoAc83Rs2dN3t/r67MxcABGlkTWaKgVMEESxlTkqlMAkBI/D37wm5SMDTtoY2Pnb9FX123/3aP//NCxG8RaPoYIv7EZPYN//2/2av2usf//q1qwAASIZ/fNr+JcjBSaWtTLlNIgopJmesml7HKDFf0bma/96KP+3//Z/6/p+BPr3dPHRqmLH/rGs8Jn/eJDLClyTAGQDA1a01FO2EEgrJo//NAxIUNmEYttD+yJBZ6wK7cniqkP7/R/qqO2M8X8W1f1froTcjXv9Vm7u9E+5X9b1Mr6MD2C9TAAAAUtMYAWAGGAeANpx+C8odQPgouQpZNAtGNMqppVv+6PR+n9e8EIsZfepbhg8X/80LEqQw4PlJ+Drwgz11tBda0Njp2qqnO7DUczZtU0ZdeWln+rKLU116VJGpv3ljWZiYHCFEAEAALumADABJgGIDEb3+rZnGjQOHU1mmxa8hX3f6ono6697Fn01qgl9/d/R+rIO3xS0n/80DE1A+QSiBM5/RALzLfvTyV2zfqOb808KGQERubs3tJ6tBMQU2AUq+/uOVVuRgZgRWWlRWMADABDAJAFc2q5PkPsVCCSlrjRW+pl0ReS2t+jeS6N+702/f9FpqPoo8Zk7GJa4xU+f/zQsTwFaD6EAD+ymQe9+dtF9GbbRUBGiiK5hzvGT5llct/dlSwJqXlxgWAxgEAJgCAAuYCKBKHNSvUZgXIBgYA0ABl0mCv1RBCmX60sujIlirTPRtO2g60uLpLNWto089LSuoKHrkgpv/zQMT1FTlWFEr+xGSBQJF3uqFFBOm4DIMMjSOpI88kwlmhaH1LdrWSiNVMQU1FMy4xMDBVgAAKiMAb7z1hQeMHgS9YrOkTjAIAtPyTLo9g0rndl1keeVaEvQ9H7G3/+JFteirnYhFa//NCxPgVGVYY6v6EZBCdPMoEStdCtX0X6sx20WPbRJXqUS/jhSwEX1MIdEiDAHwBYwBIAPMAkAMzAbQP49n/CjOHlwxkFQEFkNWXSEYKtO73N9RMpCSNX8fxNtbONuZZK4JFibnill0h//NAxP8ZUPoMAO/ESCYU69ZpQ1B3XE8Qse9Q1zFONi3naoz4d9ql5meYGk3D89xN9+uk65xj9pbeMiiuotBqHIJ405TRRBtexFzAdZyRut5Y1qrIjBQApMwAgABLNGAFgCRgGQEib8H/80LE6hGYRipcR7IkO5hyBaYiAIosuh3pTFfmrbWtPZt9D8/d4NkJbs+27VfYatNmLtBLTsl7ugiTrS0u1Ng32Osbof0Uv+qwVipi0nf1jlAJrm4IpKCggABMARATDSKUjM45Aviy6Hb/80DE/yJizgAo/xBlmxFUUxxHAJJzNe3R/6nW9lbftW2n1X/mIDqLVx85QhC2p8r3WySKEX/AIAYCAHXsYBAEBgUATGDsCaDgnTBzCDMOIQYwpg0Tc4fKMHsKIw7ArDBYA4MDAAwuu//zQsTQFXFWEAD+xGTgytFN34vACgCARXD1gLANxKJ863NgZG8nBcFBVjQ9D2eAh7PZ5ElY48BD1HHw8eRNZpq9/R5EurGTV73/9KU+bw74fxwIcaIwfKAgNBBcMCQMLB/w+Iz6vhgoc//zQMTWEWBKIEzn9EDraXfw/1eCFf31kCECBsh2brEJpMv/yrdmkjJd1AT/nvbZqCcyRl3+AFtA4IwDOBQ/YcY+OBlD4QkACRZk6PFBhlBFwxUYoHUfDZQ6gvhOYnBqVXw1SGrhPIm0//NCxOsmWXI4A154AFaCUxkletf8cJFhyiPIAaEkVSA1/V/5SKZmePl4xOHDc0NS9////mJigX3NUjI6YMfAIj//8kBgkkxBTUUzLjEwMKqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq//NAxK0iaopYMZugAKqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqr/80LEfgAAA0gBwAAAqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqo=";

    public static void main(String[] args) {
        String mode = resolveMode(args);
        ConfigurableApplicationContext context = new SpringApplicationBuilder(ApiApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);

        try {
            TestContext testContext = loadTestContext(context);
            System.out.println("Test wxMessageInfo: " + JSON.toJSONString(testContext.getWxMessageInfo()));
            System.out.println("Test receiverUser: " + JSON.toJSONString(testContext.getReceiverUser()));
            System.out.println("Test mode: " + mode);

            long start = System.currentTimeMillis();
            if ("voice_base64_temp".equals(mode)) {
                runVoiceBase64Temp(context, testContext, args);
                long cost = System.currentTimeMillis() - start;
                System.out.println("voice_base64_temp finished. costMs=" + cost);
                return;
            }

            CustomerReplyService customerReplyService = context.getBean(CustomerReplyService.class);
            String callbackBody = buildCallbackBody(mode);
            System.out.println("Test callbackBody: " + callbackBody);
            customerReplyService.sendReplyToCustomer(
                    testContext.getWxMessageInfo(),
                    testContext.getReceiverUser(),
                    callbackBody
            );
            long cost = System.currentTimeMillis() - start;
            System.out.println("CustomerReplyService.sendReplyToCustomer finished. mode=" + mode + ", costMs=" + cost);
        } finally {
            context.close();
        }
    }

    private static void runVoiceBase64Temp(ConfigurableApplicationContext context,
                                           TestContext testContext,
                                           String[] args) {
        MessageSendService messageSendService = context.getBean(MessageSendService.class);

        String base64Source = resolveBase64Source(args);
        Path tempMp3 = saveBase64ToTempMp3(base64Source);
        System.out.println("Temp mp3: " + tempMp3.toAbsolutePath());

        String uuid = resolveUploadUuid(testContext.getReceiverUser());
        System.out.println("Upload uuid: " + uuid);

        ApiResult<CdnUploadResponse> uploadResult = uploadVoiceByCurl(uuid, tempMp3);
        System.out.println("CdnUploadFile result: " + JSON.toJSONString(uploadResult));
        if (uploadResult == null || uploadResult.getCode() != 0 || uploadResult.getData() == null) {
            throw new IllegalStateException("CdnUploadFile failed: " + JSON.toJSONString(uploadResult));
        }

        WxMessageInfo wxMessageInfo = testContext.getWxMessageInfo();
        SendVoiceRequest request = SendVoiceRequest.builder()
                .uuid(uuid)
                .send_userid(wxMessageInfo.getSender())
                .kf_id(wxMessageInfo.getKfId())
                .isRoom(false)
                .cdnkey(resolveCdnKey(uploadResult.getData()))
                .aeskey(uploadResult.getData().getAes_key())
                .md5(uploadResult.getData().getMd5())
                .voice_time(TEST_VOICE_SECONDS)
                .fileSize(uploadResult.getData().getSize())
                .build();
        System.out.println("SendVoiceRequest: " + JSON.toJSONString(request));

        ApiResult<SendMsgResponse> sendResult = messageSendService.sendVoice(request);
        System.out.println("SendCDNVoiceMsg result: " + JSON.toJSONString(sendResult));
    }

    private static TestContext loadTestContext(ConfigurableApplicationContext context) {
        WxMessageInfoService wxMessageInfoService = context.getBean(WxMessageInfoService.class);
        WxUserInfoService wxUserInfoService = context.getBean(WxUserInfoService.class);

        WxMessageInfo wxMessageInfo = wxMessageInfoService.selectByPrimaryKey(TEST_MESSAGE_ID);
        if (wxMessageInfo == null) {
            throw new IllegalStateException("wxMessageInfo id=" + TEST_MESSAGE_ID + " not found");
        }

        WxUserInfo receiverUser = wxUserInfoService.selectByUserId(wxMessageInfo.getReceiver());
        if (receiverUser == null) {
            throw new IllegalStateException("receiverUser not found by receiver userId=" + wxMessageInfo.getReceiver());
        }
        return new TestContext(wxMessageInfo, receiverUser);
    }

    private static String resolveMode(String[] args) {
        if (args == null || args.length == 0) {
            return "all";
        }
        String mode = args[0] == null ? "" : args[0].trim().toLowerCase();
        if ("text".equals(mode)
                || "image".equals(mode)
                || "voice".equals(mode)
                || "voice_base64".equals(mode)
                || "all".equals(mode)
                || "voice_base64_temp".equals(mode)) {
            return mode;
        }
        return "all";
    }

    private static String buildCallbackBody(String mode) {
        JSONObject callbackBody = new JSONObject();
        callbackBody.put("ok", true);
        if ("text".equals(mode) || "all".equals(mode)) {
            callbackBody.put("reply", "hello");
        }

        if ("image".equals(mode) || "all".equals(mode)) {
            JSONArray images = new JSONArray();
            JSONObject image = new JSONObject();
            image.put("url", "https://pics1.baidu.com/feed/4034970a304e251f9df1360c3e7ae6077d3e53c8.jpeg");
            image.put("filename", "reply-image.jpeg");
            image.put("contentType", "image/jpeg");
            images.add(image);
            callbackBody.put("images", images);
        }

        if ("voice".equals(mode) || "voice_base64".equals(mode) || "all".equals(mode)) {
            JSONArray voices = new JSONArray();
            JSONObject voice = new JSONObject();
            if ("voice_base64".equals(mode)) {
                voice.put("base64", SAMPLE_MP3_DATA_URL);
            } else {
                voice.put("url", "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3");
                voice.put("filename", "SoundHelix-Song-1.mp3");
                voice.put("contentType", "audio/mpeg");
            }
            voice.put("voice_time", 10);
            voices.add(voice);
            callbackBody.put("voices", voices);
        }

        return callbackBody.toJSONString();
    }

    private static String resolveBase64Source(String[] args) {
        if (args != null && args.length > 1 && StringUtils.hasText(args[1])) {
            Path path = Paths.get(args[1]);
            if (Files.exists(path)) {
                try {
                    return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
                } catch (IOException e) {
                    throw new IllegalStateException("Failed to read base64 file: " + path, e);
                }
            }
            return args[1];
        }
        return SAMPLE_MP3_DATA_URL;
    }

    private static Path saveBase64ToTempMp3(String base64Source) {
        String source = base64Source == null ? "" : base64Source.trim();
        String encoded = source;
        int commaIndex = source.indexOf(',');
        if (source.startsWith("data:") && commaIndex > 0) {
            encoded = source.substring(commaIndex + 1);
        }

        try {
            byte[] bytes = Base64.getDecoder().decode(encoded.replaceAll("\\s", ""));
            Path tempFile = Files.createTempFile("customer-reply-voice-", ".mp3");
            Files.write(tempFile, bytes);
            tempFile.toFile().deleteOnExit();
            return tempFile;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to save base64 mp3 temp file", e);
        }
    }

    private static String resolveUploadUuid(WxUserInfo receiverUser) {
        if (receiverUser != null && StringUtils.hasText(receiverUser.getUuid())) {
            return receiverUser.getUuid().trim();
        }
        return TEST_UPLOAD_UUID;
    }

    private static ApiResult<CdnUploadResponse> uploadVoiceByCurl(String uuid, Path tempMp3) {
        List<String> command = new ArrayList<String>();
        command.add("curl.exe");
        command.add("--silent");
        command.add("--show-error");
        command.add("--location");
        command.add("--request");
        command.add("POST");
        command.add(CDN_UPLOAD_URL);
        command.add("--header");
        command.add("Priority: u=0");
        command.add("--form");
        command.add("uuid=" + uuid);
        command.add("--form");
        command.add("file=@" + tempMp3.toAbsolutePath() + ";type=audio/mpeg");

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);

        try {
            Process process = processBuilder.start();
            String output = readFully(process.getInputStream());
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IllegalStateException("curl upload failed, exitCode=" + exitCode + ", output=" + output);
            }

            JSONObject response = JSON.parseObject(output);
            return ApiResult.from(response, CdnUploadResponse.class);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to upload voice file by curl", e);
        }
    }

    private static String readFully(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int read;
        while ((read = inputStream.read(buffer)) >= 0) {
            outputStream.write(buffer, 0, read);
        }
        return new String(outputStream.toByteArray(), StandardCharsets.UTF_8).trim();
    }

    private static String resolveCdnKey(CdnUploadResponse uploadResponse) {
        if (uploadResponse == null) {
            return null;
        }
        if (StringUtils.hasText(uploadResponse.getCdn_key())) {
            return uploadResponse.getCdn_key();
        }
        return uploadResponse.getFileid();
    }

    private static class TestContext {
        private final WxMessageInfo wxMessageInfo;
        private final WxUserInfo receiverUser;

        private TestContext(WxMessageInfo wxMessageInfo, WxUserInfo receiverUser) {
            this.wxMessageInfo = wxMessageInfo;
            this.receiverUser = receiverUser;
        }

        public WxMessageInfo getWxMessageInfo() {
            return wxMessageInfo;
        }

        public WxUserInfo getReceiverUser() {
            return receiverUser;
        }
    }
}
