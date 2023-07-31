package com.example.expensenest.service.impl;

import com.example.expensenest.entity.Category;
import com.example.expensenest.repository.CategoryRepository;
import com.example.expensenest.service.CategoryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final Logger logger = LogManager.getLogger(CategoryServiceImpl.class);
    private CategoryRepository categoryRepository;
    public CategoryServiceImpl (CategoryRepository categoryRepository) {
        super();
        this.categoryRepository = categoryRepository;
    }

    public boolean addCategory(Category category) {
        logger.info("Adding category");
        return categoryRepository.addCategory(category);
    }

    public Category getCategoryById(int categoryId) {
        logger.info("Fetching category by id : {}" , categoryId);
        return categoryRepository.getCategoryById(categoryId);
    }

    public List<Category> getAllCategories() {
        logger.info("Fetching all categories");
        return categoryRepository.getAllCategories();
    }
}
