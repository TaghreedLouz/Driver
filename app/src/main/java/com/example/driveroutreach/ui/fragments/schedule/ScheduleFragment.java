package com.example.driveroutreach.ui.fragments.schedule;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.driveroutreach.databinding.FragmentScheduleBinding;

public class ScheduleFragment extends BaseFragment {

    FragmentScheduleBinding binding;


    public ScheduleFragment() {
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
         binding = FragmentScheduleBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }
}