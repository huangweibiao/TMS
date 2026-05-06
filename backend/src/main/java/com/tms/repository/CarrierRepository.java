package com.tms.repository;

import com.tms.entity.Carrier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 承运商Repository接口
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Repository
public interface CarrierRepository extends JpaRepositoryRepository<Carrier, Long> {

    /**
     * 根据承运商编码查询承运商
     *
     * @param carrierCode 承运商编码
     * @return 承运商对象
     */
    Optional Optional<Carrier> findByCarrierCode(String carrierCode);

    /**
     * 分页查询承运商列表
     *
     * @param carrierName 承运商名称（模糊查询）
     * @param status      状态
     * @param pageable    分页参数
     * @return 承运商分页列表
     */
    Page Page<Carrier> findByCarrierNameContainingAndStatus(String carrierName, Integer status, Pageable pageable);

    /**
     * 检查承运商编码是否存在
     *
     * @param carrierCode 承运商编码
     * @return 是否存在
     */
    boolean existsByCarrierCode(String carrierCode);
}
