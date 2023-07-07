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

import com.example.driveroutreach.R;
import com.example.driveroutreach.databinding.ActivityLoginBinding;
import com.example.driveroutreach.model.DriversNumbers;
import com.example.driveroutreach.ui.activities.Main.MainActivity;
import com.example.driveroutreach.ui.activities.Verification.VerificationActivity;
import com.example.driveroutreach.ui.app_utility.AppUtility;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity implements LoginView {

    ActivityLoginBinding binding;
    FirebaseFirestore firestore;
    SharedPreferences sp;
    SharedPreferences.Editor edit;
    public static String mobile;
    LoginPresenter loginPresenter;
    public final String DRIVER_ID_KEY = "driverId";
    public final String DRIVER_NUMBER_KEY = "driverNumber";
    String phone;
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
        loginPresenter = new LoginPresenter(this);

        sp = getSharedPreferences("sp", MODE_PRIVATE);
        edit = sp.edit();

        firestore = FirebaseFirestore.getInstance();


        binding.btnLogin.setOnClickListener(view -> {
            mobile = binding.etMobile.getText().toString().trim();
            if (TextUtils.isEmpty(mobile)) {
                binding.etMobile.setError("Enter your phone number");
                setEnabledVisibility();
                //Toast.makeText(getApplicationContext(), "Enter your phone", Toast.LENGTH_SHORT).show();
                return;
            }

            binding.progressBar.setVisibility(View.VISIBLE);
            binding.etMobile.setEnabled(false);
            binding.btnLogin.setText(R.string.sending);
            binding.btnLogin.setEnabled(false);
            loginPresenter.checkDriverIsExist(mobile);
        });
    }

    private void sendCodeVerification() {
        phone = binding.etMobile.getText().toString().trim();
        Log.e("LoginActivityLOG", phone);


        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+970" + phone,
                60,
                TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        Log.e("LoginActivityLOG", "done");
                        setEnabledVisibility();
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        AppUtility.showSnackbar(binding.getRoot(),e.getMessage());
                        binding.etMobile.setText("");
                        Log.e("LoginActivityLOG", e.toString());
                        setEnabledVisibility();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        super.onCodeSent(verificationId, token);
                        Intent intent = new Intent(LoginActivity.this, VerificationActivity.class);
                        intent.putExtra("verificationId", verificationId);
                        intent.putExtra("resendingToken", token);
                        intent.putExtra("phone", phone);
                        intent.putExtra("fromWhere", true);
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

    private void setEnabledVisibility() {
        binding.etMobile.setText("");
        binding.progressBar.setVisibility(View.GONE);
        binding.btnLogin.setText(R.string.send);
        binding.etMobile.setEnabled(true);
        binding.btnLogin.setEnabled(true);
    }


    @Override
    public void onFail(Exception exception) {
        //  Toast.makeText(LoginActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
        Log.d("LoginActivityLOG", exception.getMessage());
    }

    @Override
    public void isDriver(DriversNumbers num) {
        if (binding.etMobile.getText().toString().equals(String.valueOf(num.getMobile()))) {
            Log.d("LoginActivityLOG", String.valueOf(num.getMobile()));
            num.getId();
            edit.putString(DRIVER_ID_KEY, num.getId());
            edit.putString(DRIVER_NUMBER_KEY, String.valueOf(num.getMobile()));
            edit.commit();
            sendCodeVerification();
        }
    }

    @Override
    public void numberNotFound() {
        Log.d("LoginActivityLOG", "Does not exist");
        setEnabledVisibility();
        AppUtility.showSnackbar(binding.getRoot(),"You're not allowed to login.");
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
