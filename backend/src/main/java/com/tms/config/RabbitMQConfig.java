package com.tms.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置类
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Configuration
public class RabbitMQConfig {

    // 定义队列名称
    public static final String QUEUE_WAYBILL_CREATED = "tms.waybill.created";
    public static final String QUEUE_WAYBILL_STATUS_CHANGED = "tms.waybill.status.changed";
    public static final String QUEUE_DISPATCH_STARTED = "tms.dispatch.started";
    public static final String QUEUE_DISPATCH_COMPLETED = "tms.dispatch.completed";
    public static final String QUEUE_GPS_LOCATION = "tms.gps.location";
    public static final String QUEUE_EVENT_ALERT = "tms.event.alert";
    public static final String QUEUE_SETTLEMENT_CREATED = "tms.settlement.created";
    public static final String QUEUE_RECEIPT_UPLOADED = "tms.receipt.uploaded";

    // 定义交换机名称
    public static final String EXCHANGE_TMS = "tms.exchange";

    // 定义路由键
    public static final String ROUTING_KEY_WAYBILL_CREATED = "waybill.created";
    public static final String ROUTING_KEY_WAYBILL_STATUS_CHANGED = "waybill.status.changed";
    public static final String ROUTING_KEY_DISPATCH_STARTED = "dispatch.started";
    public static final String ROUTING_KEY_DISPATCH_COMPLETED = "dispatch.completed";
    public static final String ROUTING_KEY_GPS_LOCATION = "gps.location";
    public static final String ROUTING_KEY_EVENT_ALERT = "event.alert";
    public static final String ROUTING_KEY_SETTLEMENT_CREATED = "settlement.created";
    public static final String ROUTING_KEY_RECEIPT_UPLOADED = "receipt.uploaded";

    /**
     * 创建主题交换机
     */
    @Bean
    public TopicExchange tmsExchange() {
        return new TopicExchange(EXCHANGE_TMS);
    }

    /**
     * 创建队列：运单创建
     */
    @Bean
    public Queue waybillCreatedQueue() {
        return QueueBuilder.durable(QUEUE_WAYBILL_CREATED).build();
    }

    /**
     * 创建队列：运单状态变更
     */
    @Bean
    public Queue waybillStatusChangedQueue() {
        return QueueBuilder.durable(QUEUE_WAYBILL_STATUS_CHANGED).build();
    }

    /**
     * 创建队列：调度发车
     */
    @Bean
    public Queue dispatchStartedQueue() {
        return QueueBuilder.durable(QUEUE_DISPATCH_STARTED).build();
    }

    /**
     * 创建队列：调度完成
     */
    @Bean
    public Queue dispatchCompletedQueue() {
        return QueueBuilder.durable(QUEUE_DISPATCH_COMPLETED).build();
    }

    /**
     * 创建队列：GPS定位
     */
    @Bean
    public Queue gpsLocationQueue() {
        return QueueBuilder.durable(QUEUE_GPS_LOCATION).build();
    }

    /**
     * 创建队列：异常告警
     */
    @Bean
    public Queue eventAlertQueue() {
        return QueueBuilder.durable(QUEUE_EVENT_ALERT).build();
    }

    /**
     * 创建队列：结算单创建
     */
    @Bean
    public Queue settlementCreatedQueue() {
        return QueueBuilder.durable(QUEUE_SETTLEMENT_CREATED).build();
    }

    /**
     * 创建队列：回单上传
     */
    @Bean
    public Queue receiptUploadedQueue() {
        return QueueBuilder.durable(QUEUE_RECEIPT_UPLOADED).build();
    }

    /**
     * 绑定队列到交换机
     */
    @Bean
    public Binding waybillCreatedBinding() {
        return BindingBuilder.bind(waybillCreatedQueue())
                .to(tmsExchange())
                .with(ROUTING_KEY_WAYBILL_CREATED);
    }

    @Bean
    public Binding waybillStatusChangedBinding() {
        return BindingBuilder.bind(waybillStatusChangedQueue())
                .to(tmsExchange())
                .with(ROUTING_KEY_WAYBILL_STATUS_CHANGED);
    }

    @Bean
    public Binding dispatchStartedBinding() {
        return BindingBuilder.bind(dispatchStartedQueue())
                .to(tmsExchange())
                .with(ROUTING_KEY_DISPATCH_STARTED);
    }

    @Bean
    public Binding dispatchCompletedBinding() {
        return BindingBuilder.bind(dispatchCompletedQueue())
                .to(tmsExchange())
                .with(ROUTING_KEY_DISPATCH_COMPLETED);
    }

    @Bean
    public Binding gpsLocationBinding() {
        return BindingBuilder.bind(gpsLocationQueue())
                .to(tmsExchange())
                .with(ROUTING_KEY_GPS_LOCATION);
    }

    @Bean
    public Binding eventAlertBinding() {
        return BindingBuilder.bind(eventAlertQueue())
                .to(tmsExchange())
                .with(ROUTING_KEY_EVENT_ALERT);
    }

    @Bean
    public Binding settlementCreatedBinding() {
        return BindingBuilder.bind(settlementCreatedQueue())
                .to(tmsExchange())
                .with(ROUTING_KEY_SETTLEMENT_CREATED);
    }

    @Bean
    public Binding receiptUploadedBinding() {
        return BindingBuilder.bind(receiptUploadedQueue())
                .to(tmsExchange())
                .with(ROUTING_KEY_RECEIPT_UPLOADED);
    }

    /**
     * 配置消息转换器
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 配置RabbitTemplate
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
