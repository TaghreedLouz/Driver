package com.example.driveroutreach.ui.activities.Main;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
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
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements MainView, HomeFragment.onSendData {
    ActivityMainBinding binding;
    BottomSheetBehavior bottomSheetBehavior;
    Dialog dialog;
    Button btn_getLocation;
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String latitude, longitude, latitude_sp, longitude_sp;
    public final String LATITUDE_KEY = "latitude";
    public final String LONGITUDE_KEY = "longitude";

    SharedPreferences sp;
    SharedPreferences.Editor edit;

    @Override
    protected void onStart() {
        super.onStart();

//        sp = getSharedPreferences("spLocation", MODE_PRIVATE);
//        edit = sp.edit();
//
//        String latitude_sp = sp.getString(LONGITUDE_KEY, "null");
//        String longitude_sp = sp.getString(LATITUDE_KEY, "null");
//
//        if (latitude_sp.equals("null") && longitude_sp.equals("null")) {
//            callDialog(null);
//        } else {
//            Toast.makeText(MainActivity.this, "Your Location:" + "\n" + "Latitude= " + latitude_sp + "\n" + "Longitude= " + longitude_sp, Toast.LENGTH_SHORT).show();
//            Log.d("TAGMain", "onCreate: " + longitude + "" + latitude);
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sp = getSharedPreferences("spLocation", MODE_PRIVATE);
        edit = sp.edit();

        String latitude_sp = sp.getString(LONGITUDE_KEY, "null");
        String longitude_sp = sp.getString(LATITUDE_KEY, "null");

        if (latitude_sp.equals("null") && longitude_sp.equals("null")) {
            callDialog(null);
        } else {
            Toast.makeText(MainActivity.this, "Your Location:" + "\n" + "Latitude= " + latitude_sp + "\n" + "Longitude= " + longitude_sp, Toast.LENGTH_SHORT).show();
            Log.d("TAGMain", "onCreate: " + longitude + "" + latitude);
        }

        Toast.makeText(MainActivity.this, "Your Location:" + "\n" + "Latitude= " + latitude_sp + "\n" + "Longitude= " + longitude_sp, Toast.LENGTH_SHORT).show();


//        if (latitude_sp == null && longitude_sp == null){
//            callDialog(null);
//        }else




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

        btn_getLocation = dialogView.findViewById(R.id.btn_getLocation);
        btn_getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                //Check gps is enable or not

                dialog.dismiss();

                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    //Write Function To enable gps

                    OnGPS();
                } else {
                    //GPS is already On then
                    getLocation();
                }
            }
        });

    }

    private void getLocation() {

        //Check Permissions again

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this,

                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location LocationGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location LocationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            Intent intent = new Intent();

            if (LocationGps != null) {
                double lat = LocationNetwork.getLatitude();
                double longi = LocationNetwork.getLongitude();

                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);

                edit.putString(LATITUDE_KEY, latitude);
                edit.putString(LONGITUDE_KEY, longitude);
                edit.apply();

                intent.putExtra(LATITUDE_KEY, latitude);
                intent.putExtra(LONGITUDE_KEY, longitude);

                Toast.makeText(MainActivity.this, "Your Location:" + "\n" + "Latitude= " + latitude + "\n" + "Longitude= " + longitude, Toast.LENGTH_SHORT).show();

            } else if (LocationNetwork != null) {
                double lat = LocationNetwork.getLatitude();
                double longi = LocationNetwork.getLongitude();


                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);

                edit.putString(LATITUDE_KEY, latitude);
                edit.putString(LONGITUDE_KEY, longitude);
                edit.apply();

                intent.putExtra(LATITUDE_KEY, latitude);
                intent.putExtra(LONGITUDE_KEY, longitude);

                Toast.makeText(MainActivity.this, "Your Location:" + "\n" + "Latitude= " + latitude + "\n" + "Longitude= " + longitude, Toast.LENGTH_SHORT).show();

            } else if (LocationPassive != null) {
                double lat = LocationPassive.getLatitude();
                double longi = LocationPassive.getLongitude();
                ;

                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);

                edit.putString(LATITUDE_KEY, latitude);
                edit.putString(LONGITUDE_KEY, longitude);
                edit.apply();

                intent.putExtra(LATITUDE_KEY, latitude);
                intent.putExtra(LONGITUDE_KEY, longitude);

                Toast.makeText(MainActivity.this, "Your Location:" + "\n" + "Latitude= " + latitude + "\n" + "Longitude= " + longitude, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Can't Get Your Location", Toast.LENGTH_SHORT).show();
            }

            setResult(RESULT_OK, intent);
            //Thats All Run Your App
//            if (latitude == null || longitude == "null"){
//                callDialog(null);
//            }else {
//                Toast.makeText(MainActivity.this, "Your Location:"+"\n"+"Latitude= "+latitude+"\n"+"Longitude= "+longitude, Toast.LENGTH_SHORT).show();
//                Log.d("TAGMain", "onCreate: "+longitude+""+latitude);
//            }
            dialog.dismiss();
        }


    }

    private void OnGPS() {


        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                callDialog(null);
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}