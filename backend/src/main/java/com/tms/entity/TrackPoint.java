package com.tms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 轨迹点实体类
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Entity
@Table(name = "tms_track_point")
public class TrackPoint extends BaseEntity {

    /**
     * 调度单ID
     */
    @Column(name = "dispatch_id", nullable = false)
    private Long dispatchId;

    /**
     * 车辆ID
     */
    @Column(name = "vehicle_id", nullable = false)
    private Long vehicleId;

    /**
     * 经度
     */
    @Column(name = "longitude", nullable = false, precision = 10, scale = 7)
    private BigDecimal longitude;

    /**
     * 纬度
     */
    @Column(name = "latitude", nullable = false, precision = 10, scale = 7)
    private BigDecimal latitude;

    /**
     * 速度(km/h)
     */
    @Column(name = "speed", precision = 6, scale = 2)
    private BigDecimal speed;

    /**
     * 方向角度
     */
    @Column(name = "direction")
    private Integer direction;

    /**
     * 定位时间
     */
    @Column(name = "location_time", nullable = false)
    private LocalDateTime locationTime;

    public Long getDispatchId() {
        return dispatchId;
    }

    public void setDispatchId(Long dispatchId) {
        this.dispatchId = dispatchId;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
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

    public BigDecimal getSpeed() {
        return speed;
    }

    public void setSpeed(BigDecimal speed) {
        this.speed = speed;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public LocalDateTime getLocationTime() {
        return locationTime;
    }

    public void setLocationTime(LocalDateTime locationTime) {
        this.locationTime = locationTime;
    }
}
