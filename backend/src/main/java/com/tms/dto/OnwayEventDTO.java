package com.tms.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 在途事件DTO
 *
 * @author TMS Team
 * @version 1.0.0
 */
public class OnwayEventDTO {

    private Long id;

    /**
     * 调度单ID
     */
    @NotNull(message = "调度单ID不能为空")
    private Long dispatchId;

    /**
     * 调度单号
     */
    private String dispatchNo;

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
     * 事件类型：1-超速, 2-疲劳驾驶, 3-偏离路线, 4-长时间停留, 5-温度异常, 6-紧急报警
     */
    @NotNull(message = "事件类型不能为空")
    private Integer eventType;

    /**
     * 事件级别：1-提示, 2-警告, 3-严重
     */
    @NotNull(message = "事件级别不能为空")
    private Integer eventLevel;

    /**
     * 事件内容
     */
    private String eventContent;

    /**
     * 发生位置
     */
    private String location;

    /**
     * 事件时间
     */
    @NotNull(message = "事件时间不能为空")
    private LocalDateTime eventTime;

    /**
     * 是否处理：1-是, 0-否
     */
    private Integer isHandled = 0;

    /**
     * 处理结果
     */
    private String handleResult;

    /**
     * 处理时间
     */
    private LocalDateTime handleTime;

    /**
     * 处理人
     */
    private String handleBy;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getEventType() {
        return eventType;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }

    public Integer getEventLevel() {
        return eventLevel;
    }

    public void setEventLevel(Integer eventLevel) {
        this.eventLevel = eventLevel;
    }

    public String getEventContent() {
        return eventContent;
    }

    public void setEventContent(String eventContent) {
        this.eventContent = eventContent;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
    }

    public Integer getIsHandled() {
        return isHandled;
    }

    public void setIsHandled(Integer isHandled) {
        this.isHandled = isHandled;
    }

    public String getHandleResult() {
        return handleResult;
    }

    public void setHandleResult(String handleResult) {
        this.handleResult = handleResult;
    }

    public LocalDateTime getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(LocalDateTime handleTime) {
        this.handleTime = handleTime;
    }

    public String getHandleBy() {
        return handleBy;
    }

    public void setHandleBy(String handleBy) {
        this.handleBy = handleBy;
    }
}
