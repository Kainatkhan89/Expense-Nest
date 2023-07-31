package com.example.expensenest.repository;

import com.example.expensenest.entity.UserInsightResponse;
import com.example.expensenest.entity.UserInsights;
import com.example.expensenest.entity.UserInsightsSalesData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class UserInsightsRepository {
    private static final Logger logger = LogManager.getLogger(UserInsightsRepository.class);
    private final JdbcTemplate jdbcTemplate;

    public UserInsightsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UserInsightResponse getUserInsightResponse(int sellerId) {

        UserInsightResponse userInsightResponse = new UserInsightResponse();

        //Category Chart
        var categoryChartData = getCategoryChartData(sellerId);

        if(categoryChartData != null) {
            userInsightResponse.getUserInsightsList().add(categoryChartData);
        }

        //Product Chart
        var productChartData = getProductChartData(sellerId);

        if(productChartData != null) {
            userInsightResponse.getUserInsightsList().add(productChartData);
        }

        //Sales Chart
        var totalSalesChartData = getTotalSalesChartData(sellerId);

        if(totalSalesChartData != null) {
            userInsightResponse.getUserInsightsList().add(totalSalesChartData);
        }

        //Total Sales Chart
        var salesReportChartData = getSalesReportChartData(sellerId);

        if(salesReportChartData != null) {
            userInsightResponse.setUserInsightsSalesDataList(salesReportChartData);
        }

        return userInsightResponse;
    }

    private UserInsights getCategoryChartData(int sellerId){

        try {
            //Query to get best category
            String fetchData = "SELECT c.id, c.name, COUNT(*) AS totalCount" +
                    "   FROM category c" +
                    "   JOIN products p ON c.id = p.category" +
                    "   JOIN receiptitems ri ON p.id = ri.productid" +
                    "   JOIN receipt r ON ri.receiptid = r.id" +
                    "   WHERE r.sellerid = ?" +
                    "   GROUP BY c.id, c.name" +
                    "   ORDER BY totalCount DESC" +
                    "   LIMIT 1;";
            List<Map<String, Object>> resultList = jdbcTemplate.queryForList(fetchData, sellerId);

            // Print the fetched data for checking
            for (Map<String, Object> row : resultList) {
                UserInsights userInsights = new UserInsights();

                userInsights.setId(1);
                userInsights.setTypeOfChart("Daily Total Count");
                userInsights.setName((String) row.get("name"));
                long longTotalCount = (long) row.get("totalCount");
                userInsights.setValue(longTotalCount);

                int categoryId = (int) row.get("id");
                fetchData="SELECT\n" +
                        "    CONCAT('[', GROUP_CONCAT(day ORDER BY day SEPARATOR ', '), ']') AS days_of_month_list,\n" +
                        "    CONCAT('[', GROUP_CONCAT(category_count ORDER BY day SEPARATOR ', '), ']') AS category_count_list\n" +
                        "FROM\n" +
                        "    (\n" +
                        "        SELECT\n" +
                        "            DAYOFMONTH(receipt.dateOfPurchase) AS day,\n" +
                        "            COALESCE(COUNT(category.id), 0) AS category_count\n" +
                        "        FROM\n" +
                        "            (\n" +
                        "                SELECT\n" +
                        "                    DATE_ADD(LAST_DAY(NOW() - INTERVAL 1 MONTH), INTERVAL 1 DAY) AS start_date,\n" +
                        "                    LAST_DAY(NOW()) AS end_date\n" +
                        "            ) AS date_range\n" +
                        "        LEFT JOIN receipt ON DATE(receipt.dateOfPurchase) BETWEEN date_range.start_date AND date_range.end_date\n" +
                        "        LEFT JOIN receiptitems ON receipt.id = receiptitems.receiptid\n" +
                        "        LEFT JOIN products ON receiptitems.productid = products.id\n" +
                        "        LEFT JOIN category ON products.category = category.id\n" +
                        "        WHERE category.id = ?\n" +
                        "        AND receipt.sellerid = ?\n" +
                        "        AND receipt.isArchived = 0\n" +
                        "        GROUP BY DAYOFMONTH(receipt.dateOfPurchase)\n" +
                        "        ORDER BY DAYOFMONTH(receipt.dateOfPurchase)\n" +
                        "    ) AS sales_data;\n";

                resultList = jdbcTemplate.queryForList(fetchData, categoryId, sellerId);

                // Print the fetched data for checking
                for (Map<String, Object> row2 : resultList) {

                    userInsights.setGraphStringY((String) row2.get("category_count_list"));
                    userInsights.setGraphStringX((String) row2.get("days_of_month_list"));

                }

                return userInsights;
            }
        }catch (Exception ex)
        {
            logger.error("An exception occurred while fetching best selling category's data from the database.", ex);
        }
        return null;
    }

    private UserInsights getProductChartData(int sellerId){

        try {
            //Query to get best product
            String fetchData = "SELECT p.id, p.name, SUM(ri.quantity) as totalSum " +
                    "FROM products p " +
                    "JOIN receiptItems ri ON p.id = ri.productid " +
                    "INNER JOIN receipt r ON ri.receiptId = r.id " +
                    "WHERE sellerid = ? AND isArchived = 0 " +
                    "GROUP BY p.id, p.name " +
                    "ORDER BY totalSum DESC " +
                    "LIMIT 1;";
            List<Map<String, Object>> resultList = jdbcTemplate.queryForList(fetchData, sellerId);

            // Print the fetched data for checking
            for (Map<String, Object> row : resultList) {
                UserInsights userInsights = new UserInsights();

                userInsights.setId(2);
                userInsights.setTypeOfChart("Daily Total Count");
                userInsights.setName((String) row.get("name"));
                BigDecimal totalSum = (BigDecimal) row.get("totalSum");
                double doubleTotalSum = totalSum.doubleValue();
                userInsights.setValue(doubleTotalSum);

                int categoryId = (int) row.get("id");

                fetchData = "SELECT" +
                        "            CONCAT('[', GROUP_CONCAT(day ORDER BY day SEPARATOR ', '), ']') AS days_of_month_list," +
                        "            CONCAT('[', GROUP_CONCAT(sales_count ORDER BY day SEPARATOR ', '), ']') AS sales_count_list" +
                        "        FROM" +
                        "            (" +
                        "                SELECT" +
                        "                    DAYOFMONTH(receipt.dateOfPurchase) AS day," +
                        "                    COALESCE(SUM(quantity), 0) AS sales_count" +
                        "                FROM" +
                        "                    (" +
                        "                        SELECT" +
                        "                            DATE_ADD(LAST_DAY(NOW() - INTERVAL 1 MONTH), INTERVAL 1 DAY) AS start_date," +
                        "                            LAST_DAY(NOW()) AS end_date" +
                        "                    ) AS date_range" +
                        "                LEFT JOIN receipt ON DATE(receipt.dateOfPurchase) " +
                        "                BETWEEN date_range.start_date AND date_range.end_date" +
                        "                LEFT JOIN receiptitems ON receipt.id = receiptitems.receiptid" +
                        "                LEFT JOIN products ON receiptitems.productid = products.id" +
                        "                LEFT JOIN category ON products.category = category.id" +
                        "                WHERE  products.id = ?" +
                        "                AND receipt.sellerid = ?" +
                        "                and receipt.isArchived = 0" +
                        "                GROUP BY" +
                        "                    DAYOFMONTH(receipt.dateOfPurchase)" +
                        "                ORDER BY" +
                        "                    DAYOFMONTH(receipt.dateOfPurchase)" +
                        "            ) AS sales_data";

                resultList = jdbcTemplate.queryForList(fetchData, categoryId, sellerId);

                // Print the fetched data for checking
                for (Map<String, Object> row2 : resultList) {

                    userInsights.setGraphStringY((String) row2.get("sales_count_list"));
                    userInsights.setGraphStringX((String) row2.get("days_of_month_list"));

                }

                return userInsights;
            }
        }catch (Exception ex){
            logger.error("An exception occurred while fetching best product's data from the database.", ex);
        }
        return null;
    }

    private UserInsights getTotalSalesChartData(int sellerId){

        try {
            //Query to get total sales
            String fetchData = "SELECT SUM(r.totalAmount) as totalSum FROM receipt r WHERE sellerid = ? AND IsArchived = 0";
            List<Map<String, Object>> resultList = jdbcTemplate.queryForList(fetchData, sellerId);

            // Print the fetched data for checking
            for (Map<String, Object> row : resultList) {
                UserInsights userInsights = new UserInsights();

                userInsights.setId(3);
                userInsights.setTypeOfChart("Daily Total Sum");
                userInsights.setName("");
                BigDecimal totalSum = (BigDecimal) row.get("totalSum");
                double doubleTotalSum = totalSum.doubleValue();
                userInsights.setValue(doubleTotalSum);

                fetchData = "SELECT" +
                        "    CONCAT('[', GROUP_CONCAT(day ORDER BY day SEPARATOR ', '), ']') AS days_of_month_list," +
                        "    CONCAT('[', GROUP_CONCAT(sale_amount ORDER BY day SEPARATOR ', '), ']') AS sale_amount_list" +
                        "   FROM" +
                        "    (" +
                        "        SELECT" +
                        "            DAYOFMONTH(receipt.dateOfPurchase) AS day," +
                        "            COALESCE(SUM(receipt.totalAmount), 0) AS sale_amount" +
                        "        FROM" +
                        "            (" +
                        "                SELECT" +
                        "                    DATE_ADD(LAST_DAY(NOW() - INTERVAL 1 MONTH), INTERVAL 1 DAY) AS start_date," +
                        "                    LAST_DAY(NOW()) AS end_date" +
                        "            ) AS date_range" +
                        "        LEFT JOIN receipt ON DATE(receipt.dateOfPurchase) " +
                        "        BETWEEN date_range.start_date AND date_range.end_date        " +
                        "        WHERE receipt.sellerid = ?" +
                        "        AND receipt.isArchived = 0" +
                        "        GROUP BY DAYOFMONTH(receipt.dateOfPurchase)" +
                        "        ORDER BY DAYOFMONTH(receipt.dateOfPurchase)" +
                        "    ) AS sales_data;";

                resultList = jdbcTemplate.queryForList(fetchData, sellerId);

                // Print the fetched data for checking
                for (Map<String, Object> row2 : resultList) {

                    userInsights.setGraphStringY((String) row2.get("sale_amount_list"));
                    userInsights.setGraphStringX((String) row2.get("days_of_month_list"));

                }

                return userInsights;
            }
        }catch (Exception ex){
            logger.error("An exception occurred while fetching total sales data from the database.", ex);
        }
        return null;
    }

    private List<UserInsightsSalesData> getSalesReportChartData(int sellerId)
    {

        try {
            List<UserInsightsSalesData> response = new ArrayList<UserInsightsSalesData>();
            //Query to get total sales
            String fetchData = "    SELECT" +
                    "    category_name," +
                    "    CONCAT('[', GROUP_CONCAT(total_amount ORDER BY month_num SEPARATOR ', '), ']') AS total_amount_list," +
                    "    CONCAT('[', GROUP_CONCAT(month_num ORDER BY month_num SEPARATOR ', '), ']') AS months_list" +
                    "   FROM" +
                    "    (" +
                    "        SELECT" +
                    "            category.id AS category_id," +
                    "            category.name AS category_name," +
                    "            MONTH(receipt.dateOfPurchase) AS month_num," +
                    "            COALESCE(SUM(receipt.totalAmount), 0) AS total_amount" +
                    "        FROM" +
                    "            (" +
                    "                SELECT" +
                    "                    DATE_ADD(DATE_ADD(LAST_DAY(NOW() - INTERVAL 1 MONTH), INTERVAL 1 DAY), INTERVAL -(n.n) MONTH) AS start_date," +
                    "                    DATE_ADD(LAST_DAY(NOW()), INTERVAL -(n.n) MONTH) AS end_date" +
                    "                FROM" +
                    "                    (SELECT 0 AS n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3" +
                    "                     UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7" +
                    "                     UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11) AS n" +
                    "            ) AS months" +
                    "        LEFT JOIN receipt ON DATE(receipt.dateOfPurchase) BETWEEN months.start_date AND months.end_date" +
                    "        LEFT JOIN receiptitems ON receipt.id = receiptitems.receiptid" +
                    "        LEFT JOIN products ON receiptitems.productid = products.id" +
                    "        LEFT JOIN category ON products.category = category.id" +
                    "        WHERE receipt.sellerid = ?" +
                    "        GROUP BY category_id, category_name, MONTH(receipt.dateOfPurchase)" +
                    "        ORDER BY category_id, MONTH(receipt.dateOfPurchase)" +
                    "    ) AS category_data" +
                    "   WHERE category_name IS NOT NULL" +
                    "   GROUP BY category_name;";
            List<Map<String, Object>> resultList = jdbcTemplate.queryForList(fetchData, sellerId);

            // Print the fetched data for checking
            for (Map<String, Object> row : resultList) {
                UserInsightsSalesData userInsightsSalesData = new UserInsightsSalesData();

                userInsightsSalesData.setName((String) row.get("category_name"));
                userInsightsSalesData.setData((String) row.get("total_amount_list"));
                userInsightsSalesData.setMonth((String) row.get("months_list"));

                response.add(userInsightsSalesData);
            }
            return response;
        }catch (Exception ex){
            logger.error("An exception occurred while fetching sales report data from the database.", ex);
        }
        return null;
    }

}
