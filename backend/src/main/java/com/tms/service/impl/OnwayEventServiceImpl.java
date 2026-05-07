package com.tms.service.impl;

import com.tms.dto.OnwayEventDTO;
import com.tms.entity.Dispatch;
import com.tms.entity.OnwayEvent;
import com.tms.entity.Waybill;
import com.tms.exception.BusinessException;
import com.tms.repository.DispatchRepository;
import com.tms.repository.OnwayEventRepository;
import com.tms.repository.WaybillRepository;
import com.tms.service.OnwayEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 在途事件Service实现类
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Service
public class OnwayEventServiceImpl implements OnwayEventService {

    private final OnwayEventRepository onwayEventRepository;
    private final DispatchRepository dispatchRepository;
    private final WaybillRepository waybillRepository;

    @Autowired
    public OnwayEventServiceImpl(OnwayEventRepository onwayEventRepository,
                                 DispatchRepository dispatchRepository,
                                 WaybillRepository waybillRepository) {
        this.onwayEventRepository = onwayEventRepository;
        this.dispatchRepository = dispatchRepository;
        this.waybillRepository = waybillRepository;
    }

    @Override
    @Transactional
    @CachePut(value = "onwayEvent", key = "#result.id")
    public OnwayEvent createEvent(OnwayEventDTO dto) {
        // 验证调度单是否存在
        Dispatch dispatch = dispatchRepository.findById(dto.getDispatchId())
                .orElseThrow(() -> new BusinessException("调度单不存在"));

        // 验证运单是否存在
        Waybill waybill = waybillRepository.findById(dto.getWaybillId())
                .orElseThrow(() -> new BusinessException("运单不存在"));

        OnwayEvent event = new OnwayEvent();
        event.setDispatchId(dto.getDispatchId());
        event.setWaybillId(dto.getWaybillId());
        event.setEventType(dto.getEventType());
        event.setEventLevel(dto.getEventLevel());
        event.setEventContent(dto.getEventContent());
        event.setLocation(dto.getLocation());
        event.setEventTime(dto.getEventTime());
        event.setIsHandled(0); // 未处理

        return onwayEventRepository.save(event);
    }

    @Override
    @Transactional
    @CachePut(value = "onwayEvent", key = "#id")
    public OnwayEvent updateEvent(Long id, OnwayEventDTO dto) {
        OnwayEvent event = onwayEventRepository.findById(id)
                .orElseThrow(() -> new BusinessException("事件不存在"));

        // 只有未处理的事件可以修改
        if (event.getIsHandled() == 1) {
            throw new BusinessException("已处理的事件不能修改");
        }

        event.setEventType(dto.getEventType());
        event.setEventLevel(dto.getEventLevel());
        event.setEventContent(dto.getEventContent());
        event.setLocation(dto.getLocation());
        event.setEventTime(dto.getEventTime());

        return onwayEventRepository.save(event);
    }

    @Override
    @Transactional
    @CacheEvict(value = "onwayEvent", key = "#id")
    public void deleteEvent(Long id) {
        OnwayEvent event = onwayEventRepository.findById(id)
                .orElseThrow(() -> new BusinessException("事件不存在"));

        // 只有未处理的事件可以删除
        if (event.getIsHandled() == 1) {
            throw new BusinessException("已处理的事件不能删除");
        }

        onwayEventRepository.deleteById(id);
    }

    @Override
    @Cacheable(value = "onwayEvent", key = "#id")
    public Optional Optional<OnwayEvent> findById(Long id) {
        return onwayEventRepository.findById(id);
    }

    @Override
    public List List<OnwayEvent> findByDispatchId(Long dispatchId) {
        return onwayEventRepository.findByDispatchIdOrderByEventTimeDesc(dispatchId);
    }

    @Override
    public List List<OnwayEvent> findByDispatchIdAndIsHandled(Long dispatchId, Integer isHandled) {
        return onwayEventRepository.findByDispatchIdAndIsHandled(dispatchId, isHandled);
    }

    @Override
    public Page Page<OnwayEvent> findEvents(Long dispatchId, Integer eventType, Integer eventLevel, Integer isHandled, Pageable pageable) {
        Specification Specification<OnwayEvent> spec = (root, query, cb) -> {
            List List<Predicate> predicates = new ArrayList<>();

            if (dispatchId != null) {
                predicates.add(cb.equal(root.get("dispatchId"), dispatchId));
            }

            if (eventType != null) {
                predicates.add(cb.equal(root.get("eventType"), eventType));
            }

            if (eventLevel != null) {
                predicates.add(cb.equal(root.get("eventLevel"), eventLevel));
            }

            if (isHandled != null) {
                predicates.add(cb.equal(root.get("isHandled"), isHandled));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return onwayEventRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional
    @CachePut(value = "onwayEvent", key = "#id")
    public OnwayEvent handleEvent(Long id, String handleResult, String handleBy) {
        OnwayEvent event = onwayEventRepository.findById(id)
                .orElseThrow(() -> new BusinessException("事件不存在"));

        if (event.getIsHandled() == 1) {
            throw new BusinessException("事件已处理");
        }

        event.setIsHandled(1); // 已处理
        event.setHandleResult(handleResult);
        event.setHandleBy(handleBy);
        event.setHandleTime(LocalDateTime.now());

        return onwayEventRepository.save(event);
    }

    @Override
    public long countUnhandledEvents() {
        return onwayEventRepository.findByIsHandledOrderByEventTimeDesc(0, Pageable.unpaged()).getTotalElements();
    }
}
