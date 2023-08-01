package com.example.expensenest.service.impl;

import com.example.expensenest.entity.User;
import com.example.expensenest.service.SessionService;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class SessionServiceImpl implements SessionService {
    private static final Logger logger = LogManager.getLogger(SessionServiceImpl.class);

    private static final String Session_Key = "loggedInUserData";

    @Override
    public void createSession(User user, HttpSession session) {
        logger.info("Creating session for user: {}", user.getEmail());
        session.setAttribute(Session_Key, user);
        logger.debug("User data added to the session: {}", user);
    }

    @Override
    public void removeSession(HttpSession session) {
        User user = (User) session.getAttribute(Session_Key);
        if (user != null) {
            logger.info("Removing session for user: {}", user.getEmail());
            session.removeAttribute(Session_Key);
        } else {
            logger.debug("Session not found or already removed.");
        }
    }

    @Override
    public User getSession(HttpSession session) {
        User userSession = (User) session.getAttribute(Session_Key);
        if (userSession != null) {
            logger.info("Retrieved session data for user: {}", userSession.getEmail());
            logger.debug("Session data: {}", userSession);
        } else {
            logger.debug("Session data not found.");
        }
        return userSession;
    }
}
