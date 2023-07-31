package com.example.expensenest.service.impl;

import com.example.expensenest.entity.Products;
import com.example.expensenest.repository.ProductRepository;
import com.example.expensenest.service.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LogManager.getLogger(ProductServiceImpl.class);

    private ProductRepository productRepository;
    public ProductServiceImpl (ProductRepository productRepository) {
        super();
        this.productRepository = productRepository;
    }

    public List<Products> getProductsByCategory(int categoryId) {
        logger.info("Fetching all products by categoryId : {}" , categoryId);
        return productRepository.getProductsByCategory(categoryId);
    }

    public boolean addProduct(Products products) {
        logger.info("Adding product");
        return productRepository.addProduct(products);
    }

    public List<Products> searchProductsByQuery (int categoryId, String query) {
        logger.info("Fetching all products by categoryId : {}  and search string : {}" , categoryId, query);
        return productRepository.searchProductsByQuery(categoryId, query);
    }
}
