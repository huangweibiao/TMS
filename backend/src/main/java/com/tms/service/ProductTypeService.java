package com.tms.service;

import com.tms.entity.ProductType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * 产品类型Service接口
 *
 * @author TMS Team
 * @version 1.0.0
 */
public interface ProductTypeService {

    /**
     * 创建产品类型
     *
     * @param productType 产品类型
     * @return 创建后的产品类型
     */
    ProductType createProductType(ProductType productType);

    /**
     * 更新产品类型
     *
     * @param id          类型ID
     * @param productType 产品类型
     * @return 更新后的产品类型
     */
    ProductType updateProductType(Long id, ProductType productType);

    /**
     * 删除产品类型
     *
     * @param id 类型ID
     */
    void deleteProductType(Long id);

    /**
     * 根据ID查询产品类型
     *
     * @param id 类型ID
     * @return 产品类型
     */
    Optional<ProductType> findById(Long id);

    /**
     * 根据类型编码查询
     *
     * @param typeCode 类型编码
     * @return 产品类型
     */
    Optional<ProductType> findByTypeCode(String typeCode);

    /**
     * 查询所有产品类型
     *
     * @return 产品类型列表
     */
    List<ProductType> findAll();

    /**
     * 分页查询产品类型
     *
     * @param typeName 类型名称（模糊查询）
     * @param status   状态
     * @param pageable 分页参数
     * @return 分页结果
     */
    Page<ProductType> findProductTypes(String typeName, Integer status, Pageable pageable);

    /**
     * 根据父类型ID查询子类型
     *
     * @param parentId 父类型ID
     * @return 子类型列表
     */
    List<ProductType> findByParentId(Long parentId);

    /**
     * 启用产品类型
     *
     * @param id 类型ID
     * @return 更新后的产品类型
     */
    ProductType enableProductType(Long id);

    /**
     * 停用产品类型
     *
     * @param id 类型ID
     * @return 更新后的产品类型
     */
    ProductType disableProductType(Long id);
}
