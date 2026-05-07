package com.tms.service;

import com.tms.dto.ReportDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 报表服务接口
 *
 * @author TMS Team
 * @version 1.0.0
 */
public interface ReportService {

    /**
     * 运单统计报表
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 统计结果
     */
    List List<ReportDTO> getWaybillStatistics(LocalDate startDate, LocalDate endDate);

    /**
     * 成本分析报表
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 统计结果
     */
    Map<String, Object> getCostAnalysis(LocalDate startDate, LocalDate endDate);

    /**
     * 运输时效统计
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 统计结果
     */
    Map<String, Object> getTransportEfficiency(LocalDate startDate, LocalDate endDate);

    /**
     * 车辆利用率统计
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 统计结果
     */
    Map<String, Object> getVehicleUtilization(LocalDate startDate, LocalDate endDate);

    /**
     * 异常统计
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 统计结果
     */
    Map<String, Object> getExceptionStatistics(LocalDate startDate, LocalDate endDate);

    /**
     * KPI看板数据
     *
     * @return KPI数据
     */
    Map<String, Object> getKpiDashboard();
}
