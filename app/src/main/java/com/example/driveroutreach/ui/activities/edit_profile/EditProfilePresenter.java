package com.example.driveroutreach.ui.activities.edit_profile;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.driveroutreach.ui.base_classes.BasePresenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.concurrent.TimeUnit;

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

    public void updateNumber(String newNumber, Activity activity) {
        PhoneAuthCredential credentialCompleted ;
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+970" + newNumber,
                60, TimeUnit.SECONDS,
                activity,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        Log.e("LoginActivityLOG", "done");
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        view.onVerificationFailed(e);
                        Log.d("verification",e.getMessage());
                        //    setEnabledVisibility();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        super.onCodeSent(verificationId, token);

                        view.onSendCode( verificationId, newNumber);

                    }
                }
        );
   }


}
