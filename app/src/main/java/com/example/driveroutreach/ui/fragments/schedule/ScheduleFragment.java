package com.example.driveroutreach.ui.fragments.schedule;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.example.driveroutreach.R;
import com.example.driveroutreach.databinding.FragmentScheduleBinding;
import com.example.driveroutreach.ui.fragments.BaseFragment;
import com.example.driveroutreach.ui.fragments.schedule.all.AllFragment;
import com.example.driveroutreach.ui.fragments.schedule.daily.DailyFragment;

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

        // هان خليت العرض التلقائي ع الفراقمينت اليومي
         getChildFragmentManager().beginTransaction()
                 .add(R.id.container_schedule,new DailyFragment())
                 .commit();

        // هان خليت التلقئي محدد ع اليومي
         binding.rgButtons.check(R.id.rbtn_daily);


        binding.rgButtons.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbtn_daily){
                    getChildFragmentManager().beginTransaction()
                            .replace(R.id.container_schedule,new DailyFragment())
                            .commit();
                }else if (checkedId == R.id.rbtn_all){
                    getChildFragmentManager().beginTransaction()
                            .replace(R.id.container_schedule,new AllFragment())
                            .commit();
                }
            }
        });



        return binding.getRoot();
    }

}