package com.weichat.common.service;

import com.weichat.common.entity.WxGroupInfo;

import java.util.List;

/**
 * 微信群信息Service接口
 */
public interface WxGroupInfoService {
    /**
     * 新增微信群信息
     * @param wxGroupInfo 微信群信息
     * @return 影响行数
     */
    int insert(WxGroupInfo wxGroupInfo);

    /**
     * 根据ID查询微信群信息
     * @param id 主键ID
     * @return 微信群信息
     */
    WxGroupInfo selectByPrimaryKey(Long id);

    /**
     * 根据群ID查询微信群信息
     * @param roomId 群ID
     * @return 微信群信息
     */
    WxGroupInfo selectByRoomId(String roomId);

    /**
     * 查询所有微信群信息
     * @return 微信群信息列表
     */
    List<WxGroupInfo> selectAll();

    /**
     * 根据ID更新微信群信息
     * @param wxGroupInfo 微信群信息
     * @return 影响行数
     */
    int updateByPrimaryKey(WxGroupInfo wxGroupInfo);

    /**
     * 根据ID删除微信群信息
     * @param id 主键ID
     * @return 影响行数
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 根据创建者ID查询微信群信息
     * @param createUserId 创建者ID
     * @return 微信群信息列表
     */
    List<WxGroupInfo> selectByCreateUserId(Long createUserId);

    /**
     * 批量插入微信群信息
     * @param list 微信群信息列表
     * @return 影响行数
     */
    int batchInsert(List<WxGroupInfo> list);

    /**
     * 分页查询微信群信息
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 微信群信息列表
     */
    List<WxGroupInfo> selectAllWithPaging(int offset, int limit);

    /**
     * 根据uuid分页查询微信群信息
     * @param uuid 设备uuid
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 微信群信息列表
     */
    List<WxGroupInfo> selectByUuidWithPaging(String uuid, int offset, int limit);

    List<WxGroupInfo> selectByFiltersWithPaging(Long corpId, String uuid, int offset, int limit);
}
