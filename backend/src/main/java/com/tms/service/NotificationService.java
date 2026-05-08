package com.tms.service;

import java.util.List;
import java.util.Map;

/**
 * 消息通知服务接口
 * 提供短信、邮件、站内消息等多种通知方式
 *
 * @author TMS Team
 * @version 1.0.0
 */
public interface NotificationService {

    /**
     * 发送短信通知
     *
     * @param phoneNumber 手机号
     * @param templateCode 短信模板编码
     * @param params 模板参数
     * @return 是否发送成功
     */
    boolean sendSms(String phoneNumber, String templateCode, Map<String, String> params);

    /**
     * 批量发送短信
     *
     * @param phoneNumbers 手机号列表
     * @param templateCode 短信模板编码
     * @param params 模板参数
     * @return 发送结果
     */
    Map<String, Boolean> sendSmsBatch(List<String> phoneNumbers, String templateCode, Map<String, String> params);

    /**
     * 发送邮件通知
     *
     * @param to 收件人邮箱
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param isHtml 是否为HTML格式
     * @return 是否发送成功
     */
    boolean sendEmail(String to, String subject, String content, boolean isHtml);

    /**
     * 发送邮件（带附件）
     *
     * @param to 收件人邮箱
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param isHtml 是否为HTML格式
     * @param attachments 附件列表（文件名 -> 文件路径）
     * @return 是否发送成功
     */
    boolean sendEmailWithAttachment(String to, String subject, String content, 
                                    boolean isHtml, Map<String, String> attachments);

    /**
     * 发送站内消息
     *
     * @param userId 用户ID
     * @param title 消息标题
     * @param content 消息内容
     * @param type 消息类型
     * @return 是否发送成功
     */
    boolean sendNotification(Long userId, String title, String content, NotificationType type);

    /**
     * 批量发送站内消息
     *
     * @param userIds 用户ID列表
     * @param title 消息标题
     * @param content 消息内容
     * @param type 消息类型
     * @return 发送结果
     */
    Map<Long, Boolean> sendNotificationBatch(List<Long> userIds, String title, 
                                             String content, NotificationType type);

    /**
     * 发送运单状态变更通知
     *
     * @param waybillId 运单ID
     * @param waybillNo 运单号
     * @param oldStatus 原状态
     * @param newStatus 新状态
     * @param phoneNumber 接收通知的手机号
     */
    void sendWaybillStatusNotification(Long waybillId, String waybillNo, 
                                       Integer oldStatus, Integer newStatus, String phoneNumber);

    /**
     * 发送调度发车通知
     *
     * @param dispatchNo 调度单号
     * @param driverName 司机姓名
     * @param driverPhone 司机电话
     * @param vehicleNo 车牌号
     * @param pickupAddress 提货地址
     * @param deliveryAddress 送货地址
     */
    void sendDispatchStartNotification(String dispatchNo, String driverName, 
                                       String driverPhone, String vehicleNo,
                                       String pickupAddress, String deliveryAddress);

    /**
     * 发送异常告警通知
     *
     * @param eventType 事件类型
     * @param eventLevel 事件级别
     * @param eventContent 事件内容
     * @param location 发生位置
     * @param phoneNumbers 接收通知的手机号列表
     */
    void sendAlertNotification(Integer eventType, Integer eventLevel, String eventContent,
                               String location, List<String> phoneNumbers);

    /**
     * 发送结算单生成通知
     *
     * @param settlementNo 结算单号
     * @param partyName 结算方名称
     * @param amount 结算金额
     * @param email 接收通知的邮箱
     */
    void sendSettlementNotification(String settlementNo, String partyName, 
                                    java.math.BigDecimal amount, String email);

    /**
     * 消息类型枚举
     */
    enum NotificationType {
        SYSTEM,      // 系统消息
        BUSINESS,    // 业务消息
        ALERT,       // 告警消息
        REMINDER     // 提醒消息
    }
}
