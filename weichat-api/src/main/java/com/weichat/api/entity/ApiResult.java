package com.weichat.api.entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * 统一API响应结果（泛型版）
 *
 * @param <T> 响应数据类型
 * @author weichat
 */
@Data
@ApiModel(description = "统一API响应结果")
public class ApiResult<T> {

    @ApiModelProperty(value = "响应码，0表示成功，非0表示失败", example = "0")
    private int code;

    @ApiModelProperty(value = "响应消息", example = "ok")
    private String msg;

    @ApiModelProperty(value = "响应数据")
    private T data;

    public static <T> ApiResult<T> success(T data) {
        ApiResult<T> r = new ApiResult<>();
        r.code = 0;
        r.msg = "ok";
        r.data = data;
        return r;
    }

    public static <T> ApiResult<T> fail(String msg) {
        ApiResult<T> r = new ApiResult<>();
        r.code = -1;
        r.msg = msg;
        return r;
    }

    @SuppressWarnings("unchecked")
    public static <T> ApiResult<T> from(JSONObject resp) {
        if (resp == null) return fail("API调用失败");
        ApiResult<T> r = new ApiResult<>();
        r.code = resp.getIntValue("errcode");
        r.msg = resp.getString("errmsg");
        r.data = (T) resp.get("data");
        return r;
    }

    /**
     * 从JSON响应转换，并指定data类型（单个对象）
     */
    public static <T> ApiResult<T> from(JSONObject resp, Class<T> clazz) {
        if (resp == null) return fail("API调用失败");
        ApiResult<T> r = new ApiResult<>();
        r.code = resp.getIntValue("errcode");
        r.msg = resp.getString("errmsg");
        Object data = resp.get("data");
        if (data != null && data instanceof JSONObject) {
            r.data = ((JSONObject) data).toJavaObject(clazz);
        }
        return r;
    }

    /**
     * 从JSON响应转换，data为列表类型
     */
    public static <E> ApiResult<List<E>> fromList(JSONObject resp, Class<E> elementClass) {
        if (resp == null) return fail("API调用失败");
        ApiResult<List<E>> r = new ApiResult<>();
        r.code = resp.getIntValue("errcode");
        r.msg = resp.getString("errmsg");
        Object data = resp.get("data");
        if (data != null && data instanceof JSONArray) {
            r.data = ((JSONArray) data).toJavaList(elementClass);
        } else {
            r.data = Collections.emptyList();
        }
        return r;
    }
}
