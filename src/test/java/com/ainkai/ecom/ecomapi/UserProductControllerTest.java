/*
 * Copyright (c) 2025. Mitakshar.
 * All rights reserved.
 *
 * This is an e-commerce project built for Learning Purpose and may not be reproduced, distributed, or used without explicit permission from Mitakshar.
 *
 *
 */

package com.ainkai.ecom.ecomapi;
import com.ainkai.api.utils.BaseApiResponse;
import com.ainkai.api.v1.UserProductController;
import com.ainkai.builder.ApiResponseBuilder;
import com.ainkai.mapper.EcomApiUserMapper;
import com.ainkai.model.Product;
import com.ainkai.model.dtos.MultipleProductResponse;
import com.ainkai.model.dtos.ProductResponse;
import com.ainkai.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserProductControllerTest {

    @InjectMocks
    private UserProductController userProductController;

    @Mock
    private ProductService productService;

    @Mock
    private EcomApiUserMapper mapper;

    @Mock
    private ApiResponseBuilder builder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testFindProductByCategoryHandler() {
        // Mock input parameters
        String category = "Electronics";
        List<String> color = Arrays.asList("Red", "Blue");
        List<String> size = Arrays.asList("M", "L");
        Integer minPrice = 100;
        Integer maxPrice = 1000;
        Integer minDiscount = 10;
        String sort = "price";
        String stock = "inStock";
        Integer pageNumber = 1;
        Integer pageSize = 10;

        // Mock responses
        MultipleProductResponse mockResponse = new MultipleProductResponse();
        BaseApiResponse mockApiResponse = new BaseApiResponse(); // Create a mock BaseApiResponse object
        when(mapper.toMultipleProductResponse(any())).thenReturn(mockResponse);
        when(builder.buildSuccessApiResponse(anyString())).thenReturn(mockApiResponse);
        when(builder.buildProductDtoList(anyList())).thenReturn(Arrays.asList());

        // Call the method
        ResponseEntity<MultipleProductResponse> response = userProductController.findProductByCategoryHandler(
                category, color, size, minPrice, maxPrice, minDiscount, sort, stock, pageNumber, pageSize);

        // Verify interactions
        verify(productService, times(1)).getAllFilteredProducts(category, color, size, minPrice, maxPrice, minDiscount, sort, stock, pageNumber, pageSize);
        verify(mapper, times(1)).toMultipleProductResponse(any());
        verify(builder, times(1)).buildProductDtoList(anyList());

        // Assert response
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetProductByIdHandler() {
        // Mock input
        Long productId = 1L;

        // Mock responses
        Product mockProduct = new Product();
        ProductResponse mockProductResponse = new ProductResponse();
        BaseApiResponse mockApiResponse = new BaseApiResponse(); // Create a mock BaseApiResponse object
        when(productService.findProductById(productId)).thenReturn(mockProduct);
        when(builder.buildSuccessApiResponse(anyString())).thenReturn(mockApiResponse);
        when(mapper.toProductResponse(any())).thenReturn(mockProductResponse);
        when(mapper.toProductDto(mockProduct)).thenReturn(null); // Mock mapping behavior

        // Call the method
        ResponseEntity<ProductResponse> response = userProductController.getProductByIdHandler(productId);

        // Verify interactions
        verify(productService, times(1)).findProductById(productId);
        verify(mapper, times(1)).toProductResponse(any());

        // Assert response
        assertEquals(200, response.getStatusCodeValue());
    }
}