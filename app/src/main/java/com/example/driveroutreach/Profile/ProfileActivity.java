package com.example.driveroutreach.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.driveroutreach.R;

public class ProfileActivity extends AppCompatActivity implements ProfileView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }
}