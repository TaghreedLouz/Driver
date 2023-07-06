package com.example.driveroutreach.ui.base_classes;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {


   public SharedPreferences sp;
   public SharedPreferences.Editor edit;

    public final String DRIVER_ID_KEY = "driverId" , DRIVER_MOBILE_KEY = "driverMobile";
    public final String DRIVER_NUMBER_KEY = "driverNumber";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp = getSharedPreferences("sp", MODE_PRIVATE);
        edit = sp.edit();
    }
}
