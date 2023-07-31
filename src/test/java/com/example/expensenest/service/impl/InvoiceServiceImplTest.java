package com.example.expensenest.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.expensenest.entity.Invoice;
import com.example.expensenest.repository.InvoiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class InvoiceServiceImplTest {

    private InvoiceServiceImpl invoiceService;
    private InvoiceRepository invoiceRepository;

    @BeforeEach
    void setUp() {
        // Create a mock of InvoiceRepository
        invoiceRepository = mock(InvoiceRepository.class);
        invoiceService = new InvoiceServiceImpl(invoiceRepository);
    }

    @Test
    void testGetUserInvoices() {
        // Arrange
        int userId = 1;
        List<Invoice> expectedInvoices = Arrays.asList(
                new Invoice(),
                new Invoice()
        );

        // Mock the behavior of InvoiceRepository's findAllUserInvoices method
        when(invoiceRepository.findAllUserInvoices(userId)).thenReturn(expectedInvoices);

        // Act
        List<Invoice> actualInvoices = invoiceService.getUserInvoices(userId);

        // Assert
        assertEquals(expectedInvoices, actualInvoices);
        // Optionally, verify that the method in the mock is called with the correct argument
        verify(invoiceRepository).findAllUserInvoices(userId);
    }

    @Test
    void testGetFilteredInvoices() {
        // Arrange
        int userId = 1;
        String searchString = "Invoice";
        List<Invoice> expectedInvoices = Arrays.asList(
                new Invoice(),
                new Invoice()
        );

        // Mock the behavior of InvoiceRepository's getAllUserFilteredInvoices method
        when(invoiceRepository.getAllUserFilteredInvoices(userId, searchString)).thenReturn(expectedInvoices);

        // Act
        List<Invoice> actualInvoices = invoiceService.getFilteredInvoices(userId, searchString);

        // Assert
        assertEquals(expectedInvoices, actualInvoices);
        // Optionally, verify that the method in the mock is called with the correct arguments
        verify(invoiceRepository).getAllUserFilteredInvoices(userId, searchString);
    }

    @Test
    void testUpdateInvoiceArchiveData() {
        // Arrange
        int invoiceId = 1;
        boolean isArchived = true;
        String archiveReason = "Archived for testing";

        // Mock the behavior of InvoiceRepository's updateInvoiceArchiveData method
        when(invoiceRepository.updateInvoiceArchiveData(invoiceId, isArchived, archiveReason)).thenReturn(true);

        // Act
        boolean result = invoiceService.updateInvoiceArchiveData(invoiceId, isArchived, archiveReason);

        // Assert
        assertTrue(result);
        // Optionally, verify that the method in the mock is called with the correct arguments
        verify(invoiceRepository).updateInvoiceArchiveData(invoiceId, isArchived, archiveReason);
    }
}
