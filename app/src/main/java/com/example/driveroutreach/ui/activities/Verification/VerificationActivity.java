package com.example.driveroutreach.ui.activities.Verification;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.driveroutreach.R;
import com.example.driveroutreach.databinding.ActivityVerificationBinding;
import com.example.driveroutreach.ui.activities.Main.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.concurrent.TimeUnit;

public class VerificationActivity extends AppCompatActivity {
    ActivityVerificationBinding binding;
    String verificationId;
    private PhoneAuthProvider.ForceResendingToken resendingToken;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


            verificationId = getIntent().getStringExtra("verificationId");
            resendingToken = getIntent().getParcelableExtra("resendingToken");



        binding.btnLogin.setOnClickListener(view -> {
            Log.e("BtnLogin","Click");
            if (binding.pinView.getText().toString().trim().isEmpty()) {
                Log.e("empty", "empty");
                return;
            }

            String code = binding.pinView.getText().toString();
            if (verificationId != null) {
                binding.progressBar.setVisibility(View.VISIBLE);
                PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, code);
                FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                        .addOnCompleteListener(task -> {
                            Log.e("Done","done");
                            binding.progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(VerificationActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(VerificationActivity.this, "Verification Code Invalid", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(e -> Toast.makeText(VerificationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });






















//        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String code = binding.pinView.getText().toString();
//
//                if (TextUtils.isEmpty(code)) {
//                    Toast.makeText(VerificationActivity.this, "Enter your code OTP", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                //  verifyCode(code);
//                if (verificationId != null) {
//                    binding.progressBar.setVisibility(View.VISIBLE);binding.btnLogin.setText(R.string.verifying);
//                    binding.btnLogin.setEnabled(false);
//                    binding.pinView.setEnabled(false);
//                    binding.tvResend.setEnabled(false);
//                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
//
//
//                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            setEnabledVisibility();
//                            if (task.isSuccessful()) {
//                                // Sign in success, update UI with the signed-in user's information
//                                Log.d("VVVTAG", "signInWithCredential:success");
//                                Intent intent = new Intent(VerificationActivity.this, MainActivity.class);
//                                //todo
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(intent);
//                                //finish();
//                            } else {
//                                // Sign in failed, display a message and update the UI
//                                Log.d("VVVTAG", "signInWithCredential:failed" + task.getException().getMessage());
//                                Toast.makeText(VerificationActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    });
//                }
//            }
//        });
//
//
//        binding.tvResend.setOnClickListener(view -> {
//
//            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//            FirebaseAuthSettings firebaseAuthSettings = firebaseAuth.getFirebaseAuthSettings();
//            firebaseAuthSettings.setAppVerificationDisabledForTesting(false);
//
//            PhoneAuthOptions.Builder builder = PhoneAuthOptions.newBuilder(firebaseAuth);
//            builder.setPhoneNumber("+970" +getIntent().getStringExtra("mobile"));
//            builder.setTimeout(60L, TimeUnit.SECONDS);
//            builder.setActivity(VerificationActivity.this);
//            builder.setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                @Override
//                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//                    setEnabledVisibility();
//                    Log.d("VVVTAG", "onVerificationCompleted   :    failed");
//                }
//
//                @Override
//                public void onVerificationFailed(@NonNull FirebaseException e) {
//                    setEnabledVisibility();
//                    Toast.makeText(VerificationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    Log.d("VVVTAG", "onVerificationFailed   :  " + e.getMessage());
//                }
//
//                @Override
//                public void onCodeSent(@NonNull String newVerificationId, PhoneAuthProvider.@NonNull ForceResendingToken forceResendingToken) {
//                    setEnabledVisibility();
//                    verificationId = newVerificationId;
//                    Toast.makeText(VerificationActivity.this, "OTP sent", Toast.LENGTH_SHORT).show();
//                    Log.d("VVVTAG", "onCodeSent   :  ");
//                }
//            });
//            PhoneAuthOptions options = builder
//                    .build();
//
//            firebaseAuth.setLanguageCode("en"); // or any other language code
//            PhoneAuthProvider.verifyPhoneNumber(options);
//
//
//
//            if (binding.tvResend.isClickable()) {
//                binding.tvResend.setTextColor(Color.parseColor("#417F7A")); // set text color to green
//            } else {
//                binding.tvResend.setTextColor(Color.parseColor("#8B8B8B")); // set text color to gray
//            }
//
//
//
////                PhoneAuthProvider.getInstance().verifyPhoneNumber("+970" + getIntent().getStringExtra("mobile"), 60, TimeUnit.SECONDS, VvvvvActivity.this,
////                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
////
////                            @Override
////                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
////                                setEnabledVisibility();
////                                Log.d("VVVTAG", "onVerificationCompleted   :    failed");
////                            }
////
////                            @Override
////                            public void onVerificationFailed(@NonNull FirebaseException e) {
////                                setEnabledVisibility();
////                                Toast.makeText(VvvvvActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
////                                Log.d("VVVTAG", "onVerificationFailed   :  " + e.getMessage());
////                            }
////
////                            @Override
////                            public void onCodeSent(@NonNull String newVerificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
////                                // super.onCodeSent(verificationId, forceResendingToken);
////                                setEnabledVisibility();
////                                verificationId = newVerificationId;
////                                Toast.makeText(VvvvvActivity.this, "OTP sent", Toast.LENGTH_SHORT).show();
////                                Log.d("VVVTAG", "onCodeSent   :  ");
////
////                            }
////                        });
//
//        });

    }

    //todo
//    private void setupOTPInputs() {
//        binding.etDigit1.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//                if (!charSequence.toString().trim().isEmpty()) {
//                    binding.etDigit2.requestFocus();
//                }
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//            }
//        });
//        binding.etDigit2.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//                if (!charSequence.toString().trim().isEmpty()) {
//                    binding.etDigit3.requestFocus();
//                }
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                if (editable.toString().trim().isEmpty()) {
//                    binding.etDigit1.requestFocus();
//                }
//            }
//        });
//        binding.etDigit3.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//                if (!charSequence.toString().trim().isEmpty()) {
//                    binding.etDigit4.requestFocus();
//                }
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                if (editable.toString().trim().isEmpty()) {
//                    binding.etDigit2.requestFocus();
//                }
//            }
//        });
//        binding.etDigit4.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//                if (!charSequence.toString().trim().isEmpty()) {
//                    binding.etDigit5.requestFocus();
//                }
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                if (editable.toString().trim().isEmpty()) {
//                    binding.etDigit3.requestFocus();
//                }
//            }
//        });
//        binding.etDigit5.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//                if (!charSequence.toString().trim().isEmpty()) {
//                    binding.etDigit6.requestFocus();
//                }
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                if (editable.toString().trim().isEmpty()) {
//                    binding.etDigit4.requestFocus();
//                }
//            }
//        });
//        binding.etDigit6.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                if (editable.toString().trim().isEmpty()) {
//                    binding.etDigit5.requestFocus();
//                }
//            }
//        });
//    }

    private void setEnabledVisibility(){
        binding.progressBar.setVisibility(View.GONE);
        binding.btnLogin.setText(R.string.verify);
        binding.btnLogin.setEnabled(true);
        binding.pinView.setEnabled(true);
        binding.tvResend.setEnabled(true);
    }


}