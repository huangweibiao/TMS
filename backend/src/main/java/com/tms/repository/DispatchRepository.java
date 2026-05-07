package com.tms.repository;

import com.tms.entity.Dispatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 调度单Repository接口
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Repository
public interface DispatchRepository extends JpaRepositoryRepository<Dispatch, Long> {

    /**
     * 根据调度单号查询调度单
     *
     * @param dispatchNo 调度单号
     * @return 调度单对象
     */
    Optional Optional<Dispatch> findByDispatchNo(String dispatchNo);

    /**
     * 根据运单ID查询调度单
     *
     * @param waybillId 运单ID
     * @return 调度单对象
     */
    Optional Optional<Dispatch> findByWaybillId(Long waybillId);

    /**
     * 根据车辆ID和状态查询调度单列表
     *
     * @param vehicleId 车辆ID
     * @param status    状态
     * @return 调度单列表
     */
    List List<Dispatch> findByVehicleIdAndDispatchStatus(Long vehicleId, Integer status);

    /**
     * 分页查询调度单列表
     *
     * @param dispatchNo 调度单号（模糊查询）
     * @param status     状态
     * @param pageable   分页参数
     * @return 调度单分页列表
     */
    Page Page<Dispatch> findByDispatchNoContainingAndDispatchStatus(String dispatchNo, Integer status, Pageable pageable);

    /**
     * 根据状态查询调度单
     *
     * @param status   状态
     * @param pageable 分页参数
     * @return 调度单分页列表
     */
    Page Page<Dispatch> findByDispatchStatus(Integer status, Pageable pageable);

    /**
     * 查询指定前缀的最大调度单号
     *
     * @param prefix 前缀
     * @return 最大调度单号
     */
    @Query("SELECT MAX(d.dispatchNo) FROM Dispatch d WHERE d.dispatchNo LIKE ?1%")
    String findMaxDispatchNoByPrefix(String prefix);
}
