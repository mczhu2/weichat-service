package com.weichat.common.service.impl;

import com.weichat.common.entity.WxMessageInfo;
import com.weichat.common.mapper.WxMessageInfoMapper;
import com.weichat.common.service.WxMessageInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 微信消息信息Service实现类
 */
@Service
public class WxMessageInfoServiceImpl implements WxMessageInfoService {

    @Autowired
    private WxMessageInfoMapper wxMessageInfoMapper;

    @Override
    public int insert(WxMessageInfo wxMessageInfo) {
        return wxMessageInfoMapper.insert(wxMessageInfo);
    }

    @Override
    public WxMessageInfo selectByPrimaryKey(Long id) {
        return wxMessageInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public WxMessageInfo selectByMsgId(Long msgId) {
        return wxMessageInfoMapper.selectByMsgId(msgId);
    }

    @Override
    public List<WxMessageInfo> selectAll() {
        return wxMessageInfoMapper.selectAll();
    }

    @Override
    public int updateByPrimaryKey(WxMessageInfo wxMessageInfo) {
        return wxMessageInfoMapper.updateByPrimaryKey(wxMessageInfo);
    }

    @Override
    public int deleteByPrimaryKey(Long id) {
        return wxMessageInfoMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<WxMessageInfo> selectByReceiver(Long receiver) {
        return wxMessageInfoMapper.selectByReceiver(receiver);
    }

    @Override
    public List<WxMessageInfo> selectBySender(Long sender) {
        return wxMessageInfoMapper.selectBySender(sender);
    }

    @Override
    public List<WxMessageInfo> selectByMsgtype(Integer msgtype) {
        return wxMessageInfoMapper.selectByMsgtype(msgtype);
    }
    
    @Override
    public WxMessageInfo selectByMessageId(String messageId) {
        try {
            Long msgId = Long.parseLong(messageId);
            return wxMessageInfoMapper.selectByMsgId(msgId);
        } catch (NumberFormatException e) {
            // 如果转换失败，返回null或处理异常
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int batchInsert(List<WxMessageInfo> list) {
        return wxMessageInfoMapper.batchInsert(list);
    }

    @Override
    public List<WxMessageInfo> selectBySendTimeRange(Long startTime, Long endTime) {
        return wxMessageInfoMapper.selectBySendTimeRange(startTime, endTime);
    }
}
