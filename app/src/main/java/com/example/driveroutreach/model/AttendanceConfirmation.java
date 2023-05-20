package com.example.driveroutreach.model;

import java.util.ArrayList;

public class AttendanceConfirmation {
    String driverId;
    String date;
    String journeyId;
   // String benf;

    ArrayList<String> benf;

    public AttendanceConfirmation(ArrayList<String> benf) {
        this.benf = benf;
    }

    public AttendanceConfirmation(String journeyId, ArrayList<String> benf) {
        this.journeyId = journeyId;
        this.benf = benf;
    }

    public AttendanceConfirmation() {
    }






    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(String journeyId) {
        this.journeyId = journeyId;
    }

    public ArrayList<String> getBenf() {
        return benf;
    }

    public void setBenf(ArrayList<String> benf) {
        this.benf = benf;
    }
}
