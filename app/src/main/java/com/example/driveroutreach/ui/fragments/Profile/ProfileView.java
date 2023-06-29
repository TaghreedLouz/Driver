package com.example.driveroutreach.ui.fragments.Profile;

import com.example.driveroutreach.model.DriverProfile;

public interface ProfileView {
    //here we define the methods thats going to apply logic on activity

    void onDriverInfoFailure(Exception e);

    void onDriverInfoSuccess(DriverProfile driverProfile);


    void onGettingImgeSuccess(String img);
    void onGettingImgFailure(Exception e);


}
