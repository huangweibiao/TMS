package com.tms.service.impl;

import com.tms.dto.DriverDTO;
import com.tms.common.PageResult;
import com.tms.entity.Carrier;
import com.tms.entity.Driver;
import com.tms.exception.BusinessException;
import com.tms.repository.CarrierRepository;
import com.tms.repository.DriverRepository;
import com.tms.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 司机服务实现类
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Service
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final CarrierRepository carrierRepository;

    @Autowired
    public DriverServiceImpl(DriverRepository driverRepository, CarrierRepository carrierRepository) {
        this.driverRepository = driverRepository;
        this.carrierRepository = carrierRepository;
    }

    @Override
    @Transactional
    public DriverDTO createDriver(DriverDTO driverDTO) {
        // 检查身份证号是否已存在
        if (driverRepository.existsByIdCard(driverDTO.getIdCard())) {
            throw new BusinessException("身份证号已存在");
        }

        // 检查承运商是否存在
        if (driverDTO.getCarrierId() != null) {
            Carrier carrier = carrierRepository.findById(driverDTO.getCarrierId())
                    .orElseThrow(() -> new BusinessException("承运商不存在"));
            if (carrier.getStatus() != 1) {
                throw new BusinessException("承运商已停用");
            }
        }

        Driver driver = convertToEntity(driverDTO);
        driver.setStatus(1);
        Driver savedDriver = driverRepository.save(driver);
        return convertToDTO(savedDriver);
    }

    @Override
    @Transactional
    public DriverDTO updateDriver(Long id, DriverDTO driverDTO) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new BusinessException("司机不存在"));

        // 检查身份证号是否被其他司机使用
        if (!driver.getIdCard().equals(driverDTO.getIdCard())
                && driverRepository.existsByIdCard(driverDTO.getIdCard())) {
            throw new BusinessException("身份证号已存在");
        }

        // 检查承运商是否存在
        if (driverDTO.getCarrierId() != null) {
            Carrier carrier = carrierRepository.findById(driverDTO.getCarrierId())
                    .orElseThrow(() -> new BusinessException("承运商不存在"));
            if (carrier.getStatus() != 1) {
                throw new BusinessException("承运商已停用");
            }
        }

        driver.setDriverName(driverDTO.getDriverName());
        driver.setIdCard(driverDTO.getIdCard());
        driver.setPhone(driverDTO.getPhone());
        driver.setLicenseType(driverDTO.getLicenseType());
        driver.setLicenseNo(driverDTO.getLicenseNo());
        driver.setHireDate(driverDTO.getHireDate());
        driver.setCarrierId(driverDTO.getCarrierId());
        driver.setVehicleId(driverDTO.getVehicleId());
        driver.setStatus(driverDTO.getStatus());

        Driver updatedDriver = driverRepository.save(driver);
        return convertToDTO(updatedDriver);
    }

    @Override
    @Transactional
    public void deleteDriver(Long id) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new BusinessException("司机不存在"));
        
        // 检查司机状态，如果在途不能删除
        if (driver.getStatus() == 2) {
            throw new BusinessException("司机在途，不能删除");
        }
        
        driverRepository.deleteById(id);
    }

    @Override
    public DriverDTO getDriverById(Long id) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new BusinessException("司机不存在"));
        return convertToDTO(driver);
    }

    @Override
    public DriverDTO getDriverByIdCard(String idCard) {
        Driver driver = driverRepository.findByIdCard(idCard)
                .orElseThrow(() -> new BusinessException("司机不存在"));
        return convertToDTO(driver);
    }

    @Override
    public PageResult<DriverDTO> getDriverList(String driverName, Integer status, int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        String searchName = driverName == null ? "" : driverName;
        Integer searchStatus = status == null ? 1 : status;

        Page<Driver> page = driverRepository.findByDriverNameContainingAndStatus(
                searchName, searchStatus, pageable);

        List<DriverDTO> list = page.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new PageResult<>(pageNum, pageSize, page.getTotalElements(), list);
    }

    @Override
    public List<DriverDTO> getAvailableDrivers() {
        // 状态1-空闲的司机为可用司机
        List<Driver> drivers = driverRepository.findByStatus(1);
        return drivers.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * 将DTO转换为实体
     *
     * @param dto 司机DTO
     * @return 司机实体
     */
    private Driver convertToEntity(DriverDTO dto) {
        Driver driver = new Driver();
        driver.setId(dto.getId());
        driver.setDriverName(dto.getDriverName());
        driver.setIdCard(dto.getIdCard());
        driver.setPhone(dto.getPhone());
        driver.setLicenseType(dto.getLicenseType());
        driver.setLicenseNo(dto.getLicenseNo());
        driver.setHireDate(dto.getHireDate());
        driver.setCarrierId(dto.getCarrierId());
        driver.setVehicleId(dto.getVehicleId());
        driver.setStatus(dto.getStatus());
        return driver;
    }

    /**
     * 将实体转换为DTO
     *
     * @param entity 司机实体
     * @return 司机DTO
     */
    private DriverDTO convertToDTO(Driver entity) {
        DriverDTO dto = new DriverDTO();
        dto.setId(entity.getId());
        dto.setDriverName(entity.getDriverName());
        dto.setIdCard(entity.getIdCard());
        dto.setPhone(entity.getPhone());
        dto.setLicenseType(entity.getLicenseType());
        dto.setLicenseNo(entity.getLicenseNo());
        dto.setHireDate(entity.getHireDate());
        dto.setCarrierId(entity.getCarrierId());
        dto.setVehicleId(entity.getVehicleId());
        dto.setStatus(entity.getStatus());
        
        // 设置承运商名称
        if (entity.getCarrierId() != null) {
            carrierRepository.findById(entity.getCarrierId())
                    .ifPresent(carrier -> dto.setCarrierName(carrier.getCarrierName()));
        }
        
        return dto;
    }
}
