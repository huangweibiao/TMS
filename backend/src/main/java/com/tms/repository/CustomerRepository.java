package com.tms.repository;

import com.tms.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 客户Repository接口
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * 根据客户编码查询客户
     *
     * @param customerCode 客户编码
     * @return 客户对象
     */
    Optional<Customer> findByCustomerCode(String customerCode);

    /**
     * 分页查询客户列表
     *
     * @param customerName 客户名称（模糊查询）
     * @param status       状态
     * @param pageable     分页参数
     * @return 客户分页列表
     */
    Page<Customer> findByCustomerNameContainingAndStatus(String customerName, Integer status, Pageable pageable);

    /**
     * 检查客户编码是否存在
     *
     * @param customerCode 客户编码
     * @return 是否存在
     */
    boolean existsByCustomerCode(String customerCode);
}
