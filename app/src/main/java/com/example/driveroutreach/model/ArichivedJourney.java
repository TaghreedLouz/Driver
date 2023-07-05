package com.example.driveroutreach.model;

import java.util.ArrayList;

public class ArichivedJourney {

      String driver ;
        String region ;
        String start ;
        String end ;
        String organization ;
        String date;
        String journeyId ;
        ArrayList<String> attending;


    public ArichivedJourney(String driver, String region, String start, String end, String organization, String date, String journeyId, ArrayList<String> attending) {
        this.driver = driver;
        this.region = region;
        this.start = start;
        this.end = end;
        this.organization = organization;
        this.date = date;
        this.journeyId = journeyId;
        this.attending = attending;
    }

    public ArichivedJourney() {
    }

    public ArrayList<String> getAttending() {
        return attending;
    }

    public void setAttending(ArrayList<String> attending) {
        this.attending = attending;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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



    public String getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(String journeyId) {
        this.journeyId = journeyId;
    }
}
