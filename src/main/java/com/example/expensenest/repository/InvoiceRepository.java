package com.example.expensenest.repository;

import com.example.expensenest.entity.Invoice;
import com.example.expensenest.entity.SellerInvoice;
import com.example.expensenest.entity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class InvoiceRepository {

    private final JdbcTemplate jdbcTemplate;
    private final InvoiceItemsRepository invoiceItemsRepository;

    static boolean isSellerInvoice = false;

    public InvoiceRepository(JdbcTemplate jdbcTemplate, InvoiceItemsRepository invoiceItemsRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.invoiceItemsRepository = invoiceItemsRepository;
    }
    public List<Invoice> findAllUserInvoices(int userId) {
        isSellerInvoice = false;
        String sql = "SELECT * FROM Receipt WHERE userId=" + userId;
        return jdbcTemplate.query(sql, new InvoiceRowMapper(invoiceItemsRepository));
    }

    public List<Invoice> getAllUserFilteredInvoices(int userId, String searchString) {
        isSellerInvoice = false;
        String sql;
        if(searchString.matches("-?\\d+")) {
            sql = "SELECT * FROM Receipt WHERE userId=" + userId +
                    " AND (id="+ searchString+ " OR dateOfPurchase LIKE '%"+ searchString +"%')";
        } else {
            sql = "SELECT * FROM Receipt WHERE userId=" + userId +
                    " AND (dateOfPurchase LIKE '%"+ searchString +"%')";
        }
        return jdbcTemplate.query(sql, new InvoiceRowMapper(invoiceItemsRepository));
    }

    public boolean updateInvoiceArchiveData (int invoiceId, boolean isArchived, String archiveReason) {
        String sql = "UPDATE Receipt SET isArchived  ="+ (isArchived == false ? 0 : 1) +", archivedReason ='"+ archiveReason + "' WHERE id =" + invoiceId + ";";
        System.out.println(sql);
        jdbcTemplate.update(sql);
        return false;
    }

    public List<Invoice> findSellerInvoices(int sellerId) {
        isSellerInvoice = true;
        String sql = "SELECT r.*, u.name, u.email FROM Receipt r inner join User u on r.userId=u.Id WHERE sellerId=" + sellerId;
        return jdbcTemplate.query(sql, new InvoiceRowMapper(invoiceItemsRepository));
    }

    public List<Invoice> getSellerFilteredInvoices(int sellerId, String searchString) {

        isSellerInvoice = true;
        String sql;
        if(searchString.matches("-?\\d+")) {
            sql = "SELECT r.*, u.name, u.email FROM Receipt r inner join User u on r.userId=u.Id WHERE sellerId=" + sellerId +
                    " AND (r.id="+ searchString+ " OR dateOfPurchase LIKE '%"+ searchString +"%' " +
                    "OR totalAmount LIKE '%"+ searchString +"%' OR LOWER(name) = LOWER('"+ searchString +"') OR email LIKE '%"+ searchString +"%')";
        } else {
            sql = "SELECT r.*, u.name, u.email FROM Receipt r inner join User u on r.userId=u.Id WHERE sellerId=" + sellerId +
                    " AND ( dateOfPurchase LIKE '%"+ searchString +"%' " +
                    "OR totalAmount LIKE '%"+ searchString +"%' OR LOWER(name) = LOWER('"+ searchString +"') OR email LIKE '%"+ searchString +"%')";
        }
        return jdbcTemplate.query(sql, new InvoiceRowMapper(invoiceItemsRepository));
    }

    private static class InvoiceRowMapper implements RowMapper<Invoice> {

        final InvoiceItemsRepository invoiceItemsRepository;
        public InvoiceRowMapper (InvoiceItemsRepository invoiceItemsRepository) {
            this.invoiceItemsRepository = invoiceItemsRepository;
        }

        @Override
        public Invoice mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Invoice invoice = new Invoice();
            invoice.setId(resultSet.getInt("id"));
            invoice.setBuyerId(resultSet.getInt("userId"));
            invoice.setName(isSellerInvoice?resultSet.getString("name"): "");
            invoice.setEmail(isSellerInvoice? resultSet.getString("email"): "");
            invoice.setSellerId(resultSet.getInt("sellerId"));
            invoice.setTotalAmount(resultSet.getInt("totalAmount"));
            invoice.setArchived(resultSet.getInt("isArchived") == 1);
            invoice.setArchivedReason(resultSet.getString("archivedReason"));
            invoice.setPurchaseDate(resultSet.getString("dateOfPurchase").split(" ")[0]);
            invoice.setItems(invoiceItemsRepository.findItemsByInvoice(resultSet.getInt("id")));
            return invoice;
        }

    }


}
