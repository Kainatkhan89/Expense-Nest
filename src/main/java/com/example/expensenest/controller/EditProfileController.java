package com.example.expensenest.controller;

import com.example.expensenest.entity.User;
import com.example.expensenest.service.SessionService;
import com.example.expensenest.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EditProfileController {

    private SessionService sessionService;
    private UserService userService;

    public EditProfileController(SessionService sessionService, UserService userService){
        this.sessionService = sessionService;
        this.userService = userService;
    }

    @GetMapping("/editCustomerProfile")
    public String getEditProfile(Model model, HttpServletRequest httpServletRequest, HttpSession session) {
        User userSession = sessionService.getSession(session);
        User userInfo = userService.getUserProfile(userSession.getId());
        model.addAttribute("user", userInfo);
        return "/editCustomerProfile";
    }

    @PostMapping("/user/edit")
    public String createUser(@ModelAttribute("user") User user, Model model, HttpSession session) {
        if(userService.updateUser(user)){
            model.addAttribute("successMessage", "Changes saved successfully!");
        }
        else{
            model.addAttribute("errorMessage", "Something went wrong.");
        }
        User userSession = sessionService.getSession(session);
        User userInfo = userService.getUserProfile(userSession.getId());
        model.addAttribute("user", userInfo);
        return "/editCustomerProfile";
    }
}
