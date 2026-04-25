package com.weichat.api.entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * 统一 API 响应结果。
 *
 * @param <T> 响应数据类型
 */
@Data
@ApiModel(description = "统一 API 响应结果")
public class ApiResult<T> {

    @ApiModelProperty(value = "响应码，0 表示成功，非 0 表示失败", example = "0")
    private int code;

    @ApiModelProperty(value = "响应消息", example = "ok")
    private String msg;

    @ApiModelProperty(value = "响应数据")
    private T data;

    @ApiModelProperty(value = "总条数，分页列表场景返回", example = "125")
    private Integer total;

    public static <T> ApiResult<T> success(T data) {
        ApiResult<T> result = new ApiResult<>();
        result.code = 0;
        result.msg = "ok";
        result.data = data;
        return result;
    }

    public static <T> ApiResult<T> success(T data, Integer total) {
        ApiResult<T> result = success(data);
        result.total = total;
        return result;
    }

    public static <T> ApiResult<T> fail(String msg) {
        ApiResult<T> result = new ApiResult<>();
        result.code = -1;
        result.msg = msg;
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T> ApiResult<T> from(JSONObject resp) {
        if (resp == null) {
            return fail("API 调用失败");
        }
        ApiResult<T> result = new ApiResult<>();
        result.code = resp.getIntValue("errcode");
        result.msg = resp.getString("errmsg");
        result.data = (T) resp.get("data");
        if (resp.containsKey("total")) {
            result.total = resp.getInteger("total");
        }
        return result;
    }

    public static <T> ApiResult<T> from(JSONObject resp, Class<T> clazz) {
        if (resp == null) {
            return fail("API 调用失败");
        }
        ApiResult<T> result = new ApiResult<>();
        result.code = resp.getIntValue("errcode");
        result.msg = resp.getString("errmsg");
        Object data = resp.get("data");
        if (data instanceof JSONObject) {
            result.data = ((JSONObject) data).toJavaObject(clazz);
        }
        if (resp.containsKey("total")) {
            result.total = resp.getInteger("total");
        }
        return result;
    }

    public static <E> ApiResult<List<E>> fromList(JSONObject resp, Class<E> elementClass) {
        if (resp == null) {
            return fail("API 调用失败");
        }
        ApiResult<List<E>> result = new ApiResult<>();
        result.code = resp.getIntValue("errcode");
        result.msg = resp.getString("errmsg");
        Object data = resp.get("data");
        if (data instanceof JSONArray) {
            result.data = ((JSONArray) data).toJavaList(elementClass);
        } else {
            result.data = Collections.emptyList();
        }
        if (resp.containsKey("total")) {
            result.total = resp.getInteger("total");
        }
        return result;
    }
}
