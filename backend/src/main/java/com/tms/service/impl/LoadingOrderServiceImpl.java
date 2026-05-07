package com.tms.service.impl;

import com.tms.dto.LoadingOrderDTO;
import com.tms.entity.LoadingOrder;
import com.tms.entity.Dispatch;
import com.tms.exception.BusinessException;
import com.tms.repository.LoadingOrderRepository;
import com.tms.repository.DispatchRepository;
import com.tms.repository.WarehouseRepository;
import com.tms.service.LoadingOrderService;
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
 * 装货单Service实现类
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Service
public class LoadingOrderServiceImpl implements LoadingOrderService {

    private final LoadingOrderRepository loadingOrderRepository;
    private final DispatchRepository dispatchRepository;
    private final WarehouseRepository warehouseRepository;

    @Autowired
    public LoadingOrderServiceImpl(LoadingOrderRepository loadingOrderRepository,
                                   DispatchRepository dispatchRepository,
                                   WarehouseRepository warehouseRepository) {
        this.loadingOrderRepository = loadingOrderRepository;
        this.dispatchRepository = dispatchRepository;
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    @Transactional
    @CachePut(value = "loadingOrder", key = "#result.id")
    public LoadingOrder createLoadingOrder(LoadingOrderDTO dto) {
        // 验证调度单是否存在
        Dispatch dispatch = dispatchRepository.findById(dto.getDispatchId())
                .orElseThrow(() -> new BusinessException("调度单不存在"));

        // 验证仓库是否存在
        if (!warehouseRepository.existsById(dto.getWarehouseId())) {
            throw new BusinessException("仓库不存在");
        }

        // 生成装货单号
        String loadingNo = generateLoadingNo();

        LoadingOrder loadingOrder = new LoadingOrder();
        loadingOrder.setLoadingNo(loadingNo);
        loadingOrder.setDispatchId(dto.getDispatchId());
        loadingOrder.setWarehouseId(dto.getWarehouseId());
        loadingOrder.setOperator(dto.getOperator());
        loadingOrder.setStatus(1); // 待装货
        loadingOrder.setRemark(dto.getRemark());

        return loadingOrderRepository.save(loadingOrder);
    }

    @Override
    @Transactional
    @CachePut(value = "loadingOrder", key = "#id")
    public LoadingOrder updateLoadingOrder(Long id, LoadingOrderDTO dto) {
        LoadingOrder loadingOrder = loadingOrderRepository.findById(id)
                .orElseThrow(() -> new BusinessException("装货单不存在"));

        // 只有待装货状态可以修改
        if (loadingOrder.getStatus() != 1) {
            throw new BusinessException("只有待装货状态的装货单可以修改");
        }

        // 验证仓库是否存在
        if (!warehouseRepository.existsById(dto.getWarehouseId())) {
            throw new BusinessException("仓库不存在");
        }

        loadingOrder.setWarehouseId(dto.getWarehouseId());
        loadingOrder.setRemark(dto.getRemark());

        return loadingOrderRepository.save(loadingOrder);
    }

    @Override
    @Transactional
    @CacheEvict(value = "loadingOrder", key = "#id")
    public void deleteLoadingOrder(Long id) {
        LoadingOrder loadingOrder = loadingOrderRepository.findById(id)
                .orElseThrow(() -> new BusinessException("装货单不存在"));

        // 只有待装货状态可以删除
        if (loadingOrder.getStatus() != 1) {
            throw new BusinessException("只有待装货状态的装货单可以删除");
        }

        loadingOrderRepository.deleteById(id);
    }

    @Override
    @Cacheable(value = "loadingOrder", key = "#id")
    public Optional Optional<LoadingOrder> findById(Long id) {
        return loadingOrderRepository.findById(id);
    }

    @Override
    @Cacheable(value = "loadingOrder", key = "#loadingNo")
    public Optional Optional<LoadingOrder> findByLoadingNo(String loadingNo) {
        return loadingOrderRepository.findByLoadingNo(loadingNo);
    }

    @Override
    public List List<LoadingOrder> findByDispatchId(Long dispatchId) {
        return loadingOrderRepository.findByDispatchId(dispatchId);
    }

    @Override
    public Page Page<LoadingOrder> findLoadingOrders(String loadingNo, Integer status, Pageable pageable) {
        Specification Specification<LoadingOrder> spec = (root, query, cb) -> {
            List List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(loadingNo)) {
                predicates.add(cb.like(root.get("loadingNo"), "%" + loadingNo + "%"));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return loadingOrderRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional
    @CachePut(value = "loadingOrder", key = "#id")
    public LoadingOrder startLoading(Long id, String operator) {
        LoadingOrder loadingOrder = loadingOrderRepository.findById(id)
                .orElseThrow(() -> new BusinessException("装货单不存在"));

        if (loadingOrder.getStatus() != 1) {
            throw new BusinessException("只有待装货状态的装货单可以开始装货");
        }

        loadingOrder.setStatus(2); // 装货中
        loadingOrder.setLoadingTime(LocalDateTime.now());
        loadingOrder.setOperator(operator);

        return loadingOrderRepository.save(loadingOrder);
    }

    @Override
    @Transactional
    @CachePut(value = "loadingOrder", key = "#id")
    public LoadingOrder completeLoading(Long id, String operator) {
        LoadingOrder loadingOrder = loadingOrderRepository.findById(id)
                .orElseThrow(() -> new BusinessException("装货单不存在"));

        if (loadingOrder.getStatus() != 2) {
            throw new BusinessException("只有装货中状态的装货单可以完成装货");
        }

        loadingOrder.setStatus(3); // 已完成
        loadingOrder.setOperator(operator);

        return loadingOrderRepository.save(loadingOrder);
    }

    @Override
    public String generateLoadingNo() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String prefix = "LH" + dateStr;

        // 查询当天最大装货单号
        String maxLoadingNo = loadingOrderRepository.findMaxLoadingNoByPrefix(prefix);
        int seq = 1;
        if (maxLoadingNo != null && maxLoadingNo.length() >= 4) {
            try {
                seq = Integer.parseInt(maxLoadingNo.substring(maxLoadingNo.length() - 4)) + 1;
            } catch (NumberFormatException e) {
                seq = 1;
            }
        }

        return String.format("%s%04d", prefix, seq);
    }
}
