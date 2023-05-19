package com.example.driveroutreach.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.driveroutreach.databinding.ItemAllTripsBinding;
import com.example.driveroutreach.model.SchedualAllModel;

import java.util.ArrayList;

public class AllTripsAdapter extends RecyclerView.Adapter<AllTripsAdapter.MyViewHolderAllTrips> {

    ArrayList<String> OrganizationNames;

    ArrayList<SchedualAllModel> AllData;


    public AllTripsAdapter(ArrayList<String> organizationNames) {
        OrganizationNames = organizationNames;
    }

    public AllTripsAdapter(ArrayList<String> organizationNames, ArrayList<SchedualAllModel> allData) {
        OrganizationNames = organizationNames;
        AllData = allData;
    }

    @NonNull
    @Override
    public MyViewHolderAllTrips onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAllTripsBinding binding = ItemAllTripsBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new MyViewHolderAllTrips(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderAllTrips holder, int position) {

       holder.tripName.setText(OrganizationNames.get(position));

//       holder.ChildRv.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(),LinearLayoutManager.VERTICAL,false));
//       holder.ChildRv.setAdapter(new AllTripChildAdapter(AllData));
    }

    @Override
    public int getItemCount() {
        return OrganizationNames.size();
    }

    class MyViewHolderAllTrips extends RecyclerView.ViewHolder {
        TextView tripName;
        RecyclerView ChildRv;
        public MyViewHolderAllTrips(@NonNull ItemAllTripsBinding binding) {
            super(binding.getRoot());

            tripName = binding.tvTripName;
            ChildRv = binding.childRv;

        }
    }
}
