package com.tms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 装货单实体类
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Entity
@Table(name = "tms_loading_order")
public class LoadingOrder extends BaseEntity {

    /**
     * 装货单号
     */
    @Column(name = "loading_no", nullable = false, unique = true, length = 32)
    private String loadingNo;

    /**
     * 调度单ID
     */
    @Column(name = "dispatch_id", nullable = false)
    private Long dispatchId;

    /**
     * 仓库ID
     */
    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    /**
     * 装货时间
     */
    @Column(name = "loading_time")
    private LocalDateTime loadingTime;

    /**
     * 操作人
     */
    @Column(name = "operator", length = 50)
    private String operator;

    /**
     * 状态：1-待装货, 2-装货中, 3-已完成
     */
    @Column(name = "status", nullable = false)
    private Integer status = 1;

    /**
     * 备注
     */
    @Column(name = "remark", length = 200)
    private String remark;

    public String getLoadingNo() {
        return loadingNo;
    }

    public void setLoadingNo(String loadingNo) {
        this.loadingNo = loadingNo;
    }

    public Long getDispatchId() {
        return dispatchId;
    }

    public void setDispatchId(Long dispatchId) {
        this.dispatchId = dispatchId;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public LocalDateTime getLoadingTime() {
        return loadingTime;
    }

    public void setLoadingTime(LocalDateTime loadingTime) {
        this.loadingTime = loadingTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
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
