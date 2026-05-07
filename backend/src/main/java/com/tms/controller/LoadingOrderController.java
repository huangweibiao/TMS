package com.tms.controller;

import com.tms.common.PageResult;
import com.tms.common.Result;
import com.tms.dto.LoadingOrderDTO;
import com.tms.entity.LoadingOrder;
import com.tms.service.LoadingOrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 装货单Controller
 *
 * @author TMS Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/loading-order")
public class LoadingOrderController {

    private final LoadingOrderService loadingOrderService;

    @Autowired
    public LoadingOrderController(LoadingOrderService loadingOrderService) {
        this.loadingOrderService = loadingOrderService;
    }

    /**
     * 创建装货单
     *
     * @param dto 装货单DTO
     * @return 创建结果
     */
    @PostMapping
    public Result<LoadingOrderDTO> createLoadingOrder(@Valid @RequestBody LoadingOrderDTO dto) {
        LoadingOrder loadingOrder = loadingOrderService.createLoadingOrder(dto);
        return Result.success(convertToDTO(loadingOrder));
    }

    /**
     * 更新装货单
     *
     * @param id  装货单ID
     * @param dto 装货单DTO
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public Result<LoadingOrderDTO> updateLoadingOrder(@PathVariable Long id,
                                                     @Valid @RequestBody LoadingOrderDTO dto) {
        LoadingOrder loadingOrder = loadingOrderService.updateLoadingOrder(id, dto);
        return Result.success(convertToDTO(loadingOrder));
    }

    /**
     * 删除装货单
     *
     * @param id 装货单ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteLoadingOrder(@PathVariable Long id) {
        loadingOrderService.deleteLoadingOrder(id);
        return Result.success();
    }

    /**
     * 根据ID查询装货单
     *
     * @param id 装货单ID
     * @return 装货单信息
     */
    @GetMapping("/{id}")
    public Result<LoadingOrderDTO> getLoadingOrderById(@PathVariable Long id) {
        return loadingOrderService.findById(id)
                .map(this::convertToDTO)
                .map(Result::success)
                .orElse(Result.error(404, "装货单不存在"));
    }

    /**
     * 根据装货单号查询
     *
     * @param loadingNo 装货单号
     * @return 装货单信息
     */
    @GetMapping("/by-no/{loadingNo}")
    public Result<LoadingOrderDTO> getLoadingOrderByNo(@PathVariable String loadingNo) {
        return loadingOrderService.findByLoadingNo(loadingNo)
                .map(this::convertToDTO)
                .map(Result::success)
                .orElse(Result.error(404, "装货单不存在"));
    }

    /**
     * 根据调度单ID查询装货单列表
     *
     * @param dispatchId 调度单ID
     * @return 装货单列表
     */
    @GetMapping("/by-dispatch/{dispatchId}")
    public Result<List<LoadingOrderDTO>> getLoadingOrdersByDispatch(@PathVariable Long dispatchId) {
        List<LoadingOrderDTO> list = loadingOrderService.findByDispatchId(dispatchId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return Result.success(list);
    }

    /**
     * 分页查询装货单列表
     *
     * @param loadingNo 装货单号（模糊查询）
     * @param status    状态
     * @param pageNum   页码
     * @param pageSize  每页大小
     * @return 分页结果
     */
    @GetMapping("/list")
    public Result<PageResult<LoadingOrderDTO>> getLoadingOrderList(
            @RequestParam(required = false) String loadingNo,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<LoadingOrder> page = loadingOrderService.findLoadingOrders(loadingNo, status, pageable);

        List<LoadingOrderDTO> list = page.getContent()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return Result.success(new PageResult<>(pageNum, pageSize, page.getTotalElements(), list));
    }

    /**
     * 开始装货
     *
     * @param id             装货单ID
     * @param authentication 当前用户认证信息
     * @return 操作结果
     */
    @PostMapping("/{id}/start")
    public Result<LoadingOrderDTO> startLoading(@PathVariable Long id,
                                                Authentication authentication) {
        String operator = authentication.getName();
        LoadingOrder loadingOrder = loadingOrderService.startLoading(id, operator);
        return Result.success(convertToDTO(loadingOrder));
    }

    /**
     * 完成装货
     *
     * @param id             装货单ID
     * @param authentication 当前用户认证信息
     * @return 操作结果
     */
    @PostMapping("/{id}/complete")
    public Result<LoadingOrderDTO> completeLoading(@PathVariable Long id,
                                                    Authentication authentication) {
        String operator = authentication.getName();
        LoadingOrder loadingOrder = loadingOrderService.completeLoading(id, operator);
        return Result.success(convertToDTO(loadingOrder));
    }

    /**
     * 生成装货单号
     *
     * @return 装货单号
     */
    @GetMapping("/generate-no")
    public Result<Map<String, String>> generateLoadingNo() {
        String loadingNo = loadingOrderService.generateLoadingNo();
        Map<String, String> result = new HashMap<>();
        result.put("loadingNo", loadingNo);
        return Result.success(result);
    }

    /**
     * 将实体转换为DTO
     *
     * @param entity 装货单实体
     * @return 装货单DTO
     */
    private LoadingOrderDTO convertToDTO(LoadingOrder entity) {
        LoadingOrderDTO dto = new LoadingOrderDTO();
        dto.setId(entity.getId());
        dto.setLoadingNo(entity.getLoadingNo());
        dto.setDispatchId(entity.getDispatchId());
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setLoadingTime(entity.getLoadingTime());
        dto.setOperator(entity.getOperator());
        dto.setStatus(entity.getStatus());
        dto.setRemark(entity.getRemark());
        dto.setCreateTime(entity.getCreateTime());
        dto.setUpdateTime(entity.getUpdateTime());
        return dto;
    }
}
