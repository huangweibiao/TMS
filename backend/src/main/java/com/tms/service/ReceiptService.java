package com.tms.service;

import com.tms.dto.ReceiptDTO;
import com.tms.entity.Receipt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * 回单Service接口
 *
 * @author TMS Team
 * @version 1.0.0
 */
public interface ReceiptService {

    /**
     * 创建回单
     *
     * @param dto 回单DTO
     * @return 创建后的回单
     */
    Receipt createReceipt(ReceiptDTO dto);

    /**
     * 更新回单
     *
     * @param id  回单ID
     * @param dto 回单DTO
     * @return 更新后的回单
     */
    Receipt updateReceipt(Long id, ReceiptDTO dto);

    /**
     * 删除回单
     *
     * @param id 回单ID
     */
    void deleteReceipt(Long id);

    /**
     * 根据ID查询回单
     *
     * @param id 回单ID
     * @return 回单对象
     */
    Optional<Receipt> findById(Long id);

    /**
     * 根据回单号查询
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
     * 分页查询回单列表
     *
     * @param receiptNo 回单号（模糊查询）
     * @param status    状态
     * @param pageable  分页参数
     * @return 分页结果
     */
    Page<Receipt> findReceipts(String receiptNo, Integer status, Pageable pageable);

    /**
     * 回传回单
     *
     * @param id 回单ID
     * @return 更新后的回单
     */
    Receipt returnReceipt(Long id);

    /**
     * 审核回单
     *
     * @param id     回单ID
     * @param auditBy 审核人
     * @return 更新后的回单
     */
    Receipt auditReceipt(Long id, String auditBy);

    /**
     * 生成回单号
     *
     * @return 回单号
     */
    String generateReceiptNo();
}
