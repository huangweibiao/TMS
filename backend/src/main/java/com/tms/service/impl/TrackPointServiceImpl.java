package com.tms.service.impl;

import com.tms.dto.TrackPointDTO;
import com.tms.entity.Dispatch;
import com.tms.entity.TrackPoint;
import com.tms.entity.Vehicle;
import com.tms.exception.BusinessException;
import com.tms.repository.DispatchRepository;
import com.tms.repository.TrackPointRepository;
import com.tms.repository.VehicleRepository;
import com.tms.service.TrackPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 轨迹点Service实现类
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Service
public class TrackPointServiceImpl implements TrackPointService {

    private final TrackPointRepository trackPointRepository;
    private final DispatchRepository dispatchRepository;
    private final VehicleRepository vehicleRepository;

    @Autowired
    public TrackPointServiceImpl(TrackPointRepository trackPointRepository,
                                 DispatchRepository dispatchRepository,
                                 VehicleRepository vehicleRepository) {
        this.trackPointRepository = trackPointRepository;
        this.dispatchRepository = dispatchRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    @Transactional
    @CacheEvict(value = "trackPoint", allEntries = true)
    public TrackPoint createTrackPoint(TrackPointDTO dto) {
        // 验证调度单是否存在
        Dispatch dispatch = dispatchRepository.findById(dto.getDispatchId())
                .orElseThrow(() -> new BusinessException("调度单不存在"));

        // 验证车辆是否存在
        Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId())
                .orElseThrow(() -> new BusinessException("车辆不存在"));

        TrackPoint trackPoint = new TrackPoint();
        trackPoint.setDispatchId(dto.getDispatchId());
        trackPoint.setVehicleId(dto.getVehicleId());
        trackPoint.setLongitude(dto.getLongitude());
        trackPoint.setLatitude(dto.getLatitude());
        trackPoint.setSpeed(dto.getSpeed());
        trackPoint.setDirection(dto.getDirection());
        trackPoint.setLocationTime(dto.getLocationTime());

        return trackPointRepository.save(trackPoint);
    }

    @Override
    @Transactional
    @CacheEvict(value = "trackPoint", allEntries = true)
    public List<TrackPoint> batchCreateTrackPoints(List<TrackPointDTO> dtoList) {
        List<TrackPoint> trackPoints = dtoList.stream().map(dto -> {
            TrackPoint trackPoint = new TrackPoint();
            trackPoint.setDispatchId(dto.getDispatchId());
            trackPoint.setVehicleId(dto.getVehicleId());
            trackPoint.setLongitude(dto.getLongitude());
            trackPoint.setLatitude(dto.getLatitude());
            trackPoint.setSpeed(dto.getSpeed());
            trackPoint.setDirection(dto.getDirection());
            trackPoint.setLocationTime(dto.getLocationTime());
            return trackPoint;
        }).collect(Collectors.toList());

        return trackPointRepository.saveAll(trackPoints);
    }

    @Override
    @Cacheable(value = "trackPoint", key = "#id")
    public Optional<TrackPoint> findById(Long id) {
        return trackPointRepository.findById(id);
    }

    @Override
    @Cacheable(value = "trackPoint", key = "'dispatch:' + #dispatchId")
    public List<TrackPoint> findByDispatchId(Long dispatchId) {
        return trackPointRepository.findByDispatchIdOrderByLocationTimeAsc(dispatchId);
    }

    @Override
    public List<TrackPoint> findByDispatchIdAndTimeRange(Long dispatchId, LocalDateTime startTime, LocalDateTime endTime) {
        return trackPointRepository.findByDispatchIdAndLocationTimeBetween(dispatchId, startTime, endTime);
    }

    @Override
    public List<TrackPoint> findLatestByVehicleId(Long vehicleId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return trackPointRepository.findByVehicleIdOrderByLocationTimeDesc(vehicleId, pageable).getContent();
    }

    @Override
    public Page<TrackPoint> findTrackPoints(Long dispatchId, Long vehicleId, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable) {
        Specification<TrackPoint> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (dispatchId != null) {
                predicates.add(cb.equal(root.get("dispatchId"), dispatchId));
            }

            if (vehicleId != null) {
                predicates.add(cb.equal(root.get("vehicleId"), vehicleId));
            }

            if (startTime != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("locationTime"), startTime));
            }

            if (endTime != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("locationTime"), endTime));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return trackPointRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional
    @CacheEvict(value = "trackPoint", allEntries = true)
    public long deleteTrackPointsBefore(LocalDateTime beforeTime) {
        // 这里使用原生SQL或自定义删除逻辑
        // 简化实现，实际可能需要批量删除
        List<TrackPoint> pointsToDelete = trackPointRepository.findAll().stream()
                .filter(tp -> tp.getLocationTime().isBefore(beforeTime))
                .collect(Collectors.toList());
        
        long count = pointsToDelete.size();
        trackPointRepository.deleteAll(pointsToDelete);
        return count;
    }
}
