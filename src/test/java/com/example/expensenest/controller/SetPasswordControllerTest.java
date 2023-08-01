package com.example.expensenest.controller;

import com.example.expensenest.entity.User;
import com.example.expensenest.service.EmailSenderService;
import com.example.expensenest.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@Controller
public class SetPasswordControllerTest {

    private UserService userService;
    private EmailSenderService emailSenderService;
    private SetPasswordController setPasswordController;
    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        emailSenderService = mock(EmailSenderService.class);
        setPasswordController = new SetPasswordController(userService,emailSenderService);
    }
    @Test
    void verifyUserTest() {
        String verificationCode = "some_verification_code";
        User mockUser = new User();
        mockUser.setEmail("test@example.com");

        when(userService.findByVerificationCode(verificationCode)).thenReturn(mockUser);

        // Test
        String result = setPasswordController.verifyUser(verificationCode);

        // Verify
        verify(userService).verifyUser(verificationCode);
        verify(userService).findByVerificationCode(verificationCode);

        assertEquals("redirect:/setPassword?email=test@example.com", result);

    }

    @Test
    void getSetPasswordPageTest() {
        String email = "test@example.com";
        Model model = mock(Model.class);

        // Test
        String result = setPasswordController.getSetPasswordPage(email, model);

        // Verify
        verify(model).addAttribute(eq("userPassword"), any(User.class));
        assertEquals("setPassword", result);
    }

    @Test
    void createPasswordValidPasswordTest() {
        // Test data
        User user = new User("test@example.com", "some_password");
        Model model = mock(Model.class);

        // Mocking
        when(userService.setUserPassword(user)).thenReturn(true);

        // Test
        String result = setPasswordController.createPassword(user, model);

        // Verify
        verify(userService).setUserPassword(user);
        verifyNoMoreInteractions(userService);
        assertEquals("redirect:/dashboard", result);
    }

    @Test
    void createPasswordInvalidPasswordTest() {
        // Test data
        User user = new User("test@example.com", "some_password");
        Model model = mock(Model.class);

        // Mocking
        when(userService.setUserPassword(user)).thenReturn(false);

        // Test
        String result = setPasswordController.createPassword(user, model);

        // Verify
        verify(userService).setUserPassword(user);
        verifyNoMoreInteractions(userService);
        assertEquals("setPassword", result);
    }

    @Test
    void verifyPasswordResetTest() {
        // Test data
        String code = "some_verification_code";
        String email = "test@example.com";

        // Mocking
        User mockUser = new User(email, "some_password");
        when(userService.findByVerificationCode(code)).thenReturn(mockUser);

        // Test
        String result = setPasswordController.verifyPasswordReset(code, email);

        // Verify
        verify(userService).verifyUser(code);
        verify(userService).findByVerificationCode(code);
        assertEquals("redirect:/resetPassword?email=test@example.com", result);
    }

    @Test
    void getResetPasswordPageTest() {
        // Test data
        Model model = mock(Model.class);

        // Test
        String result = setPasswordController.getResetPasswordPage(model);

        // Verify
        verify(model).addAttribute(eq("user"), any(User.class));
        assertEquals("resetPassword", result);
    }

    @Test
    void resetPasswordTest() {
        // Test data
        User user = new User("test@example.com", "new_password");
        String verificationCode = "some_verification_code";

        // Mocking
        when(userService.generateUserVerificationCode()).thenReturn(verificationCode);

        // Test
        String result = setPasswordController.resetPassword(user);

        // Verify
        verify(userService).generateUserVerificationCode();
        verify(userService).setPasswordResetVerificationCode(verificationCode, user.getEmail());
        verify(emailSenderService).sendVerificationEmail(eq("test@example.com"), eq("Reset Password"),
                eq("Click the following link to reset your password"), eq(verificationCode));
        assertEquals("redirect:/signin", result);
    }
}
