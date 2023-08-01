package com.example.expensenest.controller;

import com.example.expensenest.entity.User;
import com.example.expensenest.entity.UserInsightResponse;
import com.example.expensenest.service.SessionService;
import com.example.expensenest.service.UserInsightsService;
import com.example.expensenest.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ProductInsightsController {
    private static final Logger logger = LogManager.getLogger(ProductInsightsController.class);

    private SessionService sessionService;
    private UserService userService;
    private UserInsightsService userInsightsService;

    public ProductInsightsController(UserService userService, SessionService sessionService, UserInsightsService userInsightsService) {
        this.userService = userService;
        this.sessionService = sessionService;
        this.userInsightsService = userInsightsService;
    }

    @GetMapping("/productInsights")
    public String getSellerEditProfile(HttpSession session,Model model) {
        User userSession = sessionService.getSession(session);
        model.addAttribute("user", userSession);
        logger.info("Handling GET request for /productInsights");
        return "/productInsights";
    }

    @GetMapping("/seller-chart-data")
    @ResponseBody
    public UserInsightResponse getProductInsights(HttpServletRequest request) {
        logger.info("Handling GET request for /seller-chart-data");

        HttpSession session = request.getSession();
        User userSession = sessionService.getSession(session);
        logger.debug("User session data: {}", userSession);

        User profile = userService.getUserProfile(userSession.getId());
        logger.info("Fetching user profile for userId: {}", userSession.getId());
        logger.debug("User profile data: {}", profile);

        UserInsightResponse response = userInsightsService.getUserInsightResponse(profile.getId());
        logger.info("Fetching product insights for userId: {}", profile.getId());
        logger.debug("Product insights data: {}", response);

        return response;
    }
}
