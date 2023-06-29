package com.example.driveroutreach.ui.fragments.schedule.days;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.driveroutreach.adapters.TripAdapter;
import com.example.driveroutreach.databinding.FragmentDayBinding;
import com.example.driveroutreach.listeners.ScheduleListener;
import com.example.driveroutreach.model.JourneyModel;
import com.example.driveroutreach.ui.fragments.Home.HomeFragment;

import java.util.ArrayList;

public class DayFragment extends Fragment implements DayView{

    SharedPreferences sp;
    SharedPreferences.Editor edit;
    public final String DRIVER_ID_KEY = "driverId";
    FragmentDayBinding binding;

    @Override
    public void onGettingScheduleSuccess(ArrayList<JourneyModel> Trips) {
                                    binding.RVDay.setAdapter(new TripAdapter(Trips, getActivity(),new ScheduleListener() {
                                @Override
                                public void StartJourney(String journeyId, String date) {

                                    edit.putString("journeyDate",date);
                                    edit.putString("journeyId",journeyId);
                                    edit.putBoolean("started",true);
                                    edit.commit();

                                    getParentFragmentManager().beginTransaction().replace(com.google.android.material.R.id.container,new HomeFragment()).addToBackStack(null).commit();




                                }
                            }));
                            binding.RVDay.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));

    }

    @Override
    public void onGettingScheduleFailure(Exception e) {
          Log.d(DayFragment.class.getName(),e.getMessage());
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


        sp = getActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);

        String driverId= sp.getString(DRIVER_ID_KEY,null);
        edit = sp.edit();


            DayPresenter dayPresenter = new DayPresenter(this);
            dayPresenter.gettingSchedule(day,driverId);

//        firestore.collection("Journey").whereEqualTo("driver",driverId)
//                .whereEqualTo("day",day).get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                       if (task.isSuccessful()){
//                           Log.d("schedule",task.getResult().toString());
//
//                           for(QueryDocumentSnapshot document : task.getResult()){
//
//                               JourneyModel journeyModel = document.toObject(JourneyModel.class);
//                               Log.d("schedule",journeyModel.getOrganization()+" "+journeyModel.getDay());
//
//                               Trips.add(journeyModel);
//                           }
//
//
//                           binding.RVDay.setAdapter(new TripAdapter(Trips, getActivity(),new ScheduleListener() {
//                               @Override
//                               public void StartJourney(String journeyId, String date) {
//
//
//
//
//
//                                   edit.putString("journeyDate",date);
//                                   edit.putString("journeyId",journeyId);
//                                   edit.putBoolean("started",true);
//                                   edit.commit();
////
////                                   getChildFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
////                                   if (onDataListenerDayFrag != null) {
////                                       onDataListenerDayFrag.onDataReceivedFromDayFrag(journeyId,date);
////
////                                        edit.putString("journeyDate",date);
////                                        edit.putString("journeyId",journeyId);
////                                        edit.putBoolean("started",true);
////                                        edit.commit();
////
////                                   }else {
////                                       Log.d("transaction","Wrong Operation");
////                                   }
//
//                               }
//                           }));
//                           binding.RVDay.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
//
//                       }   else {
//                           Log.d("schedule",task.getException().getMessage());
//                       }
//                    }
//                });



        return binding.getRoot();
    }


}

