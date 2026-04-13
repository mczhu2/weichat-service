package com.weichat.api.service.mass.sender;

import com.alibaba.fastjson.JSONObject;
import com.weichat.api.vo.request.mass.MassTaskAppPayload;

public class MassTaskAppMessageMaterial {

    private String desc;
    private String appName;
    private String title;
    private String weappIconUrl;
    private String pagepath;
    private String username;
    private String appid;
    private MassTaskMediaMaterial coverMaterial;

    public static MassTaskAppMessageMaterial fromPayload(MassTaskAppPayload payload) {
        MassTaskAppMessageMaterial material = new MassTaskAppMessageMaterial();
        material.desc = payload.getDesc();
        material.appName = payload.getAppName();
        material.title = payload.getTitle();
        material.weappIconUrl = payload.getWeappIconUrl();
        material.pagepath = payload.getPagepath();
        material.username = payload.getUsername();
        material.appid = payload.getAppid();
        if (payload.getCover() != null) {
            material.coverMaterial = MassTaskMediaMaterial.fromPayload(payload.getCover());
        }
        return material;
    }

    public static MassTaskAppMessageMaterial fromJson(JSONObject json) {
        MassTaskAppMessageMaterial material = new MassTaskAppMessageMaterial();
        material.desc = json.getString("desc");
        material.appName = MassTaskMediaMaterial.readText(json, "appName", "app_name");
        material.title = json.getString("title");
        material.weappIconUrl = MassTaskMediaMaterial.readText(json, "weappIconUrl", "weapp_icon_url");
        material.pagepath = json.getString("pagepath");
        material.username = json.getString("username");
        material.appid = json.getString("appid");
        JSONObject coverJson = json.getJSONObject("cover");
        if (coverJson != null) {
            material.coverMaterial = MassTaskMediaMaterial.fromJson(coverJson);
        } else if (json.containsKey("cdnkey") || json.containsKey("cdn_key") || json.containsKey("fileid")) {
            material.coverMaterial = MassTaskMediaMaterial.fromJson(json);
        }
        return material;
    }

    public String getDesc() { return desc; }
    public String getAppName() { return appName; }
    public String getTitle() { return title; }
    public String getWeappIconUrl() { return weappIconUrl; }
    public String getPagepath() { return pagepath; }
    public String getUsername() { return username; }
    public String getAppid() { return appid; }
    public MassTaskMediaMaterial getCoverMaterial() { return coverMaterial; }
}
