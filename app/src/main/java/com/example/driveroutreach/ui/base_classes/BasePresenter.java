package com.example.driveroutreach.ui.base_classes;

import com.google.firebase.firestore.FirebaseFirestore;

public class BasePresenter {


    FirebaseFirestore firestore ;

    public BasePresenter() {
        this.firestore = FirebaseFirestore.getInstance();
    }
}
