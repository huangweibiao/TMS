package com.tms.service;

import com.tms.dto.TrackPointDTO;
import com.tms.entity.Dispatch;
import com.tms.entity.TrackPoint;
import com.tms.entity.Vehicle;
import com.tms.exception.BusinessException;
import com.tms.repository.DispatchRepository;
import com.tms.repository.TrackPointRepository;
import com.tms.repository.VehicleRepository;
import com.tms.service.impl.TrackPointServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrackPointServiceTest {

    @Mock
    private TrackPointRepository trackPointRepository;

    @Mock
    private DispatchRepository dispatchRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private TrackPointServiceImpl trackPointService;

    private TrackPoint trackPoint;
    private TrackPointDTO trackPointDTO;
    private Dispatch dispatch;
    private Vehicle vehicle;

    @BeforeEach
    void setUp() {
        dispatch = new Dispatch();
        dispatch.setId(1L);
        dispatch.setDispatchNo("DP202401010001");

        vehicle = new Vehicle();
        vehicle.setId(1L);
        vehicle.setPlateNumber("京A12345");

        trackPoint = new TrackPoint();
        trackPoint.setId(1L);
        trackPoint.setDispatchId(1L);
        trackPoint.setVehicleId(1L);
        trackPoint.setLongitude(new BigDecimal("116.4074"));
        trackPoint.setLatitude(new BigDecimal("39.9042"));
        trackPoint.setSpeed(new BigDecimal("60.00"));
        trackPoint.setDirection(90);
        trackPoint.setLocationTime(LocalDateTime.now());

        trackPointDTO = new TrackPointDTO();
        trackPointDTO.setId(1L);
        trackPointDTO.setDispatchId(1L);
        trackPointDTO.setVehicleId(1L);
        trackPointDTO.setLongitude(new BigDecimal("116.4074"));
        trackPointDTO.setLatitude(new BigDecimal("39.9042"));
        trackPointDTO.setSpeed(new BigDecimal("60.00"));
        trackPointDTO.setDirection(90);
        trackPointDTO.setLocationTime(LocalDateTime.now());
    }

    @Test
    void testFindById_Success() {
        when(trackPointRepository.findById(1L)).thenReturn(Optional.of(trackPoint));

        Optional<TrackPoint> result = trackPointService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(trackPoint.getId(), result.get().getId());
        verify(trackPointRepository).findById(1L);
    }

    @Test
    void testFindByDispatchId() {
        when(trackPointRepository.findByDispatchIdOrderByLocationTimeAsc(1L)).thenReturn(Arrays.asList(trackPoint));

        List<TrackPoint> result = trackPointService.findByDispatchId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testFindByDispatchIdAndTimeRange() {
        LocalDateTime startTime = LocalDateTime.now().minusHours(1);
        LocalDateTime endTime = LocalDateTime.now();
        
        when(trackPointRepository.findByDispatchIdAndLocationTimeBetween(1L, startTime, endTime))
                .thenReturn(Arrays.asList(trackPoint));

        List<TrackPoint> result = trackPointService.findByDispatchIdAndTimeRange(1L, startTime, endTime);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testFindLatestByVehicleId() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrackPoint> page = new PageImpl<>(Arrays.asList(trackPoint));
        when(trackPointRepository.findByVehicleIdOrderByLocationTimeDesc(eq(1L), any(Pageable.class))).thenReturn(page);

        List<TrackPoint> result = trackPointService.findLatestByVehicleId(1L, 10);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testCreateTrackPoint_Success() {
        when(dispatchRepository.findById(1L)).thenReturn(Optional.of(dispatch));
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        when(trackPointRepository.save(any(TrackPoint.class))).thenReturn(trackPoint);

        TrackPoint result = trackPointService.createTrackPoint(trackPointDTO);

        assertNotNull(result);
        assertEquals(trackPoint.getLongitude(), result.getLongitude());
        assertEquals(trackPoint.getLatitude(), result.getLatitude());
        verify(trackPointRepository).save(any(TrackPoint.class));
    }

    @Test
    void testCreateTrackPoint_DispatchNotFound() {
        when(dispatchRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            trackPointService.createTrackPoint(trackPointDTO);
        });

        assertEquals("调度单不存在", exception.getMessage());
    }

    @Test
    void testCreateTrackPoint_VehicleNotFound() {
        when(dispatchRepository.findById(1L)).thenReturn(Optional.of(dispatch));
        when(vehicleRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            trackPointService.createTrackPoint(trackPointDTO);
        });

        assertEquals("车辆不存在", exception.getMessage());
    }

    @Test
    void testBatchCreateTrackPoints_Success() {
        TrackPointDTO dto2 = new TrackPointDTO();
        dto2.setDispatchId(1L);
        dto2.setVehicleId(1L);
        dto2.setLongitude(new BigDecimal("116.4075"));
        dto2.setLatitude(new BigDecimal("39.9043"));
        dto2.setLocationTime(LocalDateTime.now());

        List<TrackPointDTO> dtoList = Arrays.asList(trackPointDTO, dto2);
        
        when(trackPointRepository.saveAll(any())).thenReturn(Arrays.asList(trackPoint, trackPoint));

        List<TrackPoint> result = trackPointService.batchCreateTrackPoints(dtoList);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(trackPointRepository).saveAll(any());
    }

    @Test
    void testFindTrackPoints() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrackPoint> page = new PageImpl<>(Arrays.asList(trackPoint), pageable, 1);
        when(trackPointRepository.findAll(any(org.springframework.data.jpa.domain.Specification.class), eq(pageable))).thenReturn(page);

        Page<TrackPoint> result = trackPointService.findTrackPoints(1L, 1L, null, null, pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void testFindTrackPoints_WithTimeRange() {
        LocalDateTime startTime = LocalDateTime.now().minusHours(1);
        LocalDateTime endTime = LocalDateTime.now();
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrackPoint> page = new PageImpl<>(Arrays.asList(trackPoint), pageable, 1);
        when(trackPointRepository.findAll(any(org.springframework.data.jpa.domain.Specification.class), eq(pageable))).thenReturn(page);

        Page<TrackPoint> result = trackPointService.findTrackPoints(1L, 1L, startTime, endTime, pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void testDeleteTrackPointsBefore() {
        LocalDateTime beforeTime = LocalDateTime.now().minusDays(30);
        when(trackPointRepository.findAll()).thenReturn(Arrays.asList(trackPoint));

        long result = trackPointService.deleteTrackPointsBefore(beforeTime);

        assertEquals(0, result);
        verify(trackPointRepository).deleteAll(any());
    }
}
