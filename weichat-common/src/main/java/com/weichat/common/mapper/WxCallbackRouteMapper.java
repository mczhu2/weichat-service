package com.weichat.common.mapper;

import com.weichat.common.entity.WxCallbackRoute;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 企微业务系统回调路由 Mapper。
 */
@Mapper
public interface WxCallbackRouteMapper {

    /**
     * 新增或更新 uuid 对应的业务系统回调地址。
     *
     * @param route 回调路由
     * @return 影响行数
     */
    int upsert(WxCallbackRoute route);

    /**
     * 根据 uuid 查询业务系统回调路由。
     *
     * @param uuid 企微运行实例 UUID
     * @return 回调路由，不存在时返回 null
     */
    WxCallbackRoute selectByUuid(@Param("uuid") String uuid);
}
