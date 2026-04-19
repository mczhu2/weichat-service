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

    /**
     * 根据uuid和ID范围分页查询回调任务
     * @param uuid 设备uuid
     * @param startId 起始ID（大于此ID）
     * @param limit 限制数量
     * @return 回调任务列表
     */
    List<WxCallbackTask> selectByUuidAndIdRange(@Param("uuid") String uuid,
                                                 @Param("startId") Long startId,
                                                 @Param("limit") int limit);

    /**
     * 查询指定uuid的最小消息ID
     * @param uuid 设备uuid
     * @return 最小消息ID
     */
    Long selectMinIdByUuid(@Param("uuid") String uuid);

    /**
     * 查询指定uuid和时间之后的最小消息ID
     * @param uuid 设备uuid
     * @param time 时间点
     * @return 最小消息ID
     */
    Long selectMinIdAfterTime(@Param("uuid") String uuid, @Param("time") java.util.Date time);
}
