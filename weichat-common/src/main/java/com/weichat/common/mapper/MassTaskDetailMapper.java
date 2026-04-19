package com.weichat.common.mapper;

import com.weichat.common.entity.MassTaskDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MassTaskDetailMapper {
    int insert(MassTaskDetail massTaskDetail);

    MassTaskDetail selectByPrimaryKey(Long id);

    int deleteByPrimaryKey(Long id);

    int updateByPrimaryKey(MassTaskDetail massTaskDetail);

    List<MassTaskDetail> selectByTaskId(@Param("taskId") Long taskId);

    List<MassTaskDetail> selectUnsentDetails(@Param("limit") int limit);

    List<MassTaskDetail> selectSchedulableDetails(@Param("limit") int limit, @Param("now") java.time.LocalDateTime now);

    int deleteByTaskId(@Param("taskId") Long taskId);
}
