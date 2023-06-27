package com.example.driveroutreach.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.driveroutreach.R;
import com.example.driveroutreach.databinding.OnboardingItemBinding;

public class OnboardingPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;

    public OnboardingPagerAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        // Inflate the layout using data binding
        OnboardingItemBinding binding = OnboardingItemBinding.inflate(layoutInflater, container, false);
        View itemView = binding.getRoot();
        container.addView(itemView);



        // Set the content for each onboarding screen
        switch (position) {
            case 0:
                binding.imageView.setImageResource(R.drawable.img_onbording1);
                binding.tvTitle.setText("Communicate with the driver\n");
                binding.tvDescription.setText("communicate directly with the driver and confirm attendance");
                break;
            case 1:
                binding.imageView.setImageResource(R.drawable.img_onbording2);
                binding.tvTitle.setText("Track your ride\n");
                binding.tvDescription.setText("Know your driver in advance and be able to view current location in real time on the map");

                break;
            case 2:
                binding.imageView.setImageResource(R.drawable.img_onbording3);
                binding.tvTitle.setText("Get to the specified place!\n");
                binding.tvDescription.setText("Get to know a driver in advance and be able to locate your actual location");

                break;
        }

        return itemView;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

}