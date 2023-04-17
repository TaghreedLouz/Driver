package com.example.driveroutreach.ui.fragments.schedule;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.driveroutreach.R;
import com.example.driveroutreach.databinding.FragmentScheduleBinding;
import com.example.driveroutreach.ui.fragments.BaseFragment;

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