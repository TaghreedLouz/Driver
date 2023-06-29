package com.example.driveroutreach.ui.activities.contact_us;

import androidx.annotation.NonNull;

import com.example.driveroutreach.model.ContactUs;
import com.example.driveroutreach.ui.app_utility.AppUtility;
import com.example.driveroutreach.ui.base_classes.BasePresenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class ContactUsPresenter extends BasePresenter {

   ContactUsView view;

   private int numOfMess;

    public ContactUsPresenter( ContactUsView view) {
        super();

        this.view=view;
    }


    public void storeMessage(String driverId, ContactUs contactUs){

         numOfMess = 1;

        firestore.collection("DriverMessages").document(driverId).
                collection(AppUtility.getDateTime()).document("Message")
                .set(contactUs)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                   if (task.isSuccessful()){
                       numOfMess+=1;
                       view.onStoringMessageSuccess();
                   }else {
                       view.onStoringMessageFailure(task.getException());
                   }
                    }
                });

    }
}
