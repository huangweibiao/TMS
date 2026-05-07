package com.tms.service;

import com.tms.common.PageResult;
import com.tms.dto.DispatchDTO;

/**
 * 调度服务接口
 *
 * @author TMS Team
 * @version 1.0.0
 */
public interface DispatchService {

    /**
     * 创建调度单（智能派车）
     *
     * @param waybillId 运单ID
     * @param vehicleId 车辆ID
     * @param driverId  司机ID
     * @param strategy  派车策略：1-距离优先, 2-成本优先, 3-时效优先
     * @return 调度单DTO
     */
    DispatchDTO assignDispatch(Long waybillId, Long vehicleId, Long driverId, Integer strategy);

    /**
     * 手动创建调度单
     *
     * @param dispatchDTO 调度单DTO
     * @return 创建后的调度单
     */
    DispatchDTO createDispatch(DispatchDTO dispatchDTO);

    /**
     * 根据ID查询调度单
     *
     * @param id 调度单ID
     * @return 调度单信息
     */
    DispatchDTO getDispatchById(Long id);

    /**
     * 根据运单ID查询调度单
     *
     * @param waybillId 运单ID
     * @return 调度单信息
     */
    DispatchDTO getDispatchByWaybillId(Long waybillId);

    /**
     * 分页查询调度单列表
     *
     * @param dispatchNo 调度单号
     * @param status     状态
     * @param pageNum    页码
     * @param pageSize   每页大小
     * @return 调度单分页列表
     */
    PageResult<DispatchDTO> getDispatchList(String dispatchNo, Integer status, int pageNum, int pageSize);

    /**
     * 确认发车
     *
     * @param id 调度单ID
     */
    void startDispatch(Long id);

    /**
     * 完成调度
     *
     * @param id 调度单ID
     */
    void completeDispatch(Long id);

    /**
     * 取消调度
     *
     * @param id     调度单ID
     * @param reason 取消原因
     */
    void cancelDispatch(Long id, String reason);
}
