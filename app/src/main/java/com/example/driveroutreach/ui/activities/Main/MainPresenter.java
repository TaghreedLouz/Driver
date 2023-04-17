package com.example.driveroutreach.ui.activities.Main;

import android.view.MenuItem;

import androidx.fragment.app.Fragment;

import com.example.driveroutreach.R;

public class MainPresenter {
    MainView view;

    public MainPresenter(MainView view) {
        this.view = view;
    }

    public void AddingFrag(Fragment fragment){
         view.onSetFragment(fragment);

    }


    public void SelectingNavIcon(MenuItem item){
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

        view.onSelectedNavIcon(fragment);

    }
}
