package com.example.driveroutreach.adapters;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.driveroutreach.R;
import com.example.driveroutreach.databinding.ItemArchiveBinding;
import com.example.driveroutreach.model.ArichivedJourney;

import java.util.ArrayList;

public class ArchiveAdapter extends RecyclerView.Adapter<ArchiveAdapter.AVH> {

ArrayList<ArichivedJourney> journeys;
Context context;

    public ArchiveAdapter(ArrayList<ArichivedJourney> journeys,Context context) {
        this.journeys = journeys;
        this.context=context;
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

        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                j.setIschildAdapterSectionVisible(true);
                notifyDataSetChanged();

            }
        });

        if (j.isIschildAdapterSectionVisible()) {
            holder.childAdapterSection.setVisibility(View.VISIBLE);
            holder.more.setVisibility(View.GONE);
            holder.itenary.setVisibility(View.GONE);
            holder.itenaryNum.setVisibility(View.GONE);
            holder.busIcon.setVisibility(View.GONE);
            holder.moreIcon.setVisibility(View.GONE);

            holder.attendingClients.setAdapter(new ArchiveAttendingClientsChildAdapter(j.getAttending(), context));
            holder.attendingClients.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        } else {
            holder.childAdapterSection.setVisibility(View.GONE);
            holder.more.setVisibility(View.VISIBLE);
            holder.itenary.setVisibility(View.VISIBLE);
            holder.itenaryNum.setVisibility(View.VISIBLE);
            holder.busIcon.setVisibility(View.VISIBLE);
            holder.total.setText(context.getText(R.string.arch_adapter_total) + " "+ String.valueOf(j.getAttending().size()) );


        }

    }

    @Override
    public int getItemCount() {
        return journeys != null? journeys.size():0;
    }

    class AVH extends RecyclerView.ViewHolder {

        TextView start, end, to,from,journeyId,date,more,itenary, itenaryNum,total;

        ImageView busIcon,moreIcon;
        RecyclerView attendingClients;
        LinearLayout childAdapterSection;

        public AVH(@NonNull ItemArchiveBinding binding) {
            super(binding.getRoot());

            start = binding.tvStartTimeItemSchedule;
            end =binding.tvArrivalsTimeItemSchedule;
            to=binding.tvArrivalsPlace;
            from=binding.tvStartingPlace;
            journeyId=binding.tvItineraryNumber;
            date=binding.tvDate;
            more = binding.tvMore;
            attendingClients = binding.rvMoreDetails;
            childAdapterSection = binding.linearLayoutMore;
            itenary = binding.tvItinerary;
            itenaryNum=binding.tvItineraryNumber;
            busIcon=binding.iconBus;
            moreIcon =binding.iconMore;
            total=binding.tvTotal;
        }
    }
}
