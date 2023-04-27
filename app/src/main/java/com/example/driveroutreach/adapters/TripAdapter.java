package com.example.driveroutreach.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.driveroutreach.databinding.ItemTripBinding;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.MyViewHolderTrip> {


    @NonNull
    @Override
    public MyViewHolderTrip onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTripBinding binding = ItemTripBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new MyViewHolderTrip(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderTrip holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class MyViewHolderTrip extends RecyclerView.ViewHolder {
        public MyViewHolderTrip(@NonNull ItemTripBinding binding) {
            super(binding.getRoot());
        }
    }
}
