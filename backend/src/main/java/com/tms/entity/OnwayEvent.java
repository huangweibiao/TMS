package com.tms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 在途事件实体类
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Entity
@Table(name = "tms_onway_event")
public class OnwayEvent extends BaseEntity {

    /**
     * 调度单ID
     */
    @Column(name = "dispatch_id", nullable = false)
    private Long dispatchId;

    /**
     * 运单ID
     */
    @Column(name = "waybill_id", nullable = false)
    private Long waybillId;

    /**
     * 事件类型：1-超速, 2-疲劳驾驶, 3-偏离路线, 4-长时间停留, 5-温度异常, 6-紧急报警
     */
    @Column(name = "event_type", nullable = false)
    private Integer eventType;

    /**
     * 事件级别：1-提示, 2-警告, 3-严重
     */
    @Column(name = "event_level", nullable = false)
    private Integer eventLevel;

    /**
     * 事件内容
     */
    @Column(name = "event_content", length = 500)
    private String eventContent;

    /**
     * 发生位置
     */
    @Column(name = "location", length = 200)
    private String location;

    /**
     * 事件时间
     */
    @Column(name = "event_time", nullable = false)
    private LocalDateTime eventTime;

    /**
     * 是否处理：1-是, 0-否
     */
    @Column(name = "is_handled")
    private Integer isHandled = 0;

    /**
     * 处理结果
     */
    @Column(name = "handle_result", length = 200)
    private String handleResult;

    /**
     * 处理时间
     */
    @Column(name = "handle_time")
    private LocalDateTime handleTime;

    /**
     * 处理人
     */
    @Column(name = "handle_by", length = 50)
    private String handleBy;

    public Long getDispatchId() {
        return dispatchId;
    }

    public void setDispatchId(Long dispatchId) {
        this.dispatchId = dispatchId;
    }

    public Long getWaybillId() {
        return waybillId;
    }

    public void setWaybillId(Long waybillId) {
        this.waybillId = waybillId;
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
