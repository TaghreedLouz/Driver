package com.example.driveroutreach.model;

import com.google.android.gms.maps.model.LatLng;

public class DriverProfile {
    String name;
    int mobile;
    String region;
    int vichuleId;
    String dayOff;
    LatLng coordinates;


    public DriverProfile(String name, int mobile, String region, int vichuleId, String dayOff, LatLng coordinates) {
        this.name = name;
        this.mobile = mobile;
        this.region = region;
        this.vichuleId = vichuleId;
        this.dayOff = dayOff;
        this.coordinates = coordinates;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMobile() {
        return mobile;
    }

    public void setMobile(int mobile) {
        this.mobile = mobile;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getVichuleId() {
        return vichuleId;
    }

    public void setVichuleId(int vichuleId) {
        this.vichuleId = vichuleId;
    }

    public String getDayOff() {
        return dayOff;
    }

    public void setDayOff(String dayOff) {
        this.dayOff = dayOff;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(LatLng coordinates) {
        this.coordinates = coordinates;
    }
}
