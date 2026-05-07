package com.tms.controller;

import com.tms.common.Result;
import com.tms.common.PageResult;
import com.tms.dto.WarehouseDTO;
import com.tms.service.WarehouseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 仓库管理控制器
 *
 * @author TMS Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/warehouses")
public class WarehouseController {

    private final WarehouseService warehouseService;

    @Autowired
    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    /**
     * 创建仓库
     *
     * @param warehouseDTO 仓库信息
     * @return 创建结果
     */
    @PostMapping
    public Result<WarehouseDTO> createWarehouse(@Valid @RequestBody WarehouseDTO warehouseDTO) {
        WarehouseDTO result = warehouseService.createWarehouse(warehouseDTO);
        return Result.success("创建成功", result);
    }

    /**
     * 更新仓库
     *
     * @param id           仓库ID
     * @param warehouseDTO 仓库信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public Result<WarehouseDTO> updateWarehouse(@PathVariable Long id, @Valid @RequestBody WarehouseDTO warehouseDTO) {
        WarehouseDTO result = warehouseService.updateWarehouse(id, warehouseDTO);
        return Result.success("更新成功", result);
    }

    /**
     * 删除仓库
     *
     * @param id 仓库ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteWarehouse(@PathVariable Long id) {
        warehouseService.deleteWarehouse(id);
        return Result.success("删除成功");
    }

    /**
     * 根据ID查询仓库
     *
     * @param id 仓库ID
     * @return 仓库信息
     */
    @GetMapping("/{id}")
    public Result<WarehouseDTO> getWarehouseById(@PathVariable Long id) {
        WarehouseDTO result = warehouseService.getWarehouseById(id);
        return Result.success(result);
    }

    /**
     * 分页查询仓库列表
     *
     * @param warehouseName 仓库名称
     * @param status        状态
     * @param pageNum       页码
     * @param pageSize      每页大小
     * @return 仓库分页列表
     */
    @GetMapping
    public Result<PageResult<WarehouseDTO>> getWarehouseList(
            @RequestParam(required = false) String warehouseName,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        PageResult<WarehouseDTO> result = warehouseService.getWarehouseList(warehouseName, status, pageNum, pageSize);
        return Result.success(result);
    }
}

