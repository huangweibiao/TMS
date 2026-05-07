package com.tms.controller;

import com.tms.common.PageResult;
import com.tms.common.Result;
import com.tms.dto.SettlementDTO;
import com.tms.entity.Settlement;
import com.tms.service.SettlementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 结算单Controller
 *
 * @author TMS Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/settlement")
public class SettlementController {

    private final SettlementService settlementService;

    @Autowired
    public SettlementController(SettlementService settlementService) {
        this.settlementService = settlementService;
    }

    /**
     * 创建结算单
     *
     * @param dto 结算单DTO
     * @return 创建结果
     */
    @PostMapping
    public Result Result<SettlementDTO> createSettlement(@Valid @RequestBody SettlementDTO dto) {
        Settlement settlement = settlementService.createSettlement(dto);
        return Result.success(convertToDTO(settlement));
    }

    /**
     * 更新结算单
     *
     * @param id  结算单ID
     * @param dto 结算单DTO
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public Result Result<SettlementDTO> updateSettlement(@PathVariable Long id,
                                                  @Valid @RequestBody SettlementDTO dto) {
        Settlement settlement = settlementService.updateSettlement(id, dto);
        return Result.success(convertToDTO(settlement));
    }

    /**
     * 删除结算单
     *
     * @param id 结算单ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteSettlement(@PathVariable Long id) {
        settlementService.deleteSettlement(id);
        return Result.success();
    }

    /**
     * 根据ID查询结算单
     *
     * @param id 结算单ID
     * @return 结算单信息
     */
    @GetMapping("/{id}")
    public Result Result<SettlementDTO> getSettlementById(@PathVariable Long id) {
        return settlementService.findById(id)
                .map(this::convertToDTO)
                .map(Result::success)
                .orElse(Result.error(404, "结算单不存在"));
    }

    /**
     * 根据结算单号查询
     *
     * @param settlementNo 结算单号
     * @return 结算单信息
     */
    @GetMapping("/by-no/{settlementNo}")
    public Result Result<SettlementDTO> getSettlementByNo(@PathVariable String settlementNo) {
        return settlementService.findBySettlementNo(settlementNo)
                .map(this::convertToDTO)
                .map(Result::success)
                .orElse(Result.error(404, "结算单不存在"));
    }

    /**
     * 分页查询结算单列表
     *
     * @param partyType 结算方类型
     * @param partyId   结算方ID
     * @param status    状态
     * @param pageNum   页码
     * @param pageSize  每页大小
     * @return 分页结果
     */
    @GetMapping("/list")
    public Result Result<PageResultResult<SettlementDTO>> getSettlementList(
            @RequestParam(required = false) Integer partyType,
            @RequestParam(required = false) Long partyId,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page Page<Settlement> page = settlementService.findSettlements(partyType, partyId, status, pageable);

        List List<SettlementDTO> list = page.getContent()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return Result.success(new PageResult<>(pageNum, pageSize, page.getTotalElements(), list));
    }

    /**
     * 确认结算单
     *
     * @param id 结算单ID
     * @return 操作结果
     */
    @PostMapping("/{id}/confirm")
    public Result Result<SettlementDTO> confirmSettlement(@PathVariable Long id) {
        Settlement settlement = settlementService.confirmSettlement(id);
        return Result.success(convertToDTO(settlement));
    }

    /**
     * 付款
     *
     * @param id     结算单ID
     * @param params 包含amount的请求参数
     * @return 操作结果
     */
    @PostMapping("/{id}/payment")
    public Result Result<SettlementDTO> makePayment(@PathVariable Long id,
                                             @RequestBody Map<String, BigDecimal> params) {
        BigDecimal amount = params.get("amount");
        Settlement settlement = settlementService.makePayment(id, amount);
        return Result.success(convertToDTO(settlement));
    }

    /**
     * 取消结算单
     *
     * @param id     结算单ID
     * @param params 包含reason的请求参数
     * @return 操作结果
     */
    @PostMapping("/{id}/cancel")
    public Result Result<SettlementDTO> cancelSettlement(@PathVariable Long id,
                                                  @RequestBody(required = false) Map<String, String> params) {
        String reason = params != null ? params.get("reason") : null;
        Settlement settlement = settlementService.cancelSettlement(id, reason);
        return Result.success(convertToDTO(settlement));
    }

    /**
     * 生成结算单号
     *
     * @return 结算单号
     */
    @GetMapping("/generate-no")
    public Result<Map<String, String>> generateSettlementNo() {
        String settlementNo = settlementService.generateSettlementNo();
        Map<String, String> result = new HashMap<>();
        result.put("settlementNo", settlementNo);
        return Result.success(result);
    }

    /**
     * 将实体转换为DTO
     *
     * @param entity 结算单实体
     * @return 结算单DTO
     */
    private SettlementDTO convertToDTO(Settlement entity) {
        SettlementDTO dto = new SettlementDTO();
        dto.setId(entity.getId());
        dto.setSettlementNo(entity.getSettlementNo());
        dto.setPartyType(entity.getPartyType());
        dto.setPartyId(entity.getPartyId());
        dto.setSettlementStart(entity.getSettlementStart());
        dto.setSettlementEnd(entity.getSettlementEnd());
        dto.setTotalAmount(entity.getTotalAmount());
        dto.setPaidAmount(entity.getPaidAmount());
        dto.setUnpaidAmount(entity.getUnpaidAmount());
        dto.setStatus(entity.getStatus());
        dto.setConfirmTime(entity.getConfirmTime());
        dto.setPaymentTime(entity.getPaymentTime());
        dto.setInvoiceStatus(entity.getInvoiceStatus());
        dto.setRemark(entity.getRemark());
        dto.setCreateBy(entity.getCreateBy());
        dto.setCreateTime(entity.getCreateTime());
        dto.setUpdateTime(entity.getUpdateTime());
        return dto;
    }
}
