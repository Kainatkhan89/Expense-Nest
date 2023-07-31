package com.example.expensenest.controller;

import com.example.expensenest.entity.User;
import com.example.expensenest.service.EmailSenderService;
import com.example.expensenest.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SellerSignUpControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private EmailSenderService emailSenderService;

    @InjectMocks
    private SellerSignUpController sellerSignUpController;

    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetSignInForm() {
        String viewName = sellerSignUpController.getSignInForm(model);
        assertEquals("signUpSeller", viewName);
        verify(model, times(1)).addAttribute(eq("sellerSignUp"), any(User.class));
    }

    @Test
    void testCheckSignInWithSuccessfulSignUp() {
        User signUp = new User();
        signUp.setEmail("ds.dalvin@gmail.com");

        when(userService.addSeller(signUp)).thenReturn("verification_code");
        String viewName = sellerSignUpController.checkSignIn(signUp, model);

        assertEquals("/signUpSeller", viewName);
        verify(emailSenderService, times(1)).sendVerificationEmail(eq("ds.dalvin@gmail.com"), eq("Please verify your email"), eq("Click the following link to verify your email"), eq("verification_code"));
    }

    @Test
    void testCheckSignInWithUnsuccessfulSignUp() {
        User signUp = new User();
        signUp.setEmail("ds.dalvin@gmail.com");

        when(userService.addSeller(signUp)).thenReturn(null);
        String viewName = sellerSignUpController.checkSignIn(signUp,model);

        assertEquals("redirect:/signin", viewName);
        verify(emailSenderService, never()).sendVerificationEmail(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void testCheckSignInWithNullVerificationCode() {
        User signUp = new User();
        signUp.setEmail("ds.dalvin@gmail.com");

        when(userService.addSeller(signUp)).thenReturn(null);

        String viewName = sellerSignUpController.checkSignIn(signUp,model);

        assertEquals("redirect:/signin", viewName);
        verify(emailSenderService, never()).sendVerificationEmail(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void testCheckSignInWithNonNullVerificationCode() {
        User signUp = new User();
        signUp.setEmail("ds.dalvin@gmail.com");

        when(userService.addSeller(signUp)).thenReturn("verification_code");

        String viewName = sellerSignUpController.checkSignIn(signUp,model);

        assertEquals("/signUpSeller", viewName);
        verify(emailSenderService, times(1)).sendVerificationEmail(eq("ds.dalvin@gmail.com"), eq("Please verify your email"), eq("Click the following link to verify your email"), eq("verification_code"));
}
}