package com.tms.repository;

import com.tms.entity.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 地址库Repository接口
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Repository
public interface AddressRepository extends JpaRepositoryRepository<Address, Long> {

    /**
     * 根据客户ID查询地址列表
     *
     * @param customerId 客户ID
     * @return 地址列表
     */
    List List<Address> findByCustomerId(Long customerId);

    /**
     * 根据客户ID和地址类型查询地址列表
     *
     * @param customerId  客户ID
     * @param addressType 地址类型
     * @return 地址列表
     */
    List List<Address> findByCustomerIdAndAddressType(Long customerId, Integer addressType);

    /**
     * 根据客户ID查询默认地址
     *
     * @param customerId 客户ID
     * @return 默认地址
     */
    Optional Optional<Address> findByCustomerIdAndIsDefault(Long customerId, Integer isDefault);

    /**
     * 根据地址编码查询地址
     *
     * @param addressCode 地址编码
     * @return 地址对象
     */
    Optional Optional<Address> findByAddressCode(String addressCode);

    /**
     * 根据状态分页查询地址
     *
     * @param status   状态
     * @param pageable 分页参数
     * @return 地址分页列表
     */
    Page Page<Address> findByStatus(Integer status, Pageable pageable);

    /**
     * 根据客户ID和状态查询地址列表
     *
     * @param customerId 客户ID
     * @param status     状态
     * @return 地址列表
     */
    List List<Address> findByCustomerIdAndStatus(Long customerId, Integer status);
}
