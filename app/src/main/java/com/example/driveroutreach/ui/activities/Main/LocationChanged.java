package com.example.driveroutreach.ui.activities.Main;

public class LocationChanged {
    public  double latitude;
    public  double longitude;
    public LocationChanged(double latitude, double longitude) {
        this.latitude=latitude;
        this.longitude=longitude;
    }

    public LocationChanged() {
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "LocationChanged{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
