package com.tms.service;

import com.tms.dto.WarehouseDTO;
import com.tms.common.PageResult;

/**
 * 仓库服务接口
 *
 * @author TMS Team
 * @version 1.0.0
 */
public interface WarehouseService {

    /**
     * 创建仓库
     *
     * @param warehouseDTO 仓库DTO
     * @return 创建后的仓库
     */
    WarehouseDTO createWarehouse(WarehouseDTO warehouseDTO);

    /**
     * 更新仓库
     *
     * @param id           仓库ID
     * @param warehouseDTO 仓库DTO
     * @return 更新后的仓库
     */
    WarehouseDTO updateWarehouse(Long id, WarehouseDTO warehouseDTO);

    /**
     * 删除仓库
     *
     * @param id 仓库ID
     */
    void deleteWarehouse(Long id);

    /**
     * 根据ID查询仓库
     *
     * @param id 仓库ID
     * @return 仓库信息
     */
    WarehouseDTO getWarehouseById(Long id);

    /**
     * 分页查询仓库列表
     *
     * @param warehouseName 仓库名称（模糊查询）
     * @param status        状态
     * @param pageNum       页码
     * @param pageSize      每页大小
     * @return 仓库分页列表
     */
    PageResult<WarehouseDTO> getWarehouseList(String warehouseName, Integer status, int pageNum, int pageSize);
}
