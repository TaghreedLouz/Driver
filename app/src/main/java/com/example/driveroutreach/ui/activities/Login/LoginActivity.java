package com.example.driveroutreach.ui.activities.Login;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.driveroutreach.R;
import com.example.driveroutreach.databinding.ActivityLoginBinding;
import com.example.driveroutreach.ui.activities.Main.MainActivity;
import com.example.driveroutreach.ui.activities.Verification.VerificationActivity;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

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



        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(binding.etMobile.getText().toString())) {
                    binding.etMobile.setError("Enter your mobile number");
                    binding.btnLogin.setEnabled(true);
                    return;
                } else {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.btnLogin.setText(R.string.sending);
                    binding.etMobile.setEnabled(false);
                    binding.btnLogin.setEnabled(false);

                    //num
                    // String mobileNumber = binding.etMobile.getText().toString().trim();




//                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//                    FirebaseAuthSettings firebaseAuthSettings = firebaseAuth.getFirebaseAuthSettings();
//
//                    String mobileNumber = binding.etMobile.getText().toString().trim();
//
//                    PhoneAuthOptions options =
//                            PhoneAuthOptions.newBuilder(firebaseAuth)
//                                    .setPhoneNumber("+970" +mobileNumber)       // Phone number to verify
//                                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
//                                    .setActivity(LllllActivity.this);               // Activity (for callback binding)
//                                    .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                                        @Override
//                                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//                                            Log.d("LLLTAG", "onVerificationCompleted   :    failed");
//                                        }
//
//                                        @Override
//                                        public void onVerificationFailed(@NonNull FirebaseException e) {
//                                            Toast.makeText(LllllActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                                            Log.d("LLLTAG", "onVerificationFailed   :  " + e.getMessage());
//                                        }
//
//                                        @Override
//                                        public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                                            // super.onCodeSent(verificationId, forceResendingToken);
//                                            Log.d("LLLTAG", "onCodeSent   :  ");
//
//                                            // intent
//                                            Intent intent = new Intent(getBaseContext(), VvvvvActivity.class);
//                                            intent.putExtra("mobile", mobileNumber);
//                                            intent.putExtra("verificationId", verificationId);
//                                            startActivity(intent);
//                                        }
//                                    })          // OnVerificationStateChangedCallbacks
//                                    .build();
//
//                    if (firebaseAuthSettings.isAppVerificationDisabledForTesting()) {
//                        PhoneAuthProvider.verifyPhoneNumber(options);
//                    } else {
//                        firebaseAuth.setLanguageCode("en"); // or any other language code
//                        PhoneAuthProvider.verifyPhoneNumber(options);
//                    }




                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    FirebaseAuthSettings firebaseAuthSettings = firebaseAuth.getFirebaseAuthSettings();
                    firebaseAuthSettings.setAppVerificationDisabledForTesting(false);

                    String mobileNumber = binding.etMobile.getText().toString().trim();
                    PhoneAuthOptions.Builder builder = PhoneAuthOptions.newBuilder(firebaseAuth);
                    builder.setPhoneNumber("+970" + mobileNumber);
                    builder.setTimeout(60L, TimeUnit.SECONDS);
                    builder.setActivity(LoginActivity.this);
                    builder.setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                            Log.d("LLLTAG", "onVerificationCompleted   :    failed");
                        }

                        @Override
                        public void onVerificationFailed(@NonNull FirebaseException e) {
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("LLLTAG", "onVerificationFailed   :  " + e.getMessage());
                            setEnabledVisibility();
                        }

                        @Override
                        public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            super.onCodeSent(verificationId, forceResendingToken);
                            Log.d("LLLTAG", "onCode8Sent   :  "+verificationId);
                            setEnabledVisibility();
                            // intent
                            Intent intent = new Intent(getBaseContext(), VerificationActivity.class);
                            intent.putExtra("mobile", mobileNumber);
                            intent.putExtra("verificationId", verificationId);
                            startActivity(intent);
                        }
                    });
                    PhoneAuthOptions options = builder
                            .build();

                    firebaseAuth.setLanguageCode("en"); // or any other language code
                    PhoneAuthProvider.verifyPhoneNumber(options);





//                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                            "+970" + mobileNumber,
//                            60,
//                            TimeUnit.SECONDS,
//                            LllllActivity.this,
//                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                                @Override
//                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//                                    Log.d("LLLTAG", "onVerificationCompleted   :    failed");
//                                }
//
//                                @Override
//                                public void onVerificationFailed(@NonNull FirebaseException e) {
//                                    Toast.makeText(LllllActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                                    Log.d("LLLTAG", "onVerificationFailed   :  " + e.getMessage());
//                                    setEnabledVisibility();
//                                }
//
//                                @Override
//                                public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                                    super.onCodeSent(verificationId, forceResendingToken);
//                                    mVerificationId = verificationId;
//                                    mResendToken = forceResendingToken;
//                                    Log.d("LLLTAG", "onCodeSent   :  ");
//
//                                    // intent
//                                    Intent intent = new Intent(getBaseContext(), VvvvvActivity.class);
//                                    intent.putExtra("mobile", mobileNumber);
//                                    intent.putExtra("verificationId", verificationId);
//                                    startActivity(intent);
//                                    setEnabledVisibility();
//                                }
//                            }
//                    );
                }
            }
        });
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
