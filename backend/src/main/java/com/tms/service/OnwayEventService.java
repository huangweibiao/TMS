package com.tms.service;

import com.tms.dto.OnwayEventDTO;
import com.tms.entity.OnwayEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * 在途事件Service接口
 *
 * @author TMS Team
 * @version 1.0.0
 */
public interface OnwayEventService {

    /**
     * 创建在途事件
     *
     * @param dto 在途事件DTO
     * @return 创建后的在途事件
     */
    OnwayEvent createEvent(OnwayEventDTO dto);

    /**
     * 更新在途事件
     *
     * @param id  在途事件ID
     * @param dto 在途事件DTO
     * @return 更新后的在途事件
     */
    OnwayEvent updateEvent(Long id, OnwayEventDTO dto);

    /**
     * 删除在途事件
     *
     * @param id 在途事件ID
     */
    void deleteEvent(Long id);

    /**
     * 根据ID查询在途事件
     *
     * @param id 在途事件ID
     * @return 在途事件对象
     */
    Optional Optional<OnwayEvent> findById(Long id);

    /**
     * 根据调度单ID查询事件列表
     *
     * @param dispatchId 调度单ID
     * @return 事件列表
     */
    List List<OnwayEvent> findByDispatchId(Long dispatchId);

    /**
     * 根据调度单ID和处理状态查询事件
     *
     * @param dispatchId 调度单ID
     * @param isHandled  处理状态
     * @return 事件列表
     */
    List List<OnwayEvent> findByDispatchIdAndIsHandled(Long dispatchId, Integer isHandled);

    /**
     * 分页查询事件列表
     *
     * @param dispatchId 调度单ID
     * @param eventType  事件类型
     * @param eventLevel 事件级别
     * @param isHandled  处理状态
     * @param pageable   分页参数
     * @return 分页结果
     */
    Page Page<OnwayEvent> findEvents(Long dispatchId, Integer eventType, Integer eventLevel, Integer isHandled, Pageable pageable);

    /**
     * 处理事件
     *
     * @param id           事件ID
     * @param handleResult 处理结果
     * @param handleBy     处理人
     * @return 更新后的事件
     */
    OnwayEvent handleEvent(Long id, String handleResult, String handleBy);

    /**
     * 获取未处理事件数量
     *
     * @return 未处理事件数量
     */
    long countUnhandledEvents();
}
