package com.tms.service;

import com.tms.dto.TrackPointDTO;
import com.tms.entity.TrackPoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 轨迹点Service接口
 *
 * @author TMS Team
 * @version 1.0.0
 */
public interface TrackPointService {

    /**
     * 创建轨迹点
     *
     * @param dto 轨迹点DTO
     * @return 创建后的轨迹点
     */
    TrackPoint createTrackPoint(TrackPointDTO dto);

    /**
     * 批量创建轨迹点
     *
     * @param dtoList 轨迹点DTO列表
     * @return 创建后的轨迹点列表
     */
    List List<TrackPoint> batchCreateTrackPoints(List(List<TrackPointDTO> dtoList);

    /**
     * 根据ID查询轨迹点
     *
     * @param id 轨迹点ID
     * @return 轨迹点对象
     */
    Optional Optional<TrackPoint> findById(Long id);

    /**
     * 根据调度单ID查询轨迹点列表
     *
     * @param dispatchId 调度单ID
     * @return 轨迹点列表
     */
    List List<TrackPoint> findByDispatchId(Long dispatchId);

    /**
     * 根据调度单ID和时间范围查询轨迹点
     *
     * @param dispatchId 调度单ID
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @return 轨迹点列表
     */
    List List<TrackPoint> findByDispatchIdAndTimeRange(Long dispatchId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据车辆ID查询最新轨迹点
     *
     * @param vehicleId 车辆ID
     * @param limit     限制数量
     * @return 轨迹点列表
     */
    List List<TrackPoint> findLatestByVehicleId(Long vehicleId, int limit);

    /**
     * 分页查询轨迹点列表
     *
     * @param dispatchId 调度单ID
     * @param vehicleId  车辆ID
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @param pageable   分页参数
     * @return 分页结果
     */
    Page Page<TrackPoint> findTrackPoints(Long dispatchId, Long vehicleId, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

    /**
     * 删除指定时间之前的轨迹点
     *
     * @param beforeTime 时间点
     * @return 删除数量
     */
    long deleteTrackPointsBefore(LocalDateTime beforeTime);
}
