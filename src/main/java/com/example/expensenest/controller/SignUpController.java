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
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SignUpController {

    private static final Logger logger = LogManager.getLogger(SignUpController.class);
    private UserService userService;
    private  final EmailSenderService emailSenderService;

    public SignUpController(UserService userService, EmailSenderService emailSenderService) {
        this.userService = userService;
        this.emailSenderService = emailSenderService;
    }

    @GetMapping("/signup")
    public String getSignUpPage (Model model) {
        logger.info("Handling GET request for /signup");
        User user = new User();
        model.addAttribute("user", user);
        return "signup";
    }

    @PostMapping("/user/create")
    public String createUser(@ModelAttribute("user") User user, Model model) {
        logger.info("Handling POST request for /user/create");
        String code = userService.addUser(user);
        if (code == null) {
            logger.info("Handling user exists condition");
            model.addAttribute("userExists",true);
            model.addAttribute("existsMessage", "User already exists, please sign in!");

        } else {
            boolean sent = emailSenderService.sendVerificationEmail(user.getEmail(),"Please verify your email","Click the following link to verify your email", code);
            if (sent) {
                logger.info("Verification email sent: {}", user);
                model.addAttribute("successMessage", "Verification email sent!");
            } else {
                logger.error("Error occurred while sending email: {}", user);
                model.addAttribute("errorMessage", "Error occurred while sending email");
            }
            model.addAttribute("user",user);
        }
        return "signup";
    }
}
