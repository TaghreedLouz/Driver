package com.example.driveroutreach.ui.fragments.Profile;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;

import com.example.driveroutreach.ui.activities.settings.SettingsActivity;

public class ProfilePresenter {
    ProfileView view;

    public ProfilePresenter(ProfileView view) {
        this.view = view;
    }

    // here we define methods that contains the logic and coding + the methods that connect the view with the presenter

//    public void IntentTo(Context context, Class<?> activity){
//        Intent intent = new Intent(context, activity);
//        startActivity(intent);
//    }


}
