package com.tms.service.impl;

import com.tms.common.PageResult;
import com.tms.dto.DispatchDTO;
import com.tms.entity.Dispatch;
import com.tms.entity.Driver;
import com.tms.entity.Vehicle;
import com.tms.entity.Waybill;
import com.tms.exception.BusinessException;
import com.tms.repository.*;
import com.tms.service.DispatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 调度服务实现类
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Service
public class DispatchServiceImpl implements DispatchService {

    private final DispatchRepository dispatchRepository;
    private final WaybillRepository waybillRepository;
    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;
    private final CarrierRepository carrierRepository;

    @Autowired
    public DispatchServiceImpl(DispatchRepository dispatchRepository,
                               WaybillRepository waybillRepository,
                               VehicleRepository vehicleRepository,
                               DriverRepository driverRepository,
                               CarrierRepository carrierRepository) {
        this.dispatchRepository = dispatchRepository;
        this.waybillRepository = waybillRepository;
        this.vehicleRepository = vehicleRepository;
        this.driverRepository = driverRepository;
        this.carrierRepository = carrierRepository;
    }

    @Override
    @Transactional
    public DispatchDTO assignDispatch(Long waybillId, Long vehicleId, Long driverId, Integer strategy) {
        // 验证运单
        Waybill waybill = waybillRepository.findById(waybillId)
                .orElseThrow(() -> new BusinessException("运单不存在"));

        if (waybill.getWaybillStatus() != 1) {
            throw new BusinessException("只有待调度状态的运单可以进行派车");
        }

        // 验证车辆
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new BusinessException("车辆不存在"));

        if (vehicle.getStatus() != 1) {
            throw new BusinessException("车辆不可用");
        }

        // 验证司机
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new BusinessException("司机不存在"));

        if (driver.getStatus() != 1) {
            throw new BusinessException("司机不可用");
        }

        // 生成调度单号
        String dispatchNo = generateDispatchNo();

        // 创建调度单
        Dispatch dispatch = new Dispatch();
        dispatch.setDispatchNo(dispatchNo);
        dispatch.setWaybillId(waybillId);
        dispatch.setVehicleId(vehicleId);
        dispatch.setDriverId(driverId);
        dispatch.setCarrierId(vehicle.getCarrierId());
        dispatch.setDispatchStatus(1); // 待发车

        // 根据策略计算计划时间（简化实现）
        calculatePlanTime(dispatch, strategy);

        Dispatch savedDispatch = dispatchRepository.save(dispatch);

        // 更新运单状态为已调度
        waybill.setWaybillStatus(2);
        waybillRepository.save(waybill);

        // 更新车辆状态为在途
        vehicle.setStatus(2);
        vehicleRepository.save(vehicle);

        // 更新司机状态为在途
        driver.setStatus(2);
        driverRepository.save(driver);

        return convertToDTO(savedDispatch);
    }

    @Override
    @Transactional
    public DispatchDTO createDispatch(DispatchDTO dispatchDTO) {
        // 验证运单
        Waybill waybill = waybillRepository.findById(dispatchDTO.getWaybillId())
                .orElseThrow(() -> new BusinessException("运单不存在"));

        if (waybill.getWaybillStatus() != 1) {
            throw new BusinessException("只有待调度状态的运单可以创建调度单");
        }

        // 验证车辆
        Vehicle vehicle = vehicleRepository.findById(dispatchDTO.getVehicleId())
                .orElseThrow(() -> new BusinessException("车辆不存在"));

        if (vehicle.getStatus() != 1) {
            throw new BusinessException("车辆不可用");
        }

        // 验证司机
        Driver driver = driverRepository.findById(dispatchDTO.getDriverId())
                .orElseThrow(() -> new BusinessException("司机不存在"));

        if (driver.getStatus() != 1) {
            throw new BusinessException("司机不可用");
        }

        // 生成调度单号
        String dispatchNo = generateDispatchNo();
        dispatchDTO.setDispatchNo(dispatchNo);

        // 保存调度单
        Dispatch dispatch = convertToEntity(dispatchDTO);
        dispatch.setDispatchStatus(1); // 待发车
        Dispatch savedDispatch = dispatchRepository.save(dispatch);

        // 更新运单状态为已调度
        waybill.setWaybillStatus(2);
        waybillRepository.save(waybill);

        // 更新车辆状态为在途
        vehicle.setStatus(2);
        vehicleRepository.save(vehicle);

        // 更新司机状态为在途
        driver.setStatus(2);
        driverRepository.save(driver);

        return convertToDTO(savedDispatch);
    }

    @Override
    public DispatchDTO getDispatchById(Long id) {
        Dispatch dispatch = dispatchRepository.findById(id)
                .orElseThrow(() -> new BusinessException("调度单不存在"));
        return convertToDTO(dispatch);
    }

    @Override
    public DispatchDTO getDispatchByWaybillId(Long waybillId) {
        Dispatch dispatch = dispatchRepository.findByWaybillId(waybillId)
                .orElseThrow(() -> new BusinessException("调度单不存在"));
        return convertToDTO(dispatch);
    }

    @Override
    public PageResultResult<DispatchDTO> getDispatchList(String dispatchNo, Integer status, int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);

        Page Page<Dispatch> page;
        if (dispatchNo != null && !dispatchNo.isEmpty()) {
            page = dispatchRepository.findByDispatchNoContainingAndDispatchStatus(dispatchNo, status != null ? status : 1, pageable);
        } else if (status != null) {
            page = dispatchRepository.findByDispatchStatus(status, pageable);
        } else {
            page = dispatchRepository.findAll(pageable);
        }

        List List<DispatchDTO> list = page.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new PageResult<>(pageNum, pageSize, page.getTotalElements(), list);
    }

    @Override
    @Transactional
    public void startDispatch(Long id) {
        Dispatch dispatch = dispatchRepository.findById(id)
                .orElseThrow(() -> new BusinessException("调度单不存在"));

        if (dispatch.getDispatchStatus() != 1) {
            throw new BusinessException("只有待发车状态的调度单可以确认发车");
        }

        dispatch.setDispatchStatus(2); // 已发车
        dispatch.setActualStartTime(LocalDateTime.now());
        dispatchRepository.save(dispatch);

        // 更新运单状态为运输中
        Waybill waybill = waybillRepository.findById(dispatch.getWaybillId())
                .orElseThrow(() -> new BusinessException("运单不存在"));
        waybill.setWaybillStatus(4); // 运输中
        waybillRepository.save(waybill);
    }

    @Override
    @Transactional
    public void completeDispatch(Long id) {
        Dispatch dispatch = dispatchRepository.findById(id)
                .orElseThrow(() -> new BusinessException("调度单不存在"));

        if (dispatch.getDispatchStatus() != 2) {
            throw new BusinessException("只有已发车状态的调度单可以完成");
        }

        dispatch.setDispatchStatus(3); // 已完成
        dispatch.setActualEndTime(LocalDateTime.now());
        dispatchRepository.save(dispatch);

        // 更新运单状态为已签收
        Waybill waybill = waybillRepository.findById(dispatch.getWaybillId())
                .orElseThrow(() -> new BusinessException("运单不存在"));
        waybill.setWaybillStatus(5); // 签收
        waybill.setActualDeliveryTime(LocalDateTime.now());
        waybillRepository.save(waybill);

        // 更新车辆状态为可用
        Vehicle vehicle = vehicleRepository.findById(dispatch.getVehicleId())
                .orElseThrow(() -> new BusinessException("车辆不存在"));
        vehicle.setStatus(1);
        vehicleRepository.save(vehicle);

        // 更新司机状态为空闲
        Driver driver = driverRepository.findById(dispatch.getDriverId())
                .orElseThrow(() -> new BusinessException("司机不存在"));
        driver.setStatus(1);
        driverRepository.save(driver);
    }

    @Override
    @Transactional
    public void cancelDispatch(Long id, String reason) {
        Dispatch dispatch = dispatchRepository.findById(id)
                .orElseThrow(() -> new BusinessException("调度单不存在"));

        if (dispatch.getDispatchStatus() != 1) {
            throw new BusinessException("只有待发车状态的调度单可以取消");
        }

        dispatch.setDispatchStatus(4); // 已取消
        dispatchRepository.save(dispatch);

        // 更新运单状态为待调度
        Waybill waybill = waybillRepository.findById(dispatch.getWaybillId())
                .orElseThrow(() -> new BusinessException("运单不存在"));
        waybill.setWaybillStatus(1); // 待调度
        waybillRepository.save(waybill);

        // 更新车辆状态为可用
        Vehicle vehicle = vehicleRepository.findById(dispatch.getVehicleId())
                .orElseThrow(() -> new BusinessException("车辆不存在"));
        vehicle.setStatus(1);
        vehicleRepository.save(vehicle);

        // 更新司机状态为空闲
        Driver driver = driverRepository.findById(dispatch.getDriverId())
                .orElseThrow(() -> new BusinessException("司机不存在"));
        driver.setStatus(1);
        driverRepository.save(driver);
    }

    /**
     * 生成调度单号
     * 格式：DP + 年月日 + 4位序号
     *
     * @return 调度单号
     */
    private String generateDispatchNo() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String prefix = "DP" + dateStr;

        // 查询当天最大调度单号
        String maxDispatchNo = dispatchRepository.findMaxDispatchNoByPrefix(prefix);
        int seq = 1;
        if (maxDispatchNo != null && maxDispatchNo.length() >= 4) {
            try {
                seq = Integer.parseInt(maxDispatchNo.substring(maxDispatchNo.length() - 4)) + 1;
            } catch (NumberFormatException e) {
                seq = 1;
            }
        }

        return String.format("%s%04d", prefix, seq);
    }

    /**
     * 根据策略计算计划时间
     *
     * @param dispatch 调度单
     * @param strategy 策略：1-距离优先, 2-成本优先, 3-时效优先
     */
    private void calculatePlanTime(Dispatch dispatch, Integer strategy) {
        // 简化实现，实际应根据距离、路况等计算
        LocalDateTime now = LocalDateTime.now();
        dispatch.setPlannedStartTime(now.plusHours(1));
        dispatch.setPlannedEndTime(now.plusHours(24));
    }

    /**
     * 将DTO转换为实体
     *
     * @param dto 调度单DTO
     * @return 调度单实体
     */
    private Dispatch convertToEntity(DispatchDTO dto) {
        Dispatch entity = new Dispatch();
        entity.setId(dto.getId());
        entity.setDispatchNo(dto.getDispatchNo());
        entity.setWaybillId(dto.getWaybillId());
        entity.setVehicleId(dto.getVehicleId());
        entity.setDriverId(dto.getDriverId());
        entity.setCarrierId(dto.getCarrierId());
        entity.setRoutePlan(dto.getRoutePlan());
        entity.setPlannedDistance(dto.getPlannedDistance());
        entity.setActualDistance(dto.getActualDistance());
        entity.setPlannedStartTime(dto.getPlannedStartTime());
        entity.setPlannedEndTime(dto.getPlannedEndTime());
        entity.setActualStartTime(dto.getActualStartTime());
        entity.setActualEndTime(dto.getActualEndTime());
        entity.setDispatchStatus(dto.getDispatchStatus());
        entity.setCreateBy(dto.getCreateBy());
        return entity;
    }

    /**
     * 将实体转换为DTO
     *
     * @param entity 调度单实体
     * @return 调度单DTO
     */
    private DispatchDTO convertToDTO(Dispatch entity) {
        DispatchDTO dto = new DispatchDTO();
        dto.setId(entity.getId());
        dto.setDispatchNo(entity.getDispatchNo());
        dto.setWaybillId(entity.getWaybillId());
        dto.setVehicleId(entity.getVehicleId());
        dto.setDriverId(entity.getDriverId());
        dto.setCarrierId(entity.getCarrierId());
        dto.setRoutePlan(entity.getRoutePlan());
        dto.setPlannedDistance(entity.getPlannedDistance());
        dto.setActualDistance(entity.getActualDistance());
        dto.setPlannedStartTime(entity.getPlannedStartTime());
        dto.setPlannedEndTime(entity.getPlannedEndTime());
        dto.setActualStartTime(entity.getActualStartTime());
        dto.setActualEndTime(entity.getActualEndTime());
        dto.setDispatchStatus(entity.getDispatchStatus());
        dto.setCreateBy(entity.getCreateBy());

        // 查询关联信息
        waybillRepository.findById(entity.getWaybillId())
                .ifPresent(waybill -> dto.setWaybillNo(waybill.getWaybillNo()));

        vehicleRepository.findById(entity.getVehicleId())
                .ifPresent(vehicle -> dto.setPlateNumber(vehicle.getPlateNumber()));

        driverRepository.findById(entity.getDriverId())
                .ifPresent(driver -> dto.setDriverName(driver.getDriverName()));

        return dto;
    }
}
