package com.example.driveroutreach.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.driveroutreach.databinding.ItemArchiveBinding;

public class ArchiveAdapter extends RecyclerView.Adapter<ArchiveAdapter.AVH> {


    public ArchiveAdapter() {
    }

    @NonNull
    @Override
    public AVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemArchiveBinding binding = ItemArchiveBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new AVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AVH holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 15;
    }

    class AVH extends RecyclerView.ViewHolder {


        public AVH(@NonNull ItemArchiveBinding binding) {
            super(binding.getRoot());
        }
    }
}
