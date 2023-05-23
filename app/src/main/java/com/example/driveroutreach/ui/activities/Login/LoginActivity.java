package com.example.driveroutreach.ui.activities.Login;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.driveroutreach.R;
import com.example.driveroutreach.databinding.ActivityLoginBinding;
import com.example.driveroutreach.model.DriverProfile;
import com.example.driveroutreach.model.DriversNumbers;
import com.example.driveroutreach.ui.activities.Main.MainActivity;
import com.example.driveroutreach.ui.activities.Verification.VerificationActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    FirebaseFirestore firestore;
    DriversNumbers driversNumbers;
    SharedPreferences sp;
    SharedPreferences.Editor edit;
    public final String DRIVER_ID_KEY = "driverId";

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(getBaseContext(), MainActivity.class));
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sp = getSharedPreferences("sp", MODE_PRIVATE);
        edit = sp.edit();

        firestore = FirebaseFirestore.getInstance();


        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mobile = binding.etMobile.getText().toString().trim();
                Log.e("LoginActivityLOG", mobile);
                if (TextUtils.isEmpty(mobile)) {
                    binding.etMobile.setError("Enter your phone number");
                    //     setEnabledVisibility();
                    //  Toast.makeText(getApplicationContext(), "Enter your phone", Toast.LENGTH_SHORT).show();
                    return;
                }

                    firestore.collection("Drivers_numbers")
                            .whereArrayContainsAny("mobile", Collections.singletonList(mobile))
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        ArrayList<DriversNumbers> numbersArrayList = new ArrayList<>();

                                        Toast.makeText(LoginActivity.this, "if  ", Toast.LENGTH_SHORT).show();
                                        sendCodeVerification();
                                        binding.progressBar.setVisibility(View.VISIBLE);
                                        binding.btnLogin.setText(R.string.sending);
                                        binding.etMobile.setEnabled(false);
                                        binding.btnLogin.setEnabled(false);

                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                        DriversNumbers numbers = documentSnapshot.toObject(DriversNumbers.class);
                                        numbers.getId();
                                        String id = numbers.getId();
                                        int mobileFromFirestorm = numbers.getMobile();
                                        String mobileFromFirestormString = String.valueOf(mobileFromFirestorm);
                                        Log.e("TAGonComplete", "onComplete: "+mobileFromFirestormString);
                                        String mobile = binding.etMobile.getText().toString().trim();
                                        numbersArrayList.add(numbers);

                                        edit.putString(DRIVER_ID_KEY, id);
                                        edit.putString("m", mobileFromFirestormString);
                                        Toast.makeText(LoginActivity.this, "for  "+mobileFromFirestormString, Toast.LENGTH_SHORT).show();

                                        Log.e("TAGonComplete", "onComplete: "+id);
                                        edit.apply();

                                    }
                                    }else {
                                        Toast.makeText(LoginActivity.this, "else"+task.getException().toString(), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                  //  setEnabledVisibility();

//                    String m = sp.getString("m", "null");
//                    String mobilee = binding.etMobile.getText().toString().trim();
             //       Toast.makeText(LoginActivity.this, "bbb  "+m, Toast.LENGTH_SHORT).show();
   //                 setEnabledVisibility();
//                    if (mobilee.equals(m)){
//                        sendCodeVerification();
//                    }else {
//                        Toast.makeText(LoginActivity.this, "Your not allow to login", Toast.LENGTH_SHORT).show();
//                    }


                }
        });

    }

    private void sendCodeVerification() {
        String phone = binding.etMobile.getText().toString().trim();
        Log.e("LoginActivityLOG", phone);


//        if (TextUtils.isEmpty(phone)) {
//            binding.etMobile.setError("Enter your phone number");
//            binding.btnLogin.setEnabled(true);
//            Toast.makeText(this, "Enter your phone", Toast.LENGTH_SHORT).show();
//            return;
//        } else {
//            binding.progressBar.setVisibility(View.VISIBLE);
//            binding.btnLogin.setText(R.string.sending);
//            binding.etMobile.setEnabled(false);
//            binding.btnLogin.setEnabled(false);
//        }
//            if (phone.startsWith("0")) {
//                phone = phone.substring(1);
//            }
//            binding.progressBar.setVisibility(View.VISIBLE);

            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    "+970" + phone,
                    60,
                    TimeUnit.SECONDS,
                    this,
                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                            Log.e("LoginActivityLOG", "done");
                        }

                        @Override
                        public void onVerificationFailed(@NonNull FirebaseException e) {
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("LoginActivityLOG", e.toString());
                            setEnabledVisibility();
                        }

                        @Override
                        public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                            super.onCodeSent(verificationId, token);
                            binding.progressBar.setVisibility(View.GONE);
                            Intent intent = new Intent(LoginActivity.this, VerificationActivity.class);
                            intent.putExtra("verificationId", verificationId);
                            intent.putExtra("resendingToken", token);
                            setEnabledVisibility();
                            startActivity(intent);
                            finish();
                        }
                    }
            );
        }


        @Override
        public void onBackPressed() {
            super.onBackPressed();
            setEnabledVisibility();
        }
        private void setEnabledVisibility(){
            binding.progressBar.setVisibility(View.GONE);
            binding.btnLogin.setText(R.string.send);
            binding.etMobile.setEnabled(true);
            binding.btnLogin.setEnabled(true);
        }
}
