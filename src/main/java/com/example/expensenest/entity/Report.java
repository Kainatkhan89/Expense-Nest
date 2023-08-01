package com.example.expensenest.entity;

public class Report {

    String startDate;

    String endDate;

    public Report(String start, String end) {
        startDate = start;
        endDate = end;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
