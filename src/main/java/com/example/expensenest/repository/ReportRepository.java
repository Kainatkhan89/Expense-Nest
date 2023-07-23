package com.example.expensenest.repository;

import com.example.expensenest.entity.SalesReport;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ReportRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReportRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<SalesReport> getSalesReportBySellerID(int id) {
        String sql = "    select p.name,sum(ri.quantity) as 'qty',p.price, sum(p.price*ri.quantity) as 'amount' from products p\n" +
                "    inner join receiptitems ri\n" +
                "    on ri.productid = p.id\n" +
                "    inner join Receipt r\n" +
                "    on r.id = ri.receiptId\n" +
                "    where r.sellerid = " + id +
                "    group by p.name,p.price;";

        return jdbcTemplate.query(sql, new ReportRepository.SalesReportRowMapper());

    }






    private static class SalesReportRowMapper implements RowMapper<SalesReport> {
        @Override
        public SalesReport mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            SalesReport salesReport = new SalesReport();
            salesReport.setName(resultSet.getString("name"));
            salesReport.setPrice(resultSet.getInt("price"));
            salesReport.setQty(resultSet.getInt("qty"));
            salesReport.setAmount(resultSet.getInt("amount"));
//        product.setId(resultSet.getInt("id"));
//        product.setName(resultSet.getString("name"));
//        product.setPrice(resultSet.getInt("price"));
//        product.setCategory(categoryRepository.findCategoryById(resultSet.getInt("category")));
//        product.setImage(resultSet.getString("image"));
            return salesReport;
        }
    }





}