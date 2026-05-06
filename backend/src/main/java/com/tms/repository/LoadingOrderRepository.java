package com.tms.repository;

import com.tms.entity.LoadingOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 装货单Repository接口
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Repository
public interface LoadingOrderRepository extends JpaRepositoryRepository<LoadingOrder, Long> {

    /**
     * 根据装货单号查询装货单
     *
     * @param loadingNo 装货单号
     * @return 装货单对象
     */
    Optional Optional<LoadingOrder> findByLoadingNo(String loadingNo);

    /**
     * 根据调度单ID查询装货单列表
     *
     * @param dispatchId 调度单ID
     * @return 装货单列表
     */
    List List<LoadingOrder> findByDispatchId(Long dispatchId);
}
