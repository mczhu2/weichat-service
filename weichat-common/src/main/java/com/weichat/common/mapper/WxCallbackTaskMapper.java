package com.weichat.common.mapper;

import com.weichat.common.entity.WxCallbackTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WxCallbackTaskMapper {

    int insert(WxCallbackTask task);

    WxCallbackTask selectByPrimaryKey(Long id);

    List<WxCallbackTask> selectPendingTasks(@Param("limit") int limit);

    int updateStatus(@Param("id") Long id, 
                     @Param("status") Integer status, 
                     @Param("errorMessage") String errorMessage);

    int incrementRetryCount(Long id);

    int updateStatusToProcessing(@Param("id") Long id, @Param("oldStatus") Integer oldStatus);
}
