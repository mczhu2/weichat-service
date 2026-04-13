package com.weichat.common.mapper;

import com.weichat.common.entity.WxGroupInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 微信群信息 Mapper 接口。
 */
@Mapper
public interface WxGroupInfoMapper {

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
    int batchInsert(@Param("list") List<WxGroupInfo> list);

    /**
     * 分页查询全部微信群信息。
     */
    List<WxGroupInfo> selectAllWithPaging(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 根据设备 uuid 分页查询微信群信息。
     */
    List<WxGroupInfo> selectByUuidWithPaging(@Param("uuid") String uuid,
                                             @Param("offset") int offset,
                                             @Param("limit") int limit);

    /**
     * 按 corpIds 和 userIdList 组合条件分页查询微信群信息。
     * corpIds 作为必填公司范围过滤，userIdList 作为可选用户过滤。
     */
    List<WxGroupInfo> selectByFiltersWithPaging(@Param("corpIds") List<Long> corpIds,
                                                @Param("userIdList") List<Long> userIdList,
                                                @Param("offset") int offset,
                                                @Param("limit") int limit);
}
