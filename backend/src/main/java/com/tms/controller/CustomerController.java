package com.tms.controller;

import com.tms.common.Result;
import com.tms.common.PageResult;
import com.tms.dto.CustomerDTO;
import com.tms.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 客户管理控制器
 *
 * @author TMS Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * 创建客户
     *
     * @param customerDTO 客户信息
     * @return 创建结果
     */
    @PostMapping
    public Result Result<CustomerDTO> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        CustomerDTO result = customerService.createCustomer(customerDTO);
        return Result.success("创建成功", result);
    }

    /**
     * 更新客户
     *
     * @param id          客户ID
     * @param customerDTO 客户信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public Result Result<CustomerDTO> updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerDTO customerDTO) {
        CustomerDTO result = customerService.updateCustomer(id, customerDTO);
        return Result.success("更新成功", result);
    }

    /**
     * 删除客户
     *
     * @param id 客户ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return Result.success("删除成功");
    }

    /**
     * 根据ID查询客户
     *
     * @param id 客户ID
     * @return 客户信息
     */
    @GetMapping("/{id}")
    public Result Result<CustomerDTO> getCustomerById(@PathVariable Long id) {
        CustomerDTO result = customerService.getCustomerById(id);
        return Result.success(result);
    }

    /**
     * 分页查询客户列表
     *
     * @param customerName 客户名称
     * @param status       状态
     * @param pageNum      页码
     * @param pageSize     每页大小
     * @return 客户分页列表
     */
    @GetMapping
    public Result Result<PageResultResult<CustomerDTO>> getCustomerList(
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        PageResultResult<CustomerDTO> result = customerService.getCustomerList(customerName, status, pageNum, pageSize);
        return Result.success(result);
    }
}
