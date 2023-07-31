package com.example.expensenest.controller;

import com.example.expensenest.entity.User;
import com.example.expensenest.entity.UserSignIn;
import com.example.expensenest.service.SessionService;
import com.example.expensenest.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignInController {
    private static final Logger logger = LogManager.getLogger(SignInController.class);

    private UserService userService;
    private SessionService sessionService;

    public SignInController(UserService userService, SessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @GetMapping("/signin")
    public String getSignInForm(Model model, HttpServletRequest httpServletRequest, HttpSession session) {
        logger.info("Handling GET request for /signin");
        UserSignIn signIn = new UserSignIn(null, null);
        model.addAttribute("userSignIn", signIn);
        logger.debug("New UserSignIn instance created: {}", signIn);
        return "signin";
    }

    @PostMapping("/signinpost")
    public String checkSignIn(@ModelAttribute("userSignIn") UserSignIn signIn, HttpSession session) {
        logger.info("Handling POST request for /signinpost");
        logger.debug("Received user's sign in data from the form: {}", signIn);

        User user = userService.getUserByEmailAndPassword(signIn);
        if (user != null) {
            sessionService.createSession(user, session);
            logger.info("User {} logged in successfully. User type: {}", user.getEmail(), user.getUserType());

            if (user.getUserType() == 1) {
                logger.info("Redirecting user {} to /dashboard", user.getEmail());
                return "redirect:/dashboard";
            } else {
                logger.info("Redirecting user {} to /seller/dashboard", user.getEmail());
                return "redirect:/seller/dashboard";
            }
        } else {
            logger.info("Invalid email or password for login attempt: {}", signIn.getEmail());
            return "redirect:/signin?signInMessage=Invalid email or password. Please try again.&isSignInSuccess=error";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        logger.info("Handling GET request for /logout");
        sessionService.removeSession(session);
        return "redirect:/signin";
    }
}
