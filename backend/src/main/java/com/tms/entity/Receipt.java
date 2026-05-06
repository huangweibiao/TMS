package com.tms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 回单实体类
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Entity
@Table(name = "tms_receipt")
public class Receipt extends BaseEntity {

    /**
     * 运单ID
     */
    @Column(name = "waybill_id", nullable = false)
    private Long waybillId;

    /**
     * 回单号
     */
    @Column(name = "receipt_no", nullable = false, unique = true, length = 32)
    private String receiptNo;

    /**
     * 类型：1-纸质回单, 2-电子签收
     */
    @Column(name = "receipt_type", nullable = false)
    private Integer receiptType = 1;

    /**
     * 签收人
     */
    @Column(name = "signer_name", length = 50)
    private String signerName;

    /**
     * 签收人电话
     */
    @Column(name = "signer_phone", length = 20)
    private String signerPhone;

    /**
     * 签收时间
     */
    @Column(name = "sign_time")
    private LocalDateTime signTime;

    /**
     * 签收照片URL
     */
    @Column(name = "sign_photo_url", length = 500)
    private String signPhotoUrl;

    /**
     * 回单文件URL
     */
    @Column(name = "receipt_file_url", length = 500)
    private String receiptFileUrl;

    /**
     * 状态：1-未回传, 2-已回传, 3-已审核
     */
    @Column(name = "status", nullable = false)
    private Integer status = 1;

    /**
     * 回传时间
     */
    @Column(name = "return_time")
    private LocalDateTime returnTime;

    /**
     * 审核时间
     */
    @Column(name = "audit_time")
    private LocalDateTime auditTime;

    /**
     * 审核人
     */
    @Column(name = "audit_by", length = 50)
    private String auditBy;

    /**
     * 备注
     */
    @Column(name = "remark", length = 200)
    private String remark;

    public Long getWaybillId() {
        return waybillId;
    }

    public void setWaybillId(Long waybillId) {
        this.waybillId = waybillId;
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
