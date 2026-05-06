package com.tms.repository;

import com.tms.entity.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 仓库Repository接口
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Repository
public interface WarehouseRepository extends JpaRepositoryRepository<Warehouse, Long> {

    /**
     * 根据仓库编码查询仓库
     *
     * @param warehouseCode 仓库编码
     * @return 仓库对象
     */
    Optional Optional<Warehouse> findByWarehouseCode(String warehouseCode);

    /**
     * 分页查询仓库列表
     *
     * @param warehouseName 仓库名称（模糊查询）
     * @param status        状态
     * @param pageable      分页参数
     * @return 仓库分页列表
     */
    Page Page<Warehouse> findByWarehouseNameContainingAndStatus(String warehouseName, Integer status, Pageable pageable);

    /**
     * 检查仓库编码是否存在
     *
     * @param warehouseCode 仓库编码
     * @return 是否存在
     */
    boolean existsByWarehouseCode(String warehouseCode);
}
