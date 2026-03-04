package com.weichat.common.service.impl;

import com.weichat.common.entity.WxFriendInfo;
import com.weichat.common.mapper.WxFriendInfoMapper;
import com.weichat.common.service.WxFriendInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 微信好友信息Service实现类
 */
@Service
public class WxFriendInfoServiceImpl implements WxFriendInfoService {

    @Autowired
    private WxFriendInfoMapper wxFriendInfoMapper;

    @Override
    public int insert(WxFriendInfo wxFriendInfo) {
        return wxFriendInfoMapper.insert(wxFriendInfo);
    }

    @Override
    public WxFriendInfo selectByPrimaryKey(Long id) {
        return wxFriendInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public WxFriendInfo selectByOwnerUserIdAndUnionid(Long ownerUserId, String unionid, Integer isExternal) {
        return wxFriendInfoMapper.selectByOwnerUserIdAndUnionid(ownerUserId, unionid, isExternal);
    }

    @Override
    public WxFriendInfo selectByUserId(Long userId) {
        return wxFriendInfoMapper.selectByUserId(userId);
    }

    @Override
    public List<WxFriendInfo> selectAll() {
        return wxFriendInfoMapper.selectAll();
    }

    @Override
    public int updateByPrimaryKey(WxFriendInfo wxFriendInfo) {
        return wxFriendInfoMapper.updateByPrimaryKey(wxFriendInfo);
    }

    @Override
    public int deleteByPrimaryKey(Long id) {
        return wxFriendInfoMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<WxFriendInfo> selectByStatus(Integer status) {
        return wxFriendInfoMapper.selectByStatus(status);
    }

    @Override
    public int batchInsert(List<WxFriendInfo> list) {
        return wxFriendInfoMapper.batchInsert(list);
    }

    @Override
    public List<WxFriendInfo> selectByCorpId(Long corpId) {
        return wxFriendInfoMapper.selectByCorpId(corpId);
    }
}
