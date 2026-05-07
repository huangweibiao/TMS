package com.tms.controller;

import com.tms.common.Result;
import com.tms.common.PageResult;
import com.tms.dto.WaybillDTO;
import com.tms.service.WaybillService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 运单管理控制器
 *
 * @author TMS Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/waybills")
public class WaybillController {

    private final WaybillService waybillService;

    @Autowired
    public WaybillController(WaybillService waybillService) {
        this.waybillService = waybillService;
    }

    /**
     * 创建运单
     *
     * @param waybillDTO 运单信息
     * @return 创建结果
     */
    @PostMapping
    public Result Result<WaybillDTO> createWaybill(@Valid @RequestBody WaybillDTO waybillDTO) {
        WaybillDTO result = waybillService.createWaybill(waybillDTO);
        return Result.success("创建成功", result);
    }

    /**
     * 更新运单
     *
     * @param id         运单ID
     * @param waybillDTO 运单信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public Result Result<WaybillDTO> updateWaybill(@PathVariable Long id, @Valid @RequestBody WaybillDTO waybillDTO) {
        WaybillDTO result = waybillService.updateWaybill(id, waybillDTO);
        return Result.success("更新成功", result);
    }

    /**
     * 删除运单
     *
     * @param id 运单ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteWaybill(@PathVariable Long id) {
        waybillService.deleteWaybill(id);
        return Result.success("删除成功");
    }

    /**
     * 根据ID查询运单
     *
     * @param id 运单ID
     * @return 运单信息
     */
    @GetMapping("/{id}")
    public Result Result<WaybillDTO> getWaybillById(@PathVariable Long id) {
        WaybillDTO result = waybillService.getWaybillById(id);
        return Result.success(result);
    }

    /**
     * 根据运单号查询运单
     *
     * @param waybillNo 运单号
     * @return 运单信息
     */
    @GetMapping("/no/{waybillNo}")
    public Result Result<WaybillDTO> getWaybillByNo(@PathVariable String waybillNo) {
        WaybillDTO result = waybillService.getWaybillByNo(waybillNo);
        return Result.success(result);
    }

    /**
     * 分页查询运单列表
     *
     * @param waybillNo 运单号
     * @param status    状态
     * @param pageNum   页码
     * @param pageSize  每页大小
     * @return 运单分页列表
     */
    @GetMapping
    public Result Result<PageResultResult<WaybillDTO>> getWaybillList(
            @RequestParam(required = false) String waybillNo,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        PageResultResult<WaybillDTO> result = waybillService.getWaybillList(waybillNo, status, pageNum, pageSize);
        return Result.success(result);
    }

    /**
     * 更新运单状态
     *
     * @param id     运单ID
     * @param status 新状态
     * @param remark 备注
     * @return 更新结果
     */
    @PutMapping("/{id}/status")
    public Result<Void> updateWaybillStatus(
            @PathVariable Long id,
            @RequestParam Integer status,
            @RequestParam(required = false) String remark) {
        waybillService.updateWaybillStatus(id, status, remark);
        return Result.success("状态更新成功");
    }

    /**
     * 取消运单
     *
     * @param id     运单ID
     * @param remark 取消原因
     * @return 取消结果
     */
    @PutMapping("/{id}/cancel")
    public Result<Void> cancelWaybill(
            @PathVariable Long id,
            @RequestParam(required = false) String remark) {
        waybillService.cancelWaybill(id, remark);
        return Result.success("运单已取消");
    }
}
