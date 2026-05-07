package com.tms.service;

import com.tms.dto.DriverDTO;
import com.tms.common.PageResult;

import java.util.List;

/**
 * 司机服务接口
 *
 * @author TMS Team
 * @version 1.0.0
 */
public interface DriverService {

    /**
     * 创建司机
     *
     * @param driverDTO 司机DTO
     * @return 创建后的司机
     */
    DriverDTO createDriver(DriverDTO driverDTO);

    /**
     * 更新司机
     *
     * @param id        司机ID
     * @param driverDTO 司机DTO
     * @return 更新后的司机
     */
    DriverDTO updateDriver(Long id, DriverDTO driverDTO);

    /**
     * 删除司机
     *
     * @param id 司机ID
     */
    void deleteDriver(Long id);

    /**
     * 根据ID查询司机
     *
     * @param id 司机ID
     * @return 司机信息
     */
    DriverDTO getDriverById(Long id);

    /**
     * 根据身份证号查询司机
     *
     * @param idCard 身份证号
     * @return 司机信息
     */
    DriverDTO getDriverByIdCard(String idCard);

    /**
     * 分页查询司机列表
     *
     * @param driverName 司机姓名（模糊查询）
     * @param status     状态
     * @param pageNum    页码
     * @param pageSize   每页大小
     * @return 司机分页列表
     */
    PageResult<DriverDTO> getDriverList(String driverName, Integer status, int pageNum, int pageSize);

    /**
     * 获取可用司机列表
     *
     * @return 可用司机列表
     */
    List List<DriverDTO> getAvailableDrivers();
}
