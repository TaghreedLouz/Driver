package com.example.driveroutreach.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.driveroutreach.databinding.ItemTripBinding;
import com.example.driveroutreach.listeners.ScheduleListener;
import com.example.driveroutreach.model.ArichivedJourney;
import com.example.driveroutreach.model.JourneyModel;
import com.example.driveroutreach.ui.app_utility.AppUtility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.MyViewHolderTrip> {

ArrayList<JourneyModel> journys;
ScheduleListener scheduleListener;
String date;
int itemPosition;
SharedPreferences sp;
SharedPreferences.Editor edit;
HashMap<String, Integer> FinishedJourneys = new HashMap<String, Integer>();
int startedPosition;
String journeyID2;




    public TripAdapter(ArrayList<JourneyModel> journys, Context context, ScheduleListener scheduleListener) {
        this.journys = journys;
        this.scheduleListener = scheduleListener;
        sp = context.getSharedPreferences("sp", Context.MODE_PRIVATE);
        edit =sp.edit();


        Gson gson2 = new Gson();
        String storedMap=sp.getString("FinishedJourneys",null);

        Type type = new TypeToken<HashMap<String, Integer>>(){}.getType();
        if (storedMap != null)
            FinishedJourneys = gson2.fromJson(storedMap, type);
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


        //here we check the values of sp to make save the state of the adapter

      int  startedPosition = sp.getInt("startedPosition",-1);
      String  journeyID2 = sp.getString("journeyId",null);
        if (sp.getInt("startedPosition",-1) > -1 && sp.getString("journeyId",null) !=null){
            if (startedPosition == holder.getAdapterPosition() &&
                    sp.getString("journeyId",null).equals(journeyModel.getJourneyId()) ){

                holder.startJourney.setEnabled(false);
                holder.startJourney.setVisibility(View.GONE);
                holder.endJourney.setVisibility(View.VISIBLE);
                Log.d("jm",String.valueOf(journeyModel.isEnabled()));
                holder.startJourney.setEnabled(journeyModel.isEnabled());

            }
        }


        if (FinishedJourneys != null){
            for(String journeyID : FinishedJourneys.keySet() ){
                if (journeyID.equals(journeyModel.getJourneyId())
                        && FinishedJourneys.get(journeyID) == holder.getAdapterPosition()){

                    holder.startJourney.setEnabled(journeyModel.isEnabled());
                }
            }

            Log.d("hash","from sp"+FinishedJourneys.toString());
        }




        holder.TimeEnds.setText(journeyModel.getEnd());
        holder.TimeStart.setText(journeyModel.getStart());
        holder.itenaryId.setText("Itinerary"+journeyModel.getJourneyId());
        holder.ArrivalPlace.setText(journeyModel.getOrganization());
        holder.PickingupPlace.setText(journeyModel.getRegion());



//to be able to start only todays journeys
//        if (AppUtility.getToday().equals(journeyModel.getDay())) {
//            holder.date.setText(AppUtility.getDate());
//        } else {
//            holder.date.setVisibility(View.GONE);
//            holder.startJourney.setVisibility(View.GONE);
//            holder.calender.setVisibility(View.GONE);
//        }


        holder.date.setText(AppUtility.getDate());



        holder.startJourney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                itemPosition = holder.getAdapterPosition();
                holder.startJourney.setVisibility(View.GONE);
                holder.endJourney.setVisibility(View.VISIBLE);

                edit.putString("journeyDate",AppUtility.getDate());
                edit.putString("journeyId",journeyModel.getJourneyId());
                edit.putBoolean("started",true);
                edit.putInt("startedPosition",itemPosition);

                edit.putString("journeyStartDate",AppUtility.getTime());
                edit.commit();

                 journeyModel.setEnabled(false);
                 notifyDataSetChanged();

                scheduleListener.StartJourney(journeyModel.getJourneyId(),date);
            }
        });



        holder.endJourney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.putString("journeyDate",null);
                edit.putString("journeyId",null);
                edit.putBoolean("started",false);
                edit.putInt("startedPosition",-1);




                FinishedJourneys.put(journeyModel.getJourneyId(),holder.getAdapterPosition());
                edit.putString("createdSps",AppUtility.getDate());
                edit.putString("FinishedJourneys",   new Gson().toJson(FinishedJourneys));
                edit.commit();

                Log.d("hash",FinishedJourneys.toString());
                holder.endJourney.setEnabled(false);
                holder.endJourney.setBackgroundColor(Color.GRAY);
                journeyModel.setEnabled(true);
                notifyDataSetChanged();


                String serializedList = sp.getString("attending", null);
                ArrayList<String> attendingArrayList;
                if (serializedList != null) {
                    Type type = new TypeToken<ArrayList<String>>() {}.getType();
                    attendingArrayList = new Gson().fromJson(serializedList, type);
                    Log.d("show",attendingArrayList.toString());
                } else {
                    attendingArrayList = new ArrayList<>();
                }

                scheduleListener.EndJourney(new ArichivedJourney(
                        journeyModel.getDriver(),
                        journeyModel.getRegion(),
                        sp.getString("journeyStartDate",null),
                        AppUtility.getTime(),
                        journeyModel.getOrganization(),
                        AppUtility.getDate(),
                        journeyModel.getJourneyId(),
                        attendingArrayList));

            }
        });

    }

    @Override
    public int getItemCount() {
        return journys != null? journys.size() :0 ;
    }

    class MyViewHolderTrip extends RecyclerView.ViewHolder {

        TextView TimeStart, TimeEnds, ArrivalPlace, PickingupPlace, itenaryId, date;
        Button startJourney, endJourney;
        ImageView calender;
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
            calender =binding.iconCalender;


        }
    }
}
