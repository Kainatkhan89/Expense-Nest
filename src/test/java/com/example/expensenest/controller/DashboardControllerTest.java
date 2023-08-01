package com.example.expensenest.controller;

import com.example.expensenest.entity.DataPoint;
import com.example.expensenest.entity.Invoice;
import com.example.expensenest.entity.User;
import com.example.expensenest.service.DashboardService;
import com.example.expensenest.service.InvoiceService;
import com.example.expensenest.service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DashboardControllerTest {

    @Mock
    private InvoiceService invoiceService;
    @Mock
    private DashboardService dashboardService;
    @Mock
    private SessionService sessionService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;
    @Mock
    private Model model;

    @InjectMocks
    private DashboardController dashboardController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserDashboard() {
        // Mock data
        User user = new User();
        user.setId(1);
        List<Map<String, Object>> invoiceData = new ArrayList<>();
        List<DataPoint> chartData = new ArrayList<>();
        List<DataPoint> barData = new ArrayList<>();
        List<DataPoint> pieData = new ArrayList<>();
        String userName = "TestUser";

        when(sessionService.getSession(session)).thenReturn(user);
        when(dashboardService.getInvoiceData(user.getId())).thenReturn(invoiceData);
        when(dashboardService.getUserName(user.getId())).thenReturn(userName);
        when(dashboardService.getChartData()).thenReturn(chartData);
        when(dashboardService.getBarData()).thenReturn(barData);
        when(dashboardService.getPieData()).thenReturn(pieData);

        // Execute the method
        String viewName = dashboardController.getUserDashboard(request, session, model);

        // Verify the results
        assertEquals("dashboard", viewName);

        // Verify that sessionService.getSession(session) is called
        verify(sessionService).getSession(session);

        // Verify that dashboardService.getInvoiceData(userId) is called
        verify(dashboardService).getInvoiceData(user.getId());

        // Verify that dashboardService.getUserName(userId) is called
        verify(dashboardService).getUserName(user.getId());

        // Verify that dashboardService.getChartData() is called
        verify(dashboardService).getChartData();

        // Verify that dashboardService.getBarData() is called
        verify(dashboardService).getBarData();

        // Verify that dashboardService.getPieData() is called
        verify(dashboardService).getPieData();

        // Verify that model attributes are added correctly
        verify(model).addAttribute("invoiceData", invoiceData);
        verify(model).addAttribute("userData", userName);
        verify(model).addAttribute("chartData", chartData);
        verify(model).addAttribute("barData", barData);
        verify(model).addAttribute("pieData", pieData);
    }

    @Test
    void testGetAllInvoices() {
        // Mock data
        User user = new User();
        user.setId(1);
        List<Invoice> invoices = new ArrayList<>();
        when(sessionService.getSession(session)).thenReturn(user);
        when(invoiceService.getUserInvoices(user.getId())).thenReturn(invoices);

        // Execute the method
        String viewName = dashboardController.getAllInvoices(request, session, model);

        // Verify the results
        assertEquals("allInvoices", viewName);
        verify(model).addAttribute("invoices", invoices);
        verify(model).addAttribute("user", user);
        verify(model).addAttribute("archivedState", false);
    }

    @Test
    void testGetArchivedInvoices() {
        // Mock data
        User user = new User();
        user.setId(1);
        List<Invoice> archivedInvoices = new ArrayList<>();
        when(sessionService.getSession(session)).thenReturn(user);
        when(invoiceService.getUserInvoices(user.getId())).thenReturn(archivedInvoices);

        // Execute the method
        String viewName = dashboardController.getArchivedInvoices(request, session, model);

        // Verify the results
        assertEquals("allInvoices", viewName);
        verify(model).addAttribute("invoices", archivedInvoices);
        verify(model).addAttribute("user", user);
        verify(model).addAttribute("archivedState", true);
    }

    @Test
    void testSearchInvoices() {
        // Mock data
        User user = new User();
        user.setId(1);
        List<Invoice> filteredInvoices = new ArrayList<>();
        String queryString = "search query";
        when(sessionService.getSession(session)).thenReturn(user);
        when(invoiceService.getFilteredInvoices(user.getId(), queryString)).thenReturn(filteredInvoices);

        // Execute the method
        String viewName = dashboardController.searchInvoices(request, session, model, queryString);

        // Verify the results
        assertEquals("allInvoices", viewName);
        verify(model).addAttribute("invoices", filteredInvoices);
        verify(model).addAttribute("user", user);
        verify(model).addAttribute("archivedState", false);
    }

    @Test
    void testSearchArchivedInvoices() {
        // Mock data
        User user = new User();
        user.setId(1);
        List<Invoice> filteredArchivedInvoices = new ArrayList<>();
        String queryString = "search query";
        when(sessionService.getSession(session)).thenReturn(user);
        when(invoiceService.getFilteredInvoices(user.getId(), queryString)).thenReturn(filteredArchivedInvoices);

        // Execute the method
        String viewName = dashboardController.searchArchivedInvoices(request, session, model, queryString);

        // Verify the results
        assertEquals("allInvoices", viewName);
        verify(model).addAttribute("invoices", filteredArchivedInvoices);
        verify(model).addAttribute("user", user);
        verify(model).addAttribute("archivedState", true);
    }

    @Test
    void testEditProfile() {
        // Execute the method
        String viewName = dashboardController.editProfile(session);

        // Verify the results
        assertEquals("editprofile", viewName);
    }

    @Test
    void testArchiveInvoices() {
        // Mock data
        String invoiceId = "1";
        String archivedReason = "Test reason";

        // Execute the method
        String viewName = dashboardController.archiveInvoices(invoiceId, archivedReason);

        // Verify the results
        assertEquals("redirect:/invoices", viewName);
        verify(invoiceService).updateInvoiceArchiveData(1, true, archivedReason);
    }

    @Test
    void testUnarchiveInvoices() {
        // Mock data
        String invoiceId = "1";

        // Execute the method
        String viewName = dashboardController.unrachiveInvoices(invoiceId);

        // Verify the results
        assertEquals("redirect:/archived", viewName);
        verify(invoiceService).updateInvoiceArchiveData(1, false, null);
    }
}
