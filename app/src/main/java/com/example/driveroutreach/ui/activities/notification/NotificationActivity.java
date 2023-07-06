package com.example.driveroutreach.ui.activities.notification;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.driveroutreach.adapters.NotificationAdapter;
import com.example.driveroutreach.databinding.ActivityNotificationBinding;

public class NotificationActivity extends AppCompatActivity {

    ActivityNotificationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.rvNotfication.setAdapter(new NotificationAdapter());
        binding.rvNotfication.setLayoutManager(new LinearLayoutManager(getBaseContext(),LinearLayoutManager.VERTICAL,false));
    }
}