package com.tms.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * 司机实体类
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Entity
@Table(name = "tms_driver")
public class Driver extends BaseEntity {

    /**
     * 司机姓名
     */
    @Column(name = "driver_name", nullable = false, length = 50)
    private String driverName;

    /**
     * 身份证号
     */
    @Column(name = "id_card", nullable = false, unique = true, length = 18)
    private String idCard;

    /**
     * 手机号
     */
    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    /**
     * 驾照类型
     */
    @Column(name = "license_type", length = 10)
    private String licenseType;

    /**
     * 驾驶证号
     */
    @Column(name = "license_no", length = 30)
    private String licenseNo;

    /**
     * 入职日期
     */
    @Column(name = "hire_date")
    private LocalDate hireDate;

    /**
     * 所属承运商ID
     */
    @Column(name = "carrier_id")
    private Long carrierId;

    /**
     * 当前绑定车辆ID
     */
    @Column(name = "vehicle_id")
    private Long vehicleId;

    /**
     * 状态：1-空闲, 2-在途, 3-休息, 4-离职
     */
    @Column(name = "status", nullable = false)
    private Integer status = 1;

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

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
