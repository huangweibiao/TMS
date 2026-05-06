package com.tms.service;

import com.tms.dto.CustomerDTO;
import com.tms.common.PageResult;

/**
 * 客户服务接口
 *
 * @author TMS Team
 * @version 1.0.0
 */
public interface CustomerService {

    /**
     * 创建客户
     *
     * @param customerDTO 客户DTO
     * @return 创建后的客户
     */
    CustomerDTO createCustomer(CustomerDTO customerDTO);

    /**
     * 更新客户
     *
     * @param id          客户ID
     * @param customerDTO 客户DTO
     * @return 更新后的客户
     */
    CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO);

    /**
     * 删除客户
     *
     * @param id 客户ID
     */
    void deleteCustomer(Long id);

    /**
     * 根据ID查询客户
     *
     * @param id 客户ID
     * @return 客户信息
     */
    CustomerDTO getCustomerById(Long id);

    /**
     * 分页查询客户列表
     *
     * @param customerName 客户名称（模糊查询）
     * @param status       状态
     * @param pageNum      页码
     * @param pageSize     每页大小
     * @return 客户分页列表
     */
    PageResultResult<CustomerDTO> getCustomerList(String customerName, Integer status, int pageNum, int pageSize);
}
