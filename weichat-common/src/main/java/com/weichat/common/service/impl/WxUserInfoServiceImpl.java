package com.weichat.common.service.impl;

import com.weichat.common.entity.WxUserInfo;
import com.weichat.common.mapper.WxUserInfoMapper;
import com.weichat.common.service.WxUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 微信用户信息Service实现类
 */
@Service
public class WxUserInfoServiceImpl implements WxUserInfoService {

    @Autowired
    private WxUserInfoMapper wxUserInfoMapper;

    @Override
    public int insert(WxUserInfo wxUserInfo) {
        return wxUserInfoMapper.insert(wxUserInfo);
    }

    @Override
    public WxUserInfo selectByPrimaryKey(Long id) {
        return wxUserInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public WxUserInfo selectByUnionid(String unionid) {
        return wxUserInfoMapper.selectByUnionid(unionid);
    }

    @Override
    public List<WxUserInfo> selectAll() {
        return wxUserInfoMapper.selectAll();
    }

    @Override
    public int updateByPrimaryKey(WxUserInfo wxUserInfo) {
        return wxUserInfoMapper.updateByPrimaryKey(wxUserInfo);
    }

    @Override
    public int deleteByPrimaryKey(Long id) {
        return wxUserInfoMapper.deleteByPrimaryKey(id);
    }

    @Override
    public WxUserInfo selectByUserId(Long userId) {
        return wxUserInfoMapper.selectByUserId(userId);
    }

    @Override
    public int batchInsert(List<WxUserInfo> list) {
        return wxUserInfoMapper.batchInsert(list);
    }

    @Override
    public WxUserInfo selectByUserIdAndCorpId(Long userId, Long corpId) {
        return wxUserInfoMapper.selectByUserIdAndCorpId(userId, corpId);
    }

    @Override
    public WxUserInfo selectByUuid(String uuid) {
        return wxUserInfoMapper.selectByUuid(uuid);
    }

    @Override
    public List<WxUserInfo> selectAllWithPaging(int offset, int limit) {
        return wxUserInfoMapper.selectAllWithPaging(offset, limit);
    }

    @Override
    public List<WxUserInfo> selectByUuidWithPaging(String uuid, int offset, int limit) {
        return wxUserInfoMapper.selectByUuidWithPaging(uuid, offset, limit);
    }

    @Override
    public List<WxUserInfo> selectByFiltersWithPaging(String uuid, List<Long> corpIds, int offset, int limit) {
        return wxUserInfoMapper.selectByFiltersWithPaging(uuid, corpIds, offset, limit);
    }
}
