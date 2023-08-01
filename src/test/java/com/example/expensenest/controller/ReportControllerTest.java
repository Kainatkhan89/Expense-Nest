package com.example.expensenest.controller;

import com.example.expensenest.entity.Report;
import com.example.expensenest.entity.SalesReport;
import com.example.expensenest.entity.User;
import com.example.expensenest.service.ReportService;
import com.example.expensenest.service.SessionService;
import com.example.expensenest.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@Controller
public class ReportControllerTest {

    @Mock
    private Model model;

    @Mock
    private HttpSession session;

    @Mock
    private HttpServletRequest httpServletRequest;


    @Mock
    private SessionService sessionService;

    @Mock
    private UserService userService;


    @Mock
    private ReportService reportService;
    @InjectMocks
    private ReportController reportController;

    @Mock
    private User user;
    @Mock
    private User userInfo;

    @Mock
    private Report report;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1);

        report = new Report("2023-07-21", "2023-07-23");

        List<SalesReport> mockReportData = new ArrayList<>();
        mockReportData.add(new SalesReport());
        when(reportService.getSalesReportData(user.getId(), report.getStartDate(), report.getEndDate()))
                .thenReturn(mockReportData);
    }

    @Test
    void getReportsPageTest() {

        Model model = mock(Model.class);
        when(sessionService.getSession(session)).thenReturn(user);

        String viewName = reportController.getReportsPage(model, httpServletRequest,session);


        verify(sessionService).getSession(session);
        verify(userService).getUserProfile(user.getId());
        verify(model, times(1)).addAttribute(eq("report"), any(Report.class));

        assertEquals("/reports", viewName);
    }

    @Test
    void generateReportTest() {
        User user = new User("user@gmail.com");
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);
        Report report = new Report(startDate.toString(), endDate.toString());

        List<SalesReport> mockReportData = new ArrayList<SalesReport>();
        when(sessionService.getSession(session)).thenReturn(user);
        when(reportController.getReportData(user.getId(), startDate.toString(), endDate.toString())).thenReturn(mockReportData);

        String resultViewName = reportController.generateReport(report, model, session);

        assertEquals("salesReport", resultViewName);
    }

//    @Test
//    void generateReportTest() {
//
//        Model model = mock(Model.class);
//        when(sessionService.getSession(session)).thenReturn(user);
//        String viewName = reportController.generateReport(report, model, session);
//
////        verify(model).addAttribute("report", report);
//
//        List<SalesReport> expectedReportData = new ArrayList<>();
//        expectedReportData.add(new SalesReport());
//        verify(sessionService).getSession(session);
////        verify(model).addAttribute("report", report);
////        verify(model).addAttribute("reportData", expectedReportData);
//        verify(model, times(1)).addAttribute(eq("reportData"), any(SalesReport.class));
//        assertEquals("salesReport", viewName);
//    }

    @Test
    void getReportDataTest() {
        // Call the helper method
        List<SalesReport> reportData = reportController.getReportData(user.getId(), "2023-07-01", "2023-07-03");

        List<SalesReport> expectedReportData = new ArrayList<>();

        assertEquals(expectedReportData, reportData);
    }
}

