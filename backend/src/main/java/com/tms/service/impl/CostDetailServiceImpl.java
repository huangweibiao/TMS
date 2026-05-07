package com.tms.service.impl;

import com.tms.common.PageResult;
import com.tms.dto.CostCalculateRequest;
import com.tms.dto.CostDetailDTO;
import com.tms.entity.CostDetail;
import com.tms.entity.Waybill;
import com.tms.exception.BusinessException;
import com.tms.repository.CostDetailRepository;
import com.tms.repository.WaybillRepository;
import com.tms.service.CostDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 费用明细服务实现类
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Service
public class CostDetailServiceImpl implements CostDetailService {

    private final CostDetailRepository costDetailRepository;
    private final WaybillRepository waybillRepository;

    @Autowired
    public CostDetailServiceImpl(CostDetailRepository costDetailRepository,
                                 WaybillRepository waybillRepository) {
        this.costDetailRepository = costDetailRepository;
        this.waybillRepository = waybillRepository;
    }

    @Override
    @Transactional
    public List List<CostDetailDTO> calculateCost(CostCalculateRequest request) {
        // 验证运单
        Waybill waybill = waybillRepository.findById(request.getWaybillId())
                .orElseThrow(() -> new BusinessException("运单不存在"));

        List List<CostDetailDTO> result = new ArrayList<>();

        // 计算基础运费
        BigDecimal freightAmount = calculateFreight(waybill, request);
        CostDetailDTO freightCost = new CostDetailDTO();
        freightCost.setWaybillId(waybill.getId());
        freightCost.setWaybillNo(waybill.getWaybillNo());
        freightCost.setCostType(1); // 运费
        freightCost.setCostTypeName("运费");
        freightCost.setAmount(freightAmount);
        freightCost.setCurrency("CNY");
        freightCost.setDirection(1); // 应收
        freightCost.setDirectionName("应收");
        freightCost.setSettlementStatus(1);
        freightCost.setRemark(request.getRemark());
        result.add(freightCost);

        // 保存基础运费
        CostDetail freightEntity = convertToEntity(freightCost);
        costDetailRepository.save(freightEntity);

        // 计算附加费用
        if (request.getAdditionalCost() != null) {
            CostCalculateRequest.AdditionalCost additional = request.getAdditionalCost();

            // 装卸费
            if (additional.getLoadingFee() != null && additional.getLoadingFee().compareTo(BigDecimal.ZERO) > 0) {
                CostDetailDTO loadingCost = createAdditionalCost(waybill, 2, "装卸费",
                        additional.getLoadingFee(), 1, "应收");
                result.add(loadingCost);
                costDetailRepository.save(convertToEntity(loadingCost));
            }

            // 保险费
            if (additional.getInsuranceFee() != null && additional.getInsuranceFee().compareTo(BigDecimal.ZERO) > 0) {
                CostDetailDTO insuranceCost = createAdditionalCost(waybill, 3, "保险费",
                        additional.getInsuranceFee(), 1, "应收");
                result.add(insuranceCost);
                costDetailRepository.save(convertToEntity(insuranceCost));
            }

            // 等待费
            if (additional.getWaitingFee() != null && additional.getWaitingFee().compareTo(BigDecimal.ZERO) > 0) {
                CostDetailDTO waitingCost = createAdditionalCost(waybill, 4, "等待费",
                        additional.getWaitingFee(), 2, "应付");
                result.add(waitingCost);
                costDetailRepository.save(convertToEntity(waitingCost));
            }

            // 高速费
            if (additional.getTollFee() != null && additional.getTollFee().compareTo(BigDecimal.ZERO) > 0) {
                CostDetailDTO tollCost = createAdditionalCost(waybill, 5, "高速费",
                        additional.getTollFee(), 2, "应付");
                result.add(tollCost);
                costDetailRepository.save(convertToEntity(tollCost));
            }

            // 其他费用
            if (additional.getOtherFee() != null && additional.getOtherFee().compareTo(BigDecimal.ZERO) > 0) {
                CostDetailDTO otherCost = createAdditionalCost(waybill, 7, "其他费用",
                        additional.getOtherFee(), 2, "应付");
                result.add(otherCost);
                costDetailRepository.save(convertToEntity(otherCost));
            }
        }

        return result;
    }

    @Override
    public List List<CostDetailDTO> getCostDetailsByWaybillId(Long waybillId) {
        List List<CostDetail> details = costDetailRepository.findByWaybillId(waybillId);
        return details.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PageResultResult<CostDetailDTO> getCostDetailList(String waybillNo, Integer costType, int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);

        Page Page<CostDetail> page;
        if (waybillNo != null && !waybillNo.isEmpty()) {
            // 先查询运单ID
            Waybill waybill = waybillRepository.findByWaybillNo(waybillNo)
                    .orElseThrow(() -> new BusinessException("运单不存在"));
            if (costType != null) {
                List List<CostDetail> list = costDetailRepository.findByWaybillIdAndCostType(waybill.getId(), costType);
                page = new org.springframework.data.domain.PageImpl<>(list, pageable, list.size());
            } else {
                List List<CostDetail> list = costDetailRepository.findByWaybillId(waybill.getId());
                page = new org.springframework.data.domain.PageImpl<>(list, pageable, list.size());
            }
        } else if (costType != null) {
            List List<CostDetail> list = costDetailRepository.findByCostType(costType);
            page = new org.springframework.data.domain.PageImpl<>(list, pageable, list.size());
        } else {
            page = costDetailRepository.findAll(pageable);
        }

        List List<CostDetailDTO> list = page.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new PageResult<>(pageNum, pageSize, page.getTotalElements(), list);
    }

    @Override
    @Transactional
    public CostDetailDTO createCostDetail(CostDetailDTO costDetailDTO) {
        CostDetail entity = convertToEntity(costDetailDTO);
        entity.setSettlementStatus(1); // 未结算
        CostDetail saved = costDetailRepository.save(entity);
        return convertToDTO(saved);
    }

    @Override
    @Transactional
    public CostDetailDTO updateCostDetail(Long id, CostDetailDTO costDetailDTO) {
        CostDetail entity = costDetailRepository.findById(id)
                .orElseThrow(() -> new BusinessException("费用明细不存在"));

        entity.setCostType(costDetailDTO.getCostType());
        entity.setAmount(costDetailDTO.getAmount());
        entity.setDirection(costDetailDTO.getDirection());
        entity.setRemark(costDetailDTO.getRemark());

        CostDetail updated = costDetailRepository.save(entity);
        return convertToDTO(updated);
    }

    @Override
    @Transactional
    public void deleteCostDetail(Long id) {
        if (!costDetailRepository.existsById(id)) {
            throw new BusinessException("费用明细不存在");
        }
        costDetailRepository.deleteById(id);
    }

    @Override
    public CostDetailDTO getCostDetailById(Long id) {
        CostDetail entity = costDetailRepository.findById(id)
                .orElseThrow(() -> new BusinessException("费用明细不存在"));
        return convertToDTO(entity);
    }

    /**
     * 计算运费
     *
     * @param waybill 运单
     * @param request 计算请求
     * @return 运费金额
     */
    private BigDecimal calculateFreight(Waybill waybill, CostCalculateRequest request) {
        BigDecimal amount = BigDecimal.ZERO;

        switch (request.getCalculateType()) {
            case 1: // 按重量计费
                if (waybill.getTotalWeight() != null && request.getUnitPrice() != null) {
                    amount = waybill.getTotalWeight().multiply(request.getUnitPrice());
                }
                break;
            case 2: // 按体积计费
                if (waybill.getTotalVolume() != null && request.getUnitPrice() != null) {
                    amount = waybill.getTotalVolume().multiply(request.getUnitPrice());
                }
                break;
            case 3: // 按里程计费
                if (request.getDistance() != null && request.getUnitPrice() != null) {
                    amount = request.getDistance().multiply(request.getUnitPrice());
                }
                break;
            default:
                throw new BusinessException("不支持的计费方式");
        }

        return amount;
    }

    /**
     * 创建附加费用DTO
     *
     * @param waybill     运单
     * @param costType    费用类型
     * @param costTypeName 费用类型名称
     * @param amount      金额
     * @param direction   方向
     * @param directionName 方向名称
     * @return 费用明细DTO
     */
    private CostDetailDTO createAdditionalCost(Waybill waybill, Integer costType, String costTypeName,
                                               BigDecimal amount, Integer direction, String directionName) {
        CostDetailDTO dto = new CostDetailDTO();
        dto.setWaybillId(waybill.getId());
        dto.setWaybillNo(waybill.getWaybillNo());
        dto.setCostType(costType);
        dto.setCostTypeName(costTypeName);
        dto.setAmount(amount);
        dto.setCurrency("CNY");
        dto.setDirection(direction);
        dto.setDirectionName(directionName);
        dto.setSettlementStatus(1);
        return dto;
    }

    /**
     * 将DTO转换为实体
     *
     * @param dto 费用明细DTO
     * @return 费用明细实体
     */
    private CostDetail convertToEntity(CostDetailDTO dto) {
        CostDetail entity = new CostDetail();
        entity.setId(dto.getId());
        entity.setWaybillId(dto.getWaybillId());
        entity.setCostType(dto.getCostType());
        entity.setAmount(dto.getAmount());
        entity.setCurrency(dto.getCurrency());
        entity.setDirection(dto.getDirection());
        entity.setSettlementStatus(dto.getSettlementStatus());
        entity.setInvoiceNo(dto.getInvoiceNo());
        entity.setSettlementId(dto.getSettlementId());
        entity.setRemark(dto.getRemark());
        return entity;
    }

    /**
     * 将实体转换为DTO
     *
     * @param entity 费用明细实体
     * @return 费用明细DTO
     */
    private CostDetailDTO convertToDTO(CostDetail entity) {
        CostDetailDTO dto = new CostDetailDTO();
        dto.setId(entity.getId());
        dto.setWaybillId(entity.getWaybillId());
        dto.setCostType(entity.getCostType());
        dto.setAmount(entity.getAmount());
        dto.setCurrency(entity.getCurrency());
        dto.setDirection(entity.getDirection());
        dto.setSettlementStatus(entity.getSettlementStatus());
        dto.setInvoiceNo(entity.getInvoiceNo());
        dto.setSettlementId(entity.getSettlementId());
        dto.setRemark(entity.getRemark());

        // 设置费用类型名称
        dto.setCostTypeName(getCostTypeName(entity.getCostType()));

        // 设置方向名称
        dto.setDirectionName(entity.getDirection() == 1 ? "应收" : "应付");

        // 查询运单号
        waybillRepository.findById(entity.getWaybillId())
                .ifPresent(waybill -> dto.setWaybillNo(waybill.getWaybillNo()));

        return dto;
    }

    /**
     * 获取费用类型名称
     *
     * @param costType 费用类型
     * @return 费用类型名称
     */
    private String getCostTypeName(Integer costType) {
        switch (costType) {
            case 1:
                return "运费";
            case 2:
                return "装卸费";
            case 3:
                return "保险费";
            case 4:
                return "等待费";
            case 5:
                return "高速费";
            case 6:
                return "油费";
            case 7:
                return "罚款";
            default:
                return "其他";
        }
    }
}
