package com.example.driveroutreach.ui.fragments.Home;

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
}
