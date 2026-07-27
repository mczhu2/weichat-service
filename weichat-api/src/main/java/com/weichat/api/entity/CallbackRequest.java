package com.weichat.api.entity;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * 回调请求参数对象
 */
public class CallbackRequest {
    
    /**
     * uuid 运行实例id
     */
    private String uuid;
    
    /**
     * 回调消息内容 JSON 字符串。
     * <p>下游回调可能将 json 作为对象或字符串传入，统一归一化为字符串，兼容现有策略解析链路。</p>
     */
    private String json;
    
    /**
     * 消息类型
     */
    private String type;
    
    public String getUuid() {
        return uuid;
    }
    
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    
    public String getJson() {
        return json;
    }
    
    @JsonSetter("json")
    public void setJson(Object json) {
        if (json == null) {
            this.json = null;
        } else if (json instanceof String) {
            this.json = (String) json;
        } else {
            this.json = JSON.toJSONString(json);
        }
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
}
