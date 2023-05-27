package com.example.driveroutreach.ui.activities.Main;

import static android.app.PendingIntent.getActivity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.driveroutreach.R;
import com.example.driveroutreach.databinding.ActivityMainBinding;
import com.example.driveroutreach.ui.fragments.Home.HomeFragment;
import com.example.driveroutreach.ui.fragments.schedule.ScheduleFragment;
import com.example.driveroutreach.ui.fragments.schedule.daily.days.DayFragment;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity implements MainView, DayFragment.OnDataListenerDayFrag {
    ActivityMainBinding binding;
    BottomSheetBehavior bottomSheetBehavior;
    Dialog dialog;
    Button btn_getLocation;
    public static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String latitude, longitude, latitude_sp, longitude_sp;
    public final String LATITUDE_KEY = "latitude";
    public final String LONGITUDE_KEY = "longitude";
    AlertDialog alertDialog;

    int LOCATION_REQUEST_CODE = 10001;
    FusedLocationProviderClient fusedLocationClient;
    SharedPreferences sp;
    SharedPreferences.Editor edit;
    private LocationRequest locationRequest;
    private static final String DIALOG_SHOWN_KEY = "dialog_shown";
    LocationCallback locationCallback;
    DatabaseReference ref ;
    public final String DRIVER_ID_KEY = "driverId";

    @Override
    protected void onStart() {
        super.onStart();
        showLocationDialog();

    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdates();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sp = getSharedPreferences("sp", MODE_PRIVATE);
        edit = sp.edit();

        String driver_id = sp.getString(DRIVER_ID_KEY,"null_id");

        Log.d("MainActivityLOG", "onCreate driver_id : "+driver_id);

        ref = FirebaseDatabase.getInstance().getReference("DriverLocation");
        GeoFire geoFire = new GeoFire(ref);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {

                if (locationResult == null){
                    return;
                }

                for (Location location : locationResult.getLocations()){
                    location.getLatitude();
                    location.getLongitude();
                    geoFire.setLocation(driver_id, new GeoLocation(location.getLongitude(), location.getLatitude()));
                    Log.d("MainActivityLOG", "onLocationResult: "+location.toString());
                }
            }
        };


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(4000);//sec
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        MainPresenter mainPresenter = new MainPresenter(this);
        mainPresenter.AddingFrag(new ScheduleFragment());

        binding.bottomNavigationMain.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mainPresenter.SelectingNavIcon(item);
                return true;
            }
        });

    }

    @Override
    public void onSetFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new ScheduleFragment()).commit();
        binding.bottomNavigationMain.setSelectedItemId(R.id.page_schedule);
    }

    @Override
    public void onSelectedNavIcon(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    private void showLocationDialog() {
        dialog = new Dialog(MainActivity.this);
        View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_dialog, findViewById(R.id.custom_dialog));
        dialog.setContentView(dialogView);
        dialog.show();

        btn_getLocation = dialogView.findViewById(R.id.btn_getLocation);
        btn_getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    checkSettingAndStartLocationUpdate();


                } else {
                    askLocationPermission();
                }

                dialog.dismiss();
            }
        });

        // Update shared preference to indicate dialog has been shown
        edit.putBoolean(DIALOG_SHOWN_KEY, true);
        edit.apply();
    }


    private void checkSettingAndStartLocationUpdate() {

        LocationSettingsRequest request = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build();

        SettingsClient client = LocationServices.getSettingsClient(MainActivity.this);
        Task<LocationSettingsResponse> locationSettingsResponseTask = client.checkLocationSettings(request);

        locationSettingsResponseTask.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                //settings of device are satisfied and we can start location update
                startLocationUpdates();
            }
        });

        locationSettingsResponseTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                if (e instanceof ResolvableApiException) {
                    ResolvableApiException apiException = (ResolvableApiException) e;
                    try {
                        apiException.startResolutionForResult(MainActivity.this, 1001);
                    } catch (IntentSender.SendIntentException ex) {
                        ex.printStackTrace();
                    }
                }

            }
        });
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    private void askLocationPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);


            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkSettingAndStartLocationUpdate();
            } else {

            }
        }
    }

    @Override
    public void onDataReceivedFromDayFrag(String journeyId, String date) {
        Log.d("TripData", journeyId + " " + date);
        HomeFragment homeFragment = HomeFragment.newInstance(journeyId, date);
        getSupportFragmentManager().beginTransaction().add(R.id.container, homeFragment).commit();
    }
}
