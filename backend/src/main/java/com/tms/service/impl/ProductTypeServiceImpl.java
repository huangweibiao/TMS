package com.tms.service.impl;

import com.tms.entity.ProductType;
import com.tms.exception.BusinessException;
import com.tms.repository.ProductTypeRepository;
import com.tms.service.ProductTypeService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 产品类型Service实现类
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Service
public class ProductTypeServiceImpl implements ProductTypeService {

    private final ProductTypeRepository productTypeRepository;

    @Autowired
    public ProductTypeServiceImpl(ProductTypeRepository productTypeRepository) {
        this.productTypeRepository = productTypeRepository;
    }

    @Override
    @Transactional
    @CachePut(value = "productType", key = "#result.id")
    public ProductType createProductType(ProductType productType) {
        // 检查类型编码是否已存在
        if (productTypeRepository.existsByTypeCode(productType.getTypeCode())) {
            throw new BusinessException("类型编码已存在");
        }

        // 设置层级
        if (productType.getParentId() != null) {
            ProductType parent = productTypeRepository.findById(productType.getParentId())
                    .orElseThrow(() -> new BusinessException("父类型不存在"));
            productType.setLevel(parent.getLevel() + 1);
        } else {
            productType.setLevel(1);
        }

        productType.setStatus(1);
        return productTypeRepository.save(productType);
    }

    @Override
    @Transactional
    @CachePut(value = "productType", key = "#id")
    public ProductType updateProductType(Long id, ProductType productType) {
        ProductType existing = productTypeRepository.findById(id)
                .orElseThrow(() -> new BusinessException("产品类型不存在"));

        // 检查类型编码是否被其他类型使用
        if (!existing.getTypeCode().equals(productType.getTypeCode())
                && productTypeRepository.existsByTypeCode(productType.getTypeCode())) {
            throw new BusinessException("类型编码已存在");
        }

        existing.setTypeCode(productType.getTypeCode());
        existing.setTypeName(productType.getTypeName());
        existing.setParentId(productType.getParentId());
        existing.setNeedTemperature(productType.getNeedTemperature());
        existing.setIsHazardous(productType.getIsHazardous());
        existing.setIsFragile(productType.getIsFragile());
        existing.setDefaultUnitWeight(productType.getDefaultUnitWeight());
        existing.setDefaultUnitVolume(productType.getDefaultUnitVolume());
        existing.setSortOrder(productType.getSortOrder());
        existing.setRemark(productType.getRemark());

        // 更新层级
        if (existing.getParentId() != null) {
            ProductType parent = productTypeRepository.findById(existing.getParentId())
                    .orElseThrow(() -> new BusinessException("父类型不存在"));
            existing.setLevel(parent.getLevel() + 1);
        } else {
            existing.setLevel(1);
        }

        return productTypeRepository.save(existing);
    }

    @Override
    @Transactional
    @CacheEvict(value = "productType", key = "#id")
    public void deleteProductType(Long id) {
        ProductType productType = productTypeRepository.findById(id)
                .orElseThrow(() -> new BusinessException("产品类型不存在"));

        // 检查是否有子类型
        List<ProductType> children = productTypeRepository.findByParentId(id);
        if (!children.isEmpty()) {
            throw new BusinessException("该产品类型下存在子类型，不能删除");
        }

        productTypeRepository.deleteById(id);
    }

    @Override
    @Cacheable(value = "productType", key = "#id")
    public Optional<ProductType> findById(Long id) {
        return productTypeRepository.findById(id);
    }

    @Override
    @Cacheable(value = "productType", key = "#typeCode")
    public Optional<ProductType> findByTypeCode(String typeCode) {
        return productTypeRepository.findByTypeCode(typeCode);
    }

    @Override
    @Cacheable(value = "productType", key = "'all'")
    public List<ProductType> findAll() {
        return productTypeRepository.findAll();
    }

    @Override
    public Page<ProductType> findProductTypes(String typeName, Integer status, Pageable pageable) {
        Specification<ProductType> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(typeName)) {
                predicates.add(cb.like(root.get("typeName"), "%" + typeName + "%"));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return productTypeRepository.findAll(spec, pageable);
    }

    @Override
    public List<ProductType> findByParentId(Long parentId) {
        return productTypeRepository.findByParentId(parentId);
    }

    @Override
    @Transactional
    @CachePut(value = "productType", key = "#id")
    public ProductType enableProductType(Long id) {
        ProductType productType = productTypeRepository.findById(id)
                .orElseThrow(() -> new BusinessException("产品类型不存在"));

        productType.setStatus(1);
        return productTypeRepository.save(productType);
    }

    @Override
    @Transactional
    @CachePut(value = "productType", key = "#id")
    public ProductType disableProductType(Long id) {
        ProductType productType = productTypeRepository.findById(id)
                .orElseThrow(() -> new BusinessException("产品类型不存在"));

        productType.setStatus(0);
        return productTypeRepository.save(productType);
    }
}
