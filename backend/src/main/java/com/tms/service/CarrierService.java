package com.tms.service;

import com.tms.dto.CarrierDTO;
import com.tms.common.PageResult;

/**
 * 承运商服务接口
 *
 * @author TMS Team
 * @version 1.0.0
 */
public interface CarrierService {

    /**
     * 创建承运商
     *
     * @param carrierDTO 承运商DTO
     * @return 创建后的承运商
     */
    CarrierDTO createCarrier(CarrierDTO carrierDTO);

    /**
     * 更新承运商
     *
     * @param id         承运商ID
     * @param carrierDTO 承运商DTO
     * @return 更新后的承运商
     */
    CarrierDTO updateCarrier(Long id, CarrierDTO carrierDTO);

    /**
     * 删除承运商
     *
     * @param id 承运商ID
     */
    void deleteCarrier(Long id);

    /**
     * 根据ID查询承运商
     *
     * @param id 承运商ID
     * @return 承运商信息
     */
    CarrierDTO getCarrierById(Long id);

    /**
     * 分页查询承运商列表
     *
     * @param carrierName 承运商名称
     * @param status      状态
     * @param pageNum     页码
     * @param pageSize    每页大小
     * @return 承运商分页列表
     */
    PageResultResult<CarrierDTO> getCarrierList(String carrierName, Integer status, int pageNum, int pageSize);
}
