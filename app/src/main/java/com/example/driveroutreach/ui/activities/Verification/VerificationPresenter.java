package com.example.driveroutreach.ui.activities.Verification;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.driveroutreach.ui.base_classes.BasePresenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.HashMap;
import java.util.Map;

public class VerificationPresenter extends BasePresenter {
    VerificationView view;

    public VerificationPresenter(VerificationView view) {
        this.view = view;
    }


    public void updatePhoneNumber(String verificationId, String verificationCode,String driverId,int newNumber) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, verificationCode);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            currentUser.updatePhoneNumber(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Phone number update successful

                                changeNumber(driverId,newNumber);
                                // Toast.makeText(getApplicationContext(), "Phone number updated successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                // Phone number update failed
                                Log.d("PhoneNum updated failed", task.getException().getMessage());
                                // Toast.makeText(getApplicationContext(), "Failed to update phone number: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    void changeNumber(String drivertId,int newNumber){
        Map<String , Object> setNewNumber = new HashMap<>();
        setNewNumber.put("mobile", newNumber );

        firestore.collection("Drivers_numbers").document(drivertId).update(setNewNumber)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            changeNumberInProfile(drivertId,newNumber);
                            Log.d("changeNumber", "Successful");
                        } else {
                            Log.d("changeNumber", task.getException().getMessage());         }
                    }
                });
    }


    void changeNumberInProfile(String drivertId,int newNumber){
        Map<String , Object> setNewNumber = new HashMap<>();
        setNewNumber.put("mobile", newNumber );

        firestore.collection("Driver").document(drivertId).update(setNewNumber)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Log.d("changeNumberProfile", "Successful");
                        } else {
                            Log.d("changeNumberProfile", task.getException().getMessage());         }
                    }
                });
    }
}
