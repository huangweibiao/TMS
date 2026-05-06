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
}
