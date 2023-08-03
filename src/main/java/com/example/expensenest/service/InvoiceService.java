package com.example.expensenest.service;

import com.example.expensenest.entity.Invoice;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InvoiceService {
    List<Invoice> getUserInvoices(int userId);
    List<Invoice> getFilteredInvoices(int userId, String searchString);

    List<Invoice> getSellerInvoices(int userId);
    List<Invoice> getFilteredSellerInvoices(int userId, String searchString);
    boolean updateInvoiceArchiveData (int invoiceId, boolean isArchived, String archiveReason);
}
