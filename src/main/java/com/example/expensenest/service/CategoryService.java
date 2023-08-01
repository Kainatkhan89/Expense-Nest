package com.example.expensenest.service;

import com.example.expensenest.entity.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    public List<Category> getAllCategories();

    public boolean addCategory(Category category);

    public Category getCategoryById (int categoryId);
}
