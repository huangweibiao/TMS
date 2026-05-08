package com.tms.service.impl;

import com.tms.dto.TempHumidityDTO;
import com.tms.entity.Dispatch;
import com.tms.entity.TempHumidity;
import com.tms.entity.Waybill;
import com.tms.exception.BusinessException;
import com.tms.repository.DispatchRepository;
import com.tms.repository.TempHumidityRepository;
import com.tms.repository.WaybillRepository;
import com.tms.service.TempHumidityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 温湿度记录Service实现类
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Service
public class TempHumidityServiceImpl implements TempHumidityService {

    private final TempHumidityRepository tempHumidityRepository;
    private final DispatchRepository dispatchRepository;
    private final WaybillRepository waybillRepository;

    @Autowired
    public TempHumidityServiceImpl(TempHumidityRepository tempHumidityRepository,
                                   DispatchRepository dispatchRepository,
                                   WaybillRepository waybillRepository) {
        this.tempHumidityRepository = tempHumidityRepository;
        this.dispatchRepository = dispatchRepository;
        this.waybillRepository = waybillRepository;
    }

    @Override
    @Transactional
    @CacheEvict(value = "tempHumidity", allEntries = true)
    public TempHumidity createRecord(TempHumidityDTO dto) {
        // 验证调度单是否存在
        Dispatch dispatch = dispatchRepository.findById(dto.getDispatchId())
                .orElseThrow(() -> new BusinessException("调度单不存在"));

        // 验证运单是否需要温控
        Waybill waybill = waybillRepository.findById(dispatch.getWaybillId())
                .orElse(null);
        if (waybill != null && waybill.getNeedTemperature() != null && waybill.getNeedTemperature() == 1) {
            // 检查温度是否在范围内
            if (waybill.getMinTemp() != null && waybill.getMaxTemp() != null) {
                checkTemperatureAlert(dto.getDispatchId(), dto.getTemperature(), waybill.getMinTemp(), waybill.getMaxTemp());
            }
        }

        TempHumidity record = new TempHumidity();
        record.setDispatchId(dto.getDispatchId());
        record.setDeviceId(dto.getDeviceId());
        record.setTemperature(dto.getTemperature());
        record.setHumidity(dto.getHumidity());
        record.setRecordTime(dto.getRecordTime());

        return tempHumidityRepository.save(record);
    }

    @Override
    @Transactional
    @CacheEvict(value = "tempHumidity", allEntries = true)
    public List<TempHumidity> batchCreateRecords(List<TempHumidityDTO> dtoList) {
        List<TempHumidity> records = dtoList.stream().map(dto -> {
            TempHumidity record = new TempHumidity();
            record.setDispatchId(dto.getDispatchId());
            record.setDeviceId(dto.getDeviceId());
            record.setTemperature(dto.getTemperature());
            record.setHumidity(dto.getHumidity());
            record.setRecordTime(dto.getRecordTime());
            return record;
        }).collect(Collectors.toList());

        return tempHumidityRepository.saveAll(records);
    }

    @Override
    @Cacheable(value = "tempHumidity", key = "#id")
    public Optional<TempHumidity> findById(Long id) {
        return tempHumidityRepository.findById(id);
    }

    @Override
    @Cacheable(value = "tempHumidity", key = "'dispatch:' + #dispatchId")
    public List<TempHumidity> findByDispatchId(Long dispatchId) {
        return tempHumidityRepository.findByDispatchIdOrderByRecordTimeAsc(dispatchId);
    }

    @Override
    public List<TempHumidity> findByDispatchIdAndTimeRange(Long dispatchId, LocalDateTime startTime, LocalDateTime endTime) {
        return tempHumidityRepository.findByDispatchIdAndRecordTimeBetween(dispatchId, startTime, endTime);
    }

    @Override
    public Page<TempHumidity> findRecords(Long dispatchId, String deviceId, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable) {
        Specification<TempHumidity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (dispatchId != null) {
                predicates.add(cb.equal(root.get("dispatchId"), dispatchId));
            }

            if (StringUtils.hasText(deviceId)) {
                predicates.add(cb.equal(root.get("deviceId"), deviceId));
            }

            if (startTime != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("recordTime"), startTime));
            }

            if (endTime != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("recordTime"), endTime));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return tempHumidityRepository.findAll(spec, pageable);
    }

    @Override
    public Optional<TempHumidity> getLatestRecord(Long dispatchId) {
        List<TempHumidity> records = tempHumidityRepository.findByDispatchIdOrderByRecordTimeAsc(dispatchId);
        return records.isEmpty() ? Optional.empty() : Optional.of(records.get(records.size() - 1));
    }

    @Override
    public boolean isTemperatureAbnormal(Long dispatchId, BigDecimal temperature, BigDecimal minTemp, BigDecimal maxTemp) {
        if (temperature == null || minTemp == null || maxTemp == null) {
            return false;
        }
        return temperature.compareTo(minTemp) < 0 || temperature.compareTo(maxTemp) > 0;
    }

    @Override
    @Transactional
    @CacheEvict(value = "tempHumidity", allEntries = true)
    public long deleteRecordsBefore(LocalDateTime beforeTime) {
        // 查询需要删除的记录
        List<TempHumidity> recordsToDelete = tempHumidityRepository.findAll().stream()
                .filter(r -> r.getRecordTime() != null && r.getRecordTime().isBefore(beforeTime))
                .collect(Collectors.toList());

        long count = recordsToDelete.size();
        tempHumidityRepository.deleteAll(recordsToDelete);
        return count;
    }

    /**
     * 检查温度告警
     *
     * @param dispatchId 调度单ID
     * @param temperature 当前温度
     * @param minTemp 最低温度
     * @param maxTemp 最高温度
     */
    private void checkTemperatureAlert(Long dispatchId, BigDecimal temperature, BigDecimal minTemp, BigDecimal maxTemp) {
        if (isTemperatureAbnormal(dispatchId, temperature, minTemp, maxTemp)) {
            // 温度异常，可以在这里触发告警事件
            // TODO: 发送温度异常告警消息到消息队列
        }
    }
}
