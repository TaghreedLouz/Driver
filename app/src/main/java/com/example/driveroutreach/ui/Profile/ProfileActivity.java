package com.example.driveroutreach.ui.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.driveroutreach.R;

public class ProfileActivity extends AppCompatActivity implements ProfileView{
 /*this activity is just responsible for handling the ui
   to get data it works with the presenter

   */
    ProfilePresenter profilePresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profilePresenter= new ProfilePresenter(this);

    }

}