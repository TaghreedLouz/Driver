package com.example.driveroutreach.ui.activities.onboarding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.example.driveroutreach.R;
import com.example.driveroutreach.adapters.OnboardingPagerAdapter;
import com.example.driveroutreach.databinding.ActivityOnboardingBinding;
import com.example.driveroutreach.ui.activities.Login.LoginActivity;

public class OnboardingActivity extends AppCompatActivity {

    ActivityOnboardingBinding binding;
    private OnboardingPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnboardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        pagerAdapter = new OnboardingPagerAdapter(this);
        binding.viewPager.setAdapter(pagerAdapter);
        binding.viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return motionEvent.getAction() == MotionEvent.ACTION_MOVE; // Only allow move events, block swipe events
            }
        });
        binding.viewPager.addOnPageChangeListener(pageChangeListener);

        initializePageIndicator();



        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.viewPager.getCurrentItem() < pagerAdapter.getCount() - 1) {
                    binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() + 1);
                    if (binding.viewPager.getCurrentItem()  == 2) {
                        // Update the text of the Next button
                        binding.btnNext.setText("Start");
                        binding.btnNext.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.img_bg_3));
                        binding.btnNext.setTextSize(10f);
                        binding.btnSkip.setVisibility(View.GONE);
                    } else if (binding.viewPager.getCurrentItem()  == 1){
                        // Reset the text of the Next button
                        binding.btnNext.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.img_bg_2));
                    } else if (binding.viewPager.getCurrentItem()  == 0){
                        // Reset the text of the Next button
                        binding.btnNext.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.img_bg_1));

                    }

                } else {
                   startActivity(new Intent(getBaseContext(), LoginActivity.class));
                    finish();
                }
            }
        });

        binding.btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), LoginActivity.class));
                finish();
            }
        });
    }

    private final ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // Not used
        }

        @Override
        public void onPageSelected(int position) {
            updatePageIndicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            // Not used
        }
    };

    private void updatePageIndicator(int currentPage) {
        int selectedDotDrawable = R.drawable.indicator_dot_selected;
        int unselectedDotDrawable = R.drawable.indicator_dot_unselected;

        binding.dot1.setImageResource(currentPage == 0 ? selectedDotDrawable : unselectedDotDrawable);
        binding.dot2.setImageResource(currentPage == 1 ? selectedDotDrawable : unselectedDotDrawable);
        binding.dot3.setImageResource(currentPage == 2 ? selectedDotDrawable : unselectedDotDrawable);
    }

    private void initializePageIndicator() {
        int selectedDotDrawable = R.drawable.indicator_dot_selected;
        int unselectedDotDrawable = R.drawable.indicator_dot_unselected;

        // Set the initial state of the dots
        binding.dot1.setImageResource(selectedDotDrawable);
        binding.dot2.setImageResource(unselectedDotDrawable);
        binding.dot3.setImageResource(unselectedDotDrawable);
    }




}