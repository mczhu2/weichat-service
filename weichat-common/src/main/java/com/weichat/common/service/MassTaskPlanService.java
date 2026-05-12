package com.weichat.common.service;

import com.weichat.common.entity.MassTaskPlan;
import com.weichat.common.mapper.MassTaskPlanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MassTaskPlanService {

    @Autowired
    private MassTaskPlanMapper massTaskPlanMapper;

    public Long createPlan(MassTaskPlan plan) {
        LocalDateTime now = LocalDateTime.now();
        if (plan.getCreateTime() == null) {
            plan.setCreateTime(now);
        }
        plan.setUpdateTime(now);
        massTaskPlanMapper.insert(plan);
        return plan.getId();
    }

    public MassTaskPlan getPlanById(Long id) {
        return massTaskPlanMapper.selectByPrimaryKey(id);
    }

    public List<MassTaskPlan> getPlanList(int offset, int limit) {
        return massTaskPlanMapper.selectAllWithPaging(offset, limit);
    }

    public int countPlans() {
        return massTaskPlanMapper.countAll();
    }

    public List<MassTaskPlan> getDuePlans(int limit) {
        return massTaskPlanMapper.selectDuePlans(LocalDateTime.now(), limit);
    }

    /**
     * 按分片查询到期计划。
     * 这里直接把分片条件下推到 SQL，避免多实例都扫到同一批计划。
     */
    public List<MassTaskPlan> getDuePlans(int limit, int shardItem, int shardCount) {
        return massTaskPlanMapper.selectDuePlansSharded(
                LocalDateTime.now(),
                limit,
                shardItem,
                shardCount
        );
    }

    public void updateById(MassTaskPlan plan) {
        plan.setUpdateTime(LocalDateTime.now());
        massTaskPlanMapper.updateByPrimaryKey(plan);
    }
}
