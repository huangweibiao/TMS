package com.tms.service;

import com.tms.dto.AddressDTO;
import com.tms.entity.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * 地址库Service接口
 *
 * @author TMS Team
 * @version 1.0.0
 */
public interface AddressService {

    /**
     * 创建地址
     *
     * @param dto 地址DTO
     * @return 创建后的地址
     */
    Address createAddress(AddressDTO dto);

    /**
     * 更新地址
     *
     * @param id  地址ID
     * @param dto 地址DTO
     * @return 更新后的地址
     */
    Address updateAddress(Long id, AddressDTO dto);

    /**
     * 删除地址
     *
     * @param id 地址ID
     */
    void deleteAddress(Long id);

    /**
     * 根据ID查询地址
     *
     * @param id 地址ID
     * @return 地址对象
     */
    Optional<Address> findById(Long id);

    /**
     * 根据客户ID查询地址列表
     *
     * @param customerId 客户ID
     * @return 地址列表
     */
    List<Address> findByCustomerId(Long customerId);

    /**
     * 根据客户ID和地址类型查询地址列表
     *
     * @param customerId  客户ID
     * @param addressType 地址类型
     * @return 地址列表
     */
    List<Address> findByCustomerIdAndType(Long customerId, Integer addressType);

    /**
     * 分页查询地址列表
     *
     * @param customerId  客户ID
     * @param addressType 地址类型
     * @param status      状态
     * @param pageable    分页参数
     * @return 分页结果
     */
    Page<Address> findAddresses(Long customerId, Integer addressType, Integer status, Pageable pageable);

    /**
     * 设置默认地址
     *
     * @param id 地址ID
     * @return 更新后的地址
     */
    Address setDefaultAddress(Long id);

    /**
     * 取消默认地址
     *
     * @param id 地址ID
     * @return 更新后的地址
     */
    Address cancelDefaultAddress(Long id);

    /**
     * 启用地址
     *
     * @param id 地址ID
     * @return 更新后的地址
     */
    Address enableAddress(Long id);

    /**
     * 停用地址
     *
     * @param id 地址ID
     * @return 更新后的地址
     */
    Address disableAddress(Long id);

    /**
     * 生成地址编码
     *
     * @return 地址编码
     */
    String generateAddressCode();
}
