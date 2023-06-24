package com.example.driveroutreach.ui.activities.Main;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.driveroutreach.R;
import com.example.driveroutreach.databinding.ActivityMainBinding;
import com.example.driveroutreach.ui.fragments.Home.HomeFragment;
import com.example.driveroutreach.ui.fragments.schedule.ScheduleFragment;
import com.example.driveroutreach.ui.fragments.schedule.days.DayFragment;
import com.firebase.geofire.GeoFire;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DatabaseReference;


public class MainActivity extends AppCompatActivity implements MainView, DayFragment.OnDataListenerDayFrag {
    ActivityMainBinding binding;
    BottomSheetBehavior bottomSheetBehavior;
    Dialog dialog;
    Button btn_getLocation;
    public static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    Float latitude_sp, longitude_sp;
    public final String LATITUDE_KEY = "latitude";
    public final String LONGITUDE_KEY = "longitude";
    AlertDialog alertDialog;
    double longitude_driver;
    double latitude_driver ;
    int LOCATION_REQUEST_CODE = 10001;
    FusedLocationProviderClient fusedLocationClient;
    SharedPreferences sp;
    SharedPreferences.Editor edit;

    private LocationRequest locationRequest;
    private static final String DIALOG_SHOWN_KEY = "dialog_shown";
    LocationCallback locationCallback;
    DatabaseReference ref ;
    public final String DRIVER_ID_KEY = "driverId";
    GeoFire geoFire;

    boolean isClicked = false;
    private static final int PERMISSIONS_REQUEST_LOCATION = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sp = getSharedPreferences("sp", MODE_PRIVATE);
        edit = sp.edit();





        String driverId= sp.getString(DRIVER_ID_KEY,null);



        MainPresenter mainPresenter = new MainPresenter(this);
        mainPresenter.AddingFrag(new ScheduleFragment());

        binding.bottomNavigationMain.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mainPresenter.SelectingNavIcon(item);
                return true;
            }
        });
        showLocationDialog();


//        if (isClicked){
//            showLocationDialog();
//
//        }else {
//            startService(new Intent(MainActivity.this, LocationService.class));
//        }

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
    public void onDataReceivedFromDayFrag(String journeyId, String date) {
        Log.d("TripData", journeyId + " " + date);
        HomeFragment homeFragment = HomeFragment.newInstance(journeyId, date);
        getSupportFragmentManager().beginTransaction().add(R.id.container, homeFragment).commit();
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

                // ااذا اخد البيرمشن ولا لا
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    startLocationService();

                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
                }

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationService();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();

            }
        }
    }


    private void startLocationService() {
        //يشغل كود السيرفس
        startService(new Intent(MainActivity.this, LocationService.class));
        dialog.dismiss();
        isClicked = true;
    }

}
