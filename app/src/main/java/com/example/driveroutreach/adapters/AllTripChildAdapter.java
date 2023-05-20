package com.example.driveroutreach.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.driveroutreach.databinding.ItemAllTripsChildRvBinding;
import com.example.driveroutreach.model.SchedualAllModel;

import java.util.ArrayList;

public class AllTripChildAdapter extends RecyclerView.Adapter<AllTripChildAdapter.item_all_trips_childVH> {

    ArrayList<SchedualAllModel> AllData;

    public AllTripChildAdapter(ArrayList<SchedualAllModel> allData) {
        AllData = allData;
    }

    @NonNull
    @Override
    public item_all_trips_childVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAllTripsChildRvBinding binding = ItemAllTripsChildRvBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new item_all_trips_childVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull item_all_trips_childVH holder, int position) {

      SchedualAllModel all=  AllData.get(position);

      holder.end.setText(all.getEnd());
      holder.start.setText(all.getStart());
      holder.day.setText(all.getDay());
    }

    @Override
    public int getItemCount() {
        return AllData.size();
    }

    class item_all_trips_childVH extends RecyclerView.ViewHolder{

        TextView end, start,day, end2,start2;
    public item_all_trips_childVH(@NonNull ItemAllTripsChildRvBinding binding) {
        super(binding.getRoot());

        end = binding.tvTimeEnd1;
        start =binding.tvTimeStart1;
        day =binding.tvDay;

    }
}
}
