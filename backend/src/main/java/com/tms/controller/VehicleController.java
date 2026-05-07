package com.tms.controller;

import com.tms.common.Result;
import com.tms.common.PageResult;
import com.tms.dto.VehicleDTO;
import com.tms.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 车辆管理控制器
 *
 * @author TMS Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    /**
     * 创建车辆
     *
     * @param vehicleDTO 车辆信息
     * @return 创建结果
     */
    @PostMapping
    public Result<VehicleDTO> createVehicle(@Valid @RequestBody VehicleDTO vehicleDTO) {
        VehicleDTO result = vehicleService.createVehicle(vehicleDTO);
        return Result.success("创建成功", result);
    }

    /**
     * 更新车辆
     *
     * @param id         车辆ID
     * @param vehicleDTO 车辆信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public Result<VehicleDTO> updateVehicle(@PathVariable Long id, @Valid @RequestBody VehicleDTO vehicleDTO) {
        VehicleDTO result = vehicleService.updateVehicle(id, vehicleDTO);
        return Result.success("更新成功", result);
    }

    /**
     * 删除车辆
     *
     * @param id 车辆ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return Result.success("删除成功");
    }

    /**
     * 根据ID查询车辆
     *
     * @param id 车辆ID
     * @return 车辆信息
     */
    @GetMapping("/{id}")
    public Result<VehicleDTO> getVehicleById(@PathVariable Long id) {
        VehicleDTO result = vehicleService.getVehicleById(id);
        return Result.success(result);
    }

    /**
     * 分页查询车辆列表
     *
     * @param plateNumber 车牌号
     * @param status      状态
     * @param pageNum     页码
     * @param pageSize    每页大小
     * @return 车辆分页列表
     */
    @GetMapping
    public Result<PageResult<VehicleDTO>> getVehicleList(
            @RequestParam(required = false) String plateNumber,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        PageResult<VehicleDTO> result = vehicleService.getVehicleList(plateNumber, status, pageNum, pageSize);
        return Result.success(result);
    }

    /**
     * 获取可用车辆列表
     *
     * @return 可用车辆列表
     */
    @GetMapping("/available")
    public Result<List<VehicleDTO>> getAvailableVehicles() {
        List<VehicleDTO> result = vehicleService.getAvailableVehicles();
        return Result.success(result);
    }
}

