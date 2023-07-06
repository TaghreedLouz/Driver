package com.example.driveroutreach.ui.activities.contact_us;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.driveroutreach.model.ContactUs;
import com.example.driveroutreach.ui.base_classes.BasePresenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;


public class ContactUsPresenter extends BasePresenter {

   ContactUsView view;

   private int numOfMess;

    public ContactUsPresenter( ContactUsView view) {
        super();

        this.view=view;
    }


    public void sendingMessage(ContactUs contactUs){



        DatabaseReference reference = db.getReference("DriverMessages");
        reference.child("Messages").push().setValue(contactUs)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            view.onSendingMessageSuccess();
                            Log.d("contactUs","succesfull");
                        } else {
                            view.onSendingMessageFailure(task.getException());
                            Log.d("contactUs",task.getException().getMessage());
                        }
                    }
                });;
    }
}
