package com.example.driveroutreach.ui.fragments.Home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.driveroutreach.R;
import com.example.driveroutreach.databinding.FragmentHomeBinding;
import com.example.driveroutreach.model.AttendanceConfirmation;
import com.example.driveroutreach.ui.activities.Main.LocationChanged;
import com.example.driveroutreach.ui.base_classes.BaseFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends BaseFragment implements HomeView, OnMapReadyCallback {
    BottomSheetBehavior bottomSheetBehavior;
    GoogleMap map;

    Task<DataSnapshot> reference;
    ArrayList<com.example.driveroutreach.model.Location> MarkersPoistions;
    FusedLocationProviderClient fusedLocationClient;
    private Marker markedPositionMarker;

    DatabaseReference ref;

    double longitude_driver;
    double latitude_driver ;
    ArrayList<AttendanceConfirmation> attendanceConfirmationArrayList;

    HomePresenter homePresenter;
   // SharedPreferences sp;
    public final String DRIVER_ID_KEY = "driverId";

    Marker driver_marker;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_JourneyId = "JourneyId";
    private static final String ARG_Date = "date";

    // TODO: Rename and change types of parameters
    private String journeyId;
    private String date;

    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance(String journeyId, String date) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_JourneyId, journeyId);
        args.putString(ARG_Date, date);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            journeyId = getArguments().getString(ARG_JourneyId);
            date = getArguments().getString(ARG_Date);
        }

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
     //   EventBus.getDefault().register(this);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentHomeBinding binding = FragmentHomeBinding.inflate(inflater, container, false);

        homePresenter = new HomePresenter(this);


        FirebaseFirestore firestore = FirebaseFirestore.getInstance();



        String driverId= sp.getString(DRIVER_ID_KEY,null);


        onSetMapFrag();

        onLocation(new LocationChanged());

        Log.d("loctation",latitude_driver + " "+ longitude_driver);

                     journeyId =sp.getString("journeyId",null);
                     date =sp.getString("journeyDate",null);

                if( sp.getBoolean("started",false)){
                    if (sp.getString("journeyId",null) !=null & sp.getString("journeyDate",null) != null) {
                        Log.d("Databack", sp.getString("journeyId",null) + "journeey iddd" + date+"driverid"+driverId);

//                        reference = FirebaseDatabase.getInstance().getReference("AttendanceConfirmation")
//                                .child(date).child(driverId).child(journeyId).get()
//                                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
//                                   if (task.isSuccessful()){
//
//                                       for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
//
//
//                                        ArrayList<AttendanceConfirmation>   benf = (ArrayList<AttendanceConfirmation>) dataSnapshot.getValue();
//
//
//
//                                           edit.putString("attending",  new Gson().toJson(benf));
//                                           edit.commit();
//
//                                           Log.d("DataReturned", benf.toString());
//
//                                       }
////
//                                   } else {
//                                       Log.d("onFailure",task.getException().getMessage());
//                                   }
//                                    }
//                                });
//



//                        reference = FirebaseDatabase.getInstance().getReference("AttendanceConfirmation").child(AppUtility.getDate())
//                                .child(driverId).child(journeyId).get()
//                                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
//Log.d("test2","in");
//                                        if (task.isSuccessful()) {
//
//                                          Log.d("test2",  task.getResult().toString());
//                                            for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
//
//
//                                                attendanceConfirmationArrayList = (ArrayList<AttendanceConfirmation>) dataSnapshot.getValue();
//
//
//
////                                                edit.putString("attending",  new Gson().toJson(benf));
////                                                edit.commit();
//
//                                                Log.d("DataReturned", attendanceConfirmationArrayList.toString());
//
//                                            }
//
//
//                                                onSetMapFrag();
//
//
//
//                                        } else {
//                                            Log.d("realtimeDatabase", task.getException().getMessage());
//                                        }
//                                    }
//                                });
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("AttendanceConfirmation")
                                .child(date).child(driverId).child(journeyId);

                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Log.d("valuess",dataSnapshot.toString());

                                if (dataSnapshot.exists()) {
                                    ArrayList<AttendanceConfirmation> attendanceList = new ArrayList<>();

                                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                      //  Log.d("valuess","inFor"+ dataSnapshot.toString());
//                                        HashMap<String,String> values = new HashMap<>();
//                                        values.p
                                     AttendanceConfirmation attendance =  childSnapshot.getValue(AttendanceConfirmation.class);

                                        //attendanceList.add(attendance);
                                        Log.d("values",attendance.toString());
                                    }

                                    // Store the attendance list or perform any required operations
                                    edit.putString("attending", new Gson().toJson(attendanceList));
                                    edit.commit();

                                    Log.d("DataReturned", attendanceList.toString());
                                } else {
                                    Log.d("DataReturned", "No data available");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.d("onFailure", databaseError.getMessage());
                            }
                        });


                    }
                }




        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AttendanceBottomSheet ABS = new AttendanceBottomSheet();
                ABS.show(getParentFragmentManager(), "");

            }
        });


        return binding.getRoot();
    }





    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

//        if (MarkersPoistions != null ) {
//            for (com.example.driveroutreach.model.Location position : MarkersPoistions) {
//                //adding benf place markers
//                MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(position.getLatitude(), position.getLongitude()));
//                Marker marker = googleMap.addMarker(markerOptions);
//
//                Log.d("client_location",new LatLng(position.getLatitude(), position.getLongitude()).toString());
//
//                LatLngBounds.Builder builder = new LatLngBounds.Builder();
//                builder.include(new LatLng(latitude_driver,longitude_driver));
//                builder.include(new LatLng(position.getLatitude(), position.getLongitude()));
//                LatLngBounds bounds = builder.build();
//                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20));
//            }
//        }


//        if (longitude_driver != 0 && latitude_driver != 0){
//            if (driver_marker != null) {
//                MarkerOptions markersPoistionsDriver = new MarkerOptions().position(new LatLng(latitude_driver, longitude_driver));
//                driver_marker = googleMap.addMarker(markersPoistionsDriver);
//            } else {
//                driver_marker.setPosition(new LatLng(latitude_driver, longitude_driver));
//            }
//        }



        Log.d("Locaaaation",new LatLng(longitude_driver,latitude_driver).toString());
    }




    public void onSetMapFrag() {
        //Setting the map in the fragment
        if (!isAdded()) return;
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.Map);
        mapFragment.getMapAsync(this);
    }


 @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLocation(LocationChanged event) {
        // Do something

     Log.d("onLocation", String.valueOf(event.latitude) + "outside IF");
        if(event!=null){
            longitude_driver= event.getLongitude();
             latitude_driver = event.getLatitude();

            Log.d("onLocation from event",event.latitude +" ,"+  event.longitude);

            Log.d("onLocation",  "event!=null");

if (map != null){
    Log.d("onLocation",  "map != null");

        Log.d("onLocation",  "latitude_driver != 0.0");
        if (driver_marker != null) {
            Log.d("onLocation",  "driver_marker != null");
            driver_marker.setPosition(new LatLng(latitude_driver, longitude_driver));


        }else {
            MarkerOptions markersPoistionsDriver = new MarkerOptions().position(new LatLng(latitude_driver, longitude_driver));
            driver_marker = map.addMarker(markersPoistionsDriver);

            Log.d("onLocation",  "else");
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(latitude_driver,longitude_driver));
        LatLngBounds bounds = builder.build();
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20));




}





        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}







