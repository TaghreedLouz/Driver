package com.example.driveroutreach.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.driveroutreach.databinding.ItemArchiveDetailsBinding;

import java.util.ArrayList;

public class bottomSheetAdapter extends RecyclerView.Adapter<bottomSheetAdapter.HolderClass> {

    ArrayList<String> name;

    public bottomSheetAdapter(ArrayList<String> name) {
        this.name = name;
    }

    @NonNull
    @Override
    public HolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemArchiveDetailsBinding binding = ItemArchiveDetailsBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new HolderClass(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderClass holder, int position) {

        String n = name.get(position);

        holder.name.setText(n);
    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    class HolderClass extends RecyclerView.ViewHolder {

        TextView name;

        public HolderClass(@NonNull ItemArchiveDetailsBinding binding) {
            super(binding.getRoot());

           name = binding.tvName;
        }
    }
}
