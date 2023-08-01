package com.example.expensenest.entity;

public class UserInsights {

    private int id;
    private String name;
    private double value;
    private String typeOfChart;
    private String graphStringX;
    private String graphStringY;

    public UserInsights(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue (){return value;}
    public void setValue(double value) {this.value =value;}
    public String getTypeOfChart() {
        return typeOfChart;
    }

    public void setTypeOfChart(String typeOfChart) {
        this.typeOfChart = typeOfChart;
    }

    public String getGraphStringX() {
        return graphStringX;
    }

    public void setGraphStringX(String graphStringX) {
        this.graphStringX = graphStringX;
    }
    public String getGraphStringY() {
        return graphStringY;
    }

    public void setGraphStringY(String graphStringY) {
        this.graphStringY = graphStringY;
    }
}
