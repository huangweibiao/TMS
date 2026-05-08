package com.tms.service.impl;

import com.tms.dto.CarrierDTO;
import com.tms.common.PageResult;
import com.tms.entity.Carrier;
import com.tms.exception.BusinessException;
import com.tms.repository.CarrierRepository;
import com.tms.service.CarrierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarrierServiceImpl implements CarrierService {

    private final CarrierRepository carrierRepository;

    @Autowired
    public CarrierServiceImpl(CarrierRepository carrierRepository) {
        this.carrierRepository = carrierRepository;
    }

    @Override
    @Transactional
    public CarrierDTO createCarrier(CarrierDTO carrierDTO) {
        if (carrierRepository.existsByCarrierCode(carrierDTO.getCarrierCode())) {
            throw new BusinessException("承运商编码已存在");
        }
        Carrier carrier = convertToEntity(carrierDTO);
        carrier.setStatus(1);
        Carrier saved = carrierRepository.save(carrier);
        return convertToDTO(saved);
    }

    @Override
    @Transactional
    public CarrierDTO updateCarrier(Long id, CarrierDTO carrierDTO) {
        Carrier carrier = carrierRepository.findById(id)
                .orElseThrow(() -> new BusinessException("承运商不存在"));
        if (!carrier.getCarrierCode().equals(carrierDTO.getCarrierCode())
                && carrierRepository.existsByCarrierCode(carrierDTO.getCarrierCode())) {
            throw new BusinessException("承运商编码已存在");
        }
        carrier.setCarrierCode(carrierDTO.getCarrierCode());
        carrier.setCarrierName(carrierDTO.getCarrierName());
        carrier.setCarrierType(carrierDTO.getCarrierType());
        carrier.setLicenseNo(carrierDTO.getLicenseNo());
        carrier.setTaxNo(carrierDTO.getTaxNo());
        carrier.setContactPerson(carrierDTO.getContactPerson());
        carrier.setContactPhone(carrierDTO.getContactPhone());
        carrier.setCooperationStart(carrierDTO.getCooperationStart());
        carrier.setCooperationEnd(carrierDTO.getCooperationEnd());
        carrier.setRating(carrierDTO.getRating());
        carrier.setStatus(carrierDTO.getStatus());
        Carrier updated = carrierRepository.save(carrier);
        return convertToDTO(updated);
    }

    @Override
    @Transactional
    public void deleteCarrier(Long id) {
        if (!carrierRepository.existsById(id)) {
            throw new BusinessException("承运商不存在");
        }
        carrierRepository.deleteById(id);
    }

    @Override
    public CarrierDTO getCarrierById(Long id) {
        Carrier carrier = carrierRepository.findById(id)
                .orElseThrow(() -> new BusinessException("承运商不存在"));
        return convertToDTO(carrier);
    }

    @Override
    public PageResult<CarrierDTO> getCarrierList(String carrierName, Integer status, int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        String searchName = carrierName == null ? "" : carrierName;
        Integer searchStatus = status == null ? 1 : status;
        Page<Carrier> page = carrierRepository.findByCarrierNameContainingAndStatus(searchName, searchStatus, pageable);
        List<CarrierDTO> list = page.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new PageResult<>(pageNum, pageSize, page.getTotalElements(), list);
    }

    private Carrier convertToEntity(CarrierDTO dto) {
        Carrier entity = new Carrier();
        entity.setId(dto.getId());
        entity.setCarrierCode(dto.getCarrierCode());
        entity.setCarrierName(dto.getCarrierName());
        entity.setCarrierType(dto.getCarrierType());
        entity.setLicenseNo(dto.getLicenseNo());
        entity.setTaxNo(dto.getTaxNo());
        entity.setContactPerson(dto.getContactPerson());
        entity.setContactPhone(dto.getContactPhone());
        entity.setCooperationStart(dto.getCooperationStart());
        entity.setCooperationEnd(dto.getCooperationEnd());
        entity.setRating(dto.getRating());
        entity.setStatus(dto.getStatus());
        return entity;
    }

    private CarrierDTO convertToDTO(Carrier entity) {
        CarrierDTO dto = new CarrierDTO();
        dto.setId(entity.getId());
        dto.setCarrierCode(entity.getCarrierCode());
        dto.setCarrierName(entity.getCarrierName());
        dto.setCarrierType(entity.getCarrierType());
        dto.setLicenseNo(entity.getLicenseNo());
        dto.setTaxNo(entity.getTaxNo());
        dto.setContactPerson(entity.getContactPerson());
        dto.setContactPhone(entity.getContactPhone());
        dto.setCooperationStart(entity.getCooperationStart());
        dto.setCooperationEnd(entity.getCooperationEnd());
        dto.setRating(entity.getRating());
        dto.setStatus(entity.getStatus());
        return dto;
    }
}
