package com.tms.service;

import com.tms.entity.Dispatch;
import com.tms.entity.Vehicle;
import com.tms.entity.Driver;
import com.tms.entity.Waybill;

import java.math.BigDecimal;
import java.util.List;

/**
 * 调度规则引擎接口
 * 提供智能配载、路径规划、运力匹配等核心调度功能
 *
 * @author TMS Team
 * @version 1.0.0
 */
public interface DispatchRuleEngine {

    /**
     * 策略类型枚举
     */
    enum StrategyType {
        DISTANCE_FIRST,    // 距离优先
        COST_FIRST,        // 成本优先
        TIME_FIRST,        // 时效优先
        BALANCE            // 平衡策略
    }

    /**
     * 智能派车
     * 根据运单信息和策略类型，自动匹配合适的车辆和司机
     *
     * @param waybill      运单信息
     * @param strategyType 策略类型
     * @return 调度方案
     */
    DispatchPlan assignVehicle(Waybill waybill, StrategyType strategyType);

    /**
     * 批量智能派车
     * 对多个运单进行批量调度优化
     *
     * @param waybills     运单列表
     * @param strategyType 策略类型
     * @return 调度方案列表
     */
    List<DispatchPlan> batchAssignVehicles(List<Waybill> waybills, StrategyType strategyType);

    /**
     * 路径规划
     * 计算从起点到终点的最优路径
     *
     * @param startAddress 起点地址
     * @param endAddress   终点地址
     * @param waypoints    途经点
     * @return 路径规划结果
     */
    RoutePlan planRoute(String startAddress, String endAddress, List<String> waypoints);

    /**
     * 计算ETA（预计到达时间）
     *
     * @param startAddress 起点地址
     * @param endAddress   终点地址
     * @param distance     距离（公里）
     * @param vehicleType  车辆类型
     * @return 预计到达时间（分钟）
     */
    int calculateETA(String startAddress, String endAddress, BigDecimal distance, String vehicleType);

    /**
     * 运力匹配评分
     * 评估车辆与运单的匹配程度
     *
     * @param waybill 运单
     * @param vehicle 车辆
     * @param driver  司机
     * @return 匹配分数（0-100）
     */
    int calculateMatchScore(Waybill waybill, Vehicle vehicle, Driver driver);

    /**
     * 检查车辆是否适合运输该运单
     *
     * @param waybill 运单
     * @param vehicle 车辆
     * @return 是否适合
     */
    boolean isVehicleSuitable(Waybill waybill, Vehicle vehicle);

    /**
     * 调度方案类
     */
    class DispatchPlan {
        private Long waybillId;
        private Long vehicleId;
        private Long driverId;
        private Long carrierId;
        private BigDecimal plannedDistance;
        private int estimatedTime; // 预计时间（分钟）
        private BigDecimal estimatedCost;
        private String routePlan;
        private int matchScore;
        private String strategy;

        // Getters and Setters
        public Long getWaybillId() {
            return waybillId;
        }

        public void setWaybillId(Long waybillId) {
            this.waybillId = waybillId;
        }

        public Long getVehicleId() {
            return vehicleId;
        }

        public void setVehicleId(Long vehicleId) {
            this.vehicleId = vehicleId;
        }

        public Long getDriverId() {
            return driverId;
        }

        public void setDriverId(Long driverId) {
            this.driverId = driverId;
        }

        public Long getCarrierId() {
            return carrierId;
        }

        public void setCarrierId(Long carrierId) {
            this.carrierId = carrierId;
        }

        public BigDecimal getPlannedDistance() {
            return plannedDistance;
        }

        public void setPlannedDistance(BigDecimal plannedDistance) {
            this.plannedDistance = plannedDistance;
        }

        public int getEstimatedTime() {
            return estimatedTime;
        }

        public void setEstimatedTime(int estimatedTime) {
            this.estimatedTime = estimatedTime;
        }

        public BigDecimal getEstimatedCost() {
            return estimatedCost;
        }

        public void setEstimatedCost(BigDecimal estimatedCost) {
            this.estimatedCost = estimatedCost;
        }

        public String getRoutePlan() {
            return routePlan;
        }

        public void setRoutePlan(String routePlan) {
            this.routePlan = routePlan;
        }

        public int getMatchScore() {
            return matchScore;
        }

        public void setMatchScore(int matchScore) {
            this.matchScore = matchScore;
        }

        public String getStrategy() {
            return strategy;
        }

        public void setStrategy(String strategy) {
            this.strategy = strategy;
        }
    }

    /**
     * 路径规划结果类
     */
    class RoutePlan {
        private String startAddress;
        private String endAddress;
        private List<String> waypoints;
        private BigDecimal totalDistance;
        private int estimatedTime;
        private String routeJson;
        private List<RouteSegment> segments;

        // Getters and Setters
        public String getStartAddress() {
            return startAddress;
        }

        public void setStartAddress(String startAddress) {
            this.startAddress = startAddress;
        }

        public String getEndAddress() {
            return endAddress;
        }

        public void setEndAddress(String endAddress) {
            this.endAddress = endAddress;
        }

        public List<String> getWaypoints() {
            return waypoints;
        }

        public void setWaypoints(List<String> waypoints) {
            this.waypoints = waypoints;
        }

        public BigDecimal getTotalDistance() {
            return totalDistance;
        }

        public void setTotalDistance(BigDecimal totalDistance) {
            this.totalDistance = totalDistance;
        }

        public int getEstimatedTime() {
            return estimatedTime;
        }

        public void setEstimatedTime(int estimatedTime) {
            this.estimatedTime = estimatedTime;
        }

        public String getRouteJson() {
            return routeJson;
        }

        public void setRouteJson(String routeJson) {
            this.routeJson = routeJson;
        }

        public List<RouteSegment> getSegments() {
            return segments;
        }

        public void setSegments(List<RouteSegment> segments) {
            this.segments = segments;
        }
    }

    /**
     * 路段信息类
     */
    class RouteSegment {
        private String fromAddress;
        private String toAddress;
        private BigDecimal distance;
        private int estimatedTime;
        private String roadType;

        // Getters and Setters
        public String getFromAddress() {
            return fromAddress;
        }

        public void setFromAddress(String fromAddress) {
            this.fromAddress = fromAddress;
        }

        public String getToAddress() {
            return toAddress;
        }

        public void setToAddress(String toAddress) {
            this.toAddress = toAddress;
        }

        public BigDecimal getDistance() {
            return distance;
        }

        public void setDistance(BigDecimal distance) {
            this.distance = distance;
        }

        public int getEstimatedTime() {
            return estimatedTime;
        }

        public void setEstimatedTime(int estimatedTime) {
            this.estimatedTime = estimatedTime;
        }

        public String getRoadType() {
            return roadType;
        }

        public void setRoadType(String roadType) {
            this.roadType = roadType;
        }
    }
}
