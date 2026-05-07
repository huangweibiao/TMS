package com.tms.service;

import com.tms.dto.WaybillDTO;
import com.tms.common.PageResult;

/**
 * 运单服务接口
 *
 * @author TMS Team
 * @version 1.0.0
 */
public interface WaybillService {

    /**
     * 创建运单
     *
     * @param waybillDTO 运单DTO
     * @return 创建后的运单
     */
    WaybillDTO createWaybill(WaybillDTO waybillDTO);

    /**
     * 更新运单
     *
     * @param id         运单ID
     * @param waybillDTO 运单DTO
     * @return 更新后的运单
     */
    WaybillDTO updateWaybill(Long id, WaybillDTO waybillDTO);

    /**
     * 删除运单
     *
     * @param id 运单ID
     */
    void deleteWaybill(Long id);

    /**
     * 根据ID查询运单
     *
     * @param id 运单ID
     * @return 运单信息
     */
    WaybillDTO getWaybillById(Long id);

    /**
     * 根据运单号查询运单
     *
     * @param waybillNo 运单号
     * @return 运单信息
     */
    WaybillDTO getWaybillByNo(String waybillNo);

    /**
     * 分页查询运单列表
     *
     * @param waybillNo 运单号
     * @param status    状态
     * @param pageNum   页码
     * @param pageSize  每页大小
     * @return 运单分页列表
     */
    PageResultResult<WaybillDTO> getWaybillList(String waybillNo, Integer status, int pageNum, int pageSize);

    /**
     * 更新运单状态
     *
     * @param id     运单ID
     * @param status 新状态
     * @param remark 备注
     */
    void updateWaybillStatus(Long id, Integer status, String remark);

    /**
     * 取消运单
     *
     * @param id     运单ID
     * @param remark 取消原因
     */
    void cancelWaybill(Long id, String remark);
}
