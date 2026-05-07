package com.tms.controller;

import com.tms.common.Result;
import com.tms.common.PageResult;
import com.tms.dto.DriverDTO;
import com.tms.service.DriverService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 司机管理控制器
 *
 * @author TMS Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/drivers")
public class DriverController {

    private final DriverService driverService;

    @Autowired
    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    /**
     * 创建司机
     *
     * @param driverDTO 司机信息
     * @return 创建结果
     */
    @PostMapping
    public Result<DriverDTO> createDriver(@Valid @RequestBody DriverDTO driverDTO) {
        DriverDTO result = driverService.createDriver(driverDTO);
        return Result.success("创建成功", result);
    }

    /**
     * 更新司机
     *
     * @param id        司机ID
     * @param driverDTO 司机信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public Result<DriverDTO> updateDriver(@PathVariable Long id, @Valid @RequestBody DriverDTO driverDTO) {
        DriverDTO result = driverService.updateDriver(id, driverDTO);
        return Result.success("更新成功", result);
    }

    /**
     * 删除司机
     *
     * @param id 司机ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteDriver(@PathVariable Long id) {
        driverService.deleteDriver(id);
        return Result.success("删除成功");
    }

    /**
     * 根据ID查询司机
     *
     * @param id 司机ID
     * @return 司机信息
     */
    @GetMapping("/{id}")
    public Result<DriverDTO> getDriverById(@PathVariable Long id) {
        DriverDTO result = driverService.getDriverById(id);
        return Result.success(result);
    }

    /**
     * 分页查询司机列表
     *
     * @param driverName 司机姓名
     * @param status     状态
     * @param pageNum    页码
     * @param pageSize   每页大小
     * @return 司机分页列表
     */
    @GetMapping
    public Result<PageResult<DriverDTO>> getDriverList(
            @RequestParam(required = false) String driverName,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        PageResult<DriverDTO> result = driverService.getDriverList(driverName, status, pageNum, pageSize);
        return Result.success(result);
    }

    /**
     * 获取可用司机列表
     *
     * @return 可用司机列表
     */
    @GetMapping("/available")
    public Result<List<DriverDTO>> getAvailableDrivers() {
        List<DriverDTO> result = driverService.getAvailableDrivers();
        return Result.success(result);
    }
}

