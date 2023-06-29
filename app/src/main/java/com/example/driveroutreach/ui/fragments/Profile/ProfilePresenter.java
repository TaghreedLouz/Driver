package com.example.driveroutreach.ui.fragments.Profile;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.driveroutreach.model.DriverProfile;
import com.example.driveroutreach.ui.base_classes.BasePresenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

public class ProfilePresenter extends BasePresenter {

    // here we define methods that contains the logic and coding + the methods that connect the view with the presenter

    ProfileView view;

    public ProfilePresenter(ProfileView view) {
        super();
        this.view = view;
    }

    public void logout(){
        FirebaseAuth.getInstance().signOut();
    }



    public void driverInfo(String driverId){
        firestore.collection("Driver").document(driverId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Log.d("Driver",task.getResult().toString());
                        if (task.isSuccessful()){


                        DriverProfile driverProfileObject = task.getResult().toObject(DriverProfile.class);
                            Log.d("Driver",task.getResult().toString());
                            view.onDriverInfoSuccess(driverProfileObject);
//                            binding.tvName.setText(driverProfileObject.getName());
//                            binding.tvNumber.setText("+972".concat(String.valueOf(driverProfileObject.getMobile())));

                        } else {

                            view.onDriverInfoFailure(task.getException());
                            Log.d("Driver's info", task.getException().getMessage());

                        }

                    }
                });
    }


    public void gettingProfileImage(String driverId){
        firestore.collection("Driver").document(driverId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){

                            String imgUrl  = (String) task.getResult().get("ImgUrl");
                            view.onGettingImgeSuccess(imgUrl);
                        }else {
                            view.onGettingImgFailure(task.getException());
                        }

                    }
                });
    }





}
