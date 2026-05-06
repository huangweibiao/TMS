package com.tms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 调度单实体类
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Entity
@Table(name = "tms_dispatch")
public class Dispatch extends BaseEntity {

    /**
     * 调度单号
     */
    @Column(name = "dispatch_no", nullable = false, unique = true, length = 32)
    private String dispatchNo;

    /**
     * 运单ID
     */
    @Column(name = "waybill_id", nullable = false)
    private Long waybillId;

    /**
     * 车辆ID
     */
    @Column(name = "vehicle_id", nullable = false)
    private Long vehicleId;

    /**
     * 司机ID
     */
    @Column(name = "driver_id", nullable = false)
    private Long driverId;

    /**
     * 承运商ID
     */
    @Column(name = "carrier_id")
    private Long carrierId;

    /**
     * 路径规划(JSON)
     */
    @Column(name = "route_plan", columnDefinition = "TEXT")
    private String routePlan;

    /**
     * 计划里程(km)
     */
    @Column(name = "planned_distance", precision = 10, scale = 2)
    private BigDecimal plannedDistance;

    /**
     * 实际里程(km)
     */
    @Column(name = "actual_distance", precision = 10, scale = 2)
    private BigDecimal actualDistance;

    /**
     * 计划发车时间
     */
    @Column(name = "planned_start_time")
    private LocalDateTime plannedStartTime;

    /**
     * 计划到达时间
     */
    @Column(name = "planned_end_time")
    private LocalDateTime plannedEndTime;

    /**
     * 实际发车时间
     */
    @Column(name = "actual_start_time")
    private LocalDateTime actualStartTime;

    /**
     * 实际到达时间
     */
    @Column(name = "actual_end_time")
    private LocalDateTime actualEndTime;

    /**
     * 状态：1-待发车, 2-已发车, 3-已完成, 4-已取消
     */
    @Column(name = "dispatch_status", nullable = false)
    private Integer dispatchStatus = 1;

    /**
     * 创建人
     */
    @Column(name = "create_by", length = 50)
    private String createBy;

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

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
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
