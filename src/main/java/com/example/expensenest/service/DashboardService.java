package com.example.expensenest.service;

import com.example.expensenest.entity.DataPoint;
import com.example.expensenest.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface DashboardService {

    String getUserName(int userId);
    List<Map<String, Object>> getInvoiceData(int userId);
    List<DataPoint> getChartData();
    List<DataPoint> getBarData();
    List<DataPoint> getPieData();
    List<DataPoint> getSevenData(int sellerId);
    List<DataPoint> getCompareData(int sellerId);
    List<DataPoint> getWeekData(int sellerId);
    List<DataPoint> getYesterdayData(int sellerId);

}
