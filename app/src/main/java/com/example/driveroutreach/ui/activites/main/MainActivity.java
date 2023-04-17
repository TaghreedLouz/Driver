package com.example.driveroutreach.ui.activites.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.driveroutreach.R;
import com.example.driveroutreach.databinding.ActivityMainBinding;
import com.example.driveroutreach.ui.fragments.schedule.ScheduleFragment;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//         هنا مجرد ما يفتح التطبيق حيكون الديفولت ع الفراقمنت يلي تحت ، ف حطيت تاعي مجرد ما يجهز تاعك الهوم ي نور
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new ScheduleFragment()).commit();
//        وهان بدي اياه يحدد ع الأيقون يلي تحت تاعت السيكيدجول
        binding.bottomNavigationMain.setSelectedItemId(R.id.page_schedule);


        binding.bottomNavigationMain.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null ;

                switch (item.getItemId()){

                    case R.id.page_home:
//                        fragment = new  فراقمنت الهوم يلي حتعمليه ي نور
                        break;

                    case R.id.page_schedule:
                         fragment = new ScheduleFragment(); // ندى
                        break;

                    case R.id.page_archive:
//                        fragment = new ...الأرشيف
                        break;

                    case R.id.page_profile:
//                        fragment = new ...البروفايل
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();

                return true;
            }
        });


    }
}