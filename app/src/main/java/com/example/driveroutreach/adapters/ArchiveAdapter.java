package com.example.driveroutreach.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.driveroutreach.databinding.ItemArchiveBinding;
import com.example.driveroutreach.model.ArichivedJourney;

import java.util.ArrayList;

public class ArchiveAdapter extends RecyclerView.Adapter<ArchiveAdapter.AVH> {

ArrayList<ArichivedJourney> journeys;

    public ArchiveAdapter(ArrayList<ArichivedJourney> journeys) {
        this.journeys = journeys;
    }


    @NonNull
    @Override
    public AVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemArchiveBinding binding = ItemArchiveBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new AVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AVH holder, int position) {

        if (journeys == null){
            return;
        }

        ArichivedJourney j = journeys.get(position);

        holder.date.setText(j.getDate());
        holder.to.setText(j.getOrganization());
        holder.from.setText(j.getRegion());
        holder.end.setText(j.getEnd());
        holder.start.setText(j.getStart());
        holder.journeyId.setText(j.getJourneyId());

    }

    @Override
    public int getItemCount() {
        return journeys != null? journeys.size():0;
    }

    class AVH extends RecyclerView.ViewHolder {

        TextView start, end, to,from,journeyId,date;

        public AVH(@NonNull ItemArchiveBinding binding) {
            super(binding.getRoot());

            start = binding.tvStartTimeItemSchedule;
            end =binding.tvArrivalsTimeItemSchedule;
            to=binding.tvArrivalsPlace;
            from=binding.tvStartingPlace;
            journeyId=binding.tvItineraryNumber;
            date=binding.tvDate;
        }
    }
}
