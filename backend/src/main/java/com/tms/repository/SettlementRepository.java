package com.tms.repository;

import com.tms.entity.Settlement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 结算单Repository接口
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Repository
public interface SettlementRepository extends JpaRepositoryRepository<Settlement, Long> {

    /**
     * 根据结算单号查询结算单
     *
     * @param settlementNo 结算单号
     * @return 结算单对象
     */
    Optional Optional<Settlement> findBySettlementNo(String settlementNo);

    /**
     * 根据结算方类型和ID分页查询结算单
     *
     * @param partyType 结算方类型
     * @param partyId   结算方ID
     * @param pageable  分页参数
     * @return 结算单分页列表
     */
    Page Page<Settlement> findByPartyTypeAndPartyId(Integer partyType, Long partyId, Pageable pageable);

    /**
     * 根据状态分页查询结算单
     *
     * @param status   状态
     * @param pageable 分页参数
     * @return 结算单分页列表
     */
    Page Page<Settlement> findByStatus(Integer status, Pageable pageable);
}
