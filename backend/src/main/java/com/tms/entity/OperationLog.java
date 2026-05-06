package com.tms.entity;

import jakarta.persistence.*;

/**
 * 操作日志实体类
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Entity
@Table(name = "tms_operation_log")
public class OperationLog extends BaseEntity {

    /**
     * 操作用户ID
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 用户名
     */
    @Column(name = "username", length = 50)
    private String username;

    /**
     * 操作类型（新增/修改/删除/查询）
     */
    @Column(name = "operation_type", length = 50)
    private String operationType;

    /**
     * 操作描述
     */
    @Column(name = "operation_desc", length = 500)
    private String operationDesc;

    /**
     * 请求URL
     */
    @Column(name = "request_url", length = 200)
    private String requestUrl;

    /**
     * 请求参数
     */
    @Column(name = "request_params", columnDefinition = "TEXT")
    private String requestParams;

    /**
     * 响应结果
     */
    @Column(name = "response_result", columnDefinition = "TEXT")
    private String responseResult;

    /**
     * IP地址
     */
    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    /**
     * 耗时(ms)
     */
    @Column(name = "cost_time")
    private Integer costTime;

    /**
     * 是否成功：1-是, 0-否
     */
    @Column(name = "is_success")
    private Integer isSuccess = 1;

    /**
     * 错误信息
     */
    @Column(name = "error_msg", length = 500)
    private String errorMsg;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getOperationDesc() {
        return operationDesc;
    }

    public void setOperationDesc(String operationDesc) {
        this.operationDesc = operationDesc;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(String requestParams) {
        this.requestParams = requestParams;
    }

    public String getResponseResult() {
        return responseResult;
    }

    public void setResponseResult(String responseResult) {
        this.responseResult = responseResult;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Integer getCostTime() {
        return costTime;
    }

    public void setCostTime(Integer costTime) {
        this.costTime = costTime;
    }

    public Integer getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Integer isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
