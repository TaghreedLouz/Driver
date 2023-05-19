package com.example.driveroutreach.model;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class JourneyModel {

    String driver;
    String region;
    String start;
    String end;
    String organization;

    ArrayList<DocumentReference> beneficiaries;

    String day;

    String journeyId;


    public JourneyModel() {
    }

    public JourneyModel(String driver, String region, String start, String end, String organization, ArrayList<DocumentReference> beneficiaries, String day, String journeyId) {
        this.driver = driver;
        this.region = region;
        this.start = start;
        this.end = end;
        this.organization = organization;
        this.beneficiaries = beneficiaries;
        this.day = day;
        this.journeyId = journeyId;
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

    public ArrayList<DocumentReference> getBeneficiaries() {
        return beneficiaries;
    }

    public void setBeneficiaries(ArrayList<DocumentReference> beneficiaries) {
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
}
