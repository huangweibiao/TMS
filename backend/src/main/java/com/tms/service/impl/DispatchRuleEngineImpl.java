package com.tms.service.impl;

import com.tms.entity.*;
import com.tms.repository.*;
import com.tms.service.DispatchRuleEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 调度规则引擎实现类
 * 实现智能配载、路径规划、运力匹配等核心调度功能
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Service
public class DispatchRuleEngineImpl implements DispatchRuleEngine {

    private final WaybillRepository waybillRepository;
    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;
    private final CarrierRepository carrierRepository;
    private final WarehouseRepository warehouseRepository;

    @Autowired
    public DispatchRuleEngineImpl(WaybillRepository waybillRepository,
                                  VehicleRepository vehicleRepository,
                                  DriverRepository driverRepository,
                                  CarrierRepository carrierRepository,
                                  WarehouseRepository warehouseRepository) {
        this.waybillRepository = waybillRepository;
        this.vehicleRepository = vehicleRepository;
        this.driverRepository = driverRepository;
        this.carrierRepository = carrierRepository;
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public DispatchPlan assignVehicle(Waybill waybill, StrategyType strategyType) {
        // 获取所有可用车辆
        List<Vehicle> availableVehicles = vehicleRepository.findByStatus(1);

        if (availableVehicles.isEmpty()) {
            return null;
        }

        // 筛选适合该运单的车辆
        List<Vehicle> suitableVehicles = availableVehicles.stream()
                .filter(v -> isVehicleSuitable(waybill, v))
                .collect(Collectors.toList());

        if (suitableVehicles.isEmpty()) {
            return null;
        }

        // 获取所有可用司机
        List List<Driver> availableDrivers = driverRepository.findByStatus(1);

        if (availableDrivers.isEmpty()) {
            return null;
        }

        // 计算每个车辆-司机组合的匹配分数
        List List<DispatchPlan> plans = new ArrayList<>();
        for (Vehicle vehicle : suitableVehicles) {
            for (Driver driver : availableDrivers) {
                // 检查司机是否绑定了该车辆
                if (driver.getVehicleId() != null && !driver.getVehicleId().equals(vehicle.getId())) {
                    continue;
                }

                DispatchPlan plan = createDispatchPlan(waybill, vehicle, driver, strategyType);
                plans.add(plan);
            }
        }

        if (plans.isEmpty()) {
            return null;
        }

        // 根据策略排序并返回最优方案
        return selectBestPlan(plans, strategyType);
    }

    @Override
    public List List<DispatchPlan> batchAssignVehicles(List(List<Waybill> waybills, StrategyType strategyType) {
        List List<DispatchPlan> plans = new ArrayList<>();

        // 简化的批量调度：逐个处理
        for (Waybill waybill : waybills) {
            DispatchPlan plan = assignVehicle(waybill, strategyType);
            if (plan != null) {
                plans.add(plan);
            }
        }

        return plans;
    }

    @Override
    public RoutePlan planRoute(String startAddress, String endAddress, List<String> waypoints) {
        // 简化的路径规划实现
        RoutePlan routePlan = new RoutePlan();
        routePlan.setStartAddress(startAddress);
        routePlan.setEndAddress(endAddress);
        routePlan.setWaypoints(waypoints);

        // 计算距离（简化：使用地址字符串长度模拟距离计算）
        BigDecimal distance = calculateDistance(startAddress, endAddress);
        routePlan.setTotalDistance(distance);

        // 估算时间（假设平均速度60km/h）
        int estimatedTime = distance.divide(new BigDecimal("60"), 0, RoundingMode.UP).intValue() * 60;
        routePlan.setEstimatedTime(estimatedTime);

        // 生成路段信息
        List List<RouteSegment> segments = new ArrayList<>();
        String currentFrom = startAddress;

        if (waypoints != null && !waypoints.isEmpty()) {
            for (String waypoint : waypoints) {
                RouteSegment segment = new RouteSegment();
                segment.setFromAddress(currentFrom);
                segment.setToAddress(waypoint);
                segment.setDistance(calculateDistance(currentFrom, waypoint));
                segment.setEstimatedTime(segment.getDistance().divide(new BigDecimal("60"), 0, RoundingMode.UP).intValue() * 60);
                segment.setRoadType("HIGHWAY");
                segments.add(segment);
                currentFrom = waypoint;
            }
        }

        // 最后一段到终点
        RouteSegment finalSegment = new RouteSegment();
        finalSegment.setFromAddress(currentFrom);
        finalSegment.setToAddress(endAddress);
        finalSegment.setDistance(calculateDistance(currentFrom, endAddress));
        finalSegment.setEstimatedTime(finalSegment.getDistance().divide(new BigDecimal("60"), 0, RoundingMode.UP).intValue() * 60);
        finalSegment.setRoadType("HIGHWAY");
        segments.add(finalSegment);

        routePlan.setSegments(segments);
        routePlan.setRouteJson(generateRouteJson(routePlan));

        return routePlan;
    }

    @Override
    public int calculateETA(String startAddress, String endAddress, BigDecimal distance, String vehicleType) {
        // 基础速度（km/h）
        int baseSpeed = 60;

        // 根据车辆类型调整速度
        if (vehicleType != null) {
            switch (vehicleType) {
                case "冷藏":
                    baseSpeed = 55;
                    break;
                case "危险品":
                    case "危化品":
                    case "hazardous":
                    case "dangerous":
                        baseSpeed = 50;
                        break;
                case "快递":
                case "express":
                    baseSpeed = 70;
                    break;
                default:
                    baseSpeed = 60;
            }
        }

        // 计算时间（分钟）
        return distance.divide(new BigDecimal(baseSpeed), 2, RoundingMode.UP)
                .multiply(new BigDecimal("60"))
                .intValue();
    }

    @Override
    public int calculateMatchScore(Waybill waybill, Vehicle vehicle, Driver driver) {
        int score = 0;

        // 载重匹配（30分）
        if (vehicle.getLoadCapacity() != null && waybill.getTotalWeight() != null) {
            BigDecimal weightRatio = waybill.getTotalWeight().divide(vehicle.getLoadCapacity(), 2, RoundingMode.HALF_UP);
            if (weightRatio.compareTo(new BigDecimal("0.5")) >= 0 && weightRatio.compareTo(new BigDecimal("0.9")) <= 0) {
                score += 30; // 理想载重范围
            } else if (weightRatio.compareTo(new BigDecimal("0.9")) < 1) {
                score += 20; // 可以承载
            } else {
                score += 5; // 超载风险
            }
        }

        // 容积匹配（20分）
        if (vehicle.getVolumeCapacity() != null && waybill.getTotalVolume() != null) {
            BigDecimal volumeRatio = waybill.getTotalVolume().divide(vehicle.getVolumeCapacity(), 2, RoundingMode.HALF_UP);
            if (volumeRatio.compareTo(new BigDecimal("0.5")) >= 0 && volumeRatio.compareTo(new BigDecimal("0.9")) <= 0) {
                score += 20;
            } else if (volumeRatio.compareTo(new BigDecimal("0.9")) < 1) {
                score += 15;
            } else {
                score += 5;
            }
        }

        // 车辆类型匹配（20分）
        if (waybill.getNeedTemperature() != null && waybill.getNeedTemperature() == 1) {
            if ("冷藏".equals(vehicle.getVehicleType())) {
                score += 20;
            }
        } else if (waybill.getIsHazardous() != null && waybill.getIsHazardous() == 1) {
            if ("危险品".equals(vehicle.getVehicleType()) || "危化品".equals(vehicle.getVehicleType())) {
                score += 20;
            }
        } else {
            score += 20; // 普通货物
        }

        // 司机资质匹配（20分）
        if (driver.getLicenseType() != null) {
            if (driver.getLicenseType().contains("A1") || driver.getLicenseType().contains("A2")) {
                score += 20; // 大型车辆驾照
            } else if (driver.getLicenseType().contains("B2")) {
                score += 15;
            } else {
                score += 10;
            }
        }

        // 距离因素（10分）
        // 假设车辆当前位置到取货点的距离（简化计算）
        score += 10;

        return Math.min(score, 100);
    }

    @Override
    public boolean isVehicleSuitable(Waybill waybill, Vehicle vehicle) {
        // 检查载重
        if (vehicle.getLoadCapacity() != null && waybill.getTotalWeight() != null) {
            if (waybill.getTotalWeight().compareTo(vehicle.getLoadCapacity()) > 0) {
                return false; // 超载
            }
        }

        // 检查容积
        if (vehicle.getVolumeCapacity() != null && waybill.getTotalVolume() != null) {
            if (waybill.getTotalVolume().compareTo(vehicle.getVolumeCapacity()) > 0) {
                return false; // 超容积
            }
        }

        // 检查车辆类型
        if (waybill.getNeedTemperature() != null && waybill.getNeedTemperature() == 1) {
            if (!"冷藏".equals(vehicle.getVehicleType())) {
                return false; // 需要冷藏车
            }
        }

        if (waybill.getIsHazardous() != null && waybill.getIsHazardous() == 1) {
            if (!("危险品".equals(vehicle.getVehicleType()) || "危化品".equals(vehicle.getVehicleType()))) {
                return false; // 需要危险品车
            }
        }

        return true;
    }

    /**
     * 创建调度方案
     */
    private DispatchPlan createDispatchPlan(Waybill waybill, Vehicle vehicle, Driver driver, StrategyType strategyType) {
        DispatchPlan plan = new DispatchPlan();
        plan.setWaybillId(waybill.getId());
        plan.setVehicleId(vehicle.getId());
        plan.setDriverId(driver.getId());
        plan.setCarrierId(vehicle.getCarrierId());
        plan.setStrategy(strategyType.name());

        // 计算匹配分数
        int matchScore = calculateMatchScore(waybill, vehicle, driver);
        plan.setMatchScore(matchScore);

        // 路径规划
        RoutePlan routePlan = planRoute(
                waybill.getShipperAddress(),
                waybill.getConsigneeAddress(),
                null
        );
        plan.setPlannedDistance(routePlan.getTotalDistance());
        plan.setEstimatedTime(routePlan.getEstimatedTime());
        plan.setRoutePlan(routePlan.getRouteJson());

        // 估算成本
        plan.setEstimatedCost(calculateCost(routePlan.getTotalDistance(), vehicle));

        return plan;
    }

    /**
     * 选择最优方案
     */
    private DispatchPlan selectBestPlan(List(List<DispatchPlan> plans, StrategyType strategyType) {
        switch (strategyType) {
            case DISTANCE_FIRST:
                return plans.stream()
                        .min(Comparator.comparing(DispatchPlan::getPlannedDistance))
                        .orElse(plans.get(0));
            case COST_FIRST:
                return plans.stream()
                        .min(Comparator.comparing(DispatchPlan::getEstimatedCost))
                        .orElse(plans.get(0));
            case TIME_FIRST:
                return plans.stream()
                        .min(Comparator.comparing(DispatchPlan::getEstimatedTime))
                        .orElse(plans.get(0));
            case BALANCE:
            default:
                // 综合考虑分数、距离、成本
                return plans.stream()
                        .max(Comparator.comparing(DispatchPlan::getMatchScore))
                        .orElse(plans.get(0));
        }
    }

    /**
     * 计算距离（简化实现）
     */
    private BigDecimal calculateDistance(String from, String to) {
        // 实际应该调用地图API
        // 这里使用简化计算：基于地址字符串长度模拟距离
        int combinedLength = (from != null ? from.length() : 0) + (to != null ? to.length() : 0);
        // 生成一个合理的距离值（50-500公里）
        return new BigDecimal(50 + (combinedLength % 450));
    }

    /**
     * 计算成本
     */
    private BigDecimal calculateCost(BigDecimal distance, Vehicle vehicle) {
        // 基础运费：每公里5元
        BigDecimal baseRate = new BigDecimal("5");

        // 根据车辆类型调整费率
        if (vehicle.getVehicleType() != null) {
            switch (vehicle.getVehicleType()) {
                case "冷藏":
                    baseRate = new BigDecimal("8");
                    break;
                case "危险品":
                case "危化品":
                    baseRate = new BigDecimal("10");
                    break;
                case "平板":
                    baseRate = new BigDecimal("6");
                    break;
                default:
                    baseRate = new BigDecimal("5");
            }
        }

        return distance.multiply(baseRate);
    }

    /**
     * 生成路径JSON
     */
    private String generateRouteJson(RoutePlan routePlan) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"start\":\"").append(routePlan.getStartAddress()).append("\",");
        json.append("\"end\":\"").append(routePlan.getEndAddress()).append("\",");
        json.append("\"distance\":").append(routePlan.getTotalDistance()).append(",");
        json.append("\"time\":").append(routePlan.getEstimatedTime()).append(",");
        json.append("\"segments\":[");

        if (routePlan.getSegments() != null) {
            for (int i = 0; i < routePlan.getSegments().size(); i++) {
                RouteSegment segment = routePlan.getSegments().get(i);
                if (i > 0) json.append(",");
                json.append("{");
                json.append("\"from\":\"").append(segment.getFromAddress()).append("\",");
                json.append("\"to\":\"").append(segment.getToAddress()).append("\",");
                json.append("\"distance\":").append(segment.getDistance()).append(",");
                json.append("\"time\":").append(segment.getEstimatedTime());
                json.append("}");
            }
        }

        json.append("]}");
        return json.toString();
    }
}
