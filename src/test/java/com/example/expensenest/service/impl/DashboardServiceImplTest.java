package com.example.expensenest.service.impl;

import com.example.expensenest.entity.DataPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardServiceImplTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    @Test
    void testGetInvoiceData() {
        int userId = 1;
        List<Map<String, Object>> expectedInvoiceData = new ArrayList<>();

        // Mocking the query result
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(userId)))
                .thenReturn(expectedInvoiceData);

        // Execute the method
        List<Map<String, Object>> actualInvoiceData = dashboardService.getInvoiceData(userId);

        // Verify the result
        assertEquals(expectedInvoiceData, actualInvoiceData);
    }

    @Test
    void testGetChartData() {
        List<DataPoint> expectedChartData = new ArrayList<>();

        // Mocking the query result
        when(jdbcTemplate.query(anyString(), any(RowMapper.class)))
                .thenReturn(expectedChartData);

        // Execute the method
        List<DataPoint> actualChartData = dashboardService.getChartData();

        // Verify the result
        assertEquals(expectedChartData, actualChartData);
    }

    @Test
    void testGetBarData() {
        List<DataPoint> expectedBarData = new ArrayList<>();

        // Mocking the query result
        when(jdbcTemplate.query(anyString(), any(RowMapper.class)))
                .thenReturn(expectedBarData);

        // Execute the method
        List<DataPoint> actualBarData = dashboardService.getBarData();

        // Verify the result
        assertEquals(expectedBarData, actualBarData);
    }

    @Test
    void testGetPieData() {
        List<DataPoint> expectedPieData = new ArrayList<>();

        // Mocking the query result
        when(jdbcTemplate.query(anyString(), any(RowMapper.class)))
                .thenReturn(expectedPieData);

        // Execute the method
        List<DataPoint> actualPieData = dashboardService.getPieData();

        // Verify the result
        assertEquals(expectedPieData, actualPieData);
    }

    @Test
    void testGetSevenData() {
        int sellerId = 1;
        List<DataPoint> expectedSevenData = new ArrayList<>();

        // Mocking the query result
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(sellerId)))
                .thenReturn(expectedSevenData);

        // Execute the method
        List<DataPoint> actualSevenData = dashboardService.getSevenData(sellerId);

        // Verify the result
        assertEquals(expectedSevenData, actualSevenData);
    }

    @Test
    void testGetCompareData() {
        int sellerId = 1;
        List<DataPoint> expectedCompareData = new ArrayList<>();

        // Mocking the query result
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(sellerId)))
                .thenReturn(expectedCompareData);

        // Execute the method
        List<DataPoint> actualCompareData = dashboardService.getCompareData(sellerId);

        // Verify the result
        assertEquals(expectedCompareData, actualCompareData);
    }

    @Test
    void testGetWeekData() {
        int sellerId = 1;
        List<DataPoint> expectedWeekData = new ArrayList<>();

        // Mocking the query result
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(sellerId)))
                .thenReturn(expectedWeekData);

        // Execute the method
        List<DataPoint> actualWeekData = dashboardService.getWeekData(sellerId);

        // Verify the result
        assertEquals(expectedWeekData, actualWeekData);
    }

    @Test
    void testGetYesterdayData() {
        int sellerId = 1;
        List<DataPoint> expectedYesterdayData = new ArrayList<>();

        // Mocking the query result
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(sellerId)))
                .thenReturn(expectedYesterdayData);

        // Execute the method
        List<DataPoint> actualYesterdayData = dashboardService.getYesterdayData(sellerId);

        // Verify the result
        assertEquals(expectedYesterdayData, actualYesterdayData);
    }
}
