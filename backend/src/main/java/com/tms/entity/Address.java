package com.tms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * 地址库实体类
 * 用于存储常用地址信息，支持发货方、收货方地址管理
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Entity
@Table(name = "tms_address")
public class Address extends BaseEntity {

    /**
     * 地址编码
     */
    @Column(name = "address_code", length = 32)
    private String addressCode;

    /**
     * 地址名称
     */
    @Column(name = "address_name", length = 100)
    private String addressName;

    /**
     * 地址类型：1-发货地址, 2-收货地址, 3-中转地址
     */
    @Column(name = "address_type")
    private Integer addressType = 1;

    /**
     * 所属客户ID
     */
    @Column(name = "customer_id")
    private Long customerId;

    /**
     * 联系人姓名
     */
    @Column(name = "contact_name", length = 50)
    private String contactName;

    /**
     * 联系人电话
     */
    @Column(name = "contact_phone", length = 20)
    private String contactPhone;

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
    @Column(name = "detail_address", length = 200)
    private String detailAddress;

    /**
     * 完整地址
     */
    @Column(name = "full_address", length = 500)
    private String fullAddress;

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
     * 邮政编码
     */
    @Column(name = "zip_code", length = 10)
    private String zipCode;

    /**
     * 是否默认地址：1-是, 0-否
     */
    @Column(name = "is_default")
    private Integer isDefault = 0;

    /**
     * 状态：1-启用, 0-停用
     */
    @Column(name = "status", nullable = false)
    private Integer status = 1;

    /**
     * 备注
     */
    @Column(name = "remark", length = 200)
    private String remark;

    public String getAddressCode() {
        return addressCode;
    }

    public void setAddressCode(String addressCode) {
        this.addressCode = addressCode;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public Integer getAddressType() {
        return addressType;
    }

    public void setAddressType(Integer addressType) {
        this.addressType = addressType;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
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

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
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

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
