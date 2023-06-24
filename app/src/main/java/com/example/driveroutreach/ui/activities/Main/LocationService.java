package com.example.driveroutreach.ui.activities.Main;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.driveroutreach.model.DriversNumbers;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class LocationService extends Service implements LocationListener {
    private LocationManager locationManager;
    private DatabaseReference locationRef;
    private DriversNumbers driver;
    Map<String, Object> locationMap;

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationRef = FirebaseDatabase.getInstance().getReference("DriverLocation");
        driver = new DriversNumbers();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        requestLocationUpdates();

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        // Store latitude and longitude in a HashMap
        locationMap = new HashMap<>();
        locationMap.put("latitude", latitude);
        locationMap.put("longitude", longitude);

        // Update the location in real-time using Firebase
        locationRef.child(String.valueOf(driver.getMobile())).setValue(locationMap);

        Log.d("LocationService", "Latitude: " + driver.getMobile() + " " + latitude + ", Longitude: " + longitude);

        Toast.makeText(this, "Latitude: " + latitude + ", Longitude: " + longitude, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    private void requestLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (locationManager != null) {
                try {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void removeLocationUpdates() {
        if (locationManager != null) {
            try {
                locationManager.removeUpdates(this);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeLocationUpdates();
        locationRef.child(String.valueOf(driver.getMobile())).setValue(locationMap);

    }
}