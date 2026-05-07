package com.tms.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 装货单DTO
 *
 * @author TMS Team
 * @version 1.0.0
 */
public class LoadingOrderDTO {

    private Long id;

    /**
     * 装货单号
     */
    private String loadingNo;

    /**
     * 调度单ID
     */
    @NotNull(message = "调度单ID不能为空")
    private Long dispatchId;

    /**
     * 调度单号（用于展示）
     */
    private String dispatchNo;

    /**
     * 仓库ID
     */
    @NotNull(message = "仓库ID不能为空")
    private Long warehouseId;

    /**
     * 仓库名称（用于展示）
     */
    private String warehouseName;

    /**
     * 装货时间
     */
    private LocalDateTime loadingTime;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 状态：1-待装货, 2-装货中, 3-已完成
     */
    private Integer status = 1;

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

    public String getDispatchNo() {
        return dispatchNo;
    }

    public void setDispatchNo(String dispatchNo) {
        this.dispatchNo = dispatchNo;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
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
