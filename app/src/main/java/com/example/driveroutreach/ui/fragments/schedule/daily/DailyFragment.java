package com.example.driveroutreach.ui.fragments.schedule.daily;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.driveroutreach.R;
import com.example.driveroutreach.adapters.DayVBAdapter;
import com.example.driveroutreach.databinding.FragmentDailyBinding;
import com.example.driveroutreach.ui.fragments.schedule.daily.days.DayFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class DailyFragment extends Fragment {
    FragmentDailyBinding binding ;

    public DailyFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         binding = FragmentDailyBinding.inflate(inflater,container,false);




        return binding.getRoot();
    }
}