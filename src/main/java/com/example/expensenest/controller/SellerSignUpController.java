package com.example.expensenest.controller;

import com.example.expensenest.entity.User;
import com.example.expensenest.service.EmailSenderService;
import com.example.expensenest.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SellerSignUpController {
    private static final Logger logger = LogManager.getLogger(SellerSignUpController.class);

    private UserService userService;
    private final EmailSenderService emailSenderService;

    public SellerSignUpController(UserService userService, EmailSenderService emailSenderService) {
        this.userService = userService;
        this.emailSenderService = emailSenderService;
    }

    @GetMapping("/signUpSeller")
    public String getSignInForm(Model model) {
        logger.info("Handling GET request for /signUpSeller");
        User signUp = new User();
        model.addAttribute("sellerSignUp", signUp);
        logger.debug("New User instance created: {}", signUp);
        return "signUpSeller";
    }

    @PostMapping("/sellersigninpost")
    public String checkSignIn(@ModelAttribute("sellerSignUp") User signUp, Model model) {
        logger.info("Handling POST request for /sellersigninpost");
        logger.debug("Received User data from the form: {}", signUp);

        String code = userService.addSeller(signUp);
        if (code == null) {
            logger.info("Seller already exists for email: {}. Redirecting to /signin.", signUp.getEmail());
            return "redirect:/signin";
        } else {
            logger.info("New seller added. Sending verification email for email: {}", signUp.getEmail());
            boolean sent = emailSenderService.sendVerificationEmail(signUp.getEmail(),"Please verify your email","Click the following link to verify your email", code);
            if (sent) {
                logger.info("Verification email sent: {}", signUp);
                model.addAttribute("successMessage", "Verification email sent!");
            } else {
                logger.error("Error occurred while sending email: {}", signUp);
                model.addAttribute("errorMessage", "Error occurred while sending email");
            }
            return "/signUpSeller";
        }
    }
}
