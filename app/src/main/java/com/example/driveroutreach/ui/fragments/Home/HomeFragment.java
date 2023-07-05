package com.example.driveroutreach.ui.fragments.Home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.driveroutreach.R;
import com.example.driveroutreach.databinding.FragmentHomeBinding;
import com.example.driveroutreach.model.Benefeciares;
import com.example.driveroutreach.ui.activities.Main.LocationChanged;
import com.example.driveroutreach.ui.base_classes.BaseFragment;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
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
    ArrayList<String> benf;
    Marker driverLocationMarker;
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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    //    sp = getActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);

        String driverId= sp.getString(DRIVER_ID_KEY,null);


    //    onGettingDriversLocation(driverId);

        onLocation(new LocationChanged());

        Log.d("loctation",latitude_driver + " "+ longitude_driver);

                     journeyId =sp.getString("journeyId",null);
                     date =sp.getString("journeyDate",null);

                if( sp.getBoolean("started",false)){
                    if (sp.getString("journeyId",null) !=null & sp.getString("journeyDate",null) != null) {
                        Log.d("Databack", journeyId + "journeey iddd" + date+"driverid"+driverId);
                        MarkersPoistions = new ArrayList<>();



                        reference = FirebaseDatabase.getInstance().getReference("AttendanceConfirmation").child("31-May-2023")
                                .child(driverId).child(journeyId).get()
                                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {

                                        if (task.isSuccessful()) {
                                            for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {


                                                benf =(ArrayList<String>) dataSnapshot.getValue();



                                                edit.putString("attending",  new Gson().toJson(benf));
                                                edit.commit();

                                                Log.d("DataReturned", benf.toString());

                                            }

                                            if (benf != null){
                                                for (int i = 0; i < benf.size(); i++) {

                                                    firestore.collection("Beneficiaries").document(benf.get(i)).get()
                                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                    if (task.isSuccessful()) {
                                                                        Benefeciares benefeciares = task.getResult().toObject(Benefeciares.class);

                                                                        Log.d("locationTag",benefeciares.getLocation().toString());

                                                                        //adding benf place markers
                                                                        MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(benefeciares.getLocation().getLatitude(), benefeciares.getLocation().getLongitude()));
                                                                        Marker marker = map.addMarker(markerOptions);




                                                                        MarkersPoistions.add(new com.example.driveroutreach.model.Location(benefeciares.getLocation().getLatitude(),benefeciares.getLocation().getLongitude()));
                                                                    } else {
                                                                        //Put exception
                                                                        Log.d("gettingLoc",task.getException().getMessage());
                                                                    }

                                                                }
                                                            });

                                                }
//                                              Log.d("clientMarkers",markedPositionMarker.toString());
                                                onSetMapFrag();

                                            }

                                        } else {
                                            Log.d("realtimeDatabase", task.getException().getMessage());
                                        }
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


 //check condition if we have the permission to get driver location, and if not we request it
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // When permission is granted
            // Call method

            startLocationUpdates();

        } else {
            // When permission is not granted
            // Call method
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }


    }


    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Check condition
        if (requestCode == 100 && (grantResults.length > 0) && (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
            // When permission are granted
            // Call  method
            startLocationUpdates();
        } else {
            // When permission are denied
            // Display toast
            Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }


    private void startLocationUpdates() {




// Adjust the initial camera position and zoom level
//        double defaultLatitude = latitude_driver; // Replace with your desired latitude
//        double defaultLongitude = longitude_driver; // Replace with your desired longitude
//        float defaultZoomLevel = 12f; // Replace with your desired zoom level

//        LatLng defaultLocation = new LatLng(defaultLatitude, defaultLongitude);
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, defaultZoomLevel));

        com.example.driveroutreach.model.Location specificLocation = new com.example.driveroutreach.model.Location(latitude_driver,longitude_driver); // Example specific location (San Francisco)
        com.example.driveroutreach.model.Location nearestLocation= findNearestLocation(specificLocation, MarkersPoistions);

     //   Log.d("Nearest location","Nearest location: " + latitude_driver + ", " + longitude_driver);
       // Log.d("Nearest location","Nearest location: " + ", " + nearestLocation.getLongitude());



        // Move the camera to the user's current location
     //   map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude_driver, longitude_driver), 20));
    }

    private com.example.driveroutreach.model.Location findNearestLocation(com.example.driveroutreach.model.Location specificLocation, ArrayList<com.example.driveroutreach.model.Location> locations) {

//this method calculates if the two points are near each other.
        com.example.driveroutreach.model.Location nearestLocation = null;
        double minDistance = 1000;
        for (com.example.driveroutreach.model.Location location : locations) {
            double distance = calculateDistance(specificLocation, location);
            if (distance < minDistance) {
               // minDistance = distance;
                nearestLocation = location;
            }
        }
        return nearestLocation;
    }


    private double calculateDistance(com.example.driveroutreach.model.Location location1, com.example.driveroutreach.model.Location location2) {

        //    Haversine Formula: The Haversine formula is a mathematical equation used for calculating distances between two points on a sphere  using their latitude and longitude coordinates.

        double earthRadius = 6371; // Radius of the earth in kilometers
        double dLat = Math.toRadians(location2.getLatitude() - location1.getLatitude());
        double dLon = Math.toRadians(location2.getLongitude() - location1.getLongitude());
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(location1.getLatitude())) * Math.cos(Math.toRadians(location2.getLatitude())) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c; // Distance in kilometers
        return distance;
    }



    public void onSetMapFrag() {
        //Setting the map in the fragment
        if (!isAdded()) return;
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.Map);
        mapFragment.getMapAsync(this);
    }




  void onGettingDriversLocation(String driverId){
      ref = FirebaseDatabase.getInstance().getReference("DriverLocation");
      GeoFire geoFire = new GeoFire(ref);

      geoFire.getLocation(driverId, new com.firebase.geofire.LocationCallback() {
          @Override
          public void onLocationResult(String key, GeoLocation location) {
              if (location != null) {
                  longitude_driver = location.longitude;
                  latitude_driver = location.latitude;

                  Log.d("CompareLocation",String.format("The location for key %s is [%f,%f]", key, location.latitude, location.longitude));

              } else {
                  Log.d("CompareLocation",String.format("There is no location for key %s in GeoFire", key));

              }
          }
          @Override
          public void onCancelled(DatabaseError databaseError) {
              Log.d("CompareLocation","There was an error getting the GeoFire location: " + databaseError);

          }
      });
  }




    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLocation(LocationChanged event) {
        // Do something
        if(event!=null){
            longitude_driver= event.longitude;
             latitude_driver = event.latitude;


             if (latitude_driver != 00){
                 if (driver_marker != null) {
                     driver_marker.setPosition(new LatLng(latitude_driver, longitude_driver));


                 } else {
                     MarkerOptions markersPoistionsDriver = new MarkerOptions().position(new LatLng(latitude_driver, longitude_driver));
                     driver_marker = map.addMarker(markersPoistionsDriver);
                 }

                 LatLngBounds.Builder builder = new LatLngBounds.Builder();
                 builder.include(new LatLng(latitude_driver,longitude_driver));

                 LatLngBounds bounds = builder.build();
                 map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20));
             }


           // onSetMapFrag();

            Log.d("onLocation from event",event.latitude +" ,"+  event.longitude);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}







