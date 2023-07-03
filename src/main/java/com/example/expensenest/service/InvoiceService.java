package com.example.expensenest.service;

import com.example.expensenest.entity.Invoice;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InvoiceService {
    List<Invoice> getUserInvoices(int userId);
}
