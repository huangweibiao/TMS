package com.tms.controller;

import com.tms.common.PageResult;
import com.tms.common.Result;
import com.tms.dto.AddressDTO;
import com.tms.entity.Address;
import com.tms.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 地址库Controller
 *
 * @author TMS Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/address")
public class AddressController {

    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    /**
     * 创建地址
     *
     * @param dto 地址DTO
     * @return 创建结果
     */
    @PostMapping
    public Result Result<AddressDTO> createAddress(@Valid @RequestBody AddressDTO dto) {
        Address address = addressService.createAddress(dto);
        return Result.success(convertToDTO(address));
    }

    /**
     * 更新地址
     *
     * @param id  地址ID
     * @param dto 地址DTO
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public Result Result<AddressDTO> updateAddress(@PathVariable Long id,
                                           @Valid @RequestBody AddressDTO dto) {
        Address address = addressService.updateAddress(id, dto);
        return Result.success(convertToDTO(address));
    }

    /**
     * 删除地址
     *
     * @param id 地址ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return Result.success();
    }

    /**
     * 根据ID查询地址
     *
     * @param id 地址ID
     * @return 地址信息
     */
    @GetMapping("/{id}")
    public Result Result<AddressDTO> getAddressById(@PathVariable Long id) {
        return addressService.findById(id)
                .map(this::convertToDTO)
                .map(Result::success)
                .orElse(Result.error(404, "地址不存在"));
    }

    /**
     * 根据客户ID查询地址列表
     *
     * @param customerId 客户ID
     * @return 地址列表
     */
    @GetMapping("/by-customer/{customerId}")
    public Result<List<List<AddressDTO>> getAddressesByCustomer(@PathVariable Long customerId) {
        List List<AddressDTO> list = addressService.findByCustomerId(customerId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return Result.success(list);
    }

    /**
     * 根据客户ID和地址类型查询地址列表
     *
     * @param customerId  客户ID
     * @param addressType 地址类型
     * @return 地址列表
     */
    @GetMapping("/by-customer/{customerId}/type/{addressType}")
    public Result<List<List<AddressDTO>> getAddressesByCustomerAndType(
            @PathVariable Long customerId,
            @PathVariable Integer addressType) {
        List List<AddressDTO> list = addressService.findByCustomerIdAndType(customerId, addressType)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return Result.success(list);
    }

    /**
     * 分页查询地址列表
     *
     * @param customerId  客户ID
     * @param addressType 地址类型
     * @param status      状态
     * @param pageNum     页码
     * @param pageSize    每页大小
     * @return 分页结果
     */
    @GetMapping("/list")
    public Result Result<PageResultResult<AddressDTO>> getAddressList(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Integer addressType,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page Page<Address> page = addressService.findAddresses(customerId, addressType, status, pageable);

        List List<AddressDTO> list = page.getContent()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return Result.success(new PageResult<>(pageNum, pageSize, page.getTotalElements(), list));
    }

    /**
     * 设置默认地址
     *
     * @param id 地址ID
     * @return 操作结果
     */
    @PostMapping("/{id}/set-default")
    public Result Result<AddressDTO> setDefaultAddress(@PathVariable Long id) {
        Address address = addressService.setDefaultAddress(id);
        return Result.success(convertToDTO(address));
    }

    /**
     * 取消默认地址
     *
     * @param id 地址ID
     * @return 操作结果
     */
    @PostMapping("/{id}/cancel-default")
    public Result Result<AddressDTO> cancelDefaultAddress(@PathVariable Long id) {
        Address address = addressService.cancelDefaultAddress(id);
        return Result.success(convertToDTO(address));
    }

    /**
     * 启用地址
     *
     * @param id 地址ID
     * @return 操作结果
     */
    @PostMapping("/{id}/enable")
    public Result Result<AddressDTO> enableAddress(@PathVariable Long id) {
        Address address = addressService.enableAddress(id);
        return Result.success(convertToDTO(address));
    }

    /**
     * 停用地址
     *
     * @param id 地址ID
     * @return 操作结果
     */
    @PostMapping("/{id}/disable")
    public Result Result<AddressDTO> disableAddress(@PathVariable Long id) {
        Address address = addressService.disableAddress(id);
        return Result.success(convertToDTO(address));
    }

    /**
     * 生成地址编码
     *
     * @return 地址编码
     */
    @GetMapping("/generate-code")
    public Result<Map<String, String>> generateAddressCode() {
        String code = addressService.generateAddressCode();
        Map<String, String> result = new HashMap<>();
        result.put("addressCode", code);
        return Result.success(result);
    }

    /**
     * 将实体转换为DTO
     *
     * @param entity 地址实体
     * @return 地址DTO
     */
    private AddressDTO convertToDTO(Address entity) {
        AddressDTO dto = new AddressDTO();
        dto.setId(entity.getId());
        dto.setAddressCode(entity.getAddressCode());
        dto.setAddressName(entity.getAddressName());
        dto.setAddressType(entity.getAddressType());
        dto.setCustomerId(entity.getCustomerId());
        dto.setContactName(entity.getContactName());
        dto.setContactPhone(entity.getContactPhone());
        dto.setProvince(entity.getProvince());
        dto.setCity(entity.getCity());
        dto.setDistrict(entity.getDistrict());
        dto.setDetailAddress(entity.getDetailAddress());
        dto.setFullAddress(entity.getFullAddress());
        dto.setLongitude(entity.getLongitude());
        dto.setLatitude(entity.getLatitude());
        dto.setZipCode(entity.getZipCode());
        dto.setIsDefault(entity.getIsDefault());
        dto.setStatus(entity.getStatus());
        dto.setRemark(entity.getRemark());
        dto.setCreateTime(entity.getCreateTime());
        dto.setUpdateTime(entity.getUpdateTime());
        return dto;
    }
}

