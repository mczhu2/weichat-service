package com.weichat.common.service;

import com.weichat.common.entity.WxUserInfo;

import java.util.List;

/**
 * 微信用户信息Service接口
 */
public interface WxUserInfoService {
    /**
     * 新增微信用户信息
     * @param wxUserInfo 微信用户信息
     * @return 影响行数
     */
    int insert(WxUserInfo wxUserInfo);

    /**
     * 根据ID查询微信用户信息
     * @param id 主键ID
     * @return 微信用户信息
     */
    WxUserInfo selectByPrimaryKey(Long id);

    /**
     * 根据UnionID查询微信用户信息
     * @param unionid 用户唯一标识
     * @return 微信用户信息
     */
    WxUserInfo selectByUnionid(String unionid);

    /**
     * 查询所有微信用户信息
     * @return 微信用户信息列表
     */
    List<WxUserInfo> selectAll();

    /**
     * 根据ID更新微信用户信息
     * @param wxUserInfo 微信用户信息
     * @return 影响行数
     */
    int updateByPrimaryKey(WxUserInfo wxUserInfo);

    /**
     * 根据ID删除微信用户信息
     * @param id 主键ID
     * @return 影响行数
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 根据用户ID查询微信用户信息
     * @param userId 用户ID
     * @return 微信用户信息
     */
    WxUserInfo selectByUserId(Long userId);

    /**
     * 批量插入微信用户信息
     * @param list 微信用户信息列表
     * @return 影响行数
     */
    int batchInsert(List<WxUserInfo> list);
    
    /**
     * 根据用户ID和企业ID查询微信用户信息
     * @param userId 用户ID
     * @param corpId 企业ID
     * @return 微信用户信息
     */
    WxUserInfo selectByUserIdAndCorpId(Long userId, Long corpId);

    /**
     * 根据unionID和企业ID查询微信用户信息
     * @param uuid 用户ID
     * @param corpId 企业ID
     * @return 微信用户信息
     */
    WxUserInfo selectByUnionIdAndCorpId(String uuid, Long corpId);
}
