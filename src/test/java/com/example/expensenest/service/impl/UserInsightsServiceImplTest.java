package com.example.expensenest.service.impl;

import com.example.expensenest.entity.UserInsightResponse;
import com.example.expensenest.repository.UserInsightsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserInsightsServiceImplTest {
    @Mock
    private UserInsightsRepository userInsightsRepository;
    @InjectMocks
    private UserInsightsServiceImpl userInsightsService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserInsightResponse() {
        int sellerId = 123;

        UserInsightResponse mockResponse = new UserInsightResponse();
        when(userInsightsRepository.getUserInsightResponse(sellerId)).thenReturn(mockResponse);

        UserInsightResponse response = userInsightsService.getUserInsightResponse(sellerId);
        assertEquals(mockResponse, response);

        verify(userInsightsRepository, times(1)).getUserInsightResponse(sellerId);
    }
    @Test
    void testGetUserInsightResponseWithNullResponse() {
        int sellerId = 456;

        when(userInsightsRepository.getUserInsightResponse(sellerId)).thenReturn(null);

        UserInsightResponse response = userInsightsService.getUserInsightResponse(sellerId);
        assertEquals(null, response);

        verify(userInsightsRepository, times(1)).getUserInsightResponse(sellerId);
    }
}
