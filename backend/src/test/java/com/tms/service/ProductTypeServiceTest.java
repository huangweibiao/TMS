package com.tms.service;

import com.tms.entity.ProductType;
import com.tms.exception.BusinessException;
import com.tms.repository.ProductTypeRepository;
import com.tms.service.impl.ProductTypeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductTypeServiceTest {

    @Mock
    private ProductTypeRepository productTypeRepository;

    @InjectMocks
    private ProductTypeServiceImpl productTypeService;

    private ProductType productType;

    @BeforeEach
    void setUp() {
        productType = new ProductType();
        productType.setId(1L);
        productType.setTypeCode("ELECTRONICS");
        productType.setTypeName("电子产品");
        productType.setStatus(1);
        productType.setParentId(0L);
    }

    @Test
    void createProductType_Success() {
        when(productTypeRepository.existsByTypeCode("ELECTRONICS")).thenReturn(false);
        when(productTypeRepository.findById(0L)).thenReturn(Optional.of(productType));
        when(productTypeRepository.save(any(ProductType.class))).thenReturn(productType);

        ProductType result = productTypeService.createProductType(productType);

        assertNotNull(result);
        assertEquals("电子产品", result.getTypeName());
        verify(productTypeRepository).save(any(ProductType.class));
    }

    @Test
    void createProductType_DuplicateCode_ThrowsException() {
        when(productTypeRepository.existsByTypeCode("ELECTRONICS")).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> productTypeService.createProductType(productType));
        assertEquals("类型编码已存在", exception.getMessage());
    }

    @Test
    void updateProductType_Success() {
        ProductType updateData = new ProductType();
        updateData.setTypeName("数码产品");
        updateData.setRemark("数码类商品");

        when(productTypeRepository.findById(1L)).thenReturn(Optional.of(productType));
        when(productTypeRepository.save(any(ProductType.class))).thenReturn(productType);

        ProductType result = productTypeService.updateProductType(1L, updateData);

        assertNotNull(result);
        verify(productTypeRepository).save(any(ProductType.class));
    }

    @Test
    void updateProductType_NotFound_ThrowsException() {
        when(productTypeRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> productTypeService.updateProductType(1L, productType));
        assertEquals("产品类型不存在", exception.getMessage());
    }

    @Test
    void deleteProductType_Success() {
        when(productTypeRepository.findById(1L)).thenReturn(Optional.of(productType));
        when(productTypeRepository.findByParentId(1L)).thenReturn(Arrays.asList());

        productTypeService.deleteProductType(1L);

        verify(productTypeRepository).deleteById(1L);
    }

    @Test
    void deleteProductType_HasChildren_ThrowsException() {
        ProductType child = new ProductType();
        child.setId(2L);

        when(productTypeRepository.findById(1L)).thenReturn(Optional.of(productType));
        when(productTypeRepository.findByParentId(1L)).thenReturn(Arrays.asList(child));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> productTypeService.deleteProductType(1L));
        assertEquals("该产品类型下存在子类型，不能删除", exception.getMessage());
    }

    @Test
    void findById_Success() {
        when(productTypeRepository.findById(1L)).thenReturn(Optional.of(productType));

        Optional<ProductType> result = productTypeService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("电子产品", result.get().getTypeName());
    }

    @Test
    void findByTypeCode_Success() {
        when(productTypeRepository.findByTypeCode("ELECTRONICS")).thenReturn(Optional.of(productType));

        Optional<ProductType> result = productTypeService.findByTypeCode("ELECTRONICS");

        assertTrue(result.isPresent());
        assertEquals("电子产品", result.get().getTypeName());
    }

    @Test
    void findAll_Success() {
        when(productTypeRepository.findAll()).thenReturn(Arrays.asList(productType));

        List<ProductType> result = productTypeService.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void findByParentId_Success() {
        when(productTypeRepository.findByParentId(0L)).thenReturn(Arrays.asList(productType));

        List<ProductType> result = productTypeService.findByParentId(0L);

        assertEquals(1, result.size());
    }

    @Test
    void findProductTypes_WithFilters() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductType> page = new PageImpl<>(Arrays.asList(productType));

        when(productTypeRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);

        Page<ProductType> result = productTypeService.findProductTypes("电子", 1, pageable);

        assertEquals(1, result.getContent().size());
    }

    @Test
    void enableProductType_Success() {
        productType.setStatus(0);
        when(productTypeRepository.findById(1L)).thenReturn(Optional.of(productType));
        when(productTypeRepository.save(any(ProductType.class))).thenReturn(productType);

        ProductType result = productTypeService.enableProductType(1L);

        assertEquals(1, result.getStatus());
    }

    @Test
    void disableProductType_Success() {
        when(productTypeRepository.findById(1L)).thenReturn(Optional.of(productType));
        when(productTypeRepository.save(any(ProductType.class))).thenReturn(productType);

        ProductType result = productTypeService.disableProductType(1L);

        assertEquals(0, result.getStatus());
    }
}
