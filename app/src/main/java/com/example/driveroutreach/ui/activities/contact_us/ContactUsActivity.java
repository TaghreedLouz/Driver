package com.example.driveroutreach.ui.activities.contact_us;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.driveroutreach.databinding.ActivityContactUsBinding;

public class ContactUsActivity extends AppCompatActivity {

    ActivityContactUsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityContactUsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}