package com.tms.service.impl;

import com.tms.dto.WarehouseDTO;
import com.tms.common.PageResult;
import com.tms.entity.Warehouse;
import com.tms.exception.BusinessException;
import com.tms.repository.WarehouseRepository;
import com.tms.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 仓库服务实现类
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Service
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;

    @Autowired
    public WarehouseServiceImpl(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    @Transactional
    public WarehouseDTO createWarehouse(WarehouseDTO warehouseDTO) {
        // 检查仓库编码是否已存在
        if (warehouseRepository.existsByWarehouseCode(warehouseDTO.getWarehouseCode())) {
            throw new BusinessException("仓库编码已存在");
        }

        Warehouse warehouse = convertToEntity(warehouseDTO);
        warehouse.setStatus(1);
        Warehouse savedWarehouse = warehouseRepository.save(warehouse);
        return convertToDTO(savedWarehouse);
    }

    @Override
    @Transactional
    public WarehouseDTO updateWarehouse(Long id, WarehouseDTO warehouseDTO) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new BusinessException("仓库不存在"));

        // 检查仓库编码是否被其他仓库使用
        if (!warehouse.getWarehouseCode().equals(warehouseDTO.getWarehouseCode())
                && warehouseRepository.existsByWarehouseCode(warehouseDTO.getWarehouseCode())) {
            throw new BusinessException("仓库编码已存在");
        }

        warehouse.setWarehouseCode(warehouseDTO.getWarehouseCode());
        warehouse.setWarehouseName(warehouseDTO.getWarehouseName());
        warehouse.setWarehouseType(warehouseDTO.getWarehouseType());
        warehouse.setProvince(warehouseDTO.getProvince());
        warehouse.setCity(warehouseDTO.getCity());
        warehouse.setDistrict(warehouseDTO.getDistrict());
        warehouse.setAddress(warehouseDTO.getAddress());
        warehouse.setLongitude(warehouseDTO.getLongitude());
        warehouse.setLatitude(warehouseDTO.getLatitude());
        warehouse.setContactPerson(warehouseDTO.getContactPerson());
        warehouse.setContactPhone(warehouseDTO.getContactPhone());
        warehouse.setStatus(warehouseDTO.getStatus());

        Warehouse updatedWarehouse = warehouseRepository.save(warehouse);
        return convertToDTO(updatedWarehouse);
    }

    @Override
    @Transactional
    public void deleteWarehouse(Long id) {
        if (!warehouseRepository.existsById(id)) {
            throw new BusinessException("仓库不存在");
        }
        warehouseRepository.deleteById(id);
    }

    @Override
    public WarehouseDTO getWarehouseById(Long id) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new BusinessException("仓库不存在"));
        return convertToDTO(warehouse);
    }

    @Override
    public PageResult<WarehouseDTO> getWarehouseList(String warehouseName, Integer status, int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        String searchName = warehouseName == null ? "" : warehouseName;
        Integer searchStatus = status == null ? 1 : status;

        Page<Warehouse> page = warehouseRepository.findByWarehouseNameContainingAndStatus(
                searchName, searchStatus, pageable);

        List<WarehouseDTO> list = page.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new PageResult<>(pageNum, pageSize, page.getTotalElements(), list);
    }

    /**
     * 将DTO转换为实体
     *
     * @param dto 仓库DTO
     * @return 仓库实体
     */
    private Warehouse convertToEntity(WarehouseDTO dto) {
        Warehouse warehouse = new Warehouse();
        warehouse.setId(dto.getId());
        warehouse.setWarehouseCode(dto.getWarehouseCode());
        warehouse.setWarehouseName(dto.getWarehouseName());
        warehouse.setWarehouseType(dto.getWarehouseType());
        warehouse.setProvince(dto.getProvince());
        warehouse.setCity(dto.getCity());
        warehouse.setDistrict(dto.getDistrict());
        warehouse.setAddress(dto.getAddress());
        warehouse.setLongitude(dto.getLongitude());
        warehouse.setLatitude(dto.getLatitude());
        warehouse.setContactPerson(dto.getContactPerson());
        warehouse.setContactPhone(dto.getContactPhone());
        warehouse.setStatus(dto.getStatus());
        return warehouse;
    }

    /**
     * 将实体转换为DTO
     *
     * @param entity 仓库实体
     * @return 仓库DTO
     */
    private WarehouseDTO convertToDTO(Warehouse entity) {
        WarehouseDTO dto = new WarehouseDTO();
        dto.setId(entity.getId());
        dto.setWarehouseCode(entity.getWarehouseCode());
        dto.setWarehouseName(entity.getWarehouseName());
        dto.setWarehouseType(entity.getWarehouseType());
        dto.setProvince(entity.getProvince());
        dto.setCity(entity.getCity());
        dto.setDistrict(entity.getDistrict());
        dto.setAddress(entity.getAddress());
        dto.setLongitude(entity.getLongitude());
        dto.setLatitude(entity.getLatitude());
        dto.setContactPerson(entity.getContactPerson());
        dto.setContactPhone(entity.getContactPhone());
        dto.setStatus(entity.getStatus());
        return dto;
    }
}
