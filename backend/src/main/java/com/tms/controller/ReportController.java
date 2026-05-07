package com.tms.controller;

import com.tms.common.Result;
import com.tms.dto.ReportDTO;
import com.tms.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 报表控制器
 *
 * @author TMS Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * 运单统计报表
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 统计结果
     */
    @GetMapping("/waybill-stat")
    public Result<List<List<ReportDTO>> getWaybillStatistics(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        List List<ReportDTO> result = reportService.getWaybillStatistics(startDate, endDate);
        return Result.success(result);
    }

    /**
     * 成本分析报表
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 统计结果
     */
    @GetMapping("/cost-analysis")
    public Result<Map<String, Object>> getCostAnalysis(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        Map<String, Object> result = reportService.getCostAnalysis(startDate, endDate);
        return Result.success(result);
    }

    /**
     * 运输时效统计
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 统计结果
     */
    @GetMapping("/transport-efficiency")
    public Result<Map<String, Object>> getTransportEfficiency(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        Map<String, Object> result = reportService.getTransportEfficiency(startDate, endDate);
        return Result.success(result);
    }

    /**
     * 车辆利用率统计
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 统计结果
     */
    @GetMapping("/vehicle-utilization")
    public Result<Map<String, Object>> getVehicleUtilization(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        Map<String, Object> result = reportService.getVehicleUtilization(startDate, endDate);
        return Result.success(result);
    }

    /**
     * 异常统计
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 统计结果
     */
    @GetMapping("/exception-statistics")
    public Result<Map<String, Object>> getExceptionStatistics(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        Map<String, Object> result = reportService.getExceptionStatistics(startDate, endDate);
        return Result.success(result);
    }

    /**
     * KPI看板数据
     *
     * @return KPI数据
     */
    @GetMapping("/kpi-dashboard")
    public Result<Map<String, Object>> getKpiDashboard() {
        Map<String, Object> result = reportService.getKpiDashboard();
        return Result.success(result);
    }
}
