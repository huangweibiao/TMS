package com.tms.controller;

import com.tms.common.Result;
import com.tms.common.PageResult;
import com.tms.dto.DispatchDTO;
import com.tms.service.DispatchService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 调度管理控制器
 *
 * @author TMS Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/dispatches")
public class DispatchController {

    private final DispatchService dispatchService;

    @Autowired
    public DispatchController(DispatchService dispatchService) {
        this.dispatchService = dispatchService;
    }

    /**
     * 智能派车
     *
     * @param waybillId 运单ID
     * @param vehicleId 车辆ID
     * @param driverId  司机ID
     * @param strategy  派车策略：1-距离优先, 2-成本优先, 3-时效优先
     * @return 调度单信息
     */
    @PostMapping("/assign")
    public Result Result<DispatchDTO> assignDispatch(
            @RequestParam Long waybillId,
            @RequestParam Long vehicleId,
            @RequestParam Long driverId,
            @RequestParam(defaultValue = "1") Integer strategy) {
        DispatchDTO result = dispatchService.assignDispatch(waybillId, vehicleId, driverId, strategy);
        return Result.success("派车成功", result);
    }

    /**
     * 手动创建调度单
     *
     * @param dispatchDTO 调度单信息
     * @return 创建结果
     */
    @PostMapping
    public Result Result<DispatchDTO> createDispatch(@Valid @RequestBody DispatchDTO dispatchDTO) {
        DispatchDTO result = dispatchService.createDispatch(dispatchDTO);
        return Result.success("创建成功", result);
    }

    /**
     * 根据ID查询调度单
     *
     * @param id 调度单ID
     * @return 调度单信息
     */
    @GetMapping("/{id}")
    public Result Result<DispatchDTO> getDispatchById(@PathVariable Long id) {
        DispatchDTO result = dispatchService.getDispatchById(id);
        return Result.success(result);
    }

    /**
     * 根据运单ID查询调度单
     *
     * @param waybillId 运单ID
     * @return 调度单信息
     */
    @GetMapping("/waybill/{waybillId}")
    public Result Result<DispatchDTO> getDispatchByWaybillId(@PathVariable Long waybillId) {
        DispatchDTO result = dispatchService.getDispatchByWaybillId(waybillId);
        return Result.success(result);
    }

    /**
     * 分页查询调度单列表
     *
     * @param dispatchNo 调度单号
     * @param status     状态
     * @param pageNum    页码
     * @param pageSize   每页大小
     * @return 调度单分页列表
     */
    @GetMapping
    public Result Result<PageResultResult<DispatchDTO>> getDispatchList(
            @RequestParam(required = false) String dispatchNo,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        PageResultResult<DispatchDTO> result = dispatchService.getDispatchList(dispatchNo, status, pageNum, pageSize);
        return Result.success(result);
    }

    /**
     * 确认发车
     *
     * @param id 调度单ID
     * @return 操作结果
     */
    @PutMapping("/{id}/start")
    public Result<Void> startDispatch(@PathVariable Long id) {
        dispatchService.startDispatch(id);
        return Result.success("发车成功");
    }

    /**
     * 完成调度
     *
     * @param id 调度单ID
     * @return 操作结果
     */
    @PutMapping("/{id}/complete")
    public Result<Void> completeDispatch(@PathVariable Long id) {
        dispatchService.completeDispatch(id);
        return Result.success("调度完成");
    }

    /**
     * 取消调度
     *
     * @param id     调度单ID
     * @param reason 取消原因
     * @return 操作结果
     */
    @PutMapping("/{id}/cancel")
    public Result<Void> cancelDispatch(
            @PathVariable Long id,
            @RequestParam(required = false) String reason) {
        dispatchService.cancelDispatch(id, reason);
        return Result.success("调度已取消");
    }
}
