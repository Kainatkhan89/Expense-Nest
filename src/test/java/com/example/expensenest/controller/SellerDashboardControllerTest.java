package com.example.expensenest.controller;

import static org.mockito.Mockito.*;

import com.example.expensenest.entity.Category;
import com.example.expensenest.entity.Products;
import com.example.expensenest.entity.User;
import com.example.expensenest.enums.CategoryType;
import com.example.expensenest.service.*;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SellerDashboardControllerTest {

    private MockMvc mockMvc;
    private SellerDashboardController sellerDashboardController;

    private InvoiceService invoiceService;
    private SessionService sessionService;
    private CategoryService categoryService;
    private ProductService productService;
    private DashboardService sellerDashboardService;

    @BeforeEach
    void setUp() {
        // Create mocks for services

        invoiceService = mock(InvoiceService.class);
        sessionService = mock(SessionService.class);
        categoryService = mock(CategoryService.class);
        productService = mock(ProductService.class);
        sellerDashboardService = mock(DashboardService.class);

        sellerDashboardController = new SellerDashboardController(invoiceService,sellerDashboardService, sessionService, categoryService, productService);
        mockMvc = MockMvcBuilders.standaloneSetup(sellerDashboardController).build();
    }

    @Test
    void testGetSellerDashboard() throws Exception {
        // Arrange
        MockHttpSession httpSession = new MockHttpSession();
        Model model = mock(Model.class);
        User user = new User("john@gmail.com", "John Doe");

        // Mock the behavior of the sessionService to return a dummy user
        when(sessionService.getSession(httpSession)).thenReturn(user);

        // Act & Assert
        mockMvc.perform(get("/seller/dashboard").session( httpSession))
                .andExpect(status().isOk())
                .andExpect(view().name("sellerDashboard"))
                .andExpect(model().attribute("user", user));
    }

    @Test
    void testGetCategories() throws Exception {
        // Arrange
        MockHttpSession httpSession = new MockHttpSession();
        Model model = mock(Model.class);
        User user = new User("john@gmail.com", "John Doe");
        List<Category> categories = Arrays.asList(new Category(), new Category());

        // Mock the behavior of the sessionService to return a dummy user
        when(sessionService.getSession(httpSession)).thenReturn(user);

        // Mock the behavior of the categoryService to return a list of dummy categories
        when(categoryService.getAllCategories()).thenReturn(categories);

        // Act & Assert
        mockMvc.perform(get("/manage/category").session(httpSession))
                .andExpect(status().isOk())
                .andExpect(view().name("categories"))
                .andExpect(model().attribute("user", user))
                .andExpect(model().attribute("categories", categories));
    }

    @Test
    void testAddCategories() throws Exception {
        // Arrange
        MockHttpSession httpSession = new MockHttpSession();
        Model model = mock(Model.class);
        User user = new User("john@gmail.com", "John Doe");

        // Mock the behavior of the sessionService to return a dummy user
        when(sessionService.getSession(httpSession)).thenReturn(user);

        List<Category> categories = Arrays.asList(new Category(), new Category());
        // Mock the behavior of the categoryService to return all category types
        when(categoryService.getAllCategories()).thenReturn(categories);

        // Act & Assert
        mockMvc.perform(get("/add/category").session(httpSession))
                .andExpect(status().isOk())
                .andExpect(view().name("addCategory"))
                .andExpect(model().attribute("user", user))
                .andExpect(model().attribute("categoryTypes", CategoryType.values()))
                .andExpect(model().attributeExists("category"));
    }

    @Test
    void testCreateCategory() throws Exception {
        // Arrange
        MockHttpSession httpSession = new MockHttpSession();
        Category category = new Category();
        category.setImage("Electronics");

        // Mock the behavior of the categoryService to add the category
        when(categoryService.addCategory(category)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/create/category").flashAttr("category", category).session( httpSession))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manage/category"));

        // Optionally, you can verify that the categoryService.addCategory() method is called with the correct argument
        verify(categoryService).addCategory(category);
    }

    @Test
    void testGetProductsByCategory() throws Exception {
        // Arrange
        MockHttpSession httpSession = new MockHttpSession();
        Model model = mock(Model.class);
        User user = new User("john@gmail.com", "John Doe");
        int categoryId = 1;
        Category category = new Category();
        List<Products> products = Arrays.asList(new Products(), new Products());

        // Mock the behavior of the sessionService to return a dummy user
        when(sessionService.getSession(httpSession)).thenReturn(user);

        // Mock the behavior of the categoryService to return a dummy category
        when(categoryService.getCategoryById(categoryId)).thenReturn(category);

        // Mock the behavior of the productService to return a list of dummy products
        when(productService.getProductsByCategory(categoryId)).thenReturn(products);

        // Act & Assert
        mockMvc.perform(get("/category/{categoryId}", categoryId).session( httpSession))
                .andExpect(status().isOk())
                .andExpect(view().name("categoryProducts"))
                .andExpect(model().attribute("user", user))
                .andExpect(model().attribute("category", category))
                .andExpect(model().attribute("products", products));
    }

    @Test
    void testAddNewProduct() throws Exception {
        // Arrange
        MockHttpSession httpSession = new MockHttpSession();
        Model model = mock(Model.class);
        User user = new User("john@gmail.com", "John Doe");
        List<Category> categoryTypes = Arrays.asList(new Category(), new Category());

        // Mock the behavior of the sessionService to return a dummy user
        when(sessionService.getSession(httpSession)).thenReturn(user);

        // Mock the behavior of the categoryService to return all category types
        when(categoryService.getAllCategories()).thenReturn(categoryTypes);

        // Act & Assert
        mockMvc.perform(get("/add/product").session( httpSession))
                .andExpect(status().isOk())
                .andExpect(view().name("addProduct"))
                .andExpect(model().attribute("user", user))
                .andExpect(model().attribute("categoryTypes", categoryTypes))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    void testCreateProduct() throws Exception {
        // Arrange
        MockHttpSession httpSession = new MockHttpSession();
        Products product = new Products();
        byte[] fileContent = "Dummy Image Content".getBytes();
        MultipartFile dummyImageFile = new MockMultipartFile("imageFile", "dummyImage.jpg", "image/jpeg", fileContent);

        // Set the dummy image file to the category
        product.setImageFile(dummyImageFile);

        // Mock the behavior of the productService to add the product
        when(productService.addProduct(product)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/create/product").flashAttr("product", product).session( httpSession))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manage/category"));

        // Optionally, you can verify that the productService.addProduct() method is called with the correct argument
        verify(productService).addProduct(product);
    }

    @Test
    void testSearchProducts() throws Exception {
        // Arrange
        MockHttpSession httpSession = new MockHttpSession();
        Model model = mock(Model.class);
        int categoryId = 1;
        String queryString = "Product";
        Category category = new Category();
        List<Products> products = Arrays.asList(new Products(), new Products());

        // Mock the behavior of the categoryService to return a dummy category
        when(categoryService.getCategoryById(categoryId)).thenReturn(category);

        // Mock the behavior of the productService to return a list of dummy products
        when(productService.searchProductsByQuery(categoryId, queryString)).thenReturn(products);

        // Act & Assert
        mockMvc.perform(post("/category/{categoryId}", categoryId).param("queryString", queryString).session(httpSession))
                .andExpect(status().isOk())
                .andExpect(view().name("categoryProducts"))
                .andExpect(model().attribute("category", category))
                .andExpect(model().attribute("products", products));
    }
}

