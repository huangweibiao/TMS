package com.tms.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 温湿度记录DTO
 *
 * @author TMS Team
 * @version 1.0.0
 */
public class TempHumidityDTO {

    private Long id;

    /**
     * 调度单ID
     */
    @NotNull(message = "调度单ID不能为空")
    private Long dispatchId;

    /**
     * 调度单号
     */
    private String dispatchNo;

    /**
     * 设备ID
     */
    @NotNull(message = "设备ID不能为空")
    private String deviceId;

    /**
     * 温度(℃)
     */
    @NotNull(message = "温度不能为空")
    private BigDecimal temperature;

    /**
     * 湿度(%)
     */
    @NotNull(message = "湿度不能为空")
    private BigDecimal humidity;

    /**
     * 记录时间
     */
    @NotNull(message = "记录时间不能为空")
    private LocalDateTime recordTime;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDispatchId() {
        return dispatchId;
    }

    public void setDispatchId(Long dispatchId) {
        this.dispatchId = dispatchId;
    }

    public String getDispatchNo() {
        return dispatchNo;
    }

    public void setDispatchNo(String dispatchNo) {
        this.dispatchNo = dispatchNo;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public BigDecimal getTemperature() {
        return temperature;
    }

    public void setTemperature(BigDecimal temperature) {
        this.temperature = temperature;
    }

    public BigDecimal getHumidity() {
        return humidity;
    }

    public void setHumidity(BigDecimal humidity) {
        this.humidity = humidity;
    }

    public LocalDateTime getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(LocalDateTime recordTime) {
        this.recordTime = recordTime;
    }

    private java.time.LocalDateTime createTime;
    private java.time.LocalDateTime updateTime;

    public java.time.LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(java.time.LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public java.time.LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(java.time.LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
