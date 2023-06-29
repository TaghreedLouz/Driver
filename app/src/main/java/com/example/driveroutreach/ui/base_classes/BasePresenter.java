package com.example.driveroutreach.ui.base_classes;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class BasePresenter {


    public FirebaseFirestore firestore ;
   public FirebaseStorage firebaseStorage;
   public FirebaseAuth firebaseAuth;



    public BasePresenter() {
        this.firestore = FirebaseFirestore.getInstance();
        this.firebaseStorage = FirebaseStorage.getInstance();
        this.firebaseAuth = FirebaseAuth.getInstance();

    }

}
