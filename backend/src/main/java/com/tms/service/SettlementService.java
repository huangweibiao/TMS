package com.tms.service;

import com.tms.dto.SettlementDTO;
import com.tms.entity.Settlement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * 结算单Service接口
 *
 * @author TMS Team
 * @version 1.0.0
 */
public interface SettlementService {

    /**
     * 创建结算单
     *
     * @param dto 结算单DTO
     * @return 创建后的结算单
     */
    Settlement createSettlement(SettlementDTO dto);

    /**
     * 更新结算单
     *
     * @param id  结算单ID
     * @param dto 结算单DTO
     * @return 更新后的结算单
     */
    Settlement updateSettlement(Long id, SettlementDTO dto);

    /**
     * 删除结算单
     *
     * @param id 结算单ID
     */
    void deleteSettlement(Long id);

    /**
     * 根据ID查询结算单
     *
     * @param id 结算单ID
     * @return 结算单对象
     */
    Optional<Settlement> findById(Long id);

    /**
     * 根据结算单号查询
     *
     * @param settlementNo 结算单号
     * @return 结算单对象
     */
    Optional<Settlement> findBySettlementNo(String settlementNo);

    /**
     * 分页查询结算单列表
     *
     * @param partyType 结算方类型
     * @param partyId   结算方ID
     * @param status    状态
     * @param pageable  分页参数
     * @return 分页结果
     */
    Page<Settlement> findSettlements(Integer partyType, Long partyId, Integer status, Pageable pageable);

    /**
     * 确认结算单
     *
     * @param id 结算单ID
     * @return 更新后的结算单
     */
    Settlement confirmSettlement(Long id);

    /**
     * 付款
     *
     * @param id     结算单ID
     * @param amount 付款金额
     * @return 更新后的结算单
     */
    Settlement makePayment(Long id, BigDecimal amount);

    /**
     * 取消结算单
     *
     * @param id     结算单ID
     * @param reason 取消原因
     * @return 更新后的结算单
     */
    Settlement cancelSettlement(Long id, String reason);

    /**
     * 生成结算单号
     *
     * @return 结算单号
     */
    String generateSettlementNo();
}
