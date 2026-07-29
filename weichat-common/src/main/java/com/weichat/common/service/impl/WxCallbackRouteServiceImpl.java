package com.weichat.common.service.impl;

import com.weichat.common.entity.WxCallbackRoute;
import com.weichat.common.mapper.WxCallbackRouteMapper;
import com.weichat.common.service.WxCallbackRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 企微业务系统回调路由 Service 实现。
 */
@Service
public class WxCallbackRouteServiceImpl implements WxCallbackRouteService {

    @Autowired
    private WxCallbackRouteMapper wxCallbackRouteMapper;

    @Override
    public int upsert(WxCallbackRoute route) {
        return wxCallbackRouteMapper.upsert(route);
    }

    @Override
    public WxCallbackRoute selectByUuid(String uuid) {
        return wxCallbackRouteMapper.selectByUuid(uuid);
    }
}
