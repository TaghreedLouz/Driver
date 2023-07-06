package com.example.driveroutreach.ui.fragments.schedule.days;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.driveroutreach.model.ArichivedJourney;
import com.example.driveroutreach.model.JourneyModel;
import com.example.driveroutreach.ui.base_classes.BasePresenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DayPresenter extends BasePresenter {

    DayView view;
    ArrayList<JourneyModel> Trips;
    public DayPresenter(DayView view) {
        super();
        this.view=view;
        Trips = new ArrayList<>();
    }

    void gettingSchedule(String day, String driverId, Context context, String title){
        //Setting the progress
        ProgressDialog progressDialog= new ProgressDialog(context);
        progressDialog.setTitle(title);
        progressDialog.show();

        firestore.collection("Journey").whereEqualTo("driver",driverId)
                .whereEqualTo("day",day).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()){
                            Log.d("schedule",task.getResult().toString());

                            for(QueryDocumentSnapshot document : task.getResult()){

                                JourneyModel journeyModel = document.toObject(JourneyModel.class);
                                Log.d("schedule",journeyModel.getOrganization()+" "+journeyModel.getDay());

                              Trips.add(journeyModel);

                            }

                              view.onGettingScheduleSuccess(Trips);


                        }   else {
                            view.onGettingScheduleFailure(task.getException());


                        }
                    }
                });
    }




    void storeArchivedJourney2(ArichivedJourney journey, String driverId){

        firestore.collection("Journey_Archive").document()
                .set(journey).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            view.storeArchivedJourneySuccess();
                        }else {
                            view.storeArchivedJourneyFailure(task.getException());
                        }
                    }
                });
    }
}
