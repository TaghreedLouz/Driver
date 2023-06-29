package com.example.driveroutreach.model;

public class ContactUs {
    String message;
    String date;
    String driverId;
    String title;

    public ContactUs() {
    }

    public ContactUs(String message, String date, String driverId, String title) {
        this.message = message;
        this.date = date;
        this.driverId = driverId;
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
