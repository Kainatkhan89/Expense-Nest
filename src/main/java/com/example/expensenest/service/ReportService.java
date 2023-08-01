package com.example.expensenest.service;

import com.example.expensenest.entity.SalesReport;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReportService {

    List<SalesReport> getSalesReportData(int sellerId, String startDate, String endDate);
}
