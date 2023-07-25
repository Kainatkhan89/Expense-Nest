package com.example.expensenest.controller;

import com.example.expensenest.entity.Category;
import com.example.expensenest.entity.Products;
import com.example.expensenest.entity.User;
import com.example.expensenest.enums.CategoryType;
import com.example.expensenest.service.CategoryService;
import com.example.expensenest.service.InvoiceService;
import com.example.expensenest.service.ProductService;
import com.example.expensenest.service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SellerDashboardController {

    private InvoiceService invoiceService;
    private SessionService sessionService;

    private CategoryService categoryService;
    private ProductService productService;

    public SellerDashboardController(InvoiceService invoiceService, SessionService sessionService,
                                     CategoryService categoryService, ProductService productService) {
        this.invoiceService = invoiceService;
        this.sessionService = sessionService;
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @GetMapping("/seller/dashboard")
    public String getSellerDashboard (HttpServletRequest request, HttpSession session, Model model) {
        User userSession = sessionService.getSession(session);
        model.addAttribute("user", userSession);
        return "sellerDashboard";
    }

    @GetMapping("/manage/category")
    public String getCategories (HttpServletRequest request, HttpSession session, Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "categories";
    }

    @GetMapping("/add/category")
    public String addCategories (HttpServletRequest request, HttpSession session, Model model) {
        model.addAttribute("categoryTypes", CategoryType.values());
        model.addAttribute("category", new Category());
        return "addCategory";
    }

    @PostMapping("/create/category")
    public String createCategory (@ModelAttribute("category") Category category) {
        category.setImage(category.formatImageData(category.getImage()));
        categoryService.addCategory(category);
        return "redirect:/manage/category";
    }

    @GetMapping("/category/{categoryId}")
    public String getProductsByCategory (HttpServletRequest request, HttpSession session, Model model, @PathVariable(value="categoryId") String categoryId) {
        Category category = categoryService.getCategoryById(Integer.valueOf(categoryId));
        model.addAttribute("category", category);
        model.addAttribute("products", productService.getProductsByCategory(Integer.valueOf(categoryId)));
        return "categoryProducts";
    }

    @GetMapping("/add/product")
    public String addNewProduct (HttpServletRequest request, HttpSession session, Model model) {
        model.addAttribute("categoryTypes", categoryService.getAllCategories());
        model.addAttribute("product", new Products());
        return "addProduct";
    }

    @PostMapping("/create/product")
    public String createProduct (@ModelAttribute("product") Products product) {
        product.storeAndProcessImage();
        productService.addProduct(product);
        return "redirect:/manage/category";
    }

    @PostMapping("/category/{categoryId}")
    public String searchProducts (Model model,@PathVariable(value="categoryId") String categoryId, @ModelAttribute("queryString") String queryString) {
        Category category = categoryService.getCategoryById(Integer.valueOf(categoryId));
        model.addAttribute("category", category);
        model.addAttribute("products", productService.searchProductsByQuery(Integer.valueOf(categoryId), queryString));
        return "categoryProducts";
    }

    @GetMapping("/seller/invoices")
    public String getAllInvoices (HttpServletRequest request, HttpSession session, Model model) {
        User userSession = sessionService.getSession(session);
        model.addAttribute("invoices", invoiceService.getUserInvoices(userSession.getId()));
        model.addAttribute("user", userSession);
        model.addAttribute("archivedState", false);
        return "allInvoicesSeller";
    }

    @PostMapping("/seller/invoices")
    public String searchInvoices (HttpServletRequest request, HttpSession session, Model model, @ModelAttribute("queryString") String queryString) {
        User userSession = sessionService.getSession(session);
        model.addAttribute("invoices", invoiceService.getFilteredInvoices(userSession.getId(), queryString));
        model.addAttribute("user", userSession);
        model.addAttribute("archivedState", false);
        return "allInvoicesSeller";
    }
    @GetMapping("/seller/archived")
    public String getArchivedInvoices (HttpServletRequest request, HttpSession session, Model model) {
        User userSession = sessionService.getSession(session);
        model.addAttribute("invoices", invoiceService.getUserInvoices(userSession.getId()));
        model.addAttribute("user", userSession);
        model.addAttribute("archivedState", true);
        return "allInvoicesSeller";
    }

    @PostMapping("/seller/archived")
    public String searchArchivedInvoices (HttpServletRequest request, HttpSession session, Model model, @ModelAttribute("queryString") String queryString) {
        User userSession = sessionService.getSession(session);
        model.addAttribute("invoices", invoiceService.getFilteredInvoices(userSession.getId(), queryString));
        model.addAttribute("user", userSession);
        model.addAttribute("archivedState", true);
        return "allInvoicesSeller";
    }

    @PostMapping("/seller/archive/{invoiceId}")
    public String archiveInvoices (@PathVariable(value="invoiceId") String invoiceId, @ModelAttribute("archivedReason") String archivedReason) {
        invoiceService.updateInvoiceArchiveData(Integer.valueOf(invoiceId), true, archivedReason);
        return "redirect:/seller/invoices";
    }

    @PostMapping("/seller/unarchive/{invoiceId}")
    public String unrachiveInvoices (@PathVariable(value="invoiceId") String invoiceId) {
        invoiceService.updateInvoiceArchiveData(Integer.valueOf(invoiceId), false, null);
        return "redirect:/seller/archived";
    }

}
