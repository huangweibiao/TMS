package com.tms.event;

import com.tms.config.RabbitMQConfig;
import com.tms.entity.*;
import com.tms.repository.*;
import com.tms.service.NotificationService;
import com.tms.service.ReceiptService;
import com.tms.service.TrackPointService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * TMS事件监听器
 * 处理业务事件，实现异步业务逻辑
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Component
public class TmsEventListener {

    private static final Logger logger = LoggerFactory.getLogger(TmsEventListener.class);

    private final WaybillRepository waybillRepository;
    private final DispatchRepository dispatchRepository;
    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;
    private final CustomerRepository customerRepository;
    private final CarrierRepository carrierRepository;
    private final SettlementRepository settlementRepository;
    private final CostDetailRepository costDetailRepository;
    private final ReceiptRepository receiptRepository;
    private final TrackPointService trackPointService;
    private final ReceiptService receiptService;
    private final NotificationService notificationService;

    @Autowired
    public TmsEventListener(WaybillRepository waybillRepository,
                            DispatchRepository dispatchRepository,
                            DriverRepository driverRepository,
                            VehicleRepository vehicleRepository,
                            CustomerRepository customerRepository,
                            CarrierRepository carrierRepository,
                            SettlementRepository settlementRepository,
                            CostDetailRepository costDetailRepository,
                            ReceiptRepository receiptRepository,
                            TrackPointService trackPointService,
                            ReceiptService receiptService,
                            NotificationService notificationService) {
        this.waybillRepository = waybillRepository;
        this.dispatchRepository = dispatchRepository;
        this.driverRepository = driverRepository;
        this.vehicleRepository = vehicleRepository;
        this.customerRepository = customerRepository;
        this.carrierRepository = carrierRepository;
        this.settlementRepository = settlementRepository;
        this.costDetailRepository = costDetailRepository;
        this.receiptRepository = receiptRepository;
        this.trackPointService = trackPointService;
        this.receiptService = receiptService;
        this.notificationService = notificationService;
    }

    /**
     * 监听运单创建事件
     */
    @RabbitListener(queues = RabbitMQConfig.QUEUE_WAYBILL_CREATED)
    @Transactional
    public void handleWaybillCreated(Map<String, Object> event) {
        logger.info("收到运单创建事件: {}", event);

        try {
            Long waybillId = Long.valueOf(event.get("waybillId").toString());

            // 1. 更新报表统计缓存
            logger.info("更新运单统计缓存，运单ID: {}", waybillId);

            // 2. 发送创建成功通知给客户
            waybillRepository.findById(waybillId).ifPresent(waybill -> {
                customerRepository.findById(waybill.getCustomerId()).ifPresent(customer -> {
                    if (customer.getContactPhone() != null) {
                        Map<String, String> params = new HashMap<>();
                        params.put("waybillNo", waybill.getWaybillNo());
                        params.put("status", "待调度");
                        notificationService.sendSms(customer.getContactPhone(), "WAYBILL_CREATED", params);
                    }
                });
            });

            // 3. 触发自动调度（如果是紧急运单）
            // 这里可以根据业务规则决定是否自动调度

            logger.info("运单创建事件处理完成，运单ID: {}", waybillId);
        } catch (Exception e) {
            logger.error("处理运单创建事件失败", e);
        }
    }

    /**
     * 监听运单状态变更事件
     */
    @RabbitListener(queues = RabbitMQConfig.QUEUE_WAYBILL_STATUS_CHANGED)
    @Transactional
    public void handleWaybillStatusChanged(Map<String, Object> event) {
        logger.info("收到运单状态变更事件: {}", event);

        try {
            Long waybillId = Long.valueOf(event.get("waybillId").toString());
            Integer oldStatus = Integer.valueOf(event.get("oldStatus").toString());
            Integer newStatus = Integer.valueOf(event.get("newStatus").toString());

            // 1. 发送状态变更通知
            waybillRepository.findById(waybillId).ifPresent(waybill -> {
                customerRepository.findById(waybill.getCustomerId()).ifPresent(customer -> {
                    if (customer.getContactPhone() != null) {
                        notificationService.sendWaybillStatusNotification(
                                waybillId, waybill.getWaybillNo(), oldStatus, newStatus, customer.getContactPhone());
                    }
                });
            });

            // 2. 更新报表统计
            logger.info("更新运单统计，运单ID: {}，状态: {} -> {}", waybillId, oldStatus, newStatus);

            logger.info("运单状态变更事件处理完成，运单ID: {}", waybillId);
        } catch (Exception e) {
            logger.error("处理运单状态变更事件失败", e);
        }
    }

    /**
     * 监听调度发车事件
     */
    @RabbitListener(queues = RabbitMQConfig.QUEUE_DISPATCH_STARTED)
    @Transactional
    public void handleDispatchStarted(Map<String, Object> event) {
        logger.info("收到调度发车事件: {}", event);

        try {
            Long dispatchId = Long.valueOf(event.get("dispatchId").toString());

            dispatchRepository.findById(dispatchId).ifPresent(dispatch -> {
                // 1. 更新车辆和司机状态为"在途"
                if (dispatch.getVehicleId() != null) {
                    vehicleRepository.findById(dispatch.getVehicleId()).ifPresent(vehicle -> {
                        vehicle.setStatus(2); // 在途
                        vehicleRepository.save(vehicle);
                    });
                }

                if (dispatch.getDriverId() != null) {
                    driverRepository.findById(dispatch.getDriverId()).ifPresent(driver -> {
                        driver.setStatus(2); // 在途
                        driverRepository.save(driver);
                    });
                }

                // 2. 发送发车通知给司机和客户
                waybillRepository.findById(dispatch.getWaybillId()).ifPresent(waybill -> {
                    driverRepository.findById(dispatch.getDriverId()).ifPresent(driver -> {
                        vehicleRepository.findById(dispatch.getVehicleId()).ifPresent(vehicle -> {
                            // 通知司机
                            notificationService.sendDispatchStartNotification(
                                    dispatch.getDispatchNo(),
                                    driver.getDriverName(),
                                    driver.getPhone(),
                                    vehicle.getPlateNumber(),
                                    waybill.getShipperAddress(),
                                    waybill.getConsigneeAddress()
                            );

                            // 通知客户
                            customerRepository.findById(waybill.getCustomerId()).ifPresent(customer -> {
                                if (customer.getContactPhone() != null) {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("waybillNo", waybill.getWaybillNo());
                                    params.put("driverName", driver.getDriverName());
                                    params.put("driverPhone", driver.getPhone());
                                    params.put("vehicleNo", vehicle.getPlateNumber());
                                    notificationService.sendSms(customer.getContactPhone(), "DISPATCH_STARTED", params);
                                }
                            });
                        });
                    });
                });

                // 3. 开始在途监控
                logger.info("开始在途监控，调度单ID: {}", dispatchId);
            });

            logger.info("调度发车事件处理完成，调度单ID: {}", dispatchId);
        } catch (Exception e) {
            logger.error("处理调度发车事件失败", e);
        }
    }

    /**
     * 监听调度完成事件
     */
    @RabbitListener(queues = RabbitMQConfig.QUEUE_DISPATCH_COMPLETED)
    @Transactional
    public void handleDispatchCompleted(Map<String, Object> event) {
        logger.info("收到调度完成事件: {}", event);

        try {
            Long dispatchId = Long.valueOf(event.get("dispatchId").toString());

            dispatchRepository.findById(dispatchId).ifPresent(dispatch -> {
                // 1. 更新车辆和司机状态为"空闲"
                if (dispatch.getVehicleId() != null) {
                    vehicleRepository.findById(dispatch.getVehicleId()).ifPresent(vehicle -> {
                        vehicle.setStatus(1); // 空闲
                        vehicleRepository.save(vehicle);
                    });
                }

                if (dispatch.getDriverId() != null) {
                    driverRepository.findById(dispatch.getDriverId()).ifPresent(driver -> {
                        driver.setStatus(1); // 空闲
                        driverRepository.save(driver);
                    });
                }

                // 2. 创建回单任务
                waybillRepository.findById(dispatch.getWaybillId()).ifPresent(waybill -> {
                    Receipt receipt = new Receipt();
                    receipt.setWaybillId(waybill.getId());
                    receipt.setReceiptNo("RC" + System.currentTimeMillis());
                    receipt.setStatus(1); // 未回传
                    receipt.setCreateTime(LocalDateTime.now());
                    receiptRepository.save(receipt);

                    logger.info("创建回单任务成功，运单号: {}", waybill.getWaybillNo());

                    // 3. 通知客户货物已送达
                    customerRepository.findById(waybill.getCustomerId()).ifPresent(customer -> {
                        if (customer.getContactPhone() != null) {
                            Map<String, String> params = new HashMap<>();
                            params.put("waybillNo", waybill.getWaybillNo());
                            params.put("address", waybill.getConsigneeAddress());
                            notificationService.sendSms(customer.getContactPhone(), "DELIVERY_COMPLETED", params);
                        }
                    });
                });

                // 4. 触发结算流程（现结客户）
                // 这里可以根据结算周期决定是否立即结算
            });

            logger.info("调度完成事件处理完成，调度单ID: {}", dispatchId);
        } catch (Exception e) {
            logger.error("处理调度完成事件失败", e);
        }
    }

    /**
     * 监听GPS定位事件
     */
    @RabbitListener(queues = RabbitMQConfig.QUEUE_GPS_LOCATION)
    public void handleGpsLocation(Map<String, Object> event) {
        logger.debug("收到GPS定位事件: {}", event);

        try {
            Long dispatchId = Long.valueOf(event.get("dispatchId").toString());
            Long vehicleId = Long.valueOf(event.get("vehicleId").toString());
            BigDecimal longitude = new BigDecimal(event.get("longitude").toString());
            BigDecimal latitude = new BigDecimal(event.get("latitude").toString());

        // 1. 保存轨迹点
        com.tms.dto.TrackPointDTO trackPointDTO = new com.tms.dto.TrackPointDTO();
        trackPointDTO.setDispatchId(dispatchId);
        trackPointDTO.setVehicleId(vehicleId);
        trackPointDTO.setLongitude(longitude);
        trackPointDTO.setLatitude(latitude);
        trackPointDTO.setSpeed(event.get("speed") != null ? new BigDecimal(event.get("speed").toString()) : null);
        trackPointDTO.setDirection(event.get("direction") != null ? Integer.valueOf(event.get("direction").toString()) : null);
        trackPointDTO.setLocationTime(LocalDateTime.now());

        trackPointService.createTrackPoint(trackPointDTO);

            // 2. 检测异常（超速、偏离路线等）
            // 这里可以实现更复杂的异常检测逻辑
            if (event.get("speed") != null) {
                BigDecimal speed = new BigDecimal(event.get("speed").toString());
                if (speed.compareTo(new BigDecimal("120")) > 0) {
                    logger.warn("检测到超速，调度单ID: {}，速度: {}", dispatchId, speed);
                    // 触发超速告警
                }
            }

        } catch (Exception e) {
            logger.error("处理GPS定位事件失败", e);
        }
    }

    /**
     * 监听异常告警事件
     */
    @RabbitListener(queues = RabbitMQConfig.QUEUE_EVENT_ALERT)
    @Transactional
    public void handleEventAlert(Map<String, Object> event) {
        logger.info("收到异常告警事件: {}", event);

        try {
            Integer eventType = Integer.valueOf(event.get("eventType").toString());
            Integer eventLevel = Integer.valueOf(event.get("eventLevel").toString());
            String eventContent = event.get("eventContent").toString();
            String location = event.get("location") != null ? event.get("location").toString() : "";
            Long dispatchId = event.get("dispatchId") != null ? Long.valueOf(event.get("dispatchId").toString()) : null;

            // 1. 发送告警通知
            // 获取调度相关的联系人
            List<String> phoneNumbers = new ArrayList<>();

            if (dispatchId != null) {
                dispatchRepository.findById(dispatchId).ifPresent(dispatch -> {
                    // 获取司机电话
                    driverRepository.findById(dispatch.getDriverId()).ifPresent(driver -> {
                        phoneNumbers.add(driver.getPhone());
                    });

                    // 获取客户联系人电话
                    waybillRepository.findById(dispatch.getWaybillId()).ifPresent(waybill -> {
                        customerRepository.findById(waybill.getCustomerId()).ifPresent(customer -> {
                            if (customer.getContactPhone() != null) {
                                phoneNumbers.add(customer.getContactPhone());
                            }
                        });
                    });
                });
            }

            if (!phoneNumbers.isEmpty()) {
                notificationService.sendAlertNotification(eventType, eventLevel, eventContent, location, phoneNumbers);
            }

            // 2. 创建告警工单（可以集成客服系统）
            logger.info("创建告警工单，类型: {}，级别: {}，内容: {}", eventType, eventLevel, eventContent);

            logger.info("异常告警事件处理完成");
        } catch (Exception e) {
            logger.error("处理异常告警事件失败", e);
        }
    }

    /**
     * 监听结算单创建事件
     */
    @RabbitListener(queues = RabbitMQConfig.QUEUE_SETTLEMENT_CREATED)
    @Transactional
    public void handleSettlementCreated(Map<String, Object> event) {
        logger.info("收到结算单创建事件: {}", event);

        try {
            Long settlementId = Long.valueOf(event.get("settlementId").toString());

            settlementRepository.findById(settlementId).ifPresent(settlement -> {
                // 1. 发送结算单通知
                if (settlement.getPartyType() == 1) {
                    // 客户结算单
                    customerRepository.findById(settlement.getPartyId()).ifPresent(customer -> {
                        if (customer.getContactPhone() != null) {
                            Map<String, String> params = new HashMap<>();
                            params.put("settlementNo", settlement.getSettlementNo());
                            params.put("amount", settlement.getTotalAmount().toString());
                            notificationService.sendSms(customer.getContactPhone(), "SETTLEMENT_CREATED", params);
                        }
                    });
                } else {
                    // 承运商结算单
                    carrierRepository.findById(settlement.getPartyId()).ifPresent(carrier -> {
                        if (carrier.getContactPhone() != null) {
                            Map<String, String> params = new HashMap<>();
                            params.put("settlementNo", settlement.getSettlementNo());
                            params.put("amount", settlement.getTotalAmount().toString());
                            notificationService.sendSms(carrier.getContactPhone(), "SETTLEMENT_CREATED", params);
                        }
                    });
                }

                // 2. 触发发票生成（如果需要）
                // 这里可以集成发票服务

                logger.info("结算单创建事件处理完成，结算单号: {}", settlement.getSettlementNo());
            });
        } catch (Exception e) {
            logger.error("处理结算单创建事件失败", e);
        }
    }

    /**
     * 监听回单上传事件
     */
    @RabbitListener(queues = RabbitMQConfig.QUEUE_RECEIPT_UPLOADED)
    @Transactional
    public void handleReceiptUploaded(Map<String, Object> event) {
        logger.info("收到回单上传事件: {}", event);

        try {
            Long receiptId = Long.valueOf(event.get("receiptId").toString());

            receiptRepository.findById(receiptId).ifPresent(receipt -> {
                // 1. 自动审核回单
                receipt.setStatus(3); // 已审核
                receipt.setAuditTime(LocalDateTime.now());
                receipt.setAuditBy("SYSTEM");
                receiptRepository.save(receipt);

                logger.info("回单自动审核完成，回单号: {}", receipt.getReceiptNo());

                // 2. 触发结算流程
                waybillRepository.findById(receipt.getWaybillId()).ifPresent(waybill -> {
                    // 更新运单状态为"完成"
                    waybill.setWaybillStatus(7); // 完成
                    waybillRepository.save(waybill);

                    logger.info("运单状态更新为完成，运单号: {}", waybill.getWaybillNo());

                    // 通知客户回单已审核
                    customerRepository.findById(waybill.getCustomerId()).ifPresent(customer -> {
                        if (customer.getContactPhone() != null) {
                            Map<String, String> params = new HashMap<>();
                            params.put("waybillNo", waybill.getWaybillNo());
                            notificationService.sendSms(customer.getContactPhone(), "REceipt_AUDITED", params);
                        }
                    });
                });
            });

            logger.info("回单上传事件处理完成，回单ID: {}", receiptId);
        } catch (Exception e) {
            logger.error("处理回单上传事件失败", e);
        }
    }
}
