package com.example.driveroutreach.ui.fragments.archive;

import androidx.annotation.NonNull;

import com.example.driveroutreach.model.ArichivedJourney;
import com.example.driveroutreach.ui.base_classes.BasePresenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ArchivePresenter extends BasePresenter {

    ArichiveView view;

    public ArchivePresenter(ArichiveView view) {
        this.view = view;
    }

    void gettingArchivedJourneys(String driverId){

        firestore.collection("Journey_Archive").document(driverId)
                .collection("Journeys").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()){

                            ArrayList<ArichivedJourney> journeys = (ArrayList<ArichivedJourney>) task.getResult().toObjects(ArichivedJourney.class);
                            view.onGettingArchivedJourneysSuccess(journeys);
                        }else {
                            view.onGettingArchivedJourneysFailure(task.getException());
                        }
                    }
                });

    }
}
