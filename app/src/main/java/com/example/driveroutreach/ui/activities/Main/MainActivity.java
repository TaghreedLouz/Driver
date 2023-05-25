package com.example.driveroutreach.ui.activities.Main;

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
import androidx.fragment.app.Fragment;

import com.example.driveroutreach.R;
import com.example.driveroutreach.databinding.ActivityMainBinding;
import com.example.driveroutreach.ui.fragments.Home.HomeFragment;
import com.example.driveroutreach.ui.fragments.schedule.ScheduleFragment;
import com.example.driveroutreach.ui.fragments.schedule.daily.days.DayFragment;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements MainView, HomeFragment.onSendData, DayFragment.OnDataListenerDayFrag {
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

    SharedPreferences sp;
    SharedPreferences.Editor edit;
    private LocationRequest locationRequest;
    private static final String DIALOG_SHOWN_KEY = "dialog_shown";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sp = getSharedPreferences("sp", MODE_PRIVATE);
        edit = sp.edit();

        MainPresenter mainPresenter = new MainPresenter(this);
        mainPresenter.AddingFrag(new ScheduleFragment());

        binding.bottomNavigationMain.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mainPresenter.SelectingNavIcon(item);
                return true;
            }
        });

        String longitude_sp = sp.getString(LONGITUDE_KEY, "null");
        String latitude_sp = sp.getString(LATITUDE_KEY, "null");

        if (latitude_sp.equals("null") && longitude_sp.equals("null")) {
            // Check if dialog has been shown before
            boolean dialogShown = sp.getBoolean(DIALOG_SHOWN_KEY, false);
            if (!dialogShown) {
                // Request location permission if not granted
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
                } else {
                    // Permission already granted, call the method to get the current location
                    showLocationDialog();
                }
            }
        } else {
            Toast.makeText(MainActivity.this, "Your Location:" + "\n" + "Latitude= " + latitude_sp + "\n" + "Longitude= " + longitude_sp, Toast.LENGTH_SHORT).show();
            Log.d("TAGMain", "onCreate: " + "Your Location:" + "\n" + "Latitude= " + latitude_sp + "\n" + "Longitude= " + longitude_sp);
        }
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

    @Override
    public void onSend(boolean clicked) {
        if (clicked)
            binding.bottomNavigationMain.setVisibility(View.GONE);
        else
            binding.bottomNavigationMain.setVisibility(View.VISIBLE);
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
                getCurrentLocation();
                dialog.dismiss();
            }
        });

        // Update shared preference to indicate dialog has been shown
        edit.putBoolean(DIALOG_SHOWN_KEY, true);
        edit.apply();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showLocationDialog();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                getCurrentLocation();
            } else {
                showLocationNotAvailableDialog();
            }
        }
    }

    private void getCurrentLocation() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000); // 10 seconds
        locationRequest.setFastestInterval(2 * 1000); // 2 seconds

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (isGPSEnabled()) {
                    LocationServices.getFusedLocationProviderClient(MainActivity.this)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);
                                    LocationServices.getFusedLocationProviderClient(MainActivity.this).removeLocationUpdates(this);
                                    if (locationResult != null && locationResult.getLocations().size() > 0) {
                                        int index = locationResult.getLocations().size() - 1;
                                        double lati = locationResult.getLocations().get(index).getLatitude();
                                        double longi = locationResult.getLocations().get(index).getLongitude();

                                        latitude = String.valueOf(lati);
                                        longitude = String.valueOf(longi);

                                        edit.putString(LATITUDE_KEY, latitude);
                                        edit.putString(LONGITUDE_KEY, longitude);
                                        edit.apply();
                                        Toast.makeText(MainActivity.this, "Latitude: " + latitude + "\n" + "Longitude: " + longitude, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, Looper.getMainLooper());
                } else {
                    showLocationNotAvailableDialog();
                }
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            }
        }
    }

    private void showLocationNotAvailableDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Location Not Available")
                .setMessage("Unable to get the current location. Please make sure your GPS is enabled and try again.")
                .setPositiveButton("OK", null)
                .show();


    }

    private void turnOnGPS() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext()).checkLocationSettings(builder.build());
        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(MainActivity.this, "GPS is already turned on", Toast.LENGTH_SHORT).show();
                } catch (ApiException e) {
                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(MainActivity.this, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;
                    }
                }
            }
        });
    }

    private boolean isGPSEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean providerEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (providerEnabled) {
            return true;
        } else {
            GPS();
        }
        return false;
    }

    private void GPS() {
        alertDialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("GPS Permission")
                .setMessage("GPS is required for this app to work. Please enable GPS")
                .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, 2);
                    }
                })
                .setCancelable(false)
                .show();
    }

    @Override
    public void onDataReceivedFromDayFrag(String journeyId, String date) {
        Log.d("TripData", journeyId + " " + date);
        HomeFragment homeFragment = HomeFragment.newInstance(journeyId, date);
        getSupportFragmentManager().beginTransaction().add(R.id.container, homeFragment).commit();
    }
}
