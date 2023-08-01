package com.example.expensenest.repository;

import com.example.expensenest.entity.Category;
import com.example.expensenest.entity.Products;
import com.example.expensenest.enums.CategoryType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CategoryRepository {

    private final JdbcTemplate jdbcTemplate;

    public CategoryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Category findCategoryById(int id) {
        String sql = "SELECT * FROM Category WHERE id=" + id;
        return jdbcTemplate.query(sql, new CategoryRowMapper()).get(0);
    }

    public List<Category> getAllCategories() {
        String sql = "SELECT c.id, c.name, c.image, COALESCE(p.totalProducts, 0) as totalProducts\n" +
                "FROM expensenest.Category c\n" +
                "LEFT JOIN (\n" +
                "    SELECT category, count(*) as totalProducts\n" +
                "    FROM expensenest.Products\n" +
                "    GROUP BY category\n" +
                ") p ON c.id = p.category;";
        return jdbcTemplate.query(sql, new CategoryRepository.CategoryRowMapper());
    }

    public boolean addCategory(Category category) {
        String sql = "INSERT INTO expensenest.Category (name, image) VALUES (?, ?)";
        jdbcTemplate.update(sql, category.getName(), category.getImage());
        return true;
    }

    public Category getCategoryById(int categoryId) {
        String sql = "SELECT * FROM expensenest.Category WHERE id="+ categoryId;
        return jdbcTemplate.query(sql, new CategoryRepository.CategoryRowMapper()).get(0);
    }

    private static class CategoryRowMapper implements RowMapper<Category> {
        @Override
        public Category mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Category category = new Category();
            category.setId(resultSet.getInt("id"));
            category.setName(resultSet.getString("name"));
            category.setImage(resultSet.getString("image"));
            if(checkIfColumnExists(resultSet, "totalProducts")) {
                category.setTotalProducts(resultSet.getInt("totalProducts"));
            }
            return category;
        }
    }

    private static boolean checkIfColumnExists (ResultSet resultSet, String columnName) throws SQLException {
        int columnCount = resultSet.getMetaData().getColumnCount();
        boolean columnExists = false;

        for (int i = 1; i <= columnCount; i++) {
            if (columnName.equalsIgnoreCase(resultSet.getMetaData().getColumnName(i))) {
                columnExists = true;
                break;
            }
        }

        return columnExists;
    }
}
