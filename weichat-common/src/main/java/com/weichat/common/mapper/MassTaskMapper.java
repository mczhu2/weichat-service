package com.weichat.common.mapper;

import com.weichat.common.entity.MassTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MassTaskMapper {
    int insert(MassTask massTask);

    MassTask selectByPrimaryKey(Long id);

    int deleteByPrimaryKey(Long id);

    int updateByPrimaryKey(MassTask massTask);

    List<MassTask> selectAllWithPaging(@Param("offset") int offset, @Param("limit") int limit);
}
