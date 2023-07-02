package com.example.driveroutreach.ui.activities.Login;

import com.example.driveroutreach.model.DriversNumbers;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class LoginPresenter {

    private LoginView view;
    private FirebaseFirestore firestore;

    public LoginPresenter(LoginView view) {
        this.view = view;
        firestore = FirebaseFirestore.getInstance();
    }

    public void checkDriverIsExist(String mobile) {
        firestore.collection("Drivers_numbers").get().addOnCompleteListener(task -> {
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
