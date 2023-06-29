package com.example.driveroutreach.ui.base_classes;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {


    SharedPreferences sp;
    SharedPreferences.Editor edit;
    public final String DRIVER_ID_KEY = "driverId" , DRIVER_MOBILE_KEY = "driverMobile";
    public final String DRIVER_NUMBER_KEY = "driverNumber";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        sp = getSharedPreferences("sp", MODE_PRIVATE);
        edit = sp.edit();


    }
}
