package com.tms.repository;

import com.tms.entity.TempHumidity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 温湿度记录Repository接口
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Repository
public interface TempHumidityRepository extends JpaRepository<TempHumidity, Long>, JpaSpecificationExecutor<TempHumidity> {

    /**
     * 根据调度单ID查询温湿度记录
     *
     * @param dispatchId 调度单ID
     * @return 温湿度记录列表
     */
    List<TempHumidity> findByDispatchIdOrderByRecordTimeAsc(Long dispatchId);

    /**
     * 根据调度单ID和时间范围查询温湿度记录
     *
     * @param dispatchId 调度单ID
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @return 温湿度记录列表
     */
    List<TempHumidity> findByDispatchIdAndRecordTimeBetween(Long dispatchId, LocalDateTime startTime, LocalDateTime endTime);
}
