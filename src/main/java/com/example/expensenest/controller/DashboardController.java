package com.example.expensenest.controller;
import com.example.expensenest.entity.DataPoint;
import com.example.expensenest.entity.Invoice;
import com.example.expensenest.entity.User;
import com.example.expensenest.service.DashboardService;
import com.example.expensenest.service.InvoiceService;
import com.example.expensenest.service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


@Controller
public class DashboardController {

    private InvoiceService invoiceService;
    private SessionService sessionService;
    private DashboardService dashboardService;

    public DashboardController(InvoiceService invoiceService, DashboardService dashboardService, SessionService sessionService) {
        this.invoiceService = invoiceService;
        this.dashboardService = dashboardService;
        this.sessionService = sessionService;
    }

    @GetMapping("/dashboard")
    public String getUserDashboard (HttpServletRequest request, HttpSession session, Model model) {
        User userSession = sessionService.getSession(session);
        int userId = userSession.getId();
        model.addAttribute("invoiceData", dashboardService.getInvoiceData(userId));
        model.addAttribute("userData", dashboardService.getUserName(userId));

        List<DataPoint> chartData = dashboardService.getChartData();
        model.addAttribute("chartData", chartData);

        List<DataPoint> barData = dashboardService.getBarData();
        model.addAttribute("barData", barData);

        List<DataPoint> pieData = dashboardService.getPieData();
        model.addAttribute("pieData", pieData);

        return "dashboard";
    }

    @GetMapping("/invoices")
    public String getAllInvoices (HttpServletRequest request, HttpSession session, Model model) {
        User userSession = sessionService.getSession(session);
        model.addAttribute("invoices", invoiceService.getUserInvoices(userSession.getId()));
        model.addAttribute("user", userSession);
        model.addAttribute("archivedState", false);
        return "allInvoices";
    }

    @GetMapping("/archived")
    public String getArchivedInvoices (HttpServletRequest request, HttpSession session, Model model) {
        User userSession = sessionService.getSession(session);
        model.addAttribute("invoices", invoiceService.getUserInvoices(userSession.getId()));
        model.addAttribute("user", userSession);
        model.addAttribute("archivedState", true);
        return "allInvoices";
    }

    @PostMapping("/invoices")
    public String searchInvoices (HttpServletRequest request, HttpSession session, Model model, @ModelAttribute("queryString") String queryString) {
        User userSession = sessionService.getSession(session);
        model.addAttribute("invoices", invoiceService.getFilteredInvoices(userSession.getId(), queryString));
        model.addAttribute("user", userSession);
        model.addAttribute("archivedState", false);
        return "allInvoices";
    }

    @PostMapping("/archived")
    public String searchArchivedInvoices (HttpServletRequest request, HttpSession session, Model model, @ModelAttribute("queryString") String queryString) {
        User userSession = sessionService.getSession(session);
        model.addAttribute("invoices", invoiceService.getFilteredInvoices(userSession.getId(), queryString));
        model.addAttribute("user", userSession);
        model.addAttribute("archivedState", true);
        return "allInvoices";
    }

    @PostMapping("/editProfile")
    public String editProfile (HttpSession session) {
        User userSession = sessionService.getSession(session);
        return "editprofile";
    }

    @PostMapping("/archive/{invoiceId}")
    public String archiveInvoices (@PathVariable(value="invoiceId") String invoiceId, @ModelAttribute("archivedReason") String archivedReason) {
        invoiceService.updateInvoiceArchiveData(Integer.valueOf(invoiceId), true, archivedReason);
        return "redirect:/invoices";
    }

    @PostMapping("/unarchive/{invoiceId}")
    public String unrachiveInvoices (@PathVariable(value="invoiceId") String invoiceId) {
        invoiceService.updateInvoiceArchiveData(Integer.valueOf(invoiceId), false, null);
        return "redirect:/archived";
    }
}
