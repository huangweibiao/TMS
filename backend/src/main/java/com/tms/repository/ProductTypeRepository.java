package com.tms.repository;

import com.tms.entity.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 产品类型Repository接口
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Repository
public interface ProductTypeRepository extends JpaRepository<ProductType, Long>, JpaSpecificationExecutor<ProductType> {

    /**
     * 根据类型编码查询
     *
     * @param typeCode 类型编码
     * @return 产品类型
     */
    Optional<ProductType> findByTypeCode(String typeCode);

    /**
     * 根据父类型ID查询
     *
     * @param parentId 父类型ID
     * @return 产品类型列表
     */
    List<ProductType> findByParentId(Long parentId);

    /**
     * 根据状态查询
     *
     * @param status 状态
     * @return 产品类型列表
     */
    List<ProductType> findByStatus(Integer status);

    /**
     * 根据父类型ID和状态查询
     *
     * @param parentId 父类型ID
     * @param status   状态
     * @return 产品类型列表
     */
    List<ProductType> findByParentIdAndStatus(Long parentId, Integer status);

    /**
     * 检查类型编码是否存在
     *
     * @param typeCode 类型编码
     * @return 是否存在
     */
    boolean existsByTypeCode(String typeCode);
}
