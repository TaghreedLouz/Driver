package com.example.driveroutreach.model;

import java.util.ArrayList;

public class JourneyModel {

    String driver;
    String region;
    String start;
    String end;
    String organization;

    ArrayList<String> beneficiaries;

    String day;

    String journeyId;
    boolean isEnabled;


    ArrayList<SchedualAllModel> all = new ArrayList<>();

    public JourneyModel() {
    }

    public JourneyModel(String driver, String region, String start, String end, String organization, ArrayList<String> beneficiaries, String day, String journeyId) {
        this.driver = driver;
        this.region = region;
        this.start = start;
        this.end = end;
        this.organization = organization;
        this.beneficiaries = beneficiaries;
        this.day = day;
        this.journeyId = journeyId;
        this.isEnabled =true;
    }


    public JourneyModel(String driver, String region, String start, String end, String organization, ArrayList<String> beneficiaries, String journeyId) {
        this.driver = driver;
        this.region = region;
        this.start = start;
        this.end = end;
        this.organization = organization;
        this.beneficiaries = beneficiaries;
        this.day = day;
        this.journeyId = journeyId;
        this.isEnabled =true;
    }
    public ArrayList<SchedualAllModel> getAll() {
        return all;
    }

    public void setAll(ArrayList<SchedualAllModel> all) {
        this.all = all;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public ArrayList<String> getBeneficiaries() {
        return beneficiaries;
    }

    public void setBeneficiaries(ArrayList<String> beneficiaries) {
        this.beneficiaries = beneficiaries;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(String journeyId) {
        this.journeyId = journeyId;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}
