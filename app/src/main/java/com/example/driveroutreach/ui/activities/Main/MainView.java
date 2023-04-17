package com.example.driveroutreach.ui.activities.Main;

import androidx.fragment.app.Fragment;

public interface MainView {

    void onSetFragment(Fragment fragment);

    void onSelectedNavIcon(Fragment fragment);
}
