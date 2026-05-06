package com.tms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 车辆实体类
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Entity
@Table(name = "tms_vehicle")
public class Vehicle extends BaseEntity {

    /**
     * 车牌号
     */
    @Column(name = "plate_number", nullable = false, unique = true, length = 20)
    private String plateNumber;

    /**
     * 车型
     */
    @Column(name = "vehicle_type", length = 30)
    private String vehicleType;

    /**
     * 品牌
     */
    @Column(name = "vehicle_brand", length = 50)
    private String vehicleBrand;

    /**
     * 载重(吨)
     */
    @Column(name = "load_capacity", precision = 10, scale = 2)
    private BigDecimal loadCapacity;

    /**
     * 容积(立方米)
     */
    @Column(name = "volume_capacity", precision = 10, scale = 2)
    private BigDecimal volumeCapacity;

    /**
     * 车长(米)
     */
    @Column(name = "length", precision = 8, scale = 2)
    private BigDecimal length;

    /**
     * 车宽(米)
     */
    @Column(name = "width", precision = 8, scale = 2)
    private BigDecimal width;

    /**
     * 车高(米)
     */
    @Column(name = "height", precision = 8, scale = 2)
    private BigDecimal height;

    /**
     * 所属：1-自有, 2-外协
     */
    @Column(name = "owner_type")
    private Integer ownerType = 1;

    /**
     * 承运商ID
     */
    @Column(name = "carrier_id")
    private Long carrierId;

    /**
     * GPS设备ID
     */
    @Column(name = "gps_device_id", length = 50)
    private String gpsDeviceId;

    /**
     * 状态：1-可用, 2-维修, 3-报废
     */
    @Column(name = "status", nullable = false)
    private Integer status = 1;

    /**
     * 最近保养日期
     */
    @Column(name = "last_maintenance")
    private LocalDate lastMaintenance;

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleBrand() {
        return vehicleBrand;
    }

    public void setVehicleBrand(String vehicleBrand) {
        this.vehicleBrand = vehicleBrand;
    }

    public BigDecimal getLoadCapacity() {
        return loadCapacity;
    }

    public void setLoadCapacity(BigDecimal loadCapacity) {
        this.loadCapacity = loadCapacity;
    }

    public BigDecimal getVolumeCapacity() {
        return volumeCapacity;
    }

    public void setVolumeCapacity(BigDecimal volumeCapacity) {
        this.volumeCapacity = volumeCapacity;
    }

    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public Integer getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Integer ownerType) {
        this.ownerType = ownerType;
    }

    public Long getCarrierId() {
        return carrierId;
    }

    public void setCarrierId(Long carrierId) {
        this.carrierId = carrierId;
    }

    public String getGpsDeviceId() {
        return gpsDeviceId;
    }

    public void setGpsDeviceId(String gpsDeviceId) {
        this.gpsDeviceId = gpsDeviceId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDate getLastMaintenance() {
        return lastMaintenance;
    }

    public void setLastMaintenance(LocalDate lastMaintenance) {
        this.lastMaintenance = lastMaintenance;
    }
}
