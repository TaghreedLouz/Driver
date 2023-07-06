package com.example.driveroutreach.ui.activities.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.driveroutreach.R;
import com.example.driveroutreach.ui.activities.Login.LoginActivity;
import com.example.driveroutreach.ui.activities.onboarding.OnboardingActivity;
import com.example.driveroutreach.ui.app_utility.AppUtility;
import com.example.driveroutreach.ui.base_classes.BaseActivity;

import java.text.SimpleDateFormat;

public class SplashActivity extends BaseActivity {

//    SharedPreferences sp;
//    SharedPreferences.Editor edit;
    public static final String IS_FIRST_KEY = "firstTime";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        sp = getSharedPreferences("sp", MODE_PRIVATE);
//        edit = sp.edit();

        checkNewDay();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                edit.putBoolean(IS_FIRST_KEY, false);
                boolean isFirstTime = sp.getBoolean(IS_FIRST_KEY, true);
                if (isFirstTime) {
                    startActivity(new Intent(SplashActivity.this, OnboardingActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
                edit.apply();
                finish();
            }
        }, 4000);





    }

    private void checkNewDay() {
        SimpleDateFormat date= new SimpleDateFormat("dd-MMMM-yyyy");

        String previous = sp.getString("createdSps",null);
        Log.d("boolean",previous);
        if(previous != null){
          boolean x = previous.equals(AppUtility.getDate());
            Log.d("boolean",String.valueOf(x));
          if (!x){
              Log.d("boolean",String.valueOf(x)+"inside");
              edit.putString("FinishedJourneys",null);
              edit.putString("attending",null);
              edit.commit();
          }
        }

//        Date created;
//        Date currentDate;
//
//        try {
//             created=   date.parse(sp.getString("createdSps",null));
//             currentDate = date.parse(AppUtility.getDate());
//            if (sp!=null){
//                if (previous != null){
//                    if (created.compareTo(currentDate)<0){
//                        edit.putString("FinishedJourneys",null);
//                        edit.putString("attending",null);
//                        edit.commit();
//                    }
//                }
//            }
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        }
    }


}
