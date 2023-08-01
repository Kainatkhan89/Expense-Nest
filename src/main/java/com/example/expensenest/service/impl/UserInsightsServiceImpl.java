package com.example.expensenest.service.impl;

import com.example.expensenest.entity.UserInsightResponse;
import com.example.expensenest.repository.UserInsightsRepository;
import com.example.expensenest.service.UserInsightsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class UserInsightsServiceImpl implements UserInsightsService {
    private static final Logger logger = LogManager.getLogger(UserInsightsServiceImpl.class);

    private UserInsightsRepository userInsightsRepository;

    public UserInsightsServiceImpl(UserInsightsRepository userInsightsRepository) {
        this.userInsightsRepository = userInsightsRepository;
    }

    @Override
    public UserInsightResponse getUserInsightResponse(int sellerId) {
        logger.info("Fetching user insights for sellerId: {}", sellerId);

        UserInsightResponse userInsightResponse = userInsightsRepository.getUserInsightResponse(sellerId);

        if (userInsightResponse != null) {
            logger.info("User insights fetched successfully for sellerId: {}", sellerId);
            logger.debug("User insights data: {}", userInsightResponse);
        } else {
            logger.warn("No user insights found for sellerId: {}", sellerId);
        }

        return userInsightResponse;
    }
}
