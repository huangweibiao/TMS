package com.tms.repository;

import com.tms.entity.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 司机Repository接口
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Repository
public interface DriverRepository extends JpaRepositoryRepository<Driver, Long> {

    /**
     * 根据身份证号查询司机
     *
     * @param idCard 身份证号
     * @return 司机对象
     */
    Optional Optional<Driver> findByIdCard(String idCard);

    /**
     * 根据状态查询司机列表
     *
     * @param status 状态
     * @return 司机列表
     */
    List List<Driver> findByStatus(Integer status);

    /**
     * 分页查询司机列表
     *
     * @param driverName 司机姓名（模糊查询）
     * @param status     状态
     * @param pageable   分页参数
     * @return 司机分页列表
     */
    Page Page<Driver> findByDriverNameContainingAndStatus(String driverName, Integer status, Pageable pageable);

    /**
     * 检查身份证号是否存在
     *
     * @param idCard 身份证号
     * @return 是否存在
     */
    boolean existsByIdCard(String idCard);
}
