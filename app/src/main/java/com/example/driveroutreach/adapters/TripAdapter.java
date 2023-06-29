package com.example.driveroutreach.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.driveroutreach.databinding.ItemTripBinding;
import com.example.driveroutreach.listeners.ScheduleListener;
import com.example.driveroutreach.model.JourneyModel;
import com.example.driveroutreach.ui.app_utility.AppUtility;

import java.util.ArrayList;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.MyViewHolderTrip> {

ArrayList<JourneyModel> journys;
ScheduleListener scheduleListener;
String date;
int itemPosition;
SharedPreferences sp;
SharedPreferences.Editor editor;





    public TripAdapter(ArrayList<JourneyModel> journys, Context context, ScheduleListener scheduleListener) {
        this.journys = journys;
        this.scheduleListener = scheduleListener;
//        sp = context.getSharedPreferences("sp", Context.MODE_PRIVATE);
//        editor =sp.edit();
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

//        SimpleDateFormat simpleformat = new SimpleDateFormat("dd-MMMM-yyyy");
//        date=simpleformat.format(Calendar.getInstance().getTime());

        holder.date.setText(AppUtility.getDate());



        holder.startJourney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                scheduleListener.StartJourney(journeyModel.getJourneyId(),date);
                itemPosition = holder.getAdapterPosition();
                holder.startJourney.setVisibility(View.GONE);
                holder.endJourney.setVisibility(View.VISIBLE);


            }
        });



        holder.endJourney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return journys.size();
    }

    class MyViewHolderTrip extends RecyclerView.ViewHolder {

        TextView TimeStart, TimeEnds, ArrivalPlace, PickingupPlace, itenaryId, date;
        Button startJourney, endJourney;
        public MyViewHolderTrip(@NonNull ItemTripBinding binding) {
            super(binding.getRoot());

            TimeStart = binding.tvStartTimeItemSchedule;
            TimeEnds = binding.tvArrivalsTimeItemSchedule;
            ArrivalPlace = binding.tvArrivalsPlace;
            PickingupPlace = binding.tvStartingPlace;
            itenaryId = binding.tvItineraryNumber;
            startJourney = binding.btnStartJourney;
            endJourney = binding.btnEndJourney;
            date = binding.tvDate;

        }
    }
}
