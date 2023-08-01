package com.example.expensenest.controller;

import com.example.expensenest.entity.User;
import com.example.expensenest.service.EmailSenderService;
import com.example.expensenest.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

@Controller
public class SetPasswordController {
    private UserService userService;



    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_REGEX);

    private  final EmailSenderService emailSenderService;
    public  SetPasswordController(UserService userService, EmailSenderService emailSenderService) {
        this.userService = userService;
        this.emailSenderService = emailSenderService;
    }


    @GetMapping("/verify")
    public String verifyUser(@RequestParam("code") String code) {
        // Logic to verify the user based on the verification token
        userService.verifyUser(code);
        User user = userService.findByVerificationCode(code);
        // Redirect to a success page or perform any other necessary actions
        return "redirect:/setPassword?email="+user.getEmail();
    }

    @GetMapping("/setPassword")
    public String getSetPasswordPage(@RequestParam("email") String email,Model model) {
        User user = new User(email,"");
        model.addAttribute("userPassword", user);
        return "setPassword";
    }


    @PostMapping("/setPassword")
    public String createPassword(@ModelAttribute("userPassword") User user, Model model) {
                boolean valid = userService.setUserPassword(user);
        if (valid) {
            return "redirect:/dashboard";
        } else {
            return "setPassword";
        }
    }

    @GetMapping("/verifyPasswordReset")
    public String verifyPasswordReset(@RequestParam("code") String code,@RequestParam("email") String email) {
        userService.verifyUser(code);
        User user = userService.findByVerificationCode(code);
        return "redirect:/resetPassword?email="+user.getEmail();
    }
    @GetMapping("/resetPassword")
    public String getResetPasswordPage(Model model) {
        User user = new User(null);
        model.addAttribute("user",user);
        return "resetPassword";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@ModelAttribute("user") User user) {

        String code = userService.generateUserVerificationCode();
        userService.setPasswordResetVerificationCode(code,user.getEmail());
        emailSenderService.sendVerificationEmail(user.getEmail(),"Reset Password","Click the following link to reset your password", code);
        return "redirect:/signin";
    }

}
