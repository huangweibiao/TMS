package com.tms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * 仓库实体类
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Entity
@Table(name = "tms_warehouse")
public class Warehouse extends BaseEntity {

    /**
     * 仓库编码
     */
    @Column(name = "warehouse_code", nullable = false, unique = true, length = 32)
    private String warehouseCode;

    /**
     * 仓库名称
     */
    @Column(name = "warehouse_name", nullable = false, length = 100)
    private String warehouseName;

    /**
     * 类型：1-发货仓, 2-中转仓, 3-收货仓
     */
    @Column(name = "warehouse_type")
    private Integer warehouseType = 1;

    /**
     * 省
     */
    @Column(name = "province", length = 50)
    private String province;

    /**
     * 市
     */
    @Column(name = "city", length = 50)
    private String city;

    /**
     * 区
     */
    @Column(name = "district", length = 50)
    private String district;

    /**
     * 详细地址
     */
    @Column(name = "address", length = 200)
    private String address;

    /**
     * 经度
     */
    @Column(name = "longitude", precision = 10, scale = 7)
    private BigDecimal longitude;

    /**
     * 纬度
     */
    @Column(name = "latitude", precision = 10, scale = 7)
    private BigDecimal latitude;

    /**
     * 联系人
     */
    @Column(name = "contact_person", length = 50)
    private String contactPerson;

    /**
     * 联系电话
     */
    @Column(name = "contact_phone", length = 20)
    private String contactPhone;

    /**
     * 状态：1-启用, 0-停用
     */
    @Column(name = "status", nullable = false)
    private Integer status = 1;

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public Integer getWarehouseType() {
        return warehouseType;
    }

    public void setWarehouseType(Integer warehouseType) {
        this.warehouseType = warehouseType;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
