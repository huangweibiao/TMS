package com.tms.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

/**
 * 司机DTO
 *
 * @author TMS Team
 * @version 1.0.0
 */
public class DriverDTO {

    private Long id;

    /**
     * 司机姓名
     */
    @NotBlank(message = "司机姓名不能为空")
    private String driverName;

    /**
     * 身份证号
     */
    @NotBlank(message = "身份证号不能为空")
    private String idCard;

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    private String phone;

    /**
     * 驾照类型
     */
    private String licenseType;

    /**
     * 驾驶证号
     */
    private String licenseNo;

    /**
     * 入职日期
     */
    private LocalDate hireDate;

    /**
     * 所属承运商ID
     */
    private Long carrierId;

    /**
     * 承运商名称
     */
    private String carrierName;

    /**
     * 当前绑定车辆ID
     */
    private Long vehicleId;

    /**
     * 车牌号
     */
    private String plateNumber;

    /**
     * 状态：1-空闲, 2-在途, 3-休息, 4-离职
     */
    private Integer status = 1;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
