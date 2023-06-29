package com.example.driveroutreach.ui.activities.edit_profile;

import androidx.annotation.NonNull;

import com.example.driveroutreach.ui.base_classes.BasePresenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class EditProfilePresenter extends BasePresenter {
    EditProfileView view;

    public EditProfilePresenter(EditProfileView view) {
        this.view = view;
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
