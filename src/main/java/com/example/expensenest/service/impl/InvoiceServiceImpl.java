package com.example.expensenest.service.impl;

import com.example.expensenest.entity.Invoice;
import com.example.expensenest.repository.InvoiceRepository;
import com.example.expensenest.service.InvoiceService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private static final Logger logger = LogManager.getLogger(InvoiceServiceImpl.class);
    private InvoiceRepository invoiceRepository;
    public InvoiceServiceImpl (InvoiceRepository invoiceRepository) {
        super();
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public List<Invoice> getUserInvoices(int userId) {
        logger.info("Fetching all invoices by userId : {}" , userId);
        return invoiceRepository.findAllUserInvoices(userId);
    }

    @Override
    public List<Invoice> getFilteredInvoices(int userId, String searchString) {
        logger.info("Fetching all invoices by userId : {} and search query : {}" , userId, searchString);
        return invoiceRepository.getAllUserFilteredInvoices(userId, searchString);
    }

    @Override
    public boolean updateInvoiceArchiveData (int invoiceId, boolean isArchived, String archiveReason) {
        logger.info("Updating invoices by invoiceId : {}" , invoiceId);
        return invoiceRepository.updateInvoiceArchiveData(invoiceId, isArchived, archiveReason);
    }
}
