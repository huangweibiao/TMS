package com.tms.repository;

import com.tms.entity.Waybill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 运单Repository接口
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Repository
public interface WaybillRepository extends JpaRepositoryRepository<Waybill, Long> {

    /**
     * 根据运单号查询运单
     *
     * @param waybillNo 运单号
     * @return 运单对象
     */
    Optional Optional<Waybill> findByWaybillNo(String waybillNo);

    /**
     * 根据客户ID和状态分页查询运单
     *
     * @param customerId 客户ID
     * @param status     状态
     * @param pageable   分页参数
     * @return 运单分页列表
     */
    Page Page<Waybill> findByCustomerIdAndWaybillStatus(Long customerId, Integer status, Pageable pageable);

    /**
     * 检查运单号是否存在
     *
     * @param waybillNo 运单号
     * @return 是否存在
     */
    boolean existsByWaybillNo(String waybillNo);
}
