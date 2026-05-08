package com.tms.repository;

import com.tms.entity.Receipt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 回单Repository接口
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long>, JpaSpecificationExecutor<Receipt> {

    /**
     * 根据回单号查询回单
     *
     * @param receiptNo 回单号
     * @return 回单对象
     */
    Optional<Receipt> findByReceiptNo(String receiptNo);

    /**
     * 根据运单ID查询回单
     *
     * @param waybillId 运单ID
     * @return 回单对象
     */
    Optional<Receipt> findByWaybillId(Long waybillId);

    /**
     * 根据状态分页查询回单
     *
     * @param status   状态
     * @param pageable 分页参数
     * @return 回单分页列表
     */
    Page<Receipt> findByStatus(Integer status, Pageable pageable);

    /**
     * 查询指定前缀的最大回单号
     *
     * @param prefix 前缀
     * @return 最大回单号
     */
    @Query("SELECT MAX(r.receiptNo) FROM Receipt r WHERE r.receiptNo LIKE :prefix%")
    String findMaxReceiptNoByPrefix(@Param("prefix") String prefix);
}
