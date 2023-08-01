package com.example.expensenest;

import com.example.expensenest.interceptor.ActiveSessionManager;
import com.example.expensenest.interceptor.InActiveSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication()
public class ExpenseNestApplication implements CommandLineRunner, WebMvcConfigurer {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ActiveSessionManager activeSessionManager;

    @Autowired
    private InActiveSessionManager inActiveSessionManager;


    public static void main(String[] args) {
        SpringApplication.run(ExpenseNestApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }

//    Register the routes where relevant interceptor check is required
    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry) {
        interceptorRegistry.addInterceptor(activeSessionManager).addPathPatterns("/signup", "/signin", "/forgotpassword","/signUpSeller");
        interceptorRegistry.addInterceptor(inActiveSessionManager).addPathPatterns("/dashboard", "/invoices", "/archived", "/editSeller",
                "/productInsights", "/editProfile", "/editCustomerProfile", "/seller/dashboard", "/manage/category", "/add/product", "/reports");
    }

}
