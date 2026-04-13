package com.weichat.common.service;

import com.weichat.common.entity.WxGroupInfo;

import java.util.List;

/**
 * 微信群信息服务接口。
 */
public interface WxGroupInfoService {

    /**
     * 新增微信群信息。
     */
    int insert(WxGroupInfo wxGroupInfo);

    /**
     * 根据主键 ID 查询微信群信息。
     */
    WxGroupInfo selectByPrimaryKey(Long id);

    /**
     * 根据 roomId 查询微信群信息。
     */
    WxGroupInfo selectByRoomId(String roomId);

    /**
     * 查询全部微信群信息。
     */
    List<WxGroupInfo> selectAll();

    /**
     * 根据主键 ID 更新微信群信息。
     */
    int updateByPrimaryKey(WxGroupInfo wxGroupInfo);

    /**
     * 根据主键 ID 删除微信群信息。
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 根据创建人 ID 查询微信群信息。
     */
    List<WxGroupInfo> selectByCreateUserId(Long createUserId);

    /**
     * 批量新增微信群信息。
     */
    int batchInsert(List<WxGroupInfo> list);

    /**
     * 分页查询全部微信群信息。
     */
    List<WxGroupInfo> selectAllWithPaging(int offset, int limit);

    /**
     * 根据设备 uuid 分页查询微信群信息。
     */
    List<WxGroupInfo> selectByUuidWithPaging(String uuid, int offset, int limit);

    /**
     * 按 corpIds 和 userIdList 组合条件分页查询微信群信息。
     * corpIds 为圈群主过滤条件，userIdList 为可选过滤条件。
     */
    List<WxGroupInfo> selectByFiltersWithPaging(List<Long> corpIds, List<Long> userIdList, int offset, int limit);
}
