package com.example.driveroutreach.ui.fragments.Home;

import android.location.Location;

import java.util.ArrayList;

public class HomePresenter {
    HomeView view;

    public HomePresenter(HomeView view) {
        this.view = view;
    }



    public ArrayList<String> TabLayoutArrayList() {
        ArrayList<String> tabs = new ArrayList<>();
        tabs.add("Attending");
        tabs.add("Absent");
        return tabs;
    }


    public float calculateDiatance(double clientLong, double clientLat, double driverLong, double driverLat){

        Location locationDriver = new Location("point A");

        locationDriver.setLatitude(driverLat);
        locationDriver.setLongitude(driverLong);

        Location locationClient = new Location("point B");

        locationClient.setLatitude(clientLat);
        locationClient.setLongitude(clientLong);

        return locationDriver.distanceTo(locationClient);
    }
}
