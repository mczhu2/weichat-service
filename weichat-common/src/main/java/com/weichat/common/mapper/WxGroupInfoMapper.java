package com.weichat.common.mapper;

import com.weichat.common.entity.WxGroupInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 微信群信息Mapper接口
 */
@Mapper
public interface WxGroupInfoMapper {
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
    int batchInsert(@Param("list") List<WxGroupInfo> list);
}
