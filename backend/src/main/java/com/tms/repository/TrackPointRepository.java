package com.tms.repository;

import com.tms.entity.TrackPoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 轨迹点Repository接口
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Repository
public interface TrackPointRepository extends JpaRepositoryRepository<TrackPoint, Long> {

    /**
     * 根据调度单ID查询轨迹点列表
     *
     * @param dispatchId 调度单ID
     * @return 轨迹点列表
     */
    List List<TrackPoint> findByDispatchIdOrderByLocationTimeAsc(Long dispatchId);

    /**
     * 根据调度单ID和时间范围查询轨迹点
     *
     * @param dispatchId 调度单ID
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @return 轨迹点列表
     */
    List List<TrackPoint> findByDispatchIdAndLocationTimeBetween(Long dispatchId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据车辆ID查询最新轨迹点
     *
     * @param vehicleId 车辆ID
     * @param pageable  分页参数
     * @return 轨迹点分页列表
     */
    Page Page<TrackPoint> findByVehicleIdOrderByLocationTimeDesc(Long vehicleId, Pageable pageable);
}
