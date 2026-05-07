package com.tms.service.impl;

import com.tms.dto.WaybillDTO;
import com.tms.dto.WaybillDetailDTO;
import com.tms.common.PageResult;
import com.tms.entity.Waybill;
import com.tms.entity.WaybillDetail;
import com.tms.exception.BusinessException;
import com.tms.repository.CustomerRepository;
import com.tms.repository.WaybillDetailRepository;
import com.tms.repository.WaybillRepository;
import com.tms.service.WaybillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 运单服务实现类
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Service
public class WaybillServiceImpl implements WaybillService {

    private final WaybillRepository waybillRepository;
    private final WaybillDetailRepository waybillDetailRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public WaybillServiceImpl(WaybillRepository waybillRepository,
                              WaybillDetailRepository waybillDetailRepository,
                              CustomerRepository customerRepository) {
        this.waybillRepository = waybillRepository;
        this.waybillDetailRepository = waybillDetailRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public WaybillDTO createWaybill(WaybillDTO waybillDTO) {
        // 生成运单号
        String waybillNo = generateWaybillNo();
        waybillDTO.setWaybillNo(waybillNo);

        // 计算总重量、总体积、总件数
        calculateTotals(waybillDTO);

        // 保存运单主表
        Waybill waybill = convertToEntity(waybillDTO);
        waybill.setWaybillStatus(1); // 待调度
        Waybill savedWaybill = waybillRepository.save(waybill);

        // 保存运单明细
        if (waybillDTO.getDetails() != null && !waybillDTO.getDetails().isEmpty()) {
            List List<WaybillDetail> details = waybillDTO.getDetails().stream()
                    .map(detailDTO -> {
                        WaybillDetail detail = convertDetailToEntity(detailDTO);
                        detail.setWaybillId(savedWaybill.getId());
                        return detail;
                    })
                    .collect(Collectors.toList());
            waybillDetailRepository.saveAll(details);
        }

        return getWaybillById(savedWaybill.getId());
    }

    @Override
    @Transactional
    public WaybillDTO updateWaybill(Long id, WaybillDTO waybillDTO) {
        Waybill waybill = waybillRepository.findById(id)
                .orElseThrow(() -> new BusinessException("运单不存在"));

        // 只有待调度状态的运单可以修改
        if (waybill.getWaybillStatus() != 1) {
            throw new BusinessException("只有待调度状态的运单可以修改");
        }

        // 重新计算总重量、总体积、总件数
        calculateTotals(waybillDTO);

        // 更新运单主表
        waybill.setCustomerId(waybillDTO.getCustomerId());
        waybill.setCarrierId(waybillDTO.getCarrierId());
        waybill.setProductType(waybillDTO.getProductType());
        waybill.setTotalWeight(waybillDTO.getTotalWeight());
        waybill.setTotalVolume(waybillDTO.getTotalVolume());
        waybill.setTotalQuantity(waybillDTO.getTotalQuantity());
        waybill.setGoodsValue(waybillDTO.getGoodsValue());
        waybill.setIsHazardous(waybillDTO.getIsHazardous());
        waybill.setIsFragile(waybillDTO.getIsFragile());
        waybill.setNeedTemperature(waybillDTO.getNeedTemperature());
        waybill.setMinTemp(waybillDTO.getMinTemp());
        waybill.setMaxTemp(waybillDTO.getMaxTemp());
        waybill.setShipperName(waybillDTO.getShipperName());
        waybill.setShipperPhone(waybillDTO.getShipperPhone());
        waybill.setShipperAddress(waybillDTO.getShipperAddress());
        waybill.setConsigneeName(waybillDTO.getConsigneeName());
        waybill.setConsigneePhone(waybillDTO.getConsigneePhone());
        waybill.setConsigneeAddress(waybillDTO.getConsigneeAddress());
        waybill.setExpectPickupTime(waybillDTO.getExpectPickupTime());
        waybill.setExpectDeliveryTime(waybillDTO.getExpectDeliveryTime());
        waybill.setRemark(waybillDTO.getRemark());

        Waybill updatedWaybill = waybillRepository.save(waybill);

        // 删除原有明细，保存新明细
        waybillDetailRepository.deleteByWaybillId(id);
        if (waybillDTO.getDetails() != null && !waybillDTO.getDetails().isEmpty()) {
            List List<WaybillDetail> details = waybillDTO.getDetails().stream()
                    .map(detailDTO -> {
                        WaybillDetail detail = convertDetailToEntity(detailDTO);
                        detail.setWaybillId(updatedWaybill.getId());
                        return detail;
                    })
                    .collect(Collectors.toList());
            waybillDetailRepository.saveAll(details);
        }

        return getWaybillById(updatedWaybill.getId());
    }

    @Override
    @Transactional
    public void deleteWaybill(Long id) {
        Waybill waybill = waybillRepository.findById(id)
                .orElseThrow(() -> new BusinessException("运单不存在"));

        // 只有待调度状态的运单可以删除
        if (waybill.getWaybillStatus() != 1) {
            throw new BusinessException("只有待调度状态的运单可以删除");
        }

        // 删除明细
        waybillDetailRepository.deleteByWaybillId(id);
        // 删除主表
        waybillRepository.deleteById(id);
    }

    @Override
    public WaybillDTO getWaybillById(Long id) {
        Waybill waybill = waybillRepository.findById(id)
                .orElseThrow(() -> new BusinessException("运单不存在"));
        return convertToDTO(waybill);
    }

    @Override
    public WaybillDTO getWaybillByNo(String waybillNo) {
        Waybill waybill = waybillRepository.findByWaybillNo(waybillNo)
                .orElseThrow(() -> new BusinessException("运单不存在"));
        return convertToDTO(waybill);
    }

    @Override
    public PageResultResult<WaybillDTO> getWaybillList(String waybillNo, Integer status, int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);

        Page Page<Waybill> page;
        if (waybillNo != null && !waybillNo.isEmpty()) {
            // 根据运单号模糊查询
            page = waybillRepository.findByWaybillNoContaining(waybillNo, pageable);
        } else if (status != null) {
            // 根据状态查询
            page = waybillRepository.findByWaybillStatus(status, pageable);
        } else {
            // 查询全部
            page = waybillRepository.findAll(pageable);
        }

        List List<WaybillDTO> list = page.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new PageResult<>(pageNum, pageSize, page.getTotalElements(), list);
    }

    @Override
    @Transactional
    public void updateWaybillStatus(Long id, Integer status, String remark) {
        Waybill waybill = waybillRepository.findById(id)
                .orElseThrow(() -> new BusinessException("运单不存在"));

        // 状态流转验证
        validateStatusTransition(waybill.getWaybillStatus(), status);

        waybill.setWaybillStatus(status);
        if (remark != null && !remark.isEmpty()) {
            waybill.setRemark(waybill.getRemark() + "; " + remark);
        }

        // 根据状态更新相应时间
        if (status == 3) { // 提货中
            waybill.setActualPickupTime(LocalDateTime.now());
        } else if (status == 5) { // 签收
            waybill.setActualDeliveryTime(LocalDateTime.now());
        }

        waybillRepository.save(waybill);
    }

    @Override
    @Transactional
    public void cancelWaybill(Long id, String remark) {
        Waybill waybill = waybillRepository.findById(id)
                .orElseThrow(() -> new BusinessException("运单不存在"));

        // 只有待调度或已调度的运单可以取消
        if (waybill.getWaybillStatus() != 1 && waybill.getWaybillStatus() != 2) {
            throw new BusinessException("当前状态的运单不能取消");
        }

        waybill.setWaybillStatus(7); // 取消
        if (remark != null && !remark.isEmpty()) {
            waybill.setRemark(waybill.getRemark() + "; 取消原因: " + remark);
        }

        waybillRepository.save(waybill);
    }

    /**
     * 生成运单号
     * 格式：WB + 年月日 + 4位序号
     *
     * @return 运单号
     */
    private String generateWaybillNo() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String prefix = "WB" + dateStr;

        // 查询当天最大运单号
        String maxWaybillNo = waybillRepository.findMaxWaybillNoByPrefix(prefix);
        int seq = 1;
        if (maxWaybillNo != null && maxWaybillNo.length() >= 4) {
            try {
                seq = Integer.parseInt(maxWaybillNo.substring(maxWaybillNo.length() - 4)) + 1;
            } catch (NumberFormatException e) {
                seq = 1;
            }
        }

        return String.format("%s%04d", prefix, seq);
    }

    /**
     * 计算总重量、总体积、总件数
     *
     * @param waybillDTO 运单DTO
     */
    private void calculateTotals(WaybillDTO waybillDTO) {
        if (waybillDTO.getDetails() == null || waybillDTO.getDetails().isEmpty()) {
            return;
        }

        BigDecimal totalWeight = BigDecimal.ZERO;
        BigDecimal totalVolume = BigDecimal.ZERO;
        int totalQuantity = 0;

        for (WaybillDetailDTO detail : waybillDTO.getDetails()) {
            // 计算明细总价
            if (detail.getUnitPrice() != null && detail.getQuantity() != null) {
                detail.setTotalPrice(detail.getUnitPrice().multiply(new BigDecimal(detail.getQuantity())));
            }

            // 累加总重量
            if (detail.getUnitWeight() != null && detail.getQuantity() != null) {
                totalWeight = totalWeight.add(detail.getUnitWeight().multiply(new BigDecimal(detail.getQuantity())));
            }

            // 累加总体积
            if (detail.getUnitVolume() != null && detail.getQuantity() != null) {
                totalVolume = totalVolume.add(detail.getUnitVolume().multiply(new BigDecimal(detail.getQuantity())));
            }

            // 累加总件数
            if (detail.getQuantity() != null) {
                totalQuantity += detail.getQuantity();
            }
        }

        waybillDTO.setTotalWeight(totalWeight);
        waybillDTO.setTotalVolume(totalVolume);
        waybillDTO.setTotalQuantity(totalQuantity);
    }

    /**
     * 验证状态流转是否合法
     *
     * @param currentStatus 当前状态
     * @param newStatus     新状态
     */
    private void validateStatusTransition(Integer currentStatus, Integer newStatus) {
        // 状态流转规则
        // 1-待调度 -> 2-已调度
        // 2-已调度 -> 3-提货中
        // 3-提货中 -> 4-运输中
        // 4-运输中 -> 5-签收
        // 任意状态 -> 6-异常
        // 1,2 -> 7-取消

        boolean valid = false;

        switch (currentStatus) {
            case 1: // 待调度
                valid = (newStatus == 2 || newStatus == 6 || newStatus == 7);
                break;
            case 2: // 已调度
                valid = (newStatus == 3 || newStatus == 6 || newStatus == 7);
                break;
            case 3: // 提货中
                valid = (newStatus == 4 || newStatus == 6);
                break;
            case 4: // 运输中
                valid = (newStatus == 5 || newStatus == 6);
                break;
            case 5: // 签收
                valid = (newStatus == 6);
                break;
            case 6: // 异常
                valid = (newStatus == 1 || newStatus == 2 || newStatus == 3 || newStatus == 4);
                break;
            default:
                valid = false;
        }

        if (!valid) {
            throw new BusinessException("非法的状态流转: " + currentStatus + " -> " + newStatus);
        }
    }

    /**
     * 将DTO转换为实体
     *
     * @param dto 运单DTO
     * @return 运单实体
     */
    private Waybill convertToEntity(WaybillDTO dto) {
        Waybill entity = new Waybill();
        entity.setId(dto.getId());
        entity.setWaybillNo(dto.getWaybillNo());
        entity.setOrderNo(dto.getOrderNo());
        entity.setCustomerId(dto.getCustomerId());
        entity.setCarrierId(dto.getCarrierId());
        entity.setProductType(dto.getProductType());
        entity.setTotalWeight(dto.getTotalWeight());
        entity.setTotalVolume(dto.getTotalVolume());
        entity.setTotalQuantity(dto.getTotalQuantity());
        entity.setGoodsValue(dto.getGoodsValue());
        entity.setIsHazardous(dto.getIsHazardous());
        entity.setIsFragile(dto.getIsFragile());
        entity.setNeedTemperature(dto.getNeedTemperature());
        entity.setMinTemp(dto.getMinTemp());
        entity.setMaxTemp(dto.getMaxTemp());
        entity.setShipperName(dto.getShipperName());
        entity.setShipperPhone(dto.getShipperPhone());
        entity.setShipperAddress(dto.getShipperAddress());
        entity.setConsigneeName(dto.getConsigneeName());
        entity.setConsigneePhone(dto.getConsigneePhone());
        entity.setConsigneeAddress(dto.getConsigneeAddress());
        entity.setExpectPickupTime(dto.getExpectPickupTime());
        entity.setExpectDeliveryTime(dto.getExpectDeliveryTime());
        entity.setActualPickupTime(dto.getActualPickupTime());
        entity.setActualDeliveryTime(dto.getActualDeliveryTime());
        entity.setWaybillStatus(dto.getWaybillStatus());
        entity.setRemark(dto.getRemark());
        entity.setCreateBy(dto.getCreateBy());
        return entity;
    }

    /**
     * 将实体转换为DTO
     *
     * @param entity 运单实体
     * @return 运单DTO
     */
    private WaybillDTO convertToDTO(Waybill entity) {
        WaybillDTO dto = new WaybillDTO();
        dto.setId(entity.getId());
        dto.setWaybillNo(entity.getWaybillNo());
        dto.setOrderNo(entity.getOrderNo());
        dto.setCustomerId(entity.getCustomerId());
        dto.setCarrierId(entity.getCarrierId());
        dto.setProductType(entity.getProductType());
        dto.setTotalWeight(entity.getTotalWeight());
        dto.setTotalVolume(entity.getTotalVolume());
        dto.setTotalQuantity(entity.getTotalQuantity());
        dto.setGoodsValue(entity.getGoodsValue());
        dto.setIsHazardous(entity.getIsHazardous());
        dto.setIsFragile(entity.getIsFragile());
        dto.setNeedTemperature(entity.getNeedTemperature());
        dto.setMinTemp(entity.getMinTemp());
        dto.setMaxTemp(entity.getMaxTemp());
        dto.setShipperName(entity.getShipperName());
        dto.setShipperPhone(entity.getShipperPhone());
        dto.setShipperAddress(entity.getShipperAddress());
        dto.setConsigneeName(entity.getConsigneeName());
        dto.setConsigneePhone(entity.getConsigneePhone());
        dto.setConsigneeAddress(entity.getConsigneeAddress());
        dto.setExpectPickupTime(entity.getExpectPickupTime());
        dto.setExpectDeliveryTime(entity.getExpectDeliveryTime());
        dto.setActualPickupTime(entity.getActualPickupTime());
        dto.setActualDeliveryTime(entity.getActualDeliveryTime());
        dto.setWaybillStatus(entity.getWaybillStatus());
        dto.setRemark(entity.getRemark());
        dto.setCreateBy(entity.getCreateBy());

        // 查询客户名称
        customerRepository.findById(entity.getCustomerId())
                .ifPresent(customer -> dto.setCustomerName(customer.getCustomerName()));

        // 查询明细
        List List<WaybillDetail> details = waybillDetailRepository.findByWaybillId(entity.getId());
        if (!details.isEmpty()) {
            dto.setDetails(details.stream()
                    .map(this::convertDetailToDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    /**
     * 将明细DTO转换为实体
     *
     * @param dto 明细DTO
     * @return 明细实体
     */
    private WaybillDetail convertDetailToEntity(WaybillDetailDTO dto) {
        WaybillDetail entity = new WaybillDetail();
        entity.setId(dto.getId());
        entity.setWaybillId(dto.getWaybillId());
        entity.setSkuCode(dto.getSkuCode());
        entity.setSkuName(dto.getSkuName());
        entity.setQuantity(dto.getQuantity());
        entity.setUnit(dto.getUnit());
        entity.setUnitWeight(dto.getUnitWeight());
        entity.setUnitVolume(dto.getUnitVolume());
        entity.setUnitPrice(dto.getUnitPrice());
        entity.setTotalPrice(dto.getTotalPrice());
        entity.setRemark(dto.getRemark());
        return entity;
    }

    /**
     * 将明细实体转换为DTO
     *
     * @param entity 明细实体
     * @return 明细DTO
     */
    private WaybillDetailDTO convertDetailToDTO(WaybillDetail entity) {
        WaybillDetailDTO dto = new WaybillDetailDTO();
        dto.setId(entity.getId());
        dto.setWaybillId(entity.getWaybillId());
        dto.setSkuCode(entity.getSkuCode());
        dto.setSkuName(entity.getSkuName());
        dto.setQuantity(entity.getQuantity());
        dto.setUnit(entity.getUnit());
        dto.setUnitWeight(entity.getUnitWeight());
        dto.setUnitVolume(entity.getUnitVolume());
        dto.setUnitPrice(entity.getUnitPrice());
        dto.setTotalPrice(entity.getTotalPrice());
        dto.setRemark(entity.getRemark());
        return dto;
    }
}
