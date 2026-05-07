package com.tms.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 回单DTO
 *
 * @author TMS Team
 * @version 1.0.0
 */
public class ReceiptDTO {

    private Long id;

    /**
     * 运单ID
     */
    @NotNull(message = "运单ID不能为空")
    private Long waybillId;

    /**
     * 运单号
     */
    private String waybillNo;

    /**
     * 回单号
     */
    private String receiptNo;

    /**
     * 类型：1-纸质回单, 2-电子签收
     */
    private Integer receiptType = 1;

    /**
     * 签收人
     */
    private String signerName;

    /**
     * 签收人电话
     */
    private String signerPhone;

    /**
     * 签收时间
     */
    private LocalDateTime signTime;

    /**
     * 签收照片URL
     */
    private String signPhotoUrl;

    /**
     * 回单文件URL
     */
    private String receiptFileUrl;

    /**
     * 状态：1-未回传, 2-已回传, 3-已审核
     */
    private Integer status = 1;

    /**
     * 回传时间
     */
    private LocalDateTime returnTime;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

    /**
     * 审核人
     */
    private String auditBy;

    /**
     * 备注
     */
    private String remark;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWaybillId() {
        return waybillId;
    }

    public void setWaybillId(Long waybillId) {
        this.waybillId = waybillId;
    }

    public String getWaybillNo() {
        return waybillNo;
    }

    public void setWaybillNo(String waybillNo) {
        this.waybillNo = waybillNo;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public Integer getReceiptType() {
        return receiptType;
    }

    public void setReceiptType(Integer receiptType) {
        this.receiptType = receiptType;
    }

    public String getSignerName() {
        return signerName;
    }

    public void setSignerName(String signerName) {
        this.signerName = signerName;
    }

    public String getSignerPhone() {
        return signerPhone;
    }

    public void setSignerPhone(String signerPhone) {
        this.signerPhone = signerPhone;
    }

    public LocalDateTime getSignTime() {
        return signTime;
    }

    public void setSignTime(LocalDateTime signTime) {
        this.signTime = signTime;
    }

    public String getSignPhotoUrl() {
        return signPhotoUrl;
    }

    public void setSignPhotoUrl(String signPhotoUrl) {
        this.signPhotoUrl = signPhotoUrl;
    }

    public String getReceiptFileUrl() {
        return receiptFileUrl;
    }

    public void setReceiptFileUrl(String receiptFileUrl) {
        this.receiptFileUrl = receiptFileUrl;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(LocalDateTime returnTime) {
        this.returnTime = returnTime;
    }

    public LocalDateTime getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(LocalDateTime auditTime) {
        this.auditTime = auditTime;
    }

    public String getAuditBy() {
        return auditBy;
    }

    public void setAuditBy(String auditBy) {
        this.auditBy = auditBy;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
