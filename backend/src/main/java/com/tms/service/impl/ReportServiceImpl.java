package com.tms.service.impl;

import com.tms.dto.ReportDTO;
import com.tms.entity.CostDetail;
import com.tms.entity.Dispatch;
import com.tms.entity.OnwayEvent;
import com.tms.entity.Waybill;
import com.tms.repository.*;
import com.tms.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 报表服务实现类
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Service
public class ReportServiceImpl implements ReportService {

    private final WaybillRepository waybillRepository;
    private final CostDetailRepository costDetailRepository;
    private final DispatchRepository dispatchRepository;
    private final OnwayEventRepository onwayEventRepository;
    private final VehicleRepository vehicleRepository;

    @Autowired
    public ReportServiceImpl(WaybillRepository waybillRepository,
                             CostDetailRepository costDetailRepository,
                             DispatchRepository dispatchRepository,
                             OnwayEventRepository onwayEventRepository,
                             VehicleRepository vehicleRepository) {
        this.waybillRepository = waybillRepository;
        this.costDetailRepository = costDetailRepository;
        this.dispatchRepository = dispatchRepository;
        this.onwayEventRepository = onwayEventRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public List List<ReportDTO> getWaybillStatistics(LocalDate startDate, LocalDate endDate) {
        List List<ReportDTO> result = new ArrayList<>();
        
        // 获取日期范围内的运单
        LocalDateTime startTime = startDate.atStartOfDay();
        LocalDateTime endTime = endDate.plusDays(1).atStartOfDay();
        
        List List<Waybill> waybills = waybillRepository.findAll().stream()
                .filter(w -> w.getCreateTime() != null 
                        && !w.getCreateTime().isBefore(startTime) 
                        && w.getCreateTime().isBefore(endTime))
                .collect(Collectors.toList());
        
        // 按日期分组统计
        Map<String, List List<Waybill>> groupedByDate = waybills.stream()
                .collect(Collectors.groupingBy(w -> w.getCreateTime().toLocalDate().toString()));
        
        for (Map.Entry<String, List List<Waybill>> entry : groupedByDate.entrySet()) {
            ReportDTO dto = new ReportDTO();
            dto.setDate(entry.getKey());
            dto.setWaybillCount((long) entry.getValue().size());
            
            // 统计运费
            BigDecimal freightAmount = BigDecimal.ZERO;
            for (Waybill waybill : entry.getValue()) {
                List List<CostDetail> costs = costDetailRepository.findByWaybillId(waybill.getId());
                for (CostDetail cost : costs) {
                    if (cost.getCostType() == 1 && cost.getDirection() == 1) {
                        freightAmount = freightAmount.add(cost.getAmount());
                    }
                }
            }
            dto.setFreightAmount(freightAmount);
            
            result.add(dto);
        }
        
        // 按日期排序
        result.sort(Comparator.comparing(ReportDTO::getDate));
        
        return result;
    }

    @Override
    public Map<String, Object> getCostAnalysis(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> result = new HashMap<>();
        
        LocalDateTime startTime = startDate.atStartOfDay();
        LocalDateTime endTime = endDate.plusDays(1).atStartOfDay();
        
        // 获取所有费用明细
        List List<CostDetail> costDetails = costDetailRepository.findAll().stream()
                .filter(c -> c.getCreateTime() != null 
                        && !c.getCreateTime().isBefore(startTime) 
                        && c.getCreateTime().isBefore(endTime))
                .collect(Collectors.toList());
        
        // 按费用类型统计
        Map<Integer, BigDecimal> costByType = new HashMap<>();
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;
        
        for (CostDetail cost : costDetails) {
            BigDecimal amount = cost.getAmount();
            
            // 按类型统计
            costByType.merge(cost.getCostType(), amount, BigDecimal::add);
            
            // 按方向统计
            if (cost.getDirection() == 1) {
                totalIncome = totalIncome.add(amount);
            } else {
                totalExpense = totalExpense.add(amount);
            }
        }
        
        result.put("costByType", costByType);
        result.put("totalIncome", totalIncome);
        result.put("totalExpense", totalExpense);
        result.put("profit", totalIncome.subtract(totalExpense));
        
        return result;
    }

    @Override
    public Map<String, Object> getTransportEfficiency(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> result = new HashMap<>();
        
        LocalDateTime startTime = startDate.atStartOfDay();
        LocalDateTime endTime = endDate.plusDays(1).atStartOfDay();
        
        // 获取已完成的调度单
        List List<Dispatch> dispatches = dispatchRepository.findAll().stream()
                .filter(d -> d.getActualEndTime() != null 
                        && !d.getActualEndTime().isBefore(startTime) 
                        && d.getActualEndTime().isBefore(endTime))
                .collect(Collectors.toList());
        
        long totalTransportTime = 0;
        BigDecimal totalDistance = BigDecimal.ZERO;
        int count = 0;
        
        for (Dispatch dispatch : dispatches) {
            if (dispatch.getActualStartTime() != null && dispatch.getActualEndTime() != null) {
                long hours = ChronoUnit.HOURS.between(
                        dispatch.getActualStartTime(), 
                        dispatch.getActualEndTime());
                totalTransportTime += hours;
                count++;
            }
            if (dispatch.getActualDistance() != null) {
                totalDistance = totalDistance.add(dispatch.getActualDistance());
            }
        }
        
        double avgTransportTime = count > 0 ? (double) totalTransportTime / count : 0;
        
        result.put("totalDispatches", dispatches.size());
        result.put("totalDistance", totalDistance);
        result.put("avgTransportTime", avgTransportTime);
        result.put("completedCount", count);
        
        return result;
    }

    @Override
    public Map<String, Object> getVehicleUtilization(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> result = new HashMap<>();
        
        // 获取车辆总数
        long totalVehicles = vehicleRepository.count();
        long availableVehicles = vehicleRepository.findByStatus(1).size();
        long inTransitVehicles = vehicleRepository.findByStatus(2).size();
        long maintenanceVehicles = vehicleRepository.findByStatus(3).size();
        
        // 计算利用率
        double utilizationRate = totalVehicles > 0 
                ? (double) inTransitVehicles / totalVehicles * 100 
                : 0;
        
        result.put("totalVehicles", totalVehicles);
        result.put("availableVehicles", availableVehicles);
        result.put("inTransitVehicles", inTransitVehicles);
        result.put("maintenanceVehicles", maintenanceVehicles);
        result.put("utilizationRate", utilizationRate);
        
        return result;
    }

    @Override
    public Map<String, Object> getExceptionStatistics(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> result = new HashMap<>();
        
        LocalDateTime startTime = startDate.atStartOfDay();
        LocalDateTime endTime = endDate.plusDays(1).atStartOfDay();
        
        // 获取异常事件
        List List<OnwayEvent> events = onwayEventRepository.findAll().stream()
                .filter(e -> e.getEventTime() != null 
                        && !e.getEventTime().isBefore(startTime) 
                        && e.getEventTime().isBefore(endTime))
                .collect(Collectors.toList());
        
        // 按事件类型统计
        Map<Integer, Long> exceptionByType = events.stream()
                .collect(Collectors.groupingBy(OnwayEvent::getEventType, Collectors.counting()));
        
        // 按处理状态统计
        long handledCount = events.stream().filter(e -> e.getIsHandled() == 1).count();
        long unhandledCount = events.size() - handledCount;
        
        result.put("totalExceptions", events.size());
        result.put("exceptionByType", exceptionByType);
        result.put("handledCount", handledCount);
        result.put("unhandledCount", unhandledCount);
        
        return result;
    }

    @Override
    public Map<String, Object> getKpiDashboard() {
        Map<String, Object> result = new HashMap<>();
        
        LocalDate today = LocalDate.now();
        LocalDate monthStart = today.withDayOfMonth(1);
        
        // 今日运单数
        LocalDateTime todayStart = today.atStartOfDay();
        LocalDateTime todayEnd = today.plusDays(1).atStartOfDay();
        long todayWaybillCount = waybillRepository.findAll().stream()
                .filter(w -> w.getCreateTime() != null 
                        && !w.getCreateTime().isBefore(todayStart) 
                        && w.getCreateTime().isBefore(todayEnd))
                .count();
        
        // 本月运单数
        LocalDateTime monthStartTime = monthStart.atStartOfDay();
        long monthWaybillCount = waybillRepository.findAll().stream()
                .filter(w -> w.getCreateTime() != null 
                        && !w.getCreateTime().isBefore(monthStartTime))
                .count();
        
        // 在途车辆数
        long inTransitVehicles = vehicleRepository.findByStatus(2).size();
        
        // 待处理异常数
        long pendingExceptions = onwayEventRepository.findByIsHandledOrderByEventTimeDesc(0, 
                org.springframework.data.domain.PageRequest.of(0, 100)).getTotalElements();
        
        // 本月运费收入
        BigDecimal monthIncome = BigDecimal.ZERO;
        List List<CostDetail> monthCosts = costDetailRepository.findAll().stream()
                .filter(c -> c.getCreateTime() != null 
                        && !c.getCreateTime().isBefore(monthStartTime)
                        && c.getDirection() == 1)
                .collect(Collectors.toList());
        for (CostDetail cost : monthCosts) {
            monthIncome = monthIncome.add(cost.getAmount());
        }
        
        result.put("todayWaybillCount", todayWaybillCount);
        result.put("monthWaybillCount", monthWaybillCount);
        result.put("inTransitVehicles", inTransitVehicles);
        result.put("pendingExceptions", pendingExceptions);
        result.put("monthIncome", monthIncome);
        
        return result;
    }
}
