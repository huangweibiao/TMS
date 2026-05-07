package com.tms.controller;

import com.tms.common.PageResult;
import com.tms.common.Result;
import com.tms.dto.ReceiptDTO;
import com.tms.entity.Receipt;
import com.tms.service.ReceiptService;
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
 * 回单Controller
 *
 * @author TMS Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/receipt")
public class ReceiptController {

    private final ReceiptService receiptService;

    @Autowired
    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    /**
     * 创建回单
     *
     * @param dto 回单DTO
     * @return 创建结果
     */
    @PostMapping
    public Result Result<ReceiptDTO> createReceipt(@Valid @RequestBody ReceiptDTO dto) {
        Receipt receipt = receiptService.createReceipt(dto);
        return Result.success(convertToDTO(receipt));
    }

    /**
     * 更新回单
     *
     * @param id  回单ID
     * @param dto 回单DTO
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public Result Result<ReceiptDTO> updateReceipt(@PathVariable Long id,
                                            @Valid @RequestBody ReceiptDTO dto) {
        Receipt receipt = receiptService.updateReceipt(id, dto);
        return Result.success(convertToDTO(receipt));
    }

    /**
     * 删除回单
     *
     * @param id 回单ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteReceipt(@PathVariable Long id) {
        receiptService.deleteReceipt(id);
        return Result.success();
    }

    /**
     * 根据ID查询回单
     *
     * @param id 回单ID
     * @return 回单信息
     */
    @GetMapping("/{id}")
    public Result Result<ReceiptDTO> getReceiptById(@PathVariable Long id) {
        return receiptService.findById(id)
                .map(this::convertToDTO)
                .map(Result::success)
                .orElse(Result.error(404, "回单不存在"));
    }

    /**
     * 根据回单号查询
     *
     * @param receiptNo 回单号
     * @return 回单信息
     */
    @GetMapping("/by-no/{receiptNo}")
    public Result Result<ReceiptDTO> getReceiptByNo(@PathVariable String receiptNo) {
        return receiptService.findByReceiptNo(receiptNo)
                .map(this::convertToDTO)
                .map(Result::success)
                .orElse(Result.error(404, "回单不存在"));
    }

    /**
     * 根据运单ID查询回单
     *
     * @param waybillId 运单ID
     * @return 回单信息
     */
    @GetMapping("/by-waybill/{waybillId}")
    public Result Result<ReceiptDTO> getReceiptByWaybill(@PathVariable Long waybillId) {
        return receiptService.findByWaybillId(waybillId)
                .map(this::convertToDTO)
                .map(Result::success)
                .orElse(Result.error(404, "回单不存在"));
    }

    /**
     * 分页查询回单列表
     *
     * @param receiptNo 回单号（模糊查询）
     * @param status    状态
     * @param pageNum   页码
     * @param pageSize  每页大小
     * @return 分页结果
     */
    @GetMapping("/list")
    public Result Result<PageResultResult<ReceiptDTO>> getReceiptList(
            @RequestParam(required = false) String receiptNo,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page Page<Receipt> page = receiptService.findReceipts(receiptNo, status, pageable);

        List List<ReceiptDTO> list = page.getContent()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return Result.success(new PageResult<>(pageNum, pageSize, page.getTotalElements(), list));
    }

    /**
     * 回传回单
     *
     * @param id 回单ID
     * @return 操作结果
     */
    @PostMapping("/{id}/return")
    public Result Result<ReceiptDTO> returnReceipt(@PathVariable Long id) {
        Receipt receipt = receiptService.returnReceipt(id);
        return Result.success(convertToDTO(receipt));
    }

    /**
     * 审核回单
     *
     * @param id             回单ID
     * @param authentication 当前用户认证信息
     * @return 操作结果
     */
    @PostMapping("/{id}/audit")
    public Result Result<ReceiptDTO> auditReceipt(@PathVariable Long id,
                                           Authentication authentication) {
        String auditBy = authentication.getName();
        Receipt receipt = receiptService.auditReceipt(id, auditBy);
        return Result.success(convertToDTO(receipt));
    }

    /**
     * 生成回单号
     *
     * @return 回单号
     */
    @GetMapping("/generate-no")
    public Result<Map<String, String>> generateReceiptNo() {
        String receiptNo = receiptService.generateReceiptNo();
        Map<String, String> result = new HashMap<>();
        result.put("receiptNo", receiptNo);
        return Result.success(result);
    }

    /**
     * 将实体转换为DTO
     *
     * @param entity 回单实体
     * @return 回单DTO
     */
    private ReceiptDTO convertToDTO(Receipt entity) {
        ReceiptDTO dto = new ReceiptDTO();
        dto.setId(entity.getId());
        dto.setReceiptNo(entity.getReceiptNo());
        dto.setWaybillId(entity.getWaybillId());
        dto.setReceiptType(entity.getReceiptType());
        dto.setSignerName(entity.getSignerName());
        dto.setSignerPhone(entity.getSignerPhone());
        dto.setSignTime(entity.getSignTime());
        dto.setSignPhotoUrl(entity.getSignPhotoUrl());
        dto.setReceiptFileUrl(entity.getReceiptFileUrl());
        dto.setStatus(entity.getStatus());
        dto.setReturnTime(entity.getReturnTime());
        dto.setAuditTime(entity.getAuditTime());
        dto.setAuditBy(entity.getAuditBy());
        dto.setRemark(entity.getRemark());
        dto.setCreateTime(entity.getCreateTime());
        dto.setUpdateTime(entity.getUpdateTime());
        return dto;
    }
}
