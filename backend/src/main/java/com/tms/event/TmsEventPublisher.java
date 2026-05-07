package com.tms.event;

import com.tms.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * TMS事件发布器
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Component
public class TmsEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public TmsEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * 发布运单创建事件
     *
     * @param waybillId   运单ID
     * @param waybillNo   运单号
     * @param customerId  客户ID
     */
    public void publishWaybillCreated(Long waybillId, String waybillNo, Long customerId) {
        Map<String, Object> event = createBaseEvent("WAYBILL_CREATED");
        event.put("waybillId", waybillId);
        event.put("waybillNo", waybillNo);
        event.put("customerId", customerId);
        publish(RabbitMQConfig.ROUTING_KEY_WAYBILL_CREATED, event);
    }

    /**
     * 发布运单状态变更事件
     *
     * @param waybillId 运单ID
     * @param waybillNo 运单号
     * @param oldStatus 旧状态
     * @param newStatus 新状态
     */
    public void publishWaybillStatusChanged(Long waybillId, String waybillNo, Integer oldStatus, Integer newStatus) {
        Map<String, Object> event = createBaseEvent("WAYBILL_STATUS_CHANGED");
        event.put("waybillId", waybillId);
        event.put("waybillNo", waybillNo);
        event.put("oldStatus", oldStatus);
        event.put("newStatus", newStatus);
        publish(RabbitMQConfig.ROUTING_KEY_WAYBILL_STATUS_CHANGED, event);
    }

    /**
     * 发布调度发车事件
     *
     * @param dispatchId 调度单ID
     * @param dispatchNo 调度单号
     * @param waybillId  运单ID
     * @param vehicleId  车辆ID
     * @param driverId   司机ID
     */
    public void publishDispatchStarted(Long dispatchId, String dispatchNo, Long waybillId, Long vehicleId, Long driverId) {
        Map<String, Object> event = createBaseEvent("DISPATCH_STARTED");
        event.put("dispatchId", dispatchId);
        event.put("dispatchNo", dispatchNo);
        event.put("waybillId", waybillId);
        event.put("vehicleId", vehicleId);
        event.put("driverId", driverId);
        publish(RabbitMQConfig.ROUTING_KEY_DISPATCH_STARTED, event);
    }

    /**
     * 发布调度完成事件
     *
     * @param dispatchId 调度单ID
     * @param dispatchNo 调度单号
     * @param waybillId  运单ID
     */
    public void publishDispatchCompleted(Long dispatchId, String dispatchNo, Long waybillId) {
        Map<String, Object> event = createBaseEvent("DISPATCH_COMPLETED");
        event.put("dispatchId", dispatchId);
        event.put("dispatchNo", dispatchNo);
        event.put("waybillId", waybillId);
        publish(RabbitMQConfig.ROUTING_KEY_DISPATCH_COMPLETED, event);
    }

    /**
     * 发布GPS定位事件
     *
     * @param dispatchId  调度单ID
     * @param vehicleId   车辆ID
     * @param longitude   经度
     * @param latitude    纬度
     * @param speed       速度
     * @param locationTime 定位时间
     */
    public void publishGpsLocation(Long dispatchId, Long vehicleId, 
                                   java.math.BigDecimal longitude, 
                                   java.math.BigDecimal latitude,
                                   java.math.BigDecimal speed,
                                   LocalDateTime locationTime) {
        Map<String, Object> event = createBaseEvent("GPS_LOCATION");
        event.put("dispatchId", dispatchId);
        event.put("vehicleId", vehicleId);
        event.put("longitude", longitude);
        event.put("latitude", latitude);
        event.put("speed", speed);
        event.put("locationTime", locationTime);
        publish(RabbitMQConfig.ROUTING_KEY_GPS_LOCATION, event);
    }

    /**
     * 发布异常告警事件
     *
     * @param eventType   事件类型
     * @param eventLevel  事件级别
     * @param dispatchId  调度单ID
     * @param waybillId   运单ID
     * @param content     事件内容
     * @param location    发生位置
     */
    public void publishEventAlert(Integer eventType, Integer eventLevel, 
                                  Long dispatchId, Long waybillId,
                                  String content, String location) {
        Map<String, Object> event = createBaseEvent("EVENT_ALERT");
        event.put("eventType", eventType);
        event.put("eventLevel", eventLevel);
        event.put("dispatchId", dispatchId);
        event.put("waybillId", waybillId);
        event.put("content", content);
        event.put("location", location);
        event.put("eventTime", LocalDateTime.now());
        publish(RabbitMQConfig.ROUTING_KEY_EVENT_ALERT, event);
    }

    /**
     * 发布结算单创建事件
     *
     * @param settlementId 结算单ID
     * @param settlementNo 结算单号
     * @param partyType    结算方类型
     * @param partyId      结算方ID
     * @param totalAmount  总金额
     */
    public void publishSettlementCreated(Long settlementId, String settlementNo, 
                                         Integer partyType, Long partyId,
                                         java.math.BigDecimal totalAmount) {
        Map<String, Object> event = createBaseEvent("SETTLEMENT_CREATED");
        event.put("settlementId", settlementId);
        event.put("settlementNo", settlementNo);
        event.put("partyType", partyType);
        event.put("partyId", partyId);
        event.put("totalAmount", totalAmount);
        publish(RabbitMQConfig.ROUTING_KEY_SETTLEMENT_CREATED, event);
    }

    /**
     * 发布回单上传事件
     *
     * @param receiptId   回单ID
     * @param receiptNo   回单号
     * @param waybillId   运单ID
     * @param receiptType 回单类型
     */
    public void publishReceiptUploaded(Long receiptId, String receiptNo, 
                                       Long waybillId, Integer receiptType) {
        Map<String, Object> event = createBaseEvent("RECEIPT_UPLOADED");
        event.put("receiptId", receiptId);
        event.put("receiptNo", receiptNo);
        event.put("waybillId", waybillId);
        event.put("receiptType", receiptType);
        publish(RabbitMQConfig.ROUTING_KEY_RECEIPT_UPLOADED, event);
    }

    /**
     * 创建基础事件对象
     */
    private Map<String, Object> createBaseEvent(String eventType) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", eventType);
        event.put("timestamp", LocalDateTime.now());
        return event;
    }

    /**
     * 发布消息到交换机
     */
    private void publish(String routingKey, Map<String, Object> event) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_TMS, routingKey, event);
    }
}
