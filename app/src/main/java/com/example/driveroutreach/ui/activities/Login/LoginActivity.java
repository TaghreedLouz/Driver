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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

binding.btnLogin.setOnClickListener(view -> {
    sendCodeVerification();
});}

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
    private void sendCodeVerification() {
        String phone = binding.etMobile.getText().toString().trim();
        Log.e("Phone", phone);
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Enter your phone", Toast.LENGTH_SHORT).show();
            return;
        }

        if (phone.startsWith("0")) {
            phone = phone.substring(1);
        }
        binding.progressBar.setVisibility(View.VISIBLE);

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+970" + phone,
                60,
                TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        Log.e("FirebaseException", "Verification completed");
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Log.e("FirebaseException", e.toString());
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        super.onCodeSent(verificationId, token);
                        binding.progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(LoginActivity.this, VerificationActivity.class);
                        intent.putExtra("verificationId", verificationId);
                        intent.putExtra("resendingToken", token);
                        startActivity(intent);
                        finish();
                    }
                }
        );
    }
}
