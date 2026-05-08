package com.tms.scheduler;

import com.tms.entity.CostDetail;
import com.tms.entity.Customer;
import com.tms.entity.Settlement;
import com.tms.repository.CostDetailRepository;
import com.tms.repository.CustomerRepository;
import com.tms.repository.OnwayEventRepository;
import com.tms.repository.SettlementRepository;
import com.tms.repository.TrackPointRepository;
import com.tms.service.ReportService;
import com.tms.service.SettlementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TMS定时任务调度器
 * 负责结算周期触发、数据归档、报表生成等定时任务
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Component
public class TmsScheduler {

    private static final Logger logger = LoggerFactory.getLogger(TmsScheduler.class);

    private final SettlementService settlementService;
    private final ReportService reportService;
    private final CostDetailRepository costDetailRepository;
    private final SettlementRepository settlementRepository;
    private final CustomerRepository customerRepository;
    private final OnwayEventRepository onwayEventRepository;
    private final TrackPointRepository trackPointRepository;

    @Autowired
    public TmsScheduler(SettlementService settlementService,
                        ReportService reportService,
                        CostDetailRepository costDetailRepository,
                        SettlementRepository settlementRepository,
                        CustomerRepository customerRepository,
                        OnwayEventRepository onwayEventRepository,
                        TrackPointRepository trackPointRepository) {
        this.settlementService = settlementService;
        this.reportService = reportService;
        this.costDetailRepository = costDetailRepository;
        this.settlementRepository = settlementRepository;
        this.customerRepository = customerRepository;
        this.onwayEventRepository = onwayEventRepository;
        this.trackPointRepository = trackPointRepository;
    }

    /**
     * 月结结算单生成任务
     * 每月1日凌晨2点执行
     */
    @Scheduled(cron = "0 0 2 1 * ?")
    @Transactional
    public void generateMonthlySettlements() {
        logger.info("开始执行月结结算单生成任务");

        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        LocalDate startDate = lastMonth.withDayOfMonth(1);
        LocalDate endDate = lastMonth.withDayOfMonth(lastMonth.lengthOfMonth());

        // 为所有月结客户生成结算单
        List<Customer> monthlyCustomers = customerRepository.findAll().stream()
                .filter(c -> c.getSettlementCycle() != null && c.getSettlementCycle() == 1)
                .collect(Collectors.toList());

        for (Customer customer : monthlyCustomers) {
            try {
                createCustomerSettlement(customer.getId(), startDate, endDate, 1);
                logger.info("为客户 {} 生成月结结算单成功", customer.getCustomerName());
            } catch (Exception e) {
                logger.error("为客户 {} 生成月结结算单失败", customer.getCustomerName(), e);
            }
        }

        logger.info("月结结算单生成任务完成，共处理 {} 个客户", monthlyCustomers.size());
    }

    /**
     * 周结结算单生成任务
     * 每周一凌晨2点执行
     */
    @Scheduled(cron = "0 0 2 ? * MON")
    @Transactional
    public void generateWeeklySettlements() {
        logger.info("开始执行周结结算单生成任务");

        LocalDate lastWeek = LocalDate.now().minusWeeks(1);
        LocalDate startDate = lastWeek.with(java.time.DayOfWeek.MONDAY);
        LocalDate endDate = lastWeek.with(java.time.DayOfWeek.SUNDAY);

        // 为所有周结客户生成结算单
        List<Customer> weeklyCustomers = customerRepository.findAll().stream()
                .filter(c -> c.getSettlementCycle() != null && c.getSettlementCycle() == 2)
                .collect(Collectors.toList());

        for (Customer customer : weeklyCustomers) {
            try {
                createCustomerSettlement(customer.getId(), startDate, endDate, 2);
                logger.info("为客户 {} 生成周结结算单成功", customer.getCustomerName());
            } catch (Exception e) {
                logger.error("为客户 {} 生成周结结算单失败", customer.getCustomerName(), e);
            }
        }

        logger.info("周结结算单生成任务完成，共处理 {} 个客户", weeklyCustomers.size());
    }

    /**
     * 超时运单自动取消任务
     * 每小时执行一次
     */
    @Scheduled(cron = "0 0 * * * ?")
    @Transactional
    public void autoCancelTimeoutWaybills() {
        logger.info("开始执行超时运单自动取消任务");

        // 获取创建时间超过24小时且状态为待调度的运单
        LocalDateTime timeoutTime = LocalDateTime.now().minusHours(24);

        // 这里应该调用WaybillService来取消运单
        // 简化实现：记录日志
        logger.info("检查创建时间早于 {} 的待调度运单", timeoutTime);

        logger.info("超时运单自动取消任务完成");
    }

    /**
     * 在途车辆状态检查任务
     * 每30分钟执行一次
     */
    @Scheduled(cron = "0 */30 * * * ?")
    public void checkInTransitVehicles() {
        logger.info("开始执行在途车辆状态检查任务");

        // 检查长时间未上报GPS的车辆
        LocalDateTime timeoutThreshold = LocalDateTime.now().minusMinutes(30);

        // 这里应该查询在途但长时间未上报GPS的车辆
        logger.info("检查最后上报时间早于 {} 的在途车辆", timeoutThreshold);

        logger.info("在途车辆状态检查任务完成");
    }

    /**
     * 异常事件自动处理提醒任务
     * 每15分钟执行一次
     */
    @Scheduled(cron = "0 */15 * * * ?")
    public void checkUnhandledEvents() {
        logger.info("开始执行异常事件检查任务");

        // 获取超过30分钟未处理的异常事件
        LocalDateTime timeoutThreshold = LocalDateTime.now().minusMinutes(30);

        // 这里应该查询未处理且超时的异常事件
        logger.info("检查发生时间早于 {} 且未处理的异常事件", timeoutThreshold);

        logger.info("异常事件检查任务完成");
    }

    /**
     * 数据归档任务
     * 每天凌晨3点执行
     */
    @Scheduled(cron = "0 0 3 * * ?")
    @Transactional
    public void archiveOldData() {
        logger.info("开始执行数据归档任务");

        // 归档3个月前的轨迹数据
        LocalDateTime archiveThreshold = LocalDateTime.now().minusMonths(3);
        archiveTrackPoints(archiveThreshold);

        // 归档6个月前的异常事件
        LocalDateTime eventArchiveThreshold = LocalDateTime.now().minusMonths(6);
        archiveOnwayEvents(eventArchiveThreshold);

        logger.info("数据归档任务完成");
    }

    /**
     * 报表数据预计算任务
     * 每天凌晨4点执行
     */
    @Scheduled(cron = "0 0 4 * * ?")
    public void precomputeReportData() {
        logger.info("开始执行报表数据预计算任务");

        LocalDate yesterday = LocalDate.now().minusDays(1);

        try {
            // 预计算日报表数据
            reportService.getWaybillStatistics(yesterday, yesterday);
            reportService.getKpiDashboard();
            logger.info("报表数据预计算完成");
        } catch (Exception e) {
            logger.error("报表数据预计算失败", e);
        }
    }

    /**
     * 缓存刷新任务
     * 每5分钟执行一次
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void refreshCache() {
        logger.info("开始执行缓存刷新任务");

        // 刷新热点数据缓存
        // 如：在途车辆数、待处理运单数等

        logger.info("缓存刷新任务完成");
    }

    /**
     * 创建客户结算单
     */
    private void createCustomerSettlement(Long customerId, LocalDate startDate, 
                                          LocalDate endDate, Integer settlementType) {
        // 查询该客户在日期范围内的未结算费用
        LocalDateTime startTime = startDate.atStartOfDay();
        LocalDateTime endTime = endDate.plusDays(1).atStartOfDay();

        List<CostDetail> unsettledCosts = costDetailRepository.findAll().stream()
                .filter(c -> c.getDirection() != null && c.getDirection() == 1)
                .filter(c -> c.getSettlementStatus() != null && c.getSettlementStatus() == 1)
                .filter(c -> c.getCreateTime() != null)
                .filter(c -> !c.getCreateTime().isBefore(startTime) && c.getCreateTime().isBefore(endTime))
                .collect(Collectors.toList());

        if (unsettledCosts.isEmpty()) {
            logger.info("客户 {} 在 {} 至 {} 期间没有未结算费用", customerId, startDate, endDate);
            return;
        }

        // 计算总金额
        BigDecimal totalAmount = unsettledCosts.stream()
                .map(CostDetail::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 创建结算单
        Settlement settlement = new Settlement();
        settlement.setSettlementNo(generateSettlementNo());
        settlement.setPartyType(1);
        settlement.setPartyId(customerId);
        settlement.setSettlementStart(startDate);
        settlement.setSettlementEnd(endDate);
        settlement.setTotalAmount(totalAmount);
        settlement.setPaidAmount(BigDecimal.ZERO);
        settlement.setUnpaidAmount(totalAmount);
        settlement.setStatus(1);
        settlement.setInvoiceStatus(0);
        settlement.setCreateTime(LocalDateTime.now());

        settlementRepository.save(settlement);

        // 更新费用明细的结算单ID
        for (CostDetail cost : unsettledCosts) {
            cost.setSettlementId(settlement.getId());
            cost.setSettlementStatus(2);
            costDetailRepository.save(cost);
        }
    }

    /**
     * 归档轨迹点数据
     */
    private void archiveTrackPoints(LocalDateTime archiveThreshold) {
        // 实际项目中应该将数据移动到归档表或文件存储
        // 简化实现：记录日志
        logger.info("归档 {} 之前的轨迹点数据", archiveThreshold);
    }

    /**
     * 归档异常事件数据
     */
    private void archiveOnwayEvents(LocalDateTime archiveThreshold) {
        // 实际项目中应该将数据移动到归档表
        // 简化实现：记录日志
        logger.info("归档 {} 之前的异常事件数据", archiveThreshold);
    }

    /**
     * 生成结算单号
     */
    private String generateSettlementNo() {
        return "ST" + System.currentTimeMillis();
    }
}
