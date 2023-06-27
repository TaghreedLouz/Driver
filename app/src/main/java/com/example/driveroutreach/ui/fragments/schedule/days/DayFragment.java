package com.example.driveroutreach.ui.fragments.schedule.days;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.driveroutreach.adapters.TripAdapter;
import com.example.driveroutreach.databinding.FragmentDayBinding;
import com.example.driveroutreach.listeners.ScheduleListener;
import com.example.driveroutreach.model.JourneyModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DayFragment extends Fragment {


    ArrayList<JourneyModel> Trips;

    SharedPreferences sp;
    SharedPreferences.Editor edit;
    public final String DRIVER_ID_KEY = "driverId";



    public interface OnDataListenerDayFrag{
        void onDataReceivedFromDayFrag(String journeyId, String date);
    }

    OnDataListenerDayFrag onDataListenerDayFrag;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_day = "day";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
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
        FragmentDayBinding binding = FragmentDayBinding.inflate(inflater,container,false);



        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        sp = getActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);

        String driverId= sp.getString(DRIVER_ID_KEY,null);
        edit = sp.edit();

         Trips = new ArrayList<>();



        firestore.collection("Journey").whereEqualTo("driver",driverId)
                .whereEqualTo("day",day).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if (task.isSuccessful()){
                           Log.d("schedule",task.getResult().toString());

                           for(QueryDocumentSnapshot document : task.getResult()){

                               JourneyModel journeyModel = document.toObject(JourneyModel.class);
                               Log.d("schedule",journeyModel.getOrganization()+" "+journeyModel.getDay());

                               Trips.add(journeyModel);
                           }


                           binding.RVDay.setAdapter(new TripAdapter(Trips, getActivity(),new ScheduleListener() {
                               @Override
                               public void StartJourney(String journeyId, String date) {
                                   if (onDataListenerDayFrag != null) {
                                       onDataListenerDayFrag.onDataReceivedFromDayFrag(journeyId,date);

                                        edit.putString("journeyDate",date);
                                        edit.putString("journeyId",journeyId);
                                        edit.putBoolean("started",true);
                                        edit.commit();

                                   }else {
                                       Log.d("transaction","Wrong Operation");
                                   }

                               }
                           }));
                           binding.RVDay.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

                       }   else {
                           Log.d("schedule",task.getException().getMessage());
                       }
                    }
                });



        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            onDataListenerDayFrag = (OnDataListenerDayFrag) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnDataListener");
        }
    }
}

