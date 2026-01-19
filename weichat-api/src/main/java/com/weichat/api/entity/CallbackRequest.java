package com.weichat.api.entity;

/**
 * 回调请求参数对象
 */
public class CallbackRequest {
    
    /**
     * uuid 运行实例id
     */
    private String uuid;
    
    /**
     * 回调消息内容为json格式
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
    
    public void setJson(String json) {
        this.json = json;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
}
