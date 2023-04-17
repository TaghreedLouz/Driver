package com.example.driveroutreach.ui.fragments.Home;

import androidx.fragment.app.Fragment;

public class HomePresenter {
    HomeView view;

    public HomePresenter(HomeView view) {
        this.view = view;
    }

    public void SettingMap(Fragment fragment){
        view.onSetMapFrag(fragment);
    }
}
