package com.example.expensenest.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.expensenest.entity.Category;
import com.example.expensenest.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

class CategoryServiceImplTest {

    private CategoryServiceImpl categoryService;
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        // Create a mock of CategoryRepository
        categoryRepository = mock(CategoryRepository.class);
        categoryService = new CategoryServiceImpl(categoryRepository);
    }

    @Test
    void testAddCategory_Successful() {
        // Arrange
        Category category = new Category();
        category.setName("Test Category");

        // Mock the behavior of CategoryRepository's addCategory method
        when(categoryRepository.addCategory(category)).thenReturn(true);

        // Act
        boolean result = categoryService.addCategory(category);

        // Assert
        assertTrue(result);
        // Optionally, verify that the method in the mock is called with the correct argument
        verify(categoryRepository).addCategory(category);
    }

    @Test
    void testAddCategory_DuplicateCategory() {
        // Arrange
        Category category = new Category();
        category.setName("Existing Category");

        // Mock the behavior of CategoryRepository's addCategory method to return false for a duplicate category
        when(categoryRepository.addCategory(category)).thenReturn(false);

        // Act
        boolean result = categoryService.addCategory(category);

        // Assert
        assertFalse(result);
        // Optionally, verify that the method in the mock is called with the correct argument
        verify(categoryRepository).addCategory(category);
    }

    @Test
    void testGetCategoryById_ExistingCategory() {
        // Arrange
        int categoryId = 1;
        Category expectedCategory = new Category();
        expectedCategory.setId(categoryId);
        expectedCategory.setName("Test Category");

        // Mock the behavior of CategoryRepository's getCategoryById method
        when(categoryRepository.getCategoryById(categoryId)).thenReturn(expectedCategory);

        // Act
        Category actualCategory = categoryService.getCategoryById(categoryId);

        // Assert
        assertNotNull(actualCategory);
        assertEquals(expectedCategory, actualCategory);
        // Optionally, verify that the method in the mock is called with the correct argument
        verify(categoryRepository).getCategoryById(categoryId);
    }

    @Test
    void testGetCategoryById_NonExistingCategory() {
        // Arrange
        int categoryId = 100;

        // Mock the behavior of CategoryRepository's getCategoryById method for a non-existing category
        when(categoryRepository.getCategoryById(categoryId)).thenReturn(null);

        // Act
        Category category = categoryService.getCategoryById(categoryId);

        // Assert
        assertNull(category);
        // Optionally, verify that the method in the mock is called with the correct argument
        verify(categoryRepository).getCategoryById(categoryId);
    }

    @Test
    void testGetAllCategories() {
        // Arrange
        Category category1 = new Category();
        category1.setId(1);
        category1.setName("Category 1");

        Category category2 = new Category();
        category2.setId(2);
        category2.setName("Category 2");

        List<Category> expectedCategories = Arrays.asList(category1, category2);

        // Mock the behavior of CategoryRepository's getAllCategories method
        when(categoryRepository.getAllCategories()).thenReturn(expectedCategories);

        // Act
        List<Category> actualCategories = categoryService.getAllCategories();

        // Assert
        assertEquals(expectedCategories, actualCategories);
        // Optionally, verify that the method in the mock is called
        verify(categoryRepository).getAllCategories();
    }

    @Test
    void testGetAllCategories_EmptyDatabase() {
        // Arrange
        List<Category> expectedCategories = Arrays.asList();

        // Mock the behavior of CategoryRepository's getAllCategories method for an empty database
        when(categoryRepository.getAllCategories()).thenReturn(expectedCategories);

        // Act
        List<Category> actualCategories = categoryService.getAllCategories();

        // Assert
        assertEquals(expectedCategories, actualCategories);
        // Optionally, verify that the method in the mock is called
        verify(categoryRepository).getAllCategories();
    }
}
