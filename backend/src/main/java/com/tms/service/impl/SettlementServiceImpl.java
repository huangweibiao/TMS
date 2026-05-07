package com.tms.service.impl;

import com.tms.dto.SettlementDTO;
import com.tms.entity.Carrier;
import com.tms.entity.Customer;
import com.tms.entity.Settlement;
import com.tms.exception.BusinessException;
import com.tms.repository.CarrierRepository;
import com.tms.repository.CustomerRepository;
import com.tms.repository.SettlementRepository;
import com.tms.service.SettlementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 结算单Service实现类
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Service
public class SettlementServiceImpl implements SettlementService {

    private final SettlementRepository settlementRepository;
    private final CustomerRepository customerRepository;
    private final CarrierRepository carrierRepository;

    @Autowired
    public SettlementServiceImpl(SettlementRepository settlementRepository,
                                 CustomerRepository customerRepository,
                                 CarrierRepository carrierRepository) {
        this.settlementRepository = settlementRepository;
        this.customerRepository = customerRepository;
        this.carrierRepository = carrierRepository;
    }

    @Override
    @Transactional
    @CachePut(value = "settlement", key = "#result.id")
    public Settlement createSettlement(SettlementDTO dto) {
        // 验证结算方
        validateParty(dto.getPartyType(), dto.getPartyId());

        // 生成结算单号
        String settlementNo = generateSettlementNo();

        Settlement settlement = new Settlement();
        settlement.setSettlementNo(settlementNo);
        settlement.setPartyType(dto.getPartyType());
        settlement.setPartyId(dto.getPartyId());
        settlement.setSettlementStart(dto.getSettlementStart());
        settlement.setSettlementEnd(dto.getSettlementEnd());
        settlement.setTotalAmount(dto.getTotalAmount() != null ? dto.getTotalAmount() : BigDecimal.ZERO);
        settlement.setPaidAmount(BigDecimal.ZERO);
        settlement.setUnpaidAmount(settlement.getTotalAmount());
        settlement.setStatus(1); // 待确认
        settlement.setInvoiceStatus(0); // 未开票
        settlement.setRemark(dto.getRemark());
        settlement.setCreateBy(dto.getCreateBy());

        return settlementRepository.save(settlement);
    }

    @Override
    @Transactional
    @CachePut(value = "settlement", key = "#id")
    public Settlement updateSettlement(Long id, SettlementDTO dto) {
        Settlement settlement = settlementRepository.findById(id)
                .orElseThrow(() -> new BusinessException("结算单不存在"));

        // 只有待确认状态可以修改
        if (settlement.getStatus() != 1) {
            throw new BusinessException("只有待确认状态的结算单可以修改");
        }

        // 验证结算方
        validateParty(dto.getPartyType(), dto.getPartyId());

        settlement.setPartyType(dto.getPartyType());
        settlement.setPartyId(dto.getPartyId());
        settlement.setSettlementStart(dto.getSettlementStart());
        settlement.setSettlementEnd(dto.getSettlementEnd());
        
        if (dto.getTotalAmount() != null) {
            settlement.setTotalAmount(dto.getTotalAmount());
            // 重新计算未付金额
            settlement.setUnpaidAmount(dto.getTotalAmount().subtract(settlement.getPaidAmount()));
        }
        
        settlement.setRemark(dto.getRemark());

        return settlementRepository.save(settlement);
    }

    @Override
    @Transactional
    @CacheEvict(value = "settlement", key = "#id")
    public void deleteSettlement(Long id) {
        Settlement settlement = settlementRepository.findById(id)
                .orElseThrow(() -> new BusinessException("结算单不存在"));

        // 只有待确认或已取消状态可以删除
        if (settlement.getStatus() != 1 && settlement.getStatus() != 5) {
            throw new BusinessException("只有待确认或已取消状态的结算单可以删除");
        }

        settlementRepository.deleteById(id);
    }

    @Override
    @Cacheable(value = "settlement", key = "#id")
    public Optional<Settlement> findById(Long id) {
        return settlementRepository.findById(id);
    }

    @Override
    @Cacheable(value = "settlement", key = "#settlementNo")
    public Optional<Settlement> findBySettlementNo(String settlementNo) {
        return settlementRepository.findBySettlementNo(settlementNo);
    }

    @Override
    public Page<Settlement> findSettlements(Integer partyType, Long partyId, Integer status, Pageable pageable) {
        Specification<Settlement> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (partyType != null) {
                predicates.add(cb.equal(root.get("partyType"), partyType));
            }

            if (partyId != null) {
                predicates.add(cb.equal(root.get("partyId"), partyId));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return settlementRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional
    @CachePut(value = "settlement", key = "#id")
    public Settlement confirmSettlement(Long id) {
        Settlement settlement = settlementRepository.findById(id)
                .orElseThrow(() -> new BusinessException("结算单不存在"));

        if (settlement.getStatus() != 1) {
            throw new BusinessException("只有待确认状态的结算单可以确认");
        }

        settlement.setStatus(2); // 已确认
        settlement.setConfirmTime(LocalDateTime.now());

        return settlementRepository.save(settlement);
    }

    @Override
    @Transactional
    @CachePut(value = "settlement", key = "#id")
    public Settlement makePayment(Long id, BigDecimal amount) {
        Settlement settlement = settlementRepository.findById(id)
                .orElseThrow(() -> new BusinessException("结算单不存在"));

        if (settlement.getStatus() != 2 && settlement.getStatus() != 3) {
            throw new BusinessException("只有已确认或部分付款状态的结算单可以付款");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("付款金额必须大于0");
        }

        if (amount.compareTo(settlement.getUnpaidAmount()) > 0) {
            throw new BusinessException("付款金额不能超过未付金额");
        }

        // 更新已付金额和未付金额
        settlement.setPaidAmount(settlement.getPaidAmount().add(amount));
        settlement.setUnpaidAmount(settlement.getUnpaidAmount().subtract(amount));
        settlement.setPaymentTime(LocalDateTime.now());

        // 更新状态
        if (settlement.getUnpaidAmount().compareTo(BigDecimal.ZERO) == 0) {
            settlement.setStatus(4); // 已结清
        } else {
            settlement.setStatus(3); // 部分付款
        }

        return settlementRepository.save(settlement);
    }

    @Override
    @Transactional
    @CachePut(value = "settlement", key = "#id")
    public Settlement cancelSettlement(Long id, String reason) {
        Settlement settlement = settlementRepository.findById(id)
                .orElseThrow(() -> new BusinessException("结算单不存在"));

        // 只有待确认或已确认状态可以取消
        if (settlement.getStatus() != 1 && settlement.getStatus() != 2) {
            throw new BusinessException("当前状态的结算单不能取消");
        }

        settlement.setStatus(5); // 已取消
        if (StringUtils.hasText(reason)) {
            settlement.setRemark((settlement.getRemark() != null ? settlement.getRemark() + "; " : "") + "取消原因: " + reason);
        }

        return settlementRepository.save(settlement);
    }

    @Override
    public String generateSettlementNo() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String prefix = "ST" + dateStr;

        // 查询当天最大结算单号
        String maxSettlementNo = settlementRepository.findMaxSettlementNoByPrefix(prefix);
        int seq = 1;
        if (maxSettlementNo != null && maxSettlementNo.length() >= 4) {
            try {
                seq = Integer.parseInt(maxSettlementNo.substring(maxSettlementNo.length() - 4)) + 1;
            } catch (NumberFormatException e) {
                seq = 1;
            }
        }

        return String.format("%s%04d", prefix, seq);
    }

    /**
     * 验证结算方是否存在
     *
     * @param partyType 结算方类型
     * @param partyId   结算方ID
     */
    private void validateParty(Integer partyType, Long partyId) {
        if (partyType == null || partyId == null) {
            throw new BusinessException("结算方类型和ID不能为空");
        }

        if (partyType == 1) {
            // 客户
            Optional<Customer> customer = customerRepository.findById(partyId);
            if (customer.isEmpty()) {
                throw new BusinessException("客户不存在");
            }
        } else if (partyType == 2) {
            // 承运商
            Optional<Carrier> carrier = carrierRepository.findById(partyId);
            if (carrier.isEmpty()) {
                throw new BusinessException("承运商不存在");
            }
        } else {
            throw new BusinessException("无效的结算方类型");
        }
    }
}
