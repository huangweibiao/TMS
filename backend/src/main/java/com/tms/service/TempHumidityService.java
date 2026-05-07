package com.tms.service;

import com.tms.dto.TempHumidityDTO;
import com.tms.entity.TempHumidity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 温湿度记录Service接口
 *
 * @author TMS Team
 * @version 1.0.0
 */
public interface TempHumidityService {

    /**
     * 创建温湿度记录
     *
     * @param dto 温湿度记录DTO
     * @return 创建后的温湿度记录
     */
    TempHumidity createRecord(TempHumidityDTO dto);

    /**
     * 批量创建温湿度记录
     *
     * @param dtoList 温湿度记录DTO列表
     * @return 创建后的温湿度记录列表
     */
    List<TempHumidity> batchCreateRecords(List<TempHumidityDTO> dtoList);

    /**
     * 根据ID查询温湿度记录
     *
     * @param id 记录ID
     * @return 温湿度记录对象
     */
    Optional<TempHumidity> findById(Long id);

    /**
     * 根据调度单ID查询温湿度记录列表
     *
     * @param dispatchId 调度单ID
     * @return 温湿度记录列表
     */
    List<TempHumidity> findByDispatchId(Long dispatchId);

    /**
     * 根据调度单ID和时间范围查询温湿度记录
     *
     * @param dispatchId 调度单ID
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @return 温湿度记录列表
     */
    List<TempHumidity> findByDispatchIdAndTimeRange(Long dispatchId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 分页查询温湿度记录
     *
     * @param dispatchId 调度单ID
     * @param deviceId   设备ID
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @param pageable   分页参数
     * @return 分页结果
     */
    Page<TempHumidity> findRecords(Long dispatchId, String deviceId, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

    /**
     * 获取最新温湿度记录
     *
     * @param dispatchId 调度单ID
     * @return 最新温湿度记录
     */
    Optional<TempHumidity> getLatestRecord(Long dispatchId);

    /**
     * 检查温度是否异常
     *
     * @param dispatchId  调度单ID
     * @param temperature 当前温度
     * @param minTemp     最低温度阈值
     * @param maxTemp     最高温度阈值
     * @return 是否异常
     */
    boolean isTemperatureAbnormal(Long dispatchId, BigDecimal temperature, BigDecimal minTemp, BigDecimal maxTemp);

    /**
     * 删除指定时间之前的记录
     *
     * @param beforeTime 时间点
     * @return 删除数量
     */
    long deleteRecordsBefore(LocalDateTime beforeTime);
}
