package com.example.expensenest.entity;

import java.util.ArrayList;
import java.util.List;

public class UserInsightResponse {

    public UserInsightResponse(){

        userInsightsList = new ArrayList<UserInsights>();
        userInsightsSalesDataList = new ArrayList<UserInsightsSalesData>();
    }
    private List<UserInsights> userInsightsList;
    private List<UserInsightsSalesData> userInsightsSalesDataList;

    public List<UserInsights> getUserInsightsList() {
        return userInsightsList;
    }

    public void setUserInsightsList(List<UserInsights> userInsightsList) {
        this.userInsightsList = userInsightsList;
    }

    public List<UserInsightsSalesData> getUserInsightsSalesDataList() {
        return userInsightsSalesDataList;
    }

    public void setUserInsightsSalesDataList(List<UserInsightsSalesData> userInsightsSalesDataList) {
        this.userInsightsSalesDataList = userInsightsSalesDataList;
    }
}
