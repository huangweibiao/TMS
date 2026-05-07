package com.tms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * 客户实体类
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Entity
@Table(name = "tms_customer")
public class Customer extends BaseEntity {

    /**
     * 客户编码
     */
    @Column(name = "customer_code", nullable = false, unique = true, length = 32)
    private String customerCode;

    /**
     * 客户名称
     */
    @Column(name = "customer_name", nullable = false, length = 100)
    private String customerName;

    /**
     * 简称
     */
    @Column(name = "short_name", length = 50)
    private String shortName;

    /**
     * 客户类型：1-企业客户, 2-个人客户
     */
    @Column(name = "customer_type")
    private Integer customerType = 1;

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
     * 联系地址
     */
    @Column(name = "contact_address", length = 200)
    private String contactAddress;

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
     * 邮箱
     */
    @Column(name = "email", length = 100)
    private String email;

    /**
     * 传真
     */
    @Column(name = "fax", length = 20)
    private String fax;

    /**
     * 开户银行
     */
    @Column(name = "bank_name", length = 100)
    private String bankName;

    /**
     * 银行账号
     */
    @Column(name = "bank_account", length = 50)
    private String bankAccount;

    /**
     * 税号
     */
    @Column(name = "tax_no", length = 50)
    private String taxNo;

    /**
     * 营业执照号
     */
    @Column(name = "license_no", length = 50)
    private String licenseNo;

    /**
     * 信用等级：1-A, 2-B, 3-C
     */
    @Column(name = "credit_level")
    private Integer creditLevel = 1;

    /**
     * 结算周期：1-月结, 2-周结, 3-现结
     */
    @Column(name = "settlement_cycle")
    private Integer settlementCycle = 1;

    /**
     * 状态：1-启用, 0-停用
     */
    @Column(name = "status", nullable = false)
    private Integer status = 1;

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
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

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public Integer getCreditLevel() {
        return creditLevel;
    }

    public void setCreditLevel(Integer creditLevel) {
        this.creditLevel = creditLevel;
    }

    public Integer getSettlementCycle() {
        return settlementCycle;
    }

    public void setSettlementCycle(Integer settlementCycle) {
        this.settlementCycle = settlementCycle;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Integer customerType) {
        this.customerType = customerType;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getTaxNo() {
        return taxNo;
    }

    public void setTaxNo(String taxNo) {
        this.taxNo = taxNo;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }
}
