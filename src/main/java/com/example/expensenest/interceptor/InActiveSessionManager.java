package com.example.expensenest.interceptor;
import com.example.expensenest.entity.User;
import com.example.expensenest.service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class InActiveSessionManager implements HandlerInterceptor {
    private static final Logger logger = LogManager.getLogger(InActiveSessionManager.class);
    private SessionService sessionService;

    public InActiveSessionManager(SessionService sessionService){
        this.sessionService = sessionService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("Handling preHandle for request: {}", request.getRequestURI());
        // If the user is not signed in, and he is trying to access any of the pages OTHER THAN (sign up, sign in, and forgot password),
        // then redirect the user to sign in page, else continue to the page user is trying to access
        HttpSession session = request.getSession();
        User userSession = sessionService.getSession(session);
        if (userSession == null) {
            logger.info("No active session found for the request. Redirecting to /signin.");
            response.sendRedirect("/signin");
            return false;
        }
        logger.info("User {} is already signed in. Proceeding to the requested page.", userSession.getEmail());
        return true;
    }
}
