package com.example.driveroutreach.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ArchiveAttendanceAdapter extends RecyclerView.Adapter<ArchiveAttendanceAdapter.AAVH> {


    @NonNull
    @Override
    public AAVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull AAVH holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }



    class AAVH extends RecyclerView.ViewHolder {


        public AAVH(@NonNull View itemView) {
            super(itemView);
        }
    }
}
