package com.example.expensenest.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.expensenest.entity.Products;
import com.example.expensenest.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class ProductServiceImplTest {

    private ProductServiceImpl productService;
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        // Create a mock of ProductRepository
        productRepository = mock(ProductRepository.class);
        productService = new ProductServiceImpl(productRepository);
    }

    @Test
    void testGetProductsByCategory() {
        // Arrange
        int categoryId = 1;
        List<Products> expectedProducts = Arrays.asList(
                new Products(),
                new Products()
        );

        // Mock the behavior of ProductRepository's getProductsByCategory method
        when(productRepository.getProductsByCategory(categoryId)).thenReturn(expectedProducts);

        // Act
        List<Products> actualProducts = productService.getProductsByCategory(categoryId);

        // Assert
        assertEquals(expectedProducts, actualProducts);
        // Optionally, verify that the method in the mock is called with the correct argument
        verify(productRepository).getProductsByCategory(categoryId);
    }

    @Test
    void testAddProduct() {
        // Arrange
        Products product = new Products();

        // Mock the behavior of ProductRepository's addProduct method
        when(productRepository.addProduct(product)).thenReturn(true);

        // Act
        boolean result = productService.addProduct(product);

        // Assert
        assertTrue(result);
        // Optionally, verify that the method in the mock is called with the correct argument
        verify(productRepository).addProduct(product);
    }

    @Test
    void testSearchProductsByQuery() {
        // Arrange
        int categoryId = 1;
        String query = "Product";
        List<Products> expectedProducts = Arrays.asList(
                new Products(),
                new Products()
        );

        // Mock the behavior of ProductRepository's searchProductsByQuery method
        when(productRepository.searchProductsByQuery(categoryId, query)).thenReturn(expectedProducts);

        // Act
        List<Products> actualProducts = productService.searchProductsByQuery(categoryId, query);

        // Assert
        assertEquals(expectedProducts, actualProducts);
        // Optionally, verify that the method in the mock is called with the correct arguments
        verify(productRepository).searchProductsByQuery(categoryId, query);
    }
}
