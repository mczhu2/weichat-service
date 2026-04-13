package com.weichat.common.service;

import com.weichat.common.entity.WxFriendInfo;

import java.util.List;

/**
 * 微信好友信息Service接口
 */
public interface WxFriendInfoService {
    /**
     * 新增微信好友信息
     * @param wxFriendInfo 微信好友信息
     * @return 影响行数
     */
    int insert(WxFriendInfo wxFriendInfo);

    /**
     * 根据ID查询微信好友信息
     * @param id 主键ID
     * @return 微信好友信息
     */
    WxFriendInfo selectByPrimaryKey(Long id);

    /**
     * 根据UnionID查询微信好友信息
     * @param ownerUserId
     * @param unionid 用户唯一标识
     * @return 微信好友信息
     */
    WxFriendInfo selectByOwnerUserIdAndUnionid(Long ownerUserId, String unionid, Integer isExternal);

    /**
     * 根据用户ID查询微信好友信息
     * @param userId 用户ID
     * @return 微信好友信息
     */
    WxFriendInfo selectByUserId(Long userId);

    /**
     * 查询所有微信好友信息
     * @return 微信好友信息列表
     */
    List<WxFriendInfo> selectAll();

    /**
     * 根据ID更新微信好友信息
     * @param wxFriendInfo 微信好友信息
     * @return 影响行数
     */
    int updateByPrimaryKey(WxFriendInfo wxFriendInfo);

    /**
     * 根据ID删除微信好友信息
     * @param id 主键ID
     * @return 影响行数
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 根据状态查询微信好友信息
     * @param status 状态：0-正常，1-已删除，2-黑名单等
     * @return 微信好友信息列表
     */
    List<WxFriendInfo> selectByStatus(Integer status);

    /**
     * 批量插入微信好友信息
     * @param list 微信好友信息列表
     * @return 影响行数
     */
    int batchInsert(List<WxFriendInfo> list);

    /**
     * 根据企业ID查询微信好友信息列表
     * @param corpId 企业ID
     * @return 微信好友信息列表
     */
    List<WxFriendInfo> selectByCorpId(Long corpId);

    /**
     * 按条件分页查询外部联系人
     * @param uuid 设备uuid
     * @param corpIds 企业ID列表
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 外部联系人列表
     */
    List<WxFriendInfo> selectExternalByFiltersWithPaging(String uuid, List<Long> corpIds, int offset, int limit);
}
