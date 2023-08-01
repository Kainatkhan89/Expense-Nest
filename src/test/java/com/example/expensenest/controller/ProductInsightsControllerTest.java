package com.example.expensenest.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.expensenest.entity.User;
import com.example.expensenest.entity.UserInsightResponse;
import com.example.expensenest.service.SessionService;
import com.example.expensenest.service.UserInsightsService;
import com.example.expensenest.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

class ProductInsightsControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private SessionService sessionService;

    @Mock
    private UserInsightsService userInsightsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @InjectMocks
    private ProductInsightsController productInsightsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetSellerEditProfile() {
        User userSession = new User();
        when(sessionService.getSession(session)).thenReturn(userSession);

        Model model = mock(Model.class);

        String viewName = productInsightsController.getSellerEditProfile(session, model);

        assertEquals("/productInsights", viewName);

        verify(sessionService, times(1)).getSession(session);
        verify(model, times(1)).addAttribute(eq("user"), eq(userSession));
    }


    @Test
    void testGetProductInsights() {
        User userSession = new User();
        userSession.setId(123);
        when(request.getSession()).thenReturn(session);
        when(sessionService.getSession(session)).thenReturn(userSession);

        User userProfile = new User();
        userProfile.setId(123);
        when(userService.getUserProfile(123)).thenReturn(userProfile);

        UserInsightResponse insightResponse = new UserInsightResponse();
        when(userInsightsService.getUserInsightResponse(123)).thenReturn(insightResponse);

        UserInsightResponse response = productInsightsController.getProductInsights(request);

        assertEquals(insightResponse, response);

        verify(request, times(1)).getSession();
        verify(sessionService, times(1)).getSession(session);
        verify(userService, times(1)).getUserProfile(123);
        verify(userInsightsService, times(1)).getUserInsightResponse(123);
    }
}
