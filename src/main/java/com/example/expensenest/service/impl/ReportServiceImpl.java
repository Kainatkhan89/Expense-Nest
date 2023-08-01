package com.example.expensenest.service.impl;

import com.example.expensenest.entity.Invoice;
import com.example.expensenest.entity.SalesReport;
import com.example.expensenest.repository.ReportRepository;
import com.example.expensenest.service.ReportService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    ReportRepository reportRepository;

    public ReportServiceImpl(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }
    @Override
    public List<SalesReport> getSalesReportData(int sellerId, String startDate, String endDate) {
        return reportRepository.getSalesReportBySellerID(sellerId, startDate,endDate);

    }
}
