package com.example.driveroutreach.ui.activities.edit_profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.driveroutreach.databinding.ActivityEditProfileBinding;

public class EditProfileActivity extends AppCompatActivity {

    ActivityEditProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}