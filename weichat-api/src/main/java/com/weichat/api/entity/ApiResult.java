package com.weichat.api.entity;

import com.alibaba.fastjson.JSONObject;

public class ApiResult {

    private int code;
    private String msg;
    private Object data;

    public static ApiResult success(Object data) {
        ApiResult r = new ApiResult();
        r.code = 0;
        r.msg = "ok";
        r.data = data;
        return r;
    }

    public static ApiResult fail(String msg) {
        ApiResult r = new ApiResult();
        r.code = -1;
        r.msg = msg;
        return r;
    }

    public static ApiResult from(JSONObject resp) {
        if (resp == null) return fail("API调用失败");
        ApiResult r = new ApiResult();
        r.code = resp.getIntValue("errcode");
        r.msg = resp.getString("errmsg");
        r.data = resp.get("data");
        return r;
    }

    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }
    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }
}
