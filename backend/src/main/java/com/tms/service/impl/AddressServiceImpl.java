package com.tms.service.impl;

import com.tms.dto.AddressDTO;
import com.tms.entity.Address;
import com.tms.exception.BusinessException;
import com.tms.repository.AddressRepository;
import com.tms.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 地址库Service实现类
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    @Transactional
    @CachePut(value = "address", key = "#result.id")
    public Address createAddress(AddressDTO dto) {
        Address address = new Address();
        address.setAddressCode(generateAddressCode());
        address.setAddressName(dto.getAddressName());
        address.setAddressType(dto.getAddressType());
        address.setCustomerId(dto.getCustomerId());
        address.setContactName(dto.getContactName());
        address.setContactPhone(dto.getContactPhone());
        address.setProvince(dto.getProvince());
        address.setCity(dto.getCity());
        address.setDistrict(dto.getDistrict());
        address.setDetailAddress(dto.getDetailAddress());
        address.setFullAddress(buildFullAddress(dto));
        address.setLongitude(dto.getLongitude());
        address.setLatitude(dto.getLatitude());
        address.setZipCode(dto.getZipCode());
        address.setIsDefault(dto.getIsDefault() != null ? dto.getIsDefault() : 0);
        address.setStatus(1);
        address.setRemark(dto.getRemark());

        // 如果设置为默认地址，取消该客户该类型的其他默认地址
        if (address.getIsDefault() == 1 && address.getCustomerId() != null) {
            cancelOtherDefaultAddresses(address.getCustomerId(), address.getAddressType(), null);
        }

        return addressRepository.save(address);
    }

    @Override
    @Transactional
    @CachePut(value = "address", key = "#id")
    public Address updateAddress(Long id, AddressDTO dto) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new BusinessException("地址不存在"));

        address.setAddressName(dto.getAddressName());
        address.setAddressType(dto.getAddressType());
        address.setCustomerId(dto.getCustomerId());
        address.setContactName(dto.getContactName());
        address.setContactPhone(dto.getContactPhone());
        address.setProvince(dto.getProvince());
        address.setCity(dto.getCity());
        address.setDistrict(dto.getDistrict());
        address.setDetailAddress(dto.getDetailAddress());
        address.setFullAddress(buildFullAddress(dto));
        address.setLongitude(dto.getLongitude());
        address.setLatitude(dto.getLatitude());
        address.setZipCode(dto.getZipCode());
        address.setRemark(dto.getRemark());

        // 如果设置为默认地址，取消该客户该类型的其他默认地址
        if (dto.getIsDefault() != null && dto.getIsDefault() == 1 && address.getCustomerId() != null) {
            cancelOtherDefaultAddresses(address.getCustomerId(), address.getAddressType(), id);
            address.setIsDefault(1);
        }

        return addressRepository.save(address);
    }

    @Override
    @Transactional
    @CacheEvict(value = "address", key = "#id")
    public void deleteAddress(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new BusinessException("地址不存在"));

        addressRepository.deleteById(id);
    }

    @Override
    @Cacheable(value = "address", key = "#id")
    public Optional Optional<Address> findById(Long id) {
        return addressRepository.findById(id);
    }

    @Override
    public List List<Address> findByCustomerId(Long customerId) {
        return addressRepository.findByCustomerId(customerId);
    }

    @Override
    public List List<Address> findByCustomerIdAndType(Long customerId, Integer addressType) {
        return addressRepository.findByCustomerIdAndAddressType(customerId, addressType);
    }

    @Override
    public Page Page<Address> findAddresses(Long customerId, Integer addressType, Integer status, Pageable pageable) {
        Specification Specification<Address> spec = (root, query, cb) -> {
            List List<Predicate> predicates = new ArrayList<>();

            if (customerId != null) {
                predicates.add(cb.equal(root.get("customerId"), customerId));
            }

            if (addressType != null) {
                predicates.add(cb.equal(root.get("addressType"), addressType));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return addressRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional
    @CachePut(value = "address", key = "#id")
    public Address setDefaultAddress(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new BusinessException("地址不存在"));

        // 取消该客户该类型的其他默认地址
        if (address.getCustomerId() != null) {
            cancelOtherDefaultAddresses(address.getCustomerId(), address.getAddressType(), id);
        }

        address.setIsDefault(1);
        return addressRepository.save(address);
    }

    @Override
    @Transactional
    @CachePut(value = "address", key = "#id")
    public Address cancelDefaultAddress(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new BusinessException("地址不存在"));

        address.setIsDefault(0);
        return addressRepository.save(address);
    }

    @Override
    @Transactional
    @CachePut(value = "address", key = "#id")
    public Address enableAddress(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new BusinessException("地址不存在"));

        address.setStatus(1);
        return addressRepository.save(address);
    }

    @Override
    @Transactional
    @CachePut(value = "address", key = "#id")
    public Address disableAddress(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new BusinessException("地址不存在"));

        address.setStatus(0);
        return addressRepository.save(address);
    }

    @Override
    public String generateAddressCode() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String prefix = "ADR" + dateStr;

        // 查询当天最大地址编码
        // 简化实现，实际应该查询数据库
        int seq = 1;
        return String.format("%s%04d", prefix, seq);
    }

    /**
     * 构建完整地址
     */
    private String buildFullAddress(AddressDTO dto) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.hasText(dto.getProvince())) {
            sb.append(dto.getProvince());
        }
        if (StringUtils.hasText(dto.getCity())) {
            sb.append(dto.getCity());
        }
        if (StringUtils.hasText(dto.getDistrict())) {
            sb.append(dto.getDistrict());
        }
        if (StringUtils.hasText(dto.getDetailAddress())) {
            sb.append(dto.getDetailAddress());
        }
        return sb.toString();
    }

    /**
     * 取消其他默认地址
     */
    private void cancelOtherDefaultAddresses(Long customerId, Integer addressType, Long excludeId) {
        List List<Address> addresses = addressRepository.findByCustomerIdAndAddressType(customerId, addressType);
        for (Address addr : addresses) {
            if (addr.getIsDefault() == 1 && (excludeId == null || !excludeId.equals(addr.getId()))) {
                addr.setIsDefault(0);
                addressRepository.save(addr);
            }
        }
    }
}
