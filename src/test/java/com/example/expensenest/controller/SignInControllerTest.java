package com.example.expensenest.controller;

import com.example.expensenest.entity.User;
import com.example.expensenest.entity.UserSignIn;
import com.example.expensenest.service.SessionService;
import com.example.expensenest.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class SignInControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private SessionService sessionService;

    @InjectMocks
    private SignInController signInController;

    @Mock
    private Model model;

    @Mock
    private HttpSession session;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetSignInForm() {
        String viewName = signInController.getSignInForm(model, null, session);
        assertEquals("signin", viewName);
        verify(model, times(1)).addAttribute(eq("userSignIn"), any(UserSignIn.class));
    }

    @Test
    void testCheckSignInWithValidUserSeller() {
        UserSignIn signIn = new UserSignIn("jinal@gmail.com", "Admin123");
        User user = new User();
        user.setUserType(2); // Seller user type

        when(userService.getUserByEmailAndPassword(signIn)).thenReturn(user);
        String viewName = signInController.checkSignIn(signIn, session);

        assertEquals("redirect:/seller/dashboard", viewName);
        verify(sessionService, times(1)).createSession(eq(user), eq(session));
    }

    @Test
    void testCheckSignInWithValidUserInvalidUserType() {
        UserSignIn signIn = new UserSignIn("jinal@gmail.com", "Admin123");
        User user = new User();
        user.setUserType(3); // Invalid user type

        when(userService.getUserByEmailAndPassword(signIn)).thenReturn(null);

        String expectedViewName = "redirect:/signin?signInMessage=Invalid email or password. Please try again.&isSignInSuccess=error";

        String viewName = signInController.checkSignIn(signIn, session);

        assertEquals(expectedViewName, viewName);
        verify(sessionService, times(0)).createSession(eq(user), eq(session));
    }



    @Test
    void testCheckSignInWithInvalidCredentials() {
        UserSignIn signIn = new UserSignIn("jinal@gmail.com", "InvalidPass");

        when(userService.getUserByEmailAndPassword(signIn)).thenReturn(null);
        String viewName = signInController.checkSignIn(signIn, session);

        assertEquals("redirect:/signin?signInMessage=Invalid email or password. Please try again.&isSignInSuccess=error", viewName);
    }

    @Test
    void testLogout() {
        String viewName = signInController.logout(session);
        assertEquals("redirect:/signin", viewName);
        verify(sessionService, times(1)).removeSession(session);
    }
}