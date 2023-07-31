package com.example.expensenest.service.impl;

import com.example.expensenest.entity.DataPoint;
import com.example.expensenest.repository.UserInsightsRepository;
import com.example.expensenest.service.DashboardService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {
    private final JdbcTemplate jdbcTemplate;
    public DashboardServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String getUserName(int userId) {
        RowMapper<String> userRowMapper = (rs, rowNum) -> {
            String username = rs.getString("name");
            return username;
        };

        // Fetch data
        String userName = jdbcTemplate.query("SELECT SUBSTRING(name, 1, 1) AS name FROM User WHERE id =" + userId, userRowMapper).get(0);

        return userName;
    }

    @Override
    public List<Map<String, Object>> getInvoiceData(int userId) {
        Map<String, Object> invoiceData = new HashMap<>();

        RowMapper<Map<String, Object>> invoiceRowMapper = (rs, rowNum) -> {
            Map<String, Object> invoice = new HashMap<>();
            invoice.put("id", rs.getInt("id"));
            invoice.put("totalAmount", rs.getInt("totalAmount"));
            invoice.put("companyName", rs.getString("name"));
            return invoice;
        };

        // Fetch data
        List<Map<String, Object>> invoice = jdbcTemplate.query("SELECT r.id, r.totalAmount, c.name FROM receipt r INNER JOIN company c ON r.sellerId = c.id WHERE r.userId = ?", invoiceRowMapper, userId);

//        invoiceData.put("invoice", invoice);

        return invoice;
    }

    @Override
    public List<DataPoint> getChartData() {
        RowMapper<DataPoint> dataPointRowMapper = (rs, rowNum) -> {
            String label = rs.getString("price");
            double value = rs.getDouble("quantity");
            return new DataPoint(label, value);
        };

        // Fetch data
        List<DataPoint> chartData = jdbcTemplate.query("SELECT t1.price, t2.quantity FROM products t1 RIGHT JOIN receiptitems t2 ON t1.id = t2.productId LIMIT 5", dataPointRowMapper);

        return chartData;
    }

    @Override
    public List<DataPoint> getBarData() {
        RowMapper<DataPoint> barRowMapper = (rs, rowNum) -> {
            double totalAmount = rs.getDouble("totalAmount");
            Date timeStamp = rs.getDate("dateOfPurchase");
            return new DataPoint(totalAmount, timeStamp);
        };

        // Fetch data
        List<DataPoint> barData = jdbcTemplate.query("SELECT totalAmount, dateOfPurchase FROM receipt LIMIT 5", barRowMapper);

        return barData;
    }

    @Override
    public List<DataPoint> getPieData() {
        RowMapper<DataPoint> pieRowMapper = (rs, rowNum) -> {
            String name = rs.getString("name");
            Integer sumAmount = rs.getInt("totalAmount");
            return new DataPoint(name, sumAmount);
        };

        // Fetch data
        List<DataPoint> pieData = jdbcTemplate.query("SELECT c.name, SUM(r.totalAmount) AS totalAmount FROM company c JOIN receipt r ON c.id = r.sellerId GROUP BY c.name LIMIT 5", pieRowMapper);

        return pieData;
    }

    @Override
    public List<DataPoint> getSevenData(int sellerId) {
        RowMapper<DataPoint> sevenRowMapper = (rs, rowNum) -> {
            Date timeStamp = rs.getDate("dateOfPurchase");
            Integer sumSales = rs.getInt("totalAmount");
            return new DataPoint(timeStamp, sumSales);
        };

        // Fetch data
        List<DataPoint> sevenData = jdbcTemplate.query("SELECT DATE(r.dateOfPurchase) as dateOfPurchase, SUM(r.totalAmount) as totalAmount FROM receipt r WHERE sellerId = ? AND IsArchived = 0 AND r.dateOfPurchase >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) GROUP BY DATE(r.dateOfPurchase)", sevenRowMapper, sellerId);
        return sevenData;
    }

    @Override
    public List<DataPoint> getCompareData(int sellerId) {
        RowMapper<DataPoint> compareRowMapper = (rs, rowNum) -> {
            String name = rs.getString("name");
            Integer sumAmount = rs.getInt("totalSum");
            return new DataPoint(name, sumAmount);
        };

        // Fetch data
        List<DataPoint> compareData = jdbcTemplate.query("SELECT name, totalSum\n" +
                "FROM (\n" +
                "  SELECT p.name, SUM(ri.quantity) as totalSum,\n" +
                "         ROW_NUMBER() OVER (ORDER BY SUM(ri.quantity) DESC) as rank_desc,\n" +
                "         ROW_NUMBER() OVER (ORDER BY SUM(ri.quantity) ASC) as rank_asc\n" +
                "  FROM expensenest.products p \n" +
                "  JOIN expensenest.receiptItems ri ON p.id = ri.productid \n" +
                "  INNER JOIN receipt r ON ri.receiptId = r.id \n" +
                "  WHERE sellerid = ? AND isArchived = 0 \n" +
                "  GROUP BY p.name\n" +
                ") AS subquery\n" +
                "WHERE rank_desc = 1 OR rank_asc = 1;", compareRowMapper, sellerId);
        return compareData;
    }

    @Override
    public List<DataPoint> getWeekData(int sellerId) {
        RowMapper<DataPoint> weekRowMapper = (rs, rowNum) -> {
            Date timeStamp = rs.getDate("salesDate");
            Integer sumSales = rs.getInt("totalSum");
            return new DataPoint(timeStamp, sumSales);
        };

        // Fetch data
        List<DataPoint> weekData = jdbcTemplate.query("SELECT DATE(r.dateOfPurchase) as salesDate, SUM(r.totalAmount) as totalSum\n" +
                "FROM expensenest.receipt r\n" +
                "WHERE sellerid = ? \n" +
                "  AND IsArchived = 0\n" +
                "  AND r.dateOfPurchase >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)\n" +
                "  AND r.dateOfPurchase < CURDATE() \n" +
                "GROUP BY DATE(r.dateOfPurchase)\n" +
                "ORDER BY salesDate;\n", weekRowMapper, sellerId);
        return weekData;
    }

    @Override
    public List<DataPoint> getYesterdayData(int sellerId) {
        RowMapper<DataPoint> yesterdayRowMapper = (rs, rowNum) -> {
            Integer totalQuantities = rs.getInt("totalQuantities");
            Integer sumSales = rs.getInt("totalSum");
            return new DataPoint(totalQuantities, sumSales);
        };

        // Fetch data
        List<DataPoint> yesterdayData = jdbcTemplate.query("SELECT SUM(r.totalAmount) as totalSum, SUM(ri.quantity) as totalQuantities\n" +
                "FROM expensenest.receipt r\n" +
                "JOIN expensenest.receiptItems ri ON r.id = ri.receiptId\n" +
                "WHERE r.sellerid = ? \n" +
                "  AND r.IsArchived = 0\n" +
                "  AND r.dateOfPurchase >= DATE_SUB(NOW(), INTERVAL 1 DAY)", yesterdayRowMapper, sellerId);
        return yesterdayData;
    }
}
