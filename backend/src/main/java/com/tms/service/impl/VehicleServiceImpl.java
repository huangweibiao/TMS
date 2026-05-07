package com.tms.service.impl;

import com.tms.dto.VehicleDTO;
import com.tms.common.PageResult;
import com.tms.entity.Carrier;
import com.tms.entity.Vehicle;
import com.tms.exception.BusinessException;
import com.tms.repository.CarrierRepository;
import com.tms.repository.VehicleRepository;
import com.tms.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 车辆服务实现类
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Service
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final CarrierRepository carrierRepository;

    @Autowired
    public VehicleServiceImpl(VehicleRepository vehicleRepository, CarrierRepository carrierRepository) {
        this.vehicleRepository = vehicleRepository;
        this.carrierRepository = carrierRepository;
    }

    @Override
    @Transactional
    public VehicleDTO createVehicle(VehicleDTO vehicleDTO) {
        // 检查车牌号是否已存在
        if (vehicleRepository.existsByPlateNumber(vehicleDTO.getPlateNumber())) {
            throw new BusinessException("车牌号已存在");
        }

        // 如果所属为外协，检查承运商是否存在
        if (vehicleDTO.getOwnerType() != null && vehicleDTO.getOwnerType() == 2) {
            if (vehicleDTO.getCarrierId() == null) {
                throw new BusinessException("外协车辆必须选择承运商");
            }
            Carrier carrier = carrierRepository.findById(vehicleDTO.getCarrierId())
                    .orElseThrow(() -> new BusinessException("承运商不存在"));
            if (carrier.getStatus() != 1) {
                throw new BusinessException("承运商已停用，无法关联");
            }
        }

        Vehicle vehicle = convertToEntity(vehicleDTO);
        vehicle.setStatus(1);
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return convertToDTO(savedVehicle);
    }

    @Override
    @Transactional
    public VehicleDTO updateVehicle(Long id, VehicleDTO vehicleDTO) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("车辆不存在"));

        // 检查车牌号是否被其他车辆使用
        if (!vehicle.getPlateNumber().equals(vehicleDTO.getPlateNumber())
                && vehicleRepository.existsByPlateNumber(vehicleDTO.getPlateNumber())) {
            throw new BusinessException("车牌号已存在");
        }

        // 如果所属为外协，检查承运商是否存在
        if (vehicleDTO.getOwnerType() != null && vehicleDTO.getOwnerType() == 2) {
            if (vehicleDTO.getCarrierId() == null) {
                throw new BusinessException("外协车辆必须选择承运商");
            }
            Carrier carrier = carrierRepository.findById(vehicleDTO.getCarrierId())
                    .orElseThrow(() -> new BusinessException("承运商不存在"));
            if (carrier.getStatus() != 1) {
                throw new BusinessException("承运商已停用，无法关联");
            }
        }

        vehicle.setPlateNumber(vehicleDTO.getPlateNumber());
        vehicle.setVehicleType(vehicleDTO.getVehicleType());
        vehicle.setVehicleBrand(vehicleDTO.getVehicleBrand());
        vehicle.setLoadCapacity(vehicleDTO.getLoadCapacity());
        vehicle.setVolumeCapacity(vehicleDTO.getVolumeCapacity());
        vehicle.setLength(vehicleDTO.getLength());
        vehicle.setWidth(vehicleDTO.getWidth());
        vehicle.setHeight(vehicleDTO.getHeight());
        vehicle.setOwnerType(vehicleDTO.getOwnerType());
        vehicle.setCarrierId(vehicleDTO.getCarrierId());
        vehicle.setGpsDeviceId(vehicleDTO.getGpsDeviceId());
        vehicle.setStatus(vehicleDTO.getStatus());
        vehicle.setLastMaintenance(vehicleDTO.getLastMaintenance());

        Vehicle updatedVehicle = vehicleRepository.save(vehicle);
        return convertToDTO(updatedVehicle);
    }

    @Override
    @Transactional
    public void deleteVehicle(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("车辆不存在"));
        // 检查车辆状态，已报废的车辆不能删除
        if (vehicle.getStatus() != null && vehicle.getStatus() == 3) {
            throw new BusinessException("已报废车辆不能删除");
        }
        vehicleRepository.deleteById(id);
    }

    @Override
    public VehicleDTO getVehicleById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("车辆不存在"));
        return convertToDTO(vehicle);
    }

    @Override
    public VehicleDTO getVehicleByPlateNumber(String plateNumber) {
        Vehicle vehicle = vehicleRepository.findByPlateNumber(plateNumber)
                .orElseThrow(() -> new BusinessException("车辆不存在"));
        return convertToDTO(vehicle);
    }

    @Override
    public PageResult<VehicleDTO> getVehicleList(String plateNumber, Integer status, int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        String searchPlateNumber = plateNumber == null ? "" : plateNumber;
        Integer searchStatus = status == null ? 1 : status;

        Page<Vehicle> page = vehicleRepository.findByPlateNumberContainingAndStatus(
                searchPlateNumber, searchStatus, pageable);

        List<VehicleDTO> list = page.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new PageResult<>(pageNum, pageSize, page.getTotalElements(), list);
    }

    @Override
    public List<VehicleDTO> getAvailableVehicles() {
        List<Vehicle> vehicles = vehicleRepository.findByStatus(1);
        return vehicles.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * 将DTO转换为实体
     *
     * @param dto 车辆DTO
     * @return 车辆实体
     */
    private Vehicle convertToEntity(VehicleDTO dto) {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(dto.getId());
        vehicle.setPlateNumber(dto.getPlateNumber());
        vehicle.setVehicleType(dto.getVehicleType());
        vehicle.setVehicleBrand(dto.getVehicleBrand());
        vehicle.setLoadCapacity(dto.getLoadCapacity());
        vehicle.setVolumeCapacity(dto.getVolumeCapacity());
        vehicle.setLength(dto.getLength());
        vehicle.setWidth(dto.getWidth());
        vehicle.setHeight(dto.getHeight());
        vehicle.setOwnerType(dto.getOwnerType());
        vehicle.setCarrierId(dto.getCarrierId());
        vehicle.setGpsDeviceId(dto.getGpsDeviceId());
        vehicle.setStatus(dto.getStatus());
        vehicle.setLastMaintenance(dto.getLastMaintenance());
        return vehicle;
    }

    /**
     * 将实体转换为DTO
     *
     * @param entity 车辆实体
     * @return 车辆DTO
     */
    private VehicleDTO convertToDTO(Vehicle entity) {
        VehicleDTO dto = new VehicleDTO();
        dto.setId(entity.getId());
        dto.setPlateNumber(entity.getPlateNumber());
        dto.setVehicleType(entity.getVehicleType());
        dto.setVehicleBrand(entity.getVehicleBrand());
        dto.setLoadCapacity(entity.getLoadCapacity());
        dto.setVolumeCapacity(entity.getVolumeCapacity());
        dto.setLength(entity.getLength());
        dto.setWidth(entity.getWidth());
        dto.setHeight(entity.getHeight());
        dto.setOwnerType(entity.getOwnerType());
        dto.setCarrierId(entity.getCarrierId());
        dto.setGpsDeviceId(entity.getGpsDeviceId());
        dto.setStatus(entity.getStatus());
        dto.setLastMaintenance(entity.getLastMaintenance());

        // 设置承运商名称
        if (entity.getCarrierId() != null) {
            carrierRepository.findById(entity.getCarrierId())
                    .ifPresent(carrier -> dto.setCarrierName(carrier.getCarrierName()));
        }

        return dto;
    }
}
