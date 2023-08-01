package com.example.expensenest.service;


import com.example.expensenest.entity.UserInsightResponse;
import org.springframework.stereotype.Service;

@Service
public interface UserInsightsService {

    UserInsightResponse getUserInsightResponse(int sellerId);
}
