package com.example.driveroutreach.ui.activities.Login;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.driveroutreach.model.DriverProfile;
import com.example.driveroutreach.model.DriversNumbers;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginPresenter {

    private LoginView view;
    private FirebaseFirestore firestore;

    public LoginPresenter(LoginView view) {
        this.view = view;
        firestore = FirebaseFirestore.getInstance();
    }

    public void checkDriverIsExist(String mobile) {
        firestore.collection("Benf_Numbers").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean numberFound = false;
                for (QueryDocumentSnapshot document : task.getResult()) {
                    DriversNumbers num = document.toObject(DriversNumbers.class);
                    if (mobile.equals(String.valueOf(num.getMobile()))) {
                        numberFound = true;
                        view.isDriver(num);
                        break;
                    }
                }
                if (!numberFound) {
                    view.numberNotFound();
                }
            } else {
                view.onFail(task.getException());
            }
        });
    }

}
