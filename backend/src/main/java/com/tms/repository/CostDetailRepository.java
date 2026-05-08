package com.tms.repository;

import com.tms.entity.CostDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 费用明细Repository接口
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Repository
public interface CostDetailRepository extends JpaRepository<CostDetail, Long> {

    /**
     * 根据运单ID查询费用明细
     *
     * @param waybillId 运单ID
     * @return 费用明细列表
     */
    List<CostDetail> findByWaybillId(Long waybillId);

    /**
     * 根据运单ID和费用类型查询费用明细
     *
     * @param waybillId 运单ID
     * @param costType  费用类型
     * @return 费用明细列表
     */
    List<CostDetail> findByWaybillIdAndCostType(Long waybillId, Integer costType);

    /**
     * 根据结算单ID查询费用明细
     *
     * @param settlementId 结算单ID
     * @return 费用明细列表
     */
    List<CostDetail> findBySettlementId(Long settlementId);

    /**
     * 根据结算状态查询费用明细
     *
     * @param settlementStatus 结算状态
     * @return 费用明细列表
     */
    List<CostDetail> findBySettlementStatus(Integer settlementStatus);

    /**
     * 根据费用类型查询费用明细
     *
     * @param costType 费用类型
     * @return 费用明细列表
     */
    List<CostDetail> findByCostType(Integer costType);
}
