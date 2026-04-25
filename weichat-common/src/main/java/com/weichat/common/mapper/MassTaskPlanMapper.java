package com.weichat.common.mapper;

import com.weichat.common.entity.MassTaskPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface MassTaskPlanMapper {

    int insert(MassTaskPlan massTaskPlan);

    MassTaskPlan selectByPrimaryKey(Long id);

    int updateByPrimaryKey(MassTaskPlan massTaskPlan);

    List<MassTaskPlan> selectAllWithPaging(@Param("offset") int offset, @Param("limit") int limit);

    int countAll();

    List<MassTaskPlan> selectDuePlans(@Param("now") LocalDateTime now, @Param("limit") int limit);
}
