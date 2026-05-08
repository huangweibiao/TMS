package com.tms.repository;

import com.tms.entity.OnwayEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 在途事件Repository接口
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Repository
public interface OnwayEventRepository extends JpaRepository<OnwayEvent, Long>, JpaSpecificationExecutor<OnwayEvent> {

    /**
     * 根据调度单ID查询事件列表
     *
     * @param dispatchId 调度单ID
     * @return 事件列表
     */
    List<OnwayEvent> findByDispatchIdOrderByEventTimeDesc(Long dispatchId);

    /**
     * 根据调度单ID和处理状态查询事件
     *
     * @param dispatchId 调度单ID
     * @param isHandled  处理状态
     * @return 事件列表
     */
    List<OnwayEvent> findByDispatchIdAndIsHandled(Long dispatchId, Integer isHandled);

    /**
     * 分页查询未处理的事件
     *
     * @param isHandled 处理状态
     * @param pageable  分页参数
     * @return 事件分页列表
     */
    Page<OnwayEvent> findByIsHandledOrderByEventTimeDesc(Integer isHandled, Pageable pageable);
}
