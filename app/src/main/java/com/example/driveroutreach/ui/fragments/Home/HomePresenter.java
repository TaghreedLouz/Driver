package com.example.driveroutreach.ui.fragments.Home;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class HomePresenter {
    HomeView view;

    public HomePresenter(HomeView view) {
        this.view = view;
    }

    public void SettingMap(Fragment fragment) {
        view.onSetMapFrag(fragment);
    }

    public boolean BottomSheetChangingState(int state, boolean fabClicked) {
        boolean fabState = fabClicked;
        switch (state) {
            //   the bottom sheet is expanded
            case 3:
                fabState = true;


                break;
            //   the bottom sheet is hidden
            case 5:
                fabState = false;


                break;
        }
        return fabState;
    }

    public ArrayList<String> TabLayoutArrayList() {
        ArrayList<String> tabs = new ArrayList<>();
        tabs.add("Attending");
        tabs.add("Absent");
        return tabs;
    }
}
