package com.example.driveroutreach.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.driveroutreach.databinding.ItemAllTripsBinding;

public class AllTripsAdapter extends RecyclerView.Adapter<AllTripsAdapter.MyViewHolderAllTrips> {

    @NonNull
    @Override
    public MyViewHolderAllTrips onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAllTripsBinding binding = ItemAllTripsBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new MyViewHolderAllTrips(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderAllTrips holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 15;
    }

    class MyViewHolderAllTrips extends RecyclerView.ViewHolder {
        public MyViewHolderAllTrips(@NonNull ItemAllTripsBinding binding) {
            super(binding.getRoot());
        }
    }
}
