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

    SharedPreferences sp;
    SharedPreferences.Editor edit;
    private LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        callDialog(null);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);


        sp = getSharedPreferences("sp", MODE_PRIVATE);
        edit = sp.edit();

        String longitude_sp = sp.getString(LONGITUDE_KEY, "null");
        String latitude_sp = sp.getString(LATITUDE_KEY, "null");

        if (latitude_sp.equals("null") && longitude_sp.equals("null")) {
            callDialog(null);
        } else {
            Toast.makeText(MainActivity.this, "Your Location:" + "\n" + "Latitude= " + latitude_sp + "\n" + "Longitude= " + longitude_sp, Toast.LENGTH_SHORT).show();
            Log.d("TAGMain", "onCreate: " + "Your Location:" + "\n" + "Latitude= " + latitude_sp + "\n" + "Longitude= " + longitude_sp);
        }

        Toast.makeText(MainActivity.this, "Your Location:" + "\n" + "Latitude= " + latitude_sp + "\n" + "Longitude= " + longitude_sp, Toast.LENGTH_SHORT).show();



        ActivityCompat.requestPermissions(this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

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
        //         هنا مجرد ما يفتح التطبيق حيكون الديفولت ع الفراقمنت يلي تحت ، ف حطيت تاعي مجرد ما يجهز تاعك الهوم ي نور
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new ScheduleFragment()).commit();
        //        وهان بدي اياه يحدد ع الأيقون يلي تحت تاعت السيكيدجول
        binding.bottomNavigationMain.setSelectedItemId(R.id.page_schedule);
    }

    @Override
    public void onSelectedNavIcon(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    @Override
    public void onSend(boolean clicked) {

        if (clicked) binding.bottomNavigationMain.setVisibility(View.GONE);
        else binding.bottomNavigationMain.setVisibility(View.VISIBLE);

    }

    public void callDialog(View view) {
        dialog = new Dialog(MainActivity.this/*, R.style.BottomSheetTheme*/);
        View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_dialog, findViewById(R.id.custom_dialog));
        dialog.setContentView(dialogView);
        dialog.show();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1){
            dialog.show();
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){

                btn_getLocation = dialog.findViewById(R.id.btn_getLocation);
                btn_getLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        //Check gps is enable or not
                        if (isGPSEnabled()) {

                            getCurrentLocation();

                        }else {

                            turnOnGPS();
                        }

                        // dialog.dismiss();
                    }
                });


            }
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

                getCurrentLocation();
            }
        }
    }

    private void getCurrentLocation() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (isGPSEnabled()) {

                    LocationServices.getFusedLocationProviderClient(MainActivity.this)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(MainActivity.this)
                                            .removeLocationUpdates(this);

                                    if (locationResult != null && locationResult.getLocations().size() >0){

                                        int index = locationResult.getLocations().size() - 1;
                                        double lati = locationResult.getLocations().get(index).getLatitude();
                                        double longi = locationResult.getLocations().get(index).getLongitude();

                                        latitude = String.valueOf(lati);
                                        longitude = String.valueOf(longi);

                                        edit.putString(LATITUDE_KEY, latitude);
                                        edit.putString(LONGITUDE_KEY, longitude);
                                        edit.apply();
                                        Toast.makeText(MainActivity.this, "Latitude: "+ latitude + "\n" + "Longitude: "+ longitude, Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                }
                            }, Looper.getMainLooper());

                } else {
                    turnOnGPS();
                }

            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    private void turnOnGPS() {



        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(MainActivity.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();

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
                            //Device does not have location
                            break;
                    }
                }
            }
        });

    }

    private boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled = false;

        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;

    }
















//    private void getLocation() {
//
//        //Check Permissions again
//
//        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this,
//
//                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]
//                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
//        } else {
//            Location LocationGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            Location LocationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//            Location LocationPassive = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
//
//            Intent intent = new Intent();
//
//            if (LocationGps != null) {
//                double lat = LocationNetwork.getLatitude();
//                double longi = LocationNetwork.getLongitude();
//
//                latitude = String.valueOf(lat);
//                longitude = String.valueOf(longi);
//
//                edit.putString(LATITUDE_KEY, latitude);
//                edit.putString(LONGITUDE_KEY, longitude);
//                edit.apply();
//
//                intent.putExtra(LATITUDE_KEY, latitude);
//                intent.putExtra(LONGITUDE_KEY, longitude);
//
//                Toast.makeText(MainActivity.this, "Your Location:" + "\n" + "Latitude= " + latitude + "\n" + "Longitude= " + longitude, Toast.LENGTH_SHORT).show();
//
//            } else if (LocationNetwork != null) {
//                double lat = LocationNetwork.getLatitude();
//                double longi = LocationNetwork.getLongitude();
//
//
//                latitude = String.valueOf(lat);
//                longitude = String.valueOf(longi);
//
//                edit.putString(LATITUDE_KEY, latitude);
//                edit.putString(LONGITUDE_KEY, longitude);
//                edit.apply();
//
//                intent.putExtra(LATITUDE_KEY, latitude);
//                intent.putExtra(LONGITUDE_KEY, longitude);
//
//                Toast.makeText(MainActivity.this, "Your Location:" + "\n" + "Latitude= " + latitude + "\n" + "Longitude= " + longitude, Toast.LENGTH_SHORT).show();
//
//            } else if (LocationPassive != null) {
//                double lat = LocationPassive.getLatitude();
//                double longi = LocationPassive.getLongitude();
//
//                latitude = String.valueOf(lat);
//                longitude = String.valueOf(longi);
//
//                edit.putString(LATITUDE_KEY, latitude);
//                edit.putString(LONGITUDE_KEY, longitude);
//                edit.apply();
//
//                intent.putExtra(LATITUDE_KEY, latitude);
//                intent.putExtra(LONGITUDE_KEY, longitude);
//
//                Toast.makeText(MainActivity.this, "Your Location:" + "\n" + "Latitude= " + latitude + "\n" + "Longitude= " + longitude, Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(this, "Can't Get Your Location", Toast.LENGTH_SHORT).show();
//            }
//
//            setResult(RESULT_OK, intent);
//            dialog.dismiss();
//        }
//
//
//    }

//    private void OnGPS() {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                callDialog(null);
//            }
//        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                dialog.cancel();
//                callDialog(null);
//            }
//        });
//        final AlertDialog alertDialog = builder.create();
//        alertDialog.show();
//    }

    @Override
    public void onDataReceivedFromDayFrag(String journeyId, String date) {
        Log.d("TripData",journeyId + " "+ date);

        HomeFragment homeFragment=HomeFragment.newInstance(journeyId,date);
        getSupportFragmentManager().beginTransaction().add(R.id.container,homeFragment).commit();
    }
}