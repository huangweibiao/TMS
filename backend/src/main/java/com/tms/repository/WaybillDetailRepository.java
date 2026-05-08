package com.tms.repository;

import com.tms.entity.WaybillDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 运单明细Repository接口
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Repository
public interface WaybillDetailRepository extends JpaRepository<WaybillDetail, Long> {

    /**
     * 根据运单ID查询明细列表
     *
     * @param waybillId 运单ID
     * @return 明细列表
     */
    List<WaybillDetail> findByWaybillId(Long waybillId);

    /**
     * 删除运单的所有明细
     *
     * @param waybillId 运单ID
     */
    void deleteByWaybillId(Long waybillId);
}
