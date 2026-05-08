package com.tms.controller;

import com.tms.common.Result;
import com.tms.entity.ProductType;
import com.tms.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 产品类型Controller
 *
 * @author TMS Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/product-types")
public class ProductTypeController {

    private final ProductTypeService productTypeService;

    @Autowired
    public ProductTypeController(ProductTypeService productTypeService) {
        this.productTypeService = productTypeService;
    }

    /**
     * 创建产品类型
     */
    @PostMapping
    public Result<ProductType> create(@RequestBody ProductType productType) {
        return Result.success(productTypeService.createProductType(productType));
    }

    /**
     * 更新产品类型
     */
    @PutMapping("/{id}")
    public Result<ProductType> update(@PathVariable Long id, @RequestBody ProductType productType) {
        return Result.success(productTypeService.updateProductType(id, productType));
    }

    /**
     * 删除产品类型
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        productTypeService.deleteProductType(id);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/{id}")
    public Result<ProductType> getById(@PathVariable Long id) {
        return productTypeService.findById(id)
                .map(Result::success)
                .orElse(Result.error(404, "产品类型不存在"));
    }

    /**
     * 分页查询
     */
    @GetMapping
    public Result<Page<ProductType>> list(
            @RequestParam(required = false) String typeName,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        return Result.success(productTypeService.findProductTypes(typeName, status, pageable));
    }

    /**
     * 查询所有
     */
    @GetMapping("/all")
    public Result<List<ProductType>> getAll() {
        return Result.success(productTypeService.findAll());
    }

    /**
     * 根据父类型查询子类型
     */
    @GetMapping("/children/{parentId}")
    public Result<List<ProductType>> getChildren(@PathVariable Long parentId) {
        return Result.success(productTypeService.findByParentId(parentId));
    }

    /**
     * 启用产品类型
     */
    @PutMapping("/{id}/enable")
    public Result<ProductType> enable(@PathVariable Long id) {
        return Result.success(productTypeService.enableProductType(id));
    }

    /**
     * 停用产品类型
     */
    @PutMapping("/{id}/disable")
    public Result<ProductType> disable(@PathVariable Long id) {
        return Result.success(productTypeService.disableProductType(id));
    }
}
