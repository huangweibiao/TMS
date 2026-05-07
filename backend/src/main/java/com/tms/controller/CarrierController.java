package com.tms.controller;

import com.tms.common.Result;
import com.tms.common.PageResult;
import com.tms.dto.CarrierDTO;
import com.tms.service.CarrierService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 承运商管理控制器
 *
 * @author TMS Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/carriers")
public class CarrierController {

    private final CarrierService carrierService;

    @Autowired
    public CarrierController(CarrierService carrierService) {
        this.carrierService = carrierService;
    }

    /**
     * 创建承运商
     *
     * @param carrierDTO 承运商信息
     * @return 创建结果
     */
    @PostMapping
    public Result Result<CarrierDTO> createCarrier(@Valid @RequestBody CarrierDTO carrierDTO) {
        CarrierDTO result = carrierService.createCarrier(carrierDTO);
        return Result.success("创建成功", result);
    }

    /**
     * 更新承运商
     *
     * @param id         承运商ID
     * @param carrierDTO 承运商信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public Result Result<CarrierDTO> updateCarrier(@PathVariable Long id, @Valid @RequestBody CarrierDTO carrierDTO) {
        CarrierDTO result = carrierService.updateCarrier(id, carrierDTO);
        return Result.success("更新成功", result);
    }

    /**
     * 删除承运商
     *
     * @param id 承运商ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteCarrier(@PathVariable Long id) {
        carrierService.deleteCarrier(id);
        return Result.success("删除成功");
    }

    /**
     * 根据ID查询承运商
     *
     * @param id 承运商ID
     * @return 承运商信息
     */
    @GetMapping("/{id}")
    public Result Result<CarrierDTO> getCarrierById(@PathVariable Long id) {
        CarrierDTO result = carrierService.getCarrierById(id);
        return Result.success(result);
    }

    /**
     * 分页查询承运商列表
     *
     * @param carrierName 承运商名称
     * @param status      状态
     * @param pageNum     页码
     * @param pageSize    每页大小
     * @return 承运商分页列表
     */
    @GetMapping
    public Result Result<PageResultResult<CarrierDTO>> getCarrierList(
            @RequestParam(required = false) String carrierName,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        PageResultResult<CarrierDTO> result = carrierService.getCarrierList(carrierName, status, pageNum, pageSize);
        return Result.success(result);
    }
}
