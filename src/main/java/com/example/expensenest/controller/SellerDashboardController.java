package com.example.expensenest.controller;

import com.example.expensenest.entity.Category;
import com.example.expensenest.entity.DataPoint;
import com.example.expensenest.entity.Products;
import com.example.expensenest.entity.User;
import com.example.expensenest.enums.CategoryType;
import com.example.expensenest.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class SellerDashboardController {

    private SessionService sessionService;
    private CategoryService categoryService;
    private ProductService productService;
    private DashboardService sellerdashboardService;

    public SellerDashboardController(DashboardService sellerdashboardService, SessionService sessionService,
                                     CategoryService categoryService, ProductService productService) {
        this.sessionService = sessionService;
        this.categoryService = categoryService;
        this.productService = productService;
        this.sellerdashboardService = sellerdashboardService;
    }

    @GetMapping("/seller/dashboard")
    public String getSellerDashboard (HttpServletRequest request, HttpSession session, Model model) {
        User userSession = sessionService.getSession(session);

        model.addAttribute("user", userSession);

        int sellerId = userSession.getId();
        List<DataPoint> sevenData = sellerdashboardService.getSevenData(sellerId);
        model.addAttribute("sevenData", sevenData);

        List<DataPoint> compareData = sellerdashboardService.getCompareData(sellerId);
        model.addAttribute("compareData", compareData);

        List<DataPoint> weekData = sellerdashboardService.getWeekData(sellerId);
        model.addAttribute("weekData", weekData);

        List<DataPoint> yesterdayData = sellerdashboardService.getYesterdayData(sellerId);
        model.addAttribute("yesterdayData", yesterdayData);
        return "sellerDashboard";
    }

    @GetMapping("/manage/category")
    public String getCategories (HttpServletRequest request, HttpSession session, Model model) {
        User userSession = sessionService.getSession(session);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("user", userSession);
        return "categories";
    }

    @GetMapping("/add/category")
    public String addCategories (HttpServletRequest request, HttpSession session, Model model) {
        User userSession = sessionService.getSession(session);
        model.addAttribute("categoryTypes", CategoryType.values());
        model.addAttribute("category", new Category());
        model.addAttribute("user", userSession);
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
        User userSession = sessionService.getSession(session);
        model.addAttribute("user", userSession);
        Category category = categoryService.getCategoryById(Integer.valueOf(categoryId));
        model.addAttribute("category", category);
        model.addAttribute("products", productService.getProductsByCategory(Integer.valueOf(categoryId)));
        return "categoryProducts";
    }

    @GetMapping("/add/product")
    public String addNewProduct (HttpServletRequest request, HttpSession session, Model model) {
        User userSession = sessionService.getSession(session);
        model.addAttribute("user", userSession);
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
    public String searchProducts (Model model, HttpSession session, @PathVariable(value="categoryId") String categoryId, @ModelAttribute("queryString") String queryString) {
        User userSession = sessionService.getSession(session);
        model.addAttribute("user", userSession);
        Category category = categoryService.getCategoryById(Integer.valueOf(categoryId));
        model.addAttribute("category", category);
        model.addAttribute("products", productService.searchProductsByQuery(Integer.valueOf(categoryId), queryString));
        return "categoryProducts";
    }
}
