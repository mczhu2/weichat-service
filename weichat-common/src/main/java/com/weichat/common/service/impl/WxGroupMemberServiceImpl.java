package com.weichat.common.service.impl;

import com.weichat.common.entity.WxGroupMember;
import com.weichat.common.mapper.WxGroupMemberMapper;
import com.weichat.common.service.WxGroupMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 微信群成员信息Service实现类
 */
@Service
public class WxGroupMemberServiceImpl implements WxGroupMemberService {

    @Autowired
    private WxGroupMemberMapper wxGroupMemberMapper;

    @Override
    public int insert(WxGroupMember wxGroupMember) {
        return wxGroupMemberMapper.insert(wxGroupMember);
    }

    @Override
    public WxGroupMember selectByPrimaryKey(Long id) {
        return wxGroupMemberMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<WxGroupMember> selectByRoomId(String roomId) {
        return wxGroupMemberMapper.selectByRoomId(roomId);
    }

    @Override
    public WxGroupMember selectByRoomIdAndUin(String roomId, Long uin) {
        return wxGroupMemberMapper.selectByRoomIdAndUin(roomId, uin);
    }

    @Override
    public List<WxGroupMember> selectAll() {
        return wxGroupMemberMapper.selectAll();
    }

    @Override
    public int updateByPrimaryKey(WxGroupMember wxGroupMember) {
        return wxGroupMemberMapper.updateByPrimaryKey(wxGroupMember);
    }

    @Override
    public int deleteByPrimaryKey(Long id) {
        return wxGroupMemberMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int deleteByRoomId(String roomId) {
        return wxGroupMemberMapper.deleteByRoomId(roomId);
    }

    @Override
    public int batchInsert(List<WxGroupMember> list) {
        return wxGroupMemberMapper.batchInsert(list);
    }

    @Override
    public List<WxGroupMember> selectByUin(Long uin) {
        return wxGroupMemberMapper.selectByUin(uin);
    }
}
