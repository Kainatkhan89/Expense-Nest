package com.example.expensenest.controller;

import com.example.expensenest.entity.User;
import com.example.expensenest.service.SessionService;
import com.example.expensenest.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EditProfileControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private SessionService sessionService;

    @InjectMocks
    private EditProfileController editProfileController;

    @Mock
    private Model model;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private HttpSession session;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1);
    }

    @Test
    void testGetSellerEditProfile() {
        when(sessionService.getSession(session)).thenReturn(user);
        when(userService.getUserProfile(1)).thenReturn(user);

        String viewName = editProfileController.getEditProfile(model, httpServletRequest, session);

        assertEquals("/editCustomerProfile", viewName);
        verify(model, times(1)).addAttribute(eq("user"), eq(user));
    }

   /* @Test
    void testSaveProfileWithSuccessfulSave() {
        when(userService.setUserProfile(user)).thenReturn(true);

        String viewName = editProfileController.createUser(user, model);
        assertEquals("editCustomerProfile", viewName);
        verify(model, times(1)).addAttribute(eq("successMessage"), eq("Changes saved successfully!"));
        verify(model, never()).addAttribute(eq("errorMessage"), any());
    }

    @Test
    void testSaveProfileWithUnsuccessfulSave() {
        when(userService.setUserProfile(user)).thenReturn(false);

        String viewName = editProfileController.createUser(user, model);

        assertEquals("editCustomerProfile", viewName);
        verify(model, times(1)).addAttribute(eq("errorMessage"), eq("Something went wrong."));
        verify(model, never()).addAttribute(eq("successMessage"), any());
    }*/
}
