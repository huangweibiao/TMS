package com.tms.dto;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 车辆DTO
 *
 * @author TMS Team
 * @version 1.0.0
 */
public class VehicleDTO {

    private Long id;

    /**
     * 车牌号
     */
    @NotBlank(message = "车牌号不能为空")
    private String plateNumber;

    /**
     * 车型
     */
    private String vehicleType;

    /**
     * 品牌
     */
    private String vehicleBrand;

    /**
     * 载重(吨)
     */
    private BigDecimal loadCapacity;

    /**
     * 容积(立方米)
     */
    private BigDecimal volumeCapacity;

    /**
     * 车长(米)
     */
    private BigDecimal length;

    /**
     * 车宽(米)
     */
    private BigDecimal width;

    /**
     * 车高(米)
     */
    private BigDecimal height;

    /**
     * 所属：1-自有, 2-外协
     */
    private Integer ownerType = 1;

    /**
     * 承运商ID
     */
    private Long carrierId;

    /**
     * 承运商名称
     */
    private String carrierName;

    /**
     * GPS设备ID
     */
    private String gpsDeviceId;

    /**
     * 状态：1-可用, 2-维修, 3-报废
     */
    private Integer status = 1;

    /**
     * 最近保养日期
     */
    private LocalDate lastMaintenance;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
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
