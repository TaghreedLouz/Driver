package com.example.driveroutreach.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ArchiveAdapter extends RecyclerView.Adapter<ArchiveAdapter.AVH> {


    @NonNull
    @Override
    public AVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull AVH holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class AVH extends RecyclerView.ViewHolder {


        public AVH(@NonNull View itemView) {
            super(itemView);
        }
    }
}
