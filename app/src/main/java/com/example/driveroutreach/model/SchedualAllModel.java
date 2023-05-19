package com.example.driveroutreach.model;

public class SchedualAllModel {

    String day;
    String end;
    String start;
    String driver;
    String organization;

    public SchedualAllModel(String day, String end, String start, String organization) {
        this.day = day;
        this.end = end;
        this.start = start;
        this.organization = organization;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public SchedualAllModel() {
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }
}
