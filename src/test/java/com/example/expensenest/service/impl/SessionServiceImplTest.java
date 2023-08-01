package com.example.expensenest.service.impl;

import com.example.expensenest.entity.User;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SessionServiceImplTest {

        private static final String Session_Key = "loggedInUserData";

    @InjectMocks
    private SessionServiceImpl sessionService;

    @Mock
    private HttpSession session;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setEmail("jinal@gmail.com");
        user.setPassword("Admin123");
    }

    @Test
    void testCreateSession() {
        sessionService.createSession(user, session);

        verify(session, times(1)).setAttribute(eq(Session_Key), eq(user));
    }

    @Test
    void testRemoveSession() {

        when(session.getAttribute(eq(Session_Key))).thenReturn(user);
        sessionService.removeSession(session);
        verify(session, times(1)).getAttribute(eq(Session_Key));
        verify(session, times(1)).removeAttribute(eq(Session_Key));
    }


    @Test
    void testGetSession() {
        when(session.getAttribute(eq(Session_Key))).thenReturn(user);

        User result = sessionService.getSession(session);

        assertEquals(user, result);
    }
}
