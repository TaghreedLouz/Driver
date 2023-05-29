package com.example.driveroutreach.ui.fragments.Home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.driveroutreach.R;
import com.example.driveroutreach.databinding.FragmentHomeBinding;
import com.example.driveroutreach.model.Benefeciares;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
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

import java.util.ArrayList;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements HomeView, OnMapReadyCallback {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentHomeBinding binding = FragmentHomeBinding.inflate(inflater, container, false);

        HomePresenter homePresenter = new HomePresenter(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();



        if (journeyId != null) {
            Log.d("Databack", journeyId + "journeey iddd");
            MarkersPoistions = new ArrayList<>();

            reference = FirebaseDatabase.getInstance().getReference("AttendanceConfirmation").child("20-May-2023")
                    .child("1").child(journeyId).get()
                    .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {

                            if (task.isSuccessful()) {
                                for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {


                                      benf =(ArrayList<String>) dataSnapshot.getValue();

                                    Log.d("DataReturned", benf.toString());

                                    }

                                   if (!benf.isEmpty()){
                                       for (int i = 0; i < benf.size(); i++) {

                                           firestore.collection("Beneficiaries").document(benf.get(i)).get()
                                                   .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                       @Override
                                                       public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                           if (task.isSuccessful()) {
                                                               Benefeciares benefeciares = task.getResult().toObject(Benefeciares.class);

                                                               Log.d("locationTag",benefeciares.getLocation().toString());

                                                               MarkersPoistions.add(new com.example.driveroutreach.model.Location(benefeciares.getLocation().getLatitude(),benefeciares.getLocation().getLongitude()));
                                                           } else {
                                                               //Put exception
                                                           }
                                                           onSetMapFrag();
                                                       }
                                                   });

                                       }
                                   }

                            } else {
                                Log.d("realtimeDatabase", task.getException().getMessage());
                            }
                        }
                    });






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

        for (com.example.driveroutreach.model.Location position : MarkersPoistions) {

            MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(position.getLatitude(),position.getLongitude()));
            Marker marker = googleMap.addMarker(markerOptions);
        }


        // Move the camera to the first marker in the array of benf
        if (!MarkersPoistions.isEmpty()) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(MarkersPoistions.get(0).getLatitude(), MarkersPoistions.get(0).getLongitude()), 20f));
        }


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
        ref = FirebaseDatabase.getInstance().getReference("DriverLocation");
        GeoFire geoFire = new GeoFire(ref);

        geoFire.getLocation("1", new com.firebase.geofire.LocationCallback() {
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



// Adjust the initial camera position and zoom level
        double defaultLatitude = latitude_driver; // Replace with your desired latitude
        double defaultLongitude = longitude_driver; // Replace with your desired longitude
        float defaultZoomLevel = 12f; // Replace with your desired zoom level

//        LatLng defaultLocation = new LatLng(defaultLatitude, defaultLongitude);
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, defaultZoomLevel));

        com.example.driveroutreach.model.Location specificLocation = new com.example.driveroutreach.model.Location(latitude_driver,longitude_driver); // Example specific location (San Francisco)
        com.example.driveroutreach.model.Location nearestLocation= findNearestLocation(specificLocation, MarkersPoistions);

        Log.d("Nearest location","Nearest location: " + nearestLocation.getLatitude() + ", " + nearestLocation.getLongitude());


        // Move the camera to the user's current location
     //   map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude_driver, longitude_driver), 20));
    }

    private com.example.driveroutreach.model.Location findNearestLocation(com.example.driveroutreach.model.Location specificLocation, ArrayList<com.example.driveroutreach.model.Location> locations) {

//this method calculates if the two points are near each other.
        com.example.driveroutreach.model.Location nearestLocation = null;
        double minDistance = Double.MAX_VALUE;
        for (com.example.driveroutreach.model.Location location : locations) {
            double distance = calculateDistance(specificLocation, location);
            if (distance < minDistance) {
                minDistance = distance;
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
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.Map);
        mapFragment.getMapAsync(this);
    }



    }







