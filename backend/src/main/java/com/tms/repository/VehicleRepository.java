package com.tms.repository;

import com.tms.entity.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 车辆Repository接口
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    /**
     * 根据车牌号查询车辆
     *
     * @param plateNumber 车牌号
     * @return 车辆对象
     */
    Optional<Vehicle> findByPlateNumber(String plateNumber);

    /**
     * 根据状态查询车辆列表
     *
     * @param status 状态
     * @return 车辆列表
     */
    List<Vehicle> findByStatus(Integer status);

    /**
     * 分页查询车辆列表
     *
     * @param plateNumber 车牌号（模糊查询）
     * @param status      状态
     * @param pageable    分页参数
     * @return 车辆分页列表
     */
    Page<Vehicle> findByPlateNumberContainingAndStatus(String plateNumber, Integer status, Pageable pageable);

    /**
     * 检查车牌号是否存在
     *
     * @param plateNumber 车牌号
     * @return 是否存在
     */
    boolean existsByPlateNumber(String plateNumber);
}
