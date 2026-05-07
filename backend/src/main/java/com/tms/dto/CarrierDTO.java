package com.tms.dto;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 承运商DTO
 *
 * @author TMS Team
 * @version 1.0.0
 */
public class CarrierDTO {

    private Long id;

    /**
     * 承运商编码
     */
    @NotBlank(message = "承运商编码不能为空")
    private String carrierCode;

    /**
     * 承运商名称
     */
    @NotBlank(message = "承运商名称不能为空")
    private String carrierName;

    /**
     * 类型：1-自有, 2-外协, 3-专线
     */
    private Integer carrierType = 1;

    /**
     * 营业执照号
     */
    private String licenseNo;

    /**
     * 税号
     */
    private String taxNo;

    /**
     * 联系人
     */
    private String contactPerson;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 合作开始日期
     */
    private LocalDate cooperationStart;

    /**
     * 合作结束日期
     */
    private LocalDate cooperationEnd;

    /**
     * 评分(1-5)
     */
    private BigDecimal rating = new BigDecimal("5.00");

    /**
     * 状态：1-启用, 0-停用
     */
    private Integer status = 1;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public Integer getCarrierType() {
        return carrierType;
    }

    public void setCarrierType(Integer carrierType) {
        this.carrierType = carrierType;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    public String getTaxNo() {
        return taxNo;
    }

    public void setTaxNo(String taxNo) {
        this.taxNo = taxNo;
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

    public LocalDate getCooperationStart() {
        return cooperationStart;
    }

    public void setCooperationStart(LocalDate cooperationStart) {
        this.cooperationStart = cooperationStart;
    }

    public LocalDate getCooperationEnd() {
        return cooperationEnd;
    }

    public void setCooperationEnd(LocalDate cooperationEnd) {
        this.cooperationEnd = cooperationEnd;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
