package com.example.driveroutreach.ui.activities.Verification;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.driveroutreach.databinding.ActivityVerificationBinding;

public class VerificationActivity extends AppCompatActivity {

    ActivityVerificationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}