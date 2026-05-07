package com.tms.controller;

import com.tms.common.Result;
import com.tms.common.PageResult;
import com.tms.dto.CostCalculateRequest;
import com.tms.dto.CostDetailDTO;
import com.tms.service.CostDetailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 费用管理控制器
 *
 * @author TMS Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/costs")
public class CostDetailController {

    private final CostDetailService costDetailService;

    @Autowired
    public CostDetailController(CostDetailService costDetailService) {
        this.costDetailService = costDetailService;
    }

    /**
     * 计算运费
     *
     * @param request 计算请求
     * @return 费用明细列表
     */
    @PostMapping("/calculate")
    public Result<List<List<CostDetailDTO>> calculateCost(@Valid @RequestBody CostCalculateRequest request) {
        List List<CostDetailDTO> result = costDetailService.calculateCost(request);
        return Result.success("计算成功", result);
    }

    /**
     * 根据运单ID查询费用明细
     *
     * @param waybillId 运单ID
     * @return 费用明细列表
     */
    @GetMapping("/waybill/{waybillId}")
    public Result<List<List<CostDetailDTO>> getCostDetailsByWaybillId(@PathVariable Long waybillId) {
        List List<CostDetailDTO> result = costDetailService.getCostDetailsByWaybillId(waybillId);
        return Result.success(result);
    }

    /**
     * 分页查询费用明细
     *
     * @param waybillNo 运单号
     * @param costType  费用类型
     * @param pageNum   页码
     * @param pageSize  每页大小
     * @return 费用明细分页列表
     */
    @GetMapping
    public Result Result<PageResultResult<CostDetailDTO>> getCostDetailList(
            @RequestParam(required = false) String waybillNo,
            @RequestParam(required = false) Integer costType,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        PageResultResult<CostDetailDTO> result = costDetailService.getCostDetailList(waybillNo, costType, pageNum, pageSize);
        return Result.success(result);
    }

    /**
     * 创建费用明细
     *
     * @param costDetailDTO 费用明细信息
     * @return 创建结果
     */
    @PostMapping
    public Result Result<CostDetailDTO> createCostDetail(@Valid @RequestBody CostDetailDTO costDetailDTO) {
        CostDetailDTO result = costDetailService.createCostDetail(costDetailDTO);
        return Result.success("创建成功", result);
    }

    /**
     * 更新费用明细
     *
     * @param id            费用明细ID
     * @param costDetailDTO 费用明细信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public Result Result<CostDetailDTO> updateCostDetail(@PathVariable Long id, @Valid @RequestBody CostDetailDTO costDetailDTO) {
        CostDetailDTO result = costDetailService.updateCostDetail(id, costDetailDTO);
        return Result.success("更新成功", result);
    }

    /**
     * 删除费用明细
     *
     * @param id 费用明细ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteCostDetail(@PathVariable Long id) {
        costDetailService.deleteCostDetail(id);
        return Result.success("删除成功");
    }

    /**
     * 根据ID查询费用明细
     *
     * @param id 费用明细ID
     * @return 费用明细
     */
    @GetMapping("/{id}")
    public Result Result<CostDetailDTO> getCostDetailById(@PathVariable Long id) {
        CostDetailDTO result = costDetailService.getCostDetailById(id);
        return Result.success(result);
    }
}
