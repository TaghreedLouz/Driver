package com.example.driveroutreach.ui.fragments.schedule.days;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.driveroutreach.adapters.TripAdapter;
import com.example.driveroutreach.databinding.FragmentDayBinding;
import com.example.driveroutreach.listeners.ScheduleListener;
import com.example.driveroutreach.model.ArichivedJourney;
import com.example.driveroutreach.model.JourneyModel;
import com.example.driveroutreach.ui.base_classes.BaseFragment;
import com.example.driveroutreach.ui.fragments.Home.HomeFragment;

import java.util.ArrayList;

public class DayFragment extends BaseFragment implements DayView{


    public final String DRIVER_ID_KEY = "driverId";
    FragmentDayBinding binding;
    DayPresenter dayPresenter;

    @Override
    public void onGettingScheduleSuccess(ArrayList<JourneyModel> Trips) {
                                    binding.RVDay.setAdapter(new TripAdapter(Trips, getActivity(),new ScheduleListener() {
                                @Override
                                public void StartJourney(String journeyId, String date) {



                                    getParentFragmentManager().beginTransaction().replace(com.google.android.material.R.id.container,new HomeFragment()).addToBackStack(null).commit();




                                }

                                        @Override
                                        public void EndJourney(ArichivedJourney journey) {
                                            dayPresenter.storeArchivedJourney(journey,journey.getDriver());
                                        }


                                    }));
                            binding.RVDay.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));

    }

    @Override
    public void onGettingScheduleFailure(Exception e) {
          Log.d(DayFragment.class.getName(),e.getMessage());
    }

    @Override
    public void storeArchivedJourneySuccess() {
     Log.d("StoredArchive","Success");
    }

    @Override
    public void storeArchivedJourneyFailure(Exception e) {
        Log.d("StoredArchive",e.getMessage());

    }


    private static final String ARG_day = "day";
    private static final String ARG_PARAM2 = "param2";


    private String day;
    private String mParam2;

    public DayFragment() {
        // Required empty public constructor
    }

    public static DayFragment newInstance(String day) {
        DayFragment fragment = new DayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_day, day);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            day = getArguments().getString(ARG_day);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDayBinding.inflate(inflater,container,false);




        String driverId= sp.getString(DRIVER_ID_KEY,null);



        Log.d("driver",driverId);

             dayPresenter = new DayPresenter(this);
            dayPresenter.gettingSchedule(day,driverId);





        return binding.getRoot();
    }


}

