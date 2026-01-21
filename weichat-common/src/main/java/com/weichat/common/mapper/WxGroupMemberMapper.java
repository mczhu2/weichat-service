package com.weichat.common.mapper;

import com.weichat.common.entity.WxGroupMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 微信群成员信息Mapper接口
 */
@Mapper
public interface WxGroupMemberMapper {
    /**
     * 新增微信群成员信息
     * @param wxGroupMember 微信群成员信息
     * @return 影响行数
     */
    int insert(WxGroupMember wxGroupMember);

    /**
     * 根据ID查询微信群成员信息
     * @param id 主键ID
     * @return 微信群成员信息
     */
    WxGroupMember selectByPrimaryKey(Long id);

    /**
     * 根据群ID查询微信群成员信息列表
     * @param roomId 群ID
     * @return 微信群成员信息列表
     */
    List<WxGroupMember> selectByRoomId(String roomId);

    /**
     * 根据群ID和用户ID查询微信群成员信息
     * @param roomId 群ID
     * @param uin 用户ID
     * @return 微信群成员信息
     */
    WxGroupMember selectByRoomIdAndUin(@Param("roomId") String roomId, @Param("uin") Long uin);

    /**
     * 查询所有微信群成员信息
     * @return 微信群成员信息列表
     */
    List<WxGroupMember> selectAll();

    /**
     * 根据ID更新微信群成员信息
     * @param wxGroupMember 微信群成员信息
     * @return 影响行数
     */
    int updateByPrimaryKey(WxGroupMember wxGroupMember);

    /**
     * 根据ID删除微信群成员信息
     * @param id 主键ID
     * @return 影响行数
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 根据群ID删除微信群成员信息
     * @param roomId 群ID
     * @return 影响行数
     */
    int deleteByRoomId(String roomId);

    /**
     * 批量插入微信群成员信息
     * @param list 微信群成员信息列表
     * @return 影响行数
     */
    int batchInsert(@Param("list") List<WxGroupMember> list);

    /**
     * 根据用户ID查询微信群成员信息列表
     * @param uin 用户ID
     * @return 微信群成员信息列表
     */
    List<WxGroupMember> selectByUin(Long uin);
}
