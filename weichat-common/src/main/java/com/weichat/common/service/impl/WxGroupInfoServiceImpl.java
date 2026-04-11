package com.weichat.common.service.impl;

import com.weichat.common.entity.WxGroupInfo;
import com.weichat.common.mapper.WxGroupInfoMapper;
import com.weichat.common.service.WxGroupInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 微信群信息Service实现类
 */
@Service
public class WxGroupInfoServiceImpl implements WxGroupInfoService {

    @Autowired
    private WxGroupInfoMapper wxGroupInfoMapper;

    @Override
    public int insert(WxGroupInfo wxGroupInfo) {
        return wxGroupInfoMapper.insert(wxGroupInfo);
    }

    @Override
    public WxGroupInfo selectByPrimaryKey(Long id) {
        return wxGroupInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public WxGroupInfo selectByRoomId(String roomId) {
        return wxGroupInfoMapper.selectByRoomId(roomId);
    }

    @Override
    public List<WxGroupInfo> selectAll() {
        return wxGroupInfoMapper.selectAll();
    }

    @Override
    public int updateByPrimaryKey(WxGroupInfo wxGroupInfo) {
        return wxGroupInfoMapper.updateByPrimaryKey(wxGroupInfo);
    }

    @Override
    public int deleteByPrimaryKey(Long id) {
        return wxGroupInfoMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<WxGroupInfo> selectByCreateUserId(Long createUserId) {
        return wxGroupInfoMapper.selectByCreateUserId(createUserId);
    }

    @Override
    public int batchInsert(List<WxGroupInfo> list) {
        return wxGroupInfoMapper.batchInsert(list);
    }

    @Override
    public List<WxGroupInfo> selectAllWithPaging(int offset, int limit) {
        return wxGroupInfoMapper.selectAllWithPaging(offset, limit);
    }

    @Override
    public List<WxGroupInfo> selectByUuidWithPaging(String uuid, int offset, int limit) {
        return wxGroupInfoMapper.selectByUuidWithPaging(uuid, offset, limit);
    }

    @Override
    public List<WxGroupInfo> selectByFiltersWithPaging(Long corpId, String uuid, int offset, int limit) {
        return wxGroupInfoMapper.selectByFiltersWithPaging(corpId, uuid, offset, limit);
    }
}
