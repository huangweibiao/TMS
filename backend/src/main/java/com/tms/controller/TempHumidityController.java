package com.tms.controller;

import com.tms.common.PageResult;
import com.tms.common.Result;
import com.tms.dto.TempHumidityDTO;
import com.tms.entity.TempHumidity;
import com.tms.service.TempHumidityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 温湿度记录Controller
 *
 * @author TMS Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/temp-humidity")
public class TempHumidityController {

    private final TempHumidityService tempHumidityService;

    @Autowired
    public TempHumidityController(TempHumidityService tempHumidityService) {
        this.tempHumidityService = tempHumidityService;
    }

    /**
     * 创建温湿度记录
     *
     * @param dto 温湿度记录DTO
     * @return 创建结果
     */
    @PostMapping
    public Result<TempHumidityDTO> createRecord(@Valid @RequestBody TempHumidityDTO dto) {
        TempHumidity record = tempHumidityService.createRecord(dto);
        return Result.success(convertToDTO(record));
    }

    /**
     * 批量创建温湿度记录
     *
     * @param dtoList 温湿度记录DTO列表
     * @return 创建结果
     */
    @PostMapping("/batch")
    public Result<List<TempHumidityDTO>> batchCreateRecords(@Valid @RequestBody List<TempHumidityDTO> dtoList) {
        List<TempHumidity> records = tempHumidityService.batchCreateRecords(dtoList);
        List<TempHumidityDTO> result = records.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return Result.success(result);
    }

    /**
     * 根据ID查询温湿度记录
     *
     * @param id 记录ID
     * @return 温湿度记录信息
     */
    @GetMapping("/{id}")
    public Result<TempHumidityDTO> getRecordById(@PathVariable Long id) {
        return tempHumidityService.findById(id)
                .map(this::convertToDTO)
                .map(Result::success)
                .orElse(Result.error(404, "记录不存在"));
    }

    /**
     * 根据调度单ID查询温湿度记录列表
     *
     * @param dispatchId 调度单ID
     * @return 温湿度记录列表
     */
    @GetMapping("/by-dispatch/{dispatchId}")
    public Result<List<TempHumidityDTO>> getRecordsByDispatch(@PathVariable Long dispatchId) {
        List<TempHumidityDTO> list = tempHumidityService.findByDispatchId(dispatchId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return Result.success(list);
    }

    /**
     * 根据调度单ID和时间范围查询温湿度记录
     *
     * @param dispatchId 调度单ID
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @return 温湿度记录列表
     */
    @GetMapping("/by-dispatch/{dispatchId}/time-range")
    public Result<List<TempHumidityDTO>> getRecordsByDispatchAndTimeRange(
            @PathVariable Long dispatchId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        List<TempHumidityDTO> list = tempHumidityService.findByDispatchIdAndTimeRange(dispatchId, startTime, endTime)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return Result.success(list);
    }

    /**
     * 分页查询温湿度记录
     *
     * @param dispatchId 调度单ID
     * @param deviceId   设备ID
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @param pageNum    页码
     * @param pageSize   每页大小
     * @return 分页结果
     */
    @GetMapping("/list")
    public Result<PageResult<TempHumidityDTO>> getRecordList(
            @RequestParam(required = false) Long dispatchId,
            @RequestParam(required = false) String deviceId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<TempHumidity> page = tempHumidityService.findRecords(dispatchId, deviceId, startTime, endTime, pageable);

        List<TempHumidityDTO> list = page.getContent()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return Result.success(new PageResult<>(pageNum, pageSize, page.getTotalElements(), list));
    }

    /**
     * 获取最新温湿度记录
     *
     * @param dispatchId 调度单ID
     * @return 最新温湿度记录
     */
    @GetMapping("/latest/{dispatchId}")
    public Result<TempHumidityDTO> getLatestRecord(@PathVariable Long dispatchId) {
        return tempHumidityService.getLatestRecord(dispatchId)
                .map(this::convertToDTO)
                .map(Result::success)
                .orElse(Result.error(404, "暂无记录"));
    }

    /**
     * 检查温度是否异常
     *
     * @param dispatchId  调度单ID
     * @param temperature 当前温度
     * @param minTemp     最低温度阈值
     * @param maxTemp     最高温度阈值
     * @return 检查结果
     */
    @GetMapping("/check-temperature")
    public Result<Map<String, Object>> checkTemperature(
            @RequestParam Long dispatchId,
            @RequestParam java.math.BigDecimal temperature,
            @RequestParam java.math.BigDecimal minTemp,
            @RequestParam java.math.BigDecimal maxTemp) {
        boolean isAbnormal = tempHumidityService.isTemperatureAbnormal(dispatchId, temperature, minTemp, maxTemp);
        Map<String, Object> result = new HashMap<>();
        result.put("isAbnormal", isAbnormal);
        result.put("temperature", temperature);
        result.put("minTemp", minTemp);
        result.put("maxTemp", maxTemp);
        return Result.success(result);
    }

    /**
     * 将实体转换为DTO
     *
     * @param entity 温湿度记录实体
     * @return 温湿度记录DTO
     */
    private TempHumidityDTO convertToDTO(TempHumidity entity) {
        TempHumidityDTO dto = new TempHumidityDTO();
        dto.setId(entity.getId());
        dto.setDispatchId(entity.getDispatchId());
        dto.setDeviceId(entity.getDeviceId());
        dto.setTemperature(entity.getTemperature());
        dto.setHumidity(entity.getHumidity());
        dto.setRecordTime(entity.getRecordTime());
        dto.setCreateTime(entity.getCreateTime());
        dto.setUpdateTime(entity.getUpdateTime());
        return dto;
    }
}

