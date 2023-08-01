package com.example.expensenest.repository;

import com.example.expensenest.entity.InvoiceItems;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class InvoiceItemsRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ProductRepository productRepository;


    public InvoiceItemsRepository(JdbcTemplate jdbcTemplate, ProductRepository productRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.productRepository = productRepository;
    }

    public List<InvoiceItems> findItemsByInvoice(int invoiceId) {
        String sql = "SELECT * FROM ReceiptItems WHERE receiptId=" + invoiceId;
        return jdbcTemplate.query(sql, new InvoiceItemsRowMapper(productRepository));
    }

    private static class InvoiceItemsRowMapper implements RowMapper<InvoiceItems> {

        private ProductRepository productRepository;
        public InvoiceItemsRowMapper(ProductRepository productRepository) {
            this.productRepository = productRepository;
        }
        @Override
        public InvoiceItems mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            InvoiceItems item = new InvoiceItems();
            item.setInvoiceId(resultSet.getInt("id"));
            item.setQuantity(resultSet.getInt("quantity"));
            item.setProduct(productRepository.findProductById(resultSet.getInt("productId")));
            return item;
        }
    }
}
