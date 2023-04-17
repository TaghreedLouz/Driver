package com.example.driveroutreach.ui.activites.main;

import androidx.fragment.app.Fragment;

import com.example.driveroutreach.ui.fragments.BaseFragment;

public interface MainView {

    void onSetFragment(Fragment fragment);

    void onSelectedNavIcon(Fragment fragment);
}
