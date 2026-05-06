package com.tms.service.impl;

import com.tms.dto.CustomerDTO;
import com.tms.common.PageResult;
import com.tms.entity.Customer;
import com.tms.exception.BusinessException;
import com.tms.repository.CustomerRepository;
import com.tms.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 客户服务实现类
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        // 检查客户编码是否已存在
        if (customerRepository.existsByCustomerCode(customerDTO.getCustomerCode())) {
            throw new BusinessException("客户编码已存在");
        }

        Customer customer = convertToEntity(customerDTO);
        customer.setStatus(1);
        Customer savedCustomer = customerRepository.save(customer);
        return convertToDTO(savedCustomer);
    }

    @Override
    @Transactional
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new BusinessException("客户不存在"));

        // 检查客户编码是否被其他客户使用
        if (!customer.getCustomerCode().equals(customerDTO.getCustomerCode())
                && customerRepository.existsByCustomerCode(customerDTO.getCustomerCode())) {
            throw new BusinessException("客户编码已存在");
        }

        customer.setCustomerCode(customerDTO.getCustomerCode());
        customer.setCustomerName(customerDTO.getCustomerName());
        customer.setShortName(customerDTO.getShortName());
        customer.setContactPerson(customerDTO.getContactPerson());
        customer.setContactPhone(customerDTO.getContactPhone());
        customer.setContactAddress(customerDTO.getContactAddress());
        customer.setCreditLevel(customerDTO.getCreditLevel());
        customer.setSettlementCycle(customerDTO.getSettlementCycle());
        customer.setStatus(customerDTO.getStatus());

        Customer updatedCustomer = customerRepository.save(customer);
        return convertToDTO(updatedCustomer);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new BusinessException("客户不存在");
        }
        customerRepository.deleteById(id);
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new BusinessException("客户不存在"));
        return convertToDTO(customer);
    }

    @Override
    public PageResultResult<CustomerDTO> getCustomerList(String customerName, Integer status, int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        String searchName = customerName == null ? "" : customerName;
        Integer searchStatus = status == null ? 1 : status;

        Page Page<Customer> page = customerRepository.findByCustomerNameContainingAndStatus(
                searchName, searchStatus, pageable);

        List List<CustomerDTO> list = page.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new PageResult<>(pageNum, pageSize, page.getTotalElements(), list);
    }

    /**
     * 将DTO转换为实体
     *
     * @param dto 客户DTO
     * @return 客户实体
     */
    private Customer convertToEntity(CustomerDTO dto) {
        Customer customer = new Customer();
        customer.setId(dto.getId());
        customer.setCustomerCode(dto.getCustomerCode());
        customer.setCustomerName(dto.getCustomerName());
        customer.setShortName(dto.getShortName());
        customer.setContactPerson(dto.getContactPerson());
        customer.setContactPhone(dto.getContactPhone());
        customer.setContactAddress(dto.getContactAddress());
        customer.setCreditLevel(dto.getCreditLevel());
        customer.setSettlementCycle(dto.getSettlementCycle());
        customer.setStatus(dto.getStatus());
        return customer;
    }

    /**
     * 将实体转换为DTO
     *
     * @param entity 客户实体
     * @return 客户DTO
     */
    private CustomerDTO convertToDTO(Customer entity) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(entity.getId());
        dto.setCustomerCode(entity.getCustomerCode());
        dto.setCustomerName(entity.getCustomerName());
        dto.setShortName(entity.getShortName());
        dto.setContactPerson(entity.getContactPerson());
        dto.setContactPhone(entity.getContactPhone());
        dto.setContactAddress(entity.getContactAddress());
        dto.setCreditLevel(entity.getCreditLevel());
        dto.setSettlementCycle(entity.getSettlementCycle());
        dto.setStatus(entity.getStatus());
        return dto;
    }
}
