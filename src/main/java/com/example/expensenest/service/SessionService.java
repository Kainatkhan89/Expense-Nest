package com.example.expensenest.service;

import com.example.expensenest.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public interface SessionService {

    void createSession(User user, HttpSession session);
    void removeSession( HttpSession session);
    User getSession( HttpSession session);

}
