package com.tms.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 调度单DTO
 *
 * @author TMS Team
 * @version 1.0.0
 */
public class DispatchDTO {

    private Long id;

    /**
     * 调度单号
     */
    private String dispatchNo;

    /**
     * 运单ID
     */
    @NotNull(message = "运单ID不能为空")
    private Long waybillId;

    /**
     * 运单号（用于展示）
     */
    private String waybillNo;

    /**
     * 车辆ID
     */
    @NotNull(message = "车辆ID不能为空")
    private Long vehicleId;

    /**
     * 车牌号（用于展示）
     */
    private String plateNumber;

    /**
     * 司机ID
     */
    @NotNull(message = "司机ID不能为空")
    private Long driverId;

    /**
     * 司机姓名（用于展示）
     */
    private String driverName;

    /**
     * 承运商ID
     */
    private Long carrierId;

    /**
     * 路径规划(JSON)
     */
    private String routePlan;

    /**
     * 计划里程(km)
     */
    private BigDecimal plannedDistance;

    /**
     * 实际里程(km)
     */
    private BigDecimal actualDistance;

    /**
     * 计划发车时间
     */
    private LocalDateTime plannedStartTime;

    /**
     * 计划到达时间
     */
    private LocalDateTime plannedEndTime;

    /**
     * 实际发车时间
     */
    private LocalDateTime actualStartTime;

    /**
     * 实际到达时间
     */
    private LocalDateTime actualEndTime;

    /**
     * 状态：1-待发车, 2-已发车, 3-已完成, 4-已取消
     */
    private Integer dispatchStatus = 1;

    /**
     * 创建人
     */
    private String createBy;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDispatchNo() {
        return dispatchNo;
    }

    public void setDispatchNo(String dispatchNo) {
        this.dispatchNo = dispatchNo;
    }

    public Long getWaybillId() {
        return waybillId;
    }

    public void setWaybillId(Long waybillId) {
        this.waybillId = waybillId;
    }

    public String getWaybillNo() {
        return waybillNo;
    }

    public void setWaybillNo(String waybillNo) {
        this.waybillNo = waybillNo;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public Long getCarrierId() {
        return carrierId;
    }

    public void setCarrierId(Long carrierId) {
        this.carrierId = carrierId;
    }

    public String getRoutePlan() {
        return routePlan;
    }

    public void setRoutePlan(String routePlan) {
        this.routePlan = routePlan;
    }

    public BigDecimal getPlannedDistance() {
        return plannedDistance;
    }

    public void setPlannedDistance(BigDecimal plannedDistance) {
        this.plannedDistance = plannedDistance;
    }

    public BigDecimal getActualDistance() {
        return actualDistance;
    }

    public void setActualDistance(BigDecimal actualDistance) {
        this.actualDistance = actualDistance;
    }

    public LocalDateTime getPlannedStartTime() {
        return plannedStartTime;
    }

    public void setPlannedStartTime(LocalDateTime plannedStartTime) {
        this.plannedStartTime = plannedStartTime;
    }

    public LocalDateTime getPlannedEndTime() {
        return plannedEndTime;
    }

    public void setPlannedEndTime(LocalDateTime plannedEndTime) {
        this.plannedEndTime = plannedEndTime;
    }

    public LocalDateTime getActualStartTime() {
        return actualStartTime;
    }

    public void setActualStartTime(LocalDateTime actualStartTime) {
        this.actualStartTime = actualStartTime;
    }

    public LocalDateTime getActualEndTime() {
        return actualEndTime;
    }

    public void setActualEndTime(LocalDateTime actualEndTime) {
        this.actualEndTime = actualEndTime;
    }

    public Integer getDispatchStatus() {
        return dispatchStatus;
    }

    public void setDispatchStatus(Integer dispatchStatus) {
        this.dispatchStatus = dispatchStatus;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
}
