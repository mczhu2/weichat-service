package com.weichat.common.mapper;

import com.weichat.common.entity.MessageTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 消息模板Mapper接口
 *
 * @author weichat
 */
@Mapper
public interface MessageTemplateMapper {
    /**
     * 新增消息模板
     * @param messageTemplate 消息模板
     * @return 影响行数
     */
    int insert(MessageTemplate messageTemplate);

    /**
     * 根据ID查询消息模板
     * @param id 主键ID
     * @return 消息模板
     */
    MessageTemplate selectByPrimaryKey(Long id);

    /**
     * 根据ID更新消息模板
     * @param messageTemplate 消息模板
     * @return 影响行数
     */
    int updateByPrimaryKey(MessageTemplate messageTemplate);

    /**
     * 根据ID删除消息模板
     * @param id 主键ID
     * @return 影响行数
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 查询所有消息模板
     * @return 消息模板列表
     */
    List<MessageTemplate> selectAll();

    /**
     * 根据创建人查询消息模板
     * @param creator 创建人
     * @return 消息模板列表
     */
    List<MessageTemplate> selectByCreator(@Param("creator") String creator);

    /**
     * 分页查询消息模板
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 消息模板列表
     */
    List<MessageTemplate> selectWithPaging(@Param("offset") int offset, @Param("limit") int limit);
}