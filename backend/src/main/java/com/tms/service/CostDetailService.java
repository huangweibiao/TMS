package com.tms.service;

import com.tms.common.PageResult;
import com.tms.dto.CostCalculateRequest;
import com.tms.dto.CostDetailDTO;

import java.util.List;

/**
 * 费用明细服务接口
 *
 * @author TMS Team
 * @version 1.0.0
 */
public interface CostDetailService {

    /**
     * 计算运费
     *
     * @param request 计算请求
     * @return 费用明细列表
     */
    List List<CostDetailDTO> calculateCost(CostCalculateRequest request);

    /**
     * 根据运单ID查询费用明细
     *
     * @param waybillId 运单ID
     * @return 费用明细列表
     */
    List List<CostDetailDTO> getCostDetailsByWaybillId(Long waybillId);

    /**
     * 分页查询费用明细
     *
     * @param waybillNo 运单号
     * @param costType  费用类型
     * @param pageNum   页码
     * @param pageSize  每页大小
     * @return 费用明细分页列表
     */
    PageResultResult<CostDetailDTO> getCostDetailList(String waybillNo, Integer costType, int pageNum, int pageSize);

    /**
     * 创建费用明细
     *
     * @param costDetailDTO 费用明细DTO
     * @return 创建后的费用明细
     */
    CostDetailDTO createCostDetail(CostDetailDTO costDetailDTO);

    /**
     * 更新费用明细
     *
     * @param id            费用明细ID
     * @param costDetailDTO 费用明细DTO
     * @return 更新后的费用明细
     */
    CostDetailDTO updateCostDetail(Long id, CostDetailDTO costDetailDTO);

    /**
     * 删除费用明细
     *
     * @param id 费用明细ID
     */
    void deleteCostDetail(Long id);

    /**
     * 根据ID查询费用明细
     *
     * @param id 费用明细ID
     * @return 费用明细
     */
    CostDetailDTO getCostDetailById(Long id);
}
