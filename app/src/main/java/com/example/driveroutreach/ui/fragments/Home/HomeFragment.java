package com.example.driveroutreach.ui.fragments.Home;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.example.driveroutreach.ui.app_utility.AppUtility;
import com.example.driveroutreach.ui.base_classes.BaseFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
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

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("AttendanceConfirmation")
                                .child(date).child(driverId).child(journeyId);

                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Log.d("valuess",dataSnapshot.toString());

                                if (dataSnapshot.exists()) {
                                    ArrayList<String> attendanceList = new ArrayList<>();
                                    ArrayList<Location> clientLocationList = new ArrayList<>();

                                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {

                                     AttendanceConfirmation attendance =  childSnapshot.getValue(AttendanceConfirmation.class);


                                        attendanceList.add(attendance.getUserId());
                                        Log.d("values",attendance.toString());

                                        Location x = new Location("location");
                                        x.setLatitude(attendance.getLat());
                                        x.setLongitude(attendance.getLon());
                                        clientLocationList.add(x);

                                        if (map != null){
                                            //adding benf place markers
                                            MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(attendance.getLat(), attendance.getLon()));
                                            Marker marker = map.addMarker(markerOptions);
                                        }


                                    }
                                    // Store the attendance list or perform any required operations
                                    edit.putString("attending", new Gson().toJson(attendanceList));
                                    edit.commit();

                                    // check if near
                                    Handler handler = new Handler(Looper.myLooper());
                                    Runnable keepLooping= new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.d("distance","array: "+clientLocationList.toString());

                                            if (clientLocationList != null){
                                                for (Location loc:clientLocationList){
                                                    Log.d("distance","before"+String.valueOf(loc.getLongitude()));

                                                    float distance = homePresenter.calculateDiatance(
                                                            loc.getLongitude(),loc.getLatitude(),
                                                            Double.parseDouble(sp.getString("longitude",null)),
                                                            Double.parseDouble(sp.getString("latitude",null)));

                                                    Log.d("distance","distance is:"+String.valueOf(distance));
                                                    if (distance<=100) {
                                                        Log.d("distance","distance<=100");
                                                        clientLocationList.remove(loc);
                                                        AppUtility.showSnackbar(binding.getRoot(),"You are near a client");
                                                    }
                                                    //TimeUnit.MINUTES.toMillis(5)
                                                }
                                                handler.postDelayed(this,20000 );
                                            }

                                        }
                                    };


                                   // Start the periodic updates
                                    handler.post(keepLooping);



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


       moveToDriverMarker();


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
}
        }
    }

    private void moveToDriverMarker() {
       map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(31.500928391303876, 34.45740717950703),17));
        // Zoom in, animating the camera.

        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setZoomGesturesEnabled(true);
        map.getUiSettings().setScrollGesturesEnabled(true);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }



}







