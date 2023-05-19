package com.example.driveroutreach.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.driveroutreach.databinding.ItemTripBinding;
import com.example.driveroutreach.model.JourneyModel;

import java.util.ArrayList;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.MyViewHolderTrip> {

ArrayList<JourneyModel> journys;


    public TripAdapter(ArrayList<JourneyModel> journys) {
        this.journys = journys;
    }

    @NonNull
    @Override
    public MyViewHolderTrip onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTripBinding binding = ItemTripBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new MyViewHolderTrip(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderTrip holder, int position) {

        if (journys == null){
            return;
        }

        JourneyModel journeyModel = journys.get(position);
        holder.TimeEnds.setText(journeyModel.getEnd());
        holder.TimeStart.setText(journeyModel.getStart());
        holder.itenaryId.setText("Itinerary"+journeyModel.getJourneyId());
        holder.ArrivalPlace.setText(journeyModel.getOrganization());
        holder.PickingupPlace.setText(journeyModel.getRegion());



    }

    @Override
    public int getItemCount() {
        return journys.size();
    }

    class MyViewHolderTrip extends RecyclerView.ViewHolder {

        TextView TimeStart, TimeEnds, ArrivalPlace, PickingupPlace, itenaryId;
        public MyViewHolderTrip(@NonNull ItemTripBinding binding) {
            super(binding.getRoot());

            TimeStart = binding.tvStartTimeItemSchedule;
            TimeEnds = binding.tvArrivalsTimeItemSchedule;
            ArrivalPlace = binding.tvArrivalsPlace;
            PickingupPlace = binding.tvStartingPlace;
            itenaryId = binding.tvItineraryNumber;

        }
    }
}
