package com.tms.service;

import com.tms.dto.VehicleDTO;
import com.tms.common.PageResult;

import java.util.List;

/**
 * 车辆服务接口
 *
 * @author TMS Team
 * @version 1.0.0
 */
public interface VehicleService {

    /**
     * 创建车辆
     *
     * @param vehicleDTO 车辆DTO
     * @return 创建后的车辆
     */
    VehicleDTO createVehicle(VehicleDTO vehicleDTO);

    /**
     * 更新车辆
     *
     * @param id         车辆ID
     * @param vehicleDTO 车辆DTO
     * @return 更新后的车辆
     */
    VehicleDTO updateVehicle(Long id, VehicleDTO vehicleDTO);

    /**
     * 删除车辆
     *
     * @param id 车辆ID
     */
    void deleteVehicle(Long id);

    /**
     * 根据ID查询车辆
     *
     * @param id 车辆ID
     * @return 车辆信息
     */
    VehicleDTO getVehicleById(Long id);

    /**
     * 根据车牌号查询车辆
     *
     * @param plateNumber 车牌号
     * @return 车辆信息
     */
    VehicleDTO getVehicleByPlateNumber(String plateNumber);

    /**
     * 分页查询车辆列表
     *
     * @param plateNumber 车牌号（模糊查询）
     * @param status      状态
     * @param pageNum     页码
     * @param pageSize    每页大小
     * @return 车辆分页列表
     */
    PageResult<VehicleDTO> getVehicleList(String plateNumber, Integer status, int pageNum, int pageSize);

    /**
     * 获取可用车辆列表
     *
     * @return 可用车辆列表
     */
    List<VehicleDTO> getAvailableVehicles();
}
