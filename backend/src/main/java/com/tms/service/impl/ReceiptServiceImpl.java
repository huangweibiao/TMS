package com.tms.service.impl;

import com.tms.dto.ReceiptDTO;
import com.tms.entity.Receipt;
import com.tms.entity.Waybill;
import com.tms.exception.BusinessException;
import com.tms.repository.ReceiptRepository;
import com.tms.repository.WaybillRepository;
import com.tms.service.ReceiptService;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 回单Service实现类
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Service
public class ReceiptServiceImpl implements ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final WaybillRepository waybillRepository;

    @Autowired
    public ReceiptServiceImpl(ReceiptRepository receiptRepository,
                              WaybillRepository waybillRepository) {
        this.receiptRepository = receiptRepository;
        this.waybillRepository = waybillRepository;
    }

    @Override
    @Transactional
    @CachePut(value = "receipt", key = "#result.id")
    public Receipt createReceipt(ReceiptDTO dto) {
        // 验证运单是否存在
        Waybill waybill = waybillRepository.findById(dto.getWaybillId())
                .orElseThrow(() -> new BusinessException("运单不存在"));

        // 检查是否已存在回单
        Optional<Receipt> existingReceipt = receiptRepository.findByWaybillId(dto.getWaybillId());
        if (existingReceipt.isPresent()) {
            throw new BusinessException("该运单已存在回单");
        }

        // 生成回单号
        String receiptNo = generateReceiptNo();

        Receipt receipt = new Receipt();
        receipt.setReceiptNo(receiptNo);
        receipt.setWaybillId(dto.getWaybillId());
        receipt.setReceiptType(dto.getReceiptType() != null ? dto.getReceiptType() : 1);
        receipt.setSignerName(dto.getSignerName());
        receipt.setSignerPhone(dto.getSignerPhone());
        receipt.setSignTime(dto.getSignTime());
        receipt.setSignPhotoUrl(dto.getSignPhotoUrl());
        receipt.setReceiptFileUrl(dto.getReceiptFileUrl());
        receipt.setStatus(1); // 未回传
        receipt.setRemark(dto.getRemark());

        return receiptRepository.save(receipt);
    }

    @Override
    @Transactional
    @CachePut(value = "receipt", key = "#id")
    public Receipt updateReceipt(Long id, ReceiptDTO dto) {
        Receipt receipt = receiptRepository.findById(id)
                .orElseThrow(() -> new BusinessException("回单不存在"));

        // 只有未回传状态的回单可以修改
        if (receipt.getStatus() != 1) {
            throw new BusinessException("只有未回传状态的回单可以修改");
        }

        receipt.setReceiptType(dto.getReceiptType());
        receipt.setSignerName(dto.getSignerName());
        receipt.setSignerPhone(dto.getSignerPhone());
        receipt.setSignTime(dto.getSignTime());
        receipt.setSignPhotoUrl(dto.getSignPhotoUrl());
        receipt.setReceiptFileUrl(dto.getReceiptFileUrl());
        receipt.setRemark(dto.getRemark());

        return receiptRepository.save(receipt);
    }

    @Override
    @Transactional
    @CacheEvict(value = "receipt", key = "#id")
    public void deleteReceipt(Long id) {
        Receipt receipt = receiptRepository.findById(id)
                .orElseThrow(() -> new BusinessException("回单不存在"));

        // 只有未回传状态的回单可以删除
        if (receipt.getStatus() != 1) {
            throw new BusinessException("只有未回传状态的回单可以删除");
        }

        receiptRepository.deleteById(id);
    }

    @Override
    @Cacheable(value = "receipt", key = "#id")
    public Optional<Receipt> findById(Long id) {
        return receiptRepository.findById(id);
    }

    @Override
    @Cacheable(value = "receipt", key = "#receiptNo")
    public Optional<Receipt> findByReceiptNo(String receiptNo) {
        return receiptRepository.findByReceiptNo(receiptNo);
    }

    @Override
    public Optional<Receipt> findByWaybillId(Long waybillId) {
        return receiptRepository.findByWaybillId(waybillId);
    }

    @Override
    public Page<Receipt> findReceipts(String receiptNo, Integer status, Pageable pageable) {
        Specification<Receipt> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(receiptNo)) {
                predicates.add(cb.like(root.get("receiptNo"), "%" + receiptNo + "%"));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return receiptRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional
    @CachePut(value = "receipt", key = "#id")
    public Receipt returnReceipt(Long id) {
        Receipt receipt = receiptRepository.findById(id)
                .orElseThrow(() -> new BusinessException("回单不存在"));

        if (receipt.getStatus() != 1) {
            throw new BusinessException("只有未回传状态的回单可以回传");
        }

        receipt.setStatus(2); // 已回传
        receipt.setReturnTime(LocalDateTime.now());

        return receiptRepository.save(receipt);
    }

    @Override
    @Transactional
    @CachePut(value = "receipt", key = "#id")
    public Receipt auditReceipt(Long id, String auditBy) {
        Receipt receipt = receiptRepository.findById(id)
                .orElseThrow(() -> new BusinessException("回单不存在"));

        if (receipt.getStatus() != 2) {
            throw new BusinessException("只有已回传状态的回单可以审核");
        }

        receipt.setStatus(3); // 已审核
        receipt.setAuditTime(LocalDateTime.now());
        receipt.setAuditBy(auditBy);

        return receiptRepository.save(receipt);
    }

    @Override
    public String generateReceiptNo() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String prefix = "RT" + dateStr;

        // 查询当天最大回单号
        String maxReceiptNo = receiptRepository.findMaxReceiptNoByPrefix(prefix);
        int seq = 1;
        if (maxReceiptNo != null && maxReceiptNo.length() >= 4) {
            try {
                seq = Integer.parseInt(maxReceiptNo.substring(maxReceiptNo.length() - 4)) + 1;
            } catch (NumberFormatException e) {
                seq = 1;
            }
        }

        return String.format("%s%04d", prefix, seq);
    }
}
