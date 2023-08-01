package com.example.expensenest.repository;

import com.example.expensenest.entity.Products;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ProductRepository {

    private final JdbcTemplate jdbcTemplate;
    private final CategoryRepository categoryRepository;

    public ProductRepository(JdbcTemplate jdbcTemplate, CategoryRepository categoryRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.categoryRepository = categoryRepository;
    }

    public Products findProductById(int id) {
        String sql = "SELECT * FROM Products WHERE id=" + id;
        return jdbcTemplate.query(sql, new ProductsRowMapper(categoryRepository)).get(0);
    }

    public List<Products> getProductsByCategory(int categoryId) {
        String sql = "SELECT * FROM Products WHERE category=" + categoryId;
        return jdbcTemplate.query(sql, new ProductsRowMapper(categoryRepository));
    }

    public boolean addProduct(Products products) {
        String sql = "INSERT INTO expensenest.Products (name, price, category, image) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, products.getName(), products.getPrice(), products.getCategory(), products.getImage());
        return true;
    }

    public List<Products> searchProductsByQuery (int categoryId, String query) {
        String sql = "SELECT * FROM Products WHERE category=" + categoryId + " AND name LIKE '%"+ query + "%'";
        return jdbcTemplate.query(sql, new ProductsRowMapper(categoryRepository));
    }

    private static class ProductsRowMapper implements RowMapper<Products> {

        private CategoryRepository categoryRepository;
        public ProductsRowMapper(CategoryRepository categoryRepository) {
            this.categoryRepository = categoryRepository;
        }

        @Override
        public Products mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Products product = new Products();
            product.setId(resultSet.getInt("id"));
            product.setName(resultSet.getString("name"));
            product.setPrice(resultSet.getInt("price"));
            product.setCategory(resultSet.getInt("category"));
            product.setImage(resultSet.getString("image"));
            return product;
        }
    }
}
