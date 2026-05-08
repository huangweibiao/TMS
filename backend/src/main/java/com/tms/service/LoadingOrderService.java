package com.tms.service;

import com.tms.dto.LoadingOrderDTO;
import com.tms.entity.LoadingOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * 装货单Service接口
 *
 * @author TMS Team
 * @version 1.0.0
 */
public interface LoadingOrderService {

    /**
     * 创建装货单
     *
     * @param dto 装货单DTO
     * @return 创建后的装货单
     */
    LoadingOrder createLoadingOrder(LoadingOrderDTO dto);

    /**
     * 更新装货单
     *
     * @param id  装货单ID
     * @param dto 装货单DTO
     * @return 更新后的装货单
     */
    LoadingOrder updateLoadingOrder(Long id, LoadingOrderDTO dto);

    /**
     * 删除装货单
     *
     * @param id 装货单ID
     */
    void deleteLoadingOrder(Long id);

    /**
     * 根据ID查询装货单
     *
     * @param id 装货单ID
     * @return 装货单对象
     */
    Optional<LoadingOrder> findById(Long id);

    /**
     * 根据装货单号查询
     *
     * @param loadingNo 装货单号
     * @return 装货单对象
     */
    Optional<LoadingOrder> findByLoadingNo(String loadingNo);

    /**
     * 根据调度单ID查询装货单列表
     *
     * @param dispatchId 调度单ID
     * @return 装货单列表
     */
    List<LoadingOrder> findByDispatchId(Long dispatchId);

    /**
     * 分页查询装货单列表
     *
     * @param loadingNo  装货单号（模糊查询）
     * @param status     状态
     * @param pageable   分页参数
     * @return 分页结果
     */
    Page<LoadingOrder> findLoadingOrders(String loadingNo, Integer status, Pageable pageable);

    /**
     * 开始装货
     *
     * @param id       装货单ID
     * @param operator 操作人
     * @return 更新后的装货单
     */
    LoadingOrder startLoading(Long id, String operator);

    /**
     * 完成装货
     *
     * @param id       装货单ID
     * @param operator 操作人
     * @return 更新后的装货单
     */
    LoadingOrder completeLoading(Long id, String operator);

    /**
     * 生成装货单号
     *
     * @return 装货单号
     */
    String generateLoadingNo();
}
