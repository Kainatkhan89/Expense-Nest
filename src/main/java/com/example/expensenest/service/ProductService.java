package com.example.expensenest.service;
import com.example.expensenest.entity.Products;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {

    public List<Products> getProductsByCategory(int categoryId);
}
