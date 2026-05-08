package com.tms.controller;

import com.tms.common.PageResult;
import com.tms.common.Result;
import com.tms.dto.TrackPointDTO;
import com.tms.entity.TrackPoint;
import com.tms.service.TrackPointService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 轨迹点Controller
 *
 * @author TMS Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/track-point")
public class TrackPointController {

    private final TrackPointService trackPointService;

    @Autowired
    public TrackPointController(TrackPointService trackPointService) {
        this.trackPointService = trackPointService;
    }

    /**
     * 创建轨迹点
     *
     * @param dto 轨迹点DTO
     * @return 创建结果
     */
    @PostMapping
    public Result<TrackPointDTO> createTrackPoint(@Valid @RequestBody TrackPointDTO dto) {
        TrackPoint trackPoint = trackPointService.createTrackPoint(dto);
        return Result.success(convertToDTO(trackPoint));
    }

    /**
     * 批量创建轨迹点
     *
     * @param dtoList 轨迹点DTO列表
     * @return 创建结果
     */
    @PostMapping("/batch")
    public Result<List<TrackPointDTO>> batchCreateTrackPoints(@Valid @RequestBody List<TrackPointDTO> dtoList) {
        List<TrackPoint> trackPoints = trackPointService.batchCreateTrackPoints(dtoList);
        List<TrackPointDTO> result = trackPoints.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return Result.success(result);
    }

    /**
     * 根据ID查询轨迹点
     *
     * @param id 轨迹点ID
     * @return 轨迹点信息
     */
    @GetMapping("/{id}")
    public Result<TrackPointDTO> getTrackPointById(@PathVariable Long id) {
        return trackPointService.findById(id)
                .map(this::convertToDTO)
                .map(Result::success)
                .orElse(Result.error(404, "轨迹点不存在"));
    }

    /**
     * 根据调度单ID查询轨迹点列表
     *
     * @param dispatchId 调度单ID
     * @return 轨迹点列表
     */
    @GetMapping("/by-dispatch/{dispatchId}")
    public Result<List<TrackPointDTO>> getTrackPointsByDispatch(@PathVariable Long dispatchId) {
        List<TrackPointDTO> list = trackPointService.findByDispatchId(dispatchId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return Result.success(list);
    }

    /**
     * 根据调度单ID和时间范围查询轨迹点
     *
     * @param dispatchId 调度单ID
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @return 轨迹点列表
     */
    @GetMapping("/by-dispatch/{dispatchId}/time-range")
    public Result<List<TrackPointDTO>> getTrackPointsByDispatchAndTimeRange(
            @PathVariable Long dispatchId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        List<TrackPointDTO> list = trackPointService.findByDispatchIdAndTimeRange(dispatchId, startTime, endTime)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return Result.success(list);
    }

    /**
     * 根据车辆ID查询最新轨迹点
     *
     * @param vehicleId 车辆ID
     * @param limit     限制数量
     * @return 轨迹点列表
     */
    @GetMapping("/by-vehicle/{vehicleId}/latest")
    public Result<List<TrackPointDTO>> getLatestTrackPointsByVehicle(
            @PathVariable Long vehicleId,
            @RequestParam(defaultValue = "100") int limit) {
        List<TrackPointDTO> list = trackPointService.findLatestByVehicleId(vehicleId, limit)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return Result.success(list);
    }

    /**
     * 分页查询轨迹点列表
     *
     * @param dispatchId 调度单ID
     * @param vehicleId  车辆ID
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @param pageNum    页码
     * @param pageSize   每页大小
     * @return 分页结果
     */
    @GetMapping("/list")
    public Result<PageResult<TrackPointDTO>> getTrackPointList(
            @RequestParam(required = false) Long dispatchId,
            @RequestParam(required = false) Long vehicleId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<TrackPoint> page = trackPointService.findTrackPoints(dispatchId, vehicleId, startTime, endTime, pageable);

        List<TrackPointDTO> list = page.getContent()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return Result.success(new PageResult<>(pageNum, pageSize, page.getTotalElements(), list));
    }

    /**
     * 将实体转换为DTO
     *
     * @param entity 轨迹点实体
     * @return 轨迹点DTO
     */
    private TrackPointDTO convertToDTO(TrackPoint entity) {
        TrackPointDTO dto = new TrackPointDTO();
        dto.setId(entity.getId());
        dto.setDispatchId(entity.getDispatchId());
        dto.setVehicleId(entity.getVehicleId());
        dto.setLongitude(entity.getLongitude());
        dto.setLatitude(entity.getLatitude());
        dto.setSpeed(entity.getSpeed());
        dto.setDirection(entity.getDirection());
        dto.setLocationTime(entity.getLocationTime());
        dto.setCreateTime(entity.getCreateTime());
        dto.setUpdateTime(entity.getUpdateTime());
        return dto;
    }
}
