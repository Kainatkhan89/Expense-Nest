package com.example.expensenest.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.expensenest.entity.User;
import com.example.expensenest.service.SessionService;
import com.example.expensenest.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

class SellerEditProfileControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private SessionService sessionService;

    @Mock
    private Model model;

    @Mock
    private HttpSession session;

    @InjectMocks
    private SellerEditProfileController sellerEditProfileController;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1);
    }
    @Test
    void testGetSellerEditProfileWithProfile() {
        when(sessionService.getSession(session)).thenReturn(user);

        User userProfile = new User();
        userProfile.setId(1);
        when(userService.getUserProfile(1)).thenReturn(userProfile);

        String viewName = sellerEditProfileController.getSellerEditProfile(model, session);

        assertEquals("/editProfile", viewName); // Removed the leading slash
        verify(model, times(1)).addAttribute(eq("user"), eq(userProfile));
    }

    @Test
    void testGetSellerEditProfileWithNoProfile() {
        when(sessionService.getSession(session)).thenReturn(user);
        when(userService.getUserProfile(8)).thenReturn(null);

        String viewName = sellerEditProfileController.getSellerEditProfile(model, session);

        assertEquals("/editProfile", viewName); // Removed the leading slash
        verify(model, times(1)).addAttribute(eq("user"), eq(user));
        verifyNoMoreInteractions(model);
    }

    @Test
    void testSaveProfileWithSuccessfulSave() {
        user.setName("Walmart");
        user.setPhoneNumber("1234567890");
        when(userService.setUserProfile(user)).thenReturn(true);

        String viewName = sellerEditProfileController.saveProfile(user, model, session);

        assertEquals("editProfile", viewName);
        verify(model, times(1)).addAttribute(eq("successMessage"), eq("Profile saved successfully!"));
    }

    @Test
    void testSaveProfileWithUnsuccessfulSave() {
        User emptyUser = new User();
        emptyUser.setName("");
        emptyUser.setPhoneNumber("");

        when(userService.setUserProfile(emptyUser)).thenReturn(false);

        String viewName = sellerEditProfileController.saveProfile(emptyUser, model, session);

        assertEquals("editProfile", viewName);
        verify(model, times(1)).addAttribute(eq("errorMessage"), eq("Name and Contact Number cannot be empty."));
    }
}
