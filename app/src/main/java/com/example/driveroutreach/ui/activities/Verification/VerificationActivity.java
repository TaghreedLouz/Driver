package com.example.driveroutreach.ui.activities.Verification;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.driveroutreach.R;
import com.example.driveroutreach.databinding.ActivityVerificationBinding;
import com.example.driveroutreach.ui.activities.Login.LoginActivity;
import com.example.driveroutreach.ui.activities.Main.MainActivity;
import com.example.driveroutreach.ui.app_utility.AppUtility;
import com.example.driveroutreach.ui.base_classes.BaseActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerificationActivity extends BaseActivity implements VerificationView {
    ActivityVerificationBinding binding;
    String verificationId , verificationIdEdite;
    private PhoneAuthProvider.ForceResendingToken token , tokenEdite;

    String code;
    String newNumber;
    String verificationIdEdit;
    PhoneAuthCredential phoneAuthCredentialEdite , phoneAuthCredential;
    private String storedVerificationId;

  //  public final String DRIVER_ID_KEY = "driverId";




    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        VerificationPresenter presenter = new VerificationPresenter(this);

        verificationId = getIntent().getStringExtra("verificationId");
        token = getIntent().getParcelableExtra("resendingToken");


        newNumber = getIntent().getStringExtra("number");
        verificationIdEdit = getIntent().getStringExtra("verificationIdEdit");
        boolean fromlogin = getIntent().getBooleanExtra("fromWhere",false);




        Log.d("activity",String.valueOf(getIntent().getBooleanExtra("fromWhere",false)));


           if (fromlogin){
               binding.tvHintCode.setText(getString(R.string.secret_code) + " +970 " + getIntent().getStringExtra("phone"));

               Log.d("froWhere","inside: "+fromlogin);
               binding.btnLogin.setOnClickListener(view -> {
                   setEnabledVisibility();
                   Log.e("VerificationActivityLOG","Click");
                   if (TextUtils.isEmpty(binding.pinView.getText().toString().trim())) {
                       binding.pinView.setError("Enter verification code");
                       binding.pinView.setLineColor(getResources().getColor(R.color.baby_red));
                       Toast.makeText(getApplicationContext(), "Enter verification code", Toast.LENGTH_SHORT).show();
                       Log.e("VerificationActivityLOG", "empty");
                       return;
                   } else {
                       binding.progressBar.setVisibility(View.VISIBLE);
                       binding.btnLogin.setText(R.string.sending);
                       binding.pinView.setEnabled(false);
                       binding.btnLogin.setEnabled(false);

                       code = binding.pinView.getText().toString();

                       Log.d("code",binding.pinView.toString().trim());

                       if (verificationId != null) {
                           binding.progressBar.setVisibility(View.VISIBLE);

                           phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, code);

                           Log.d("fromWhere","login");
                           FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                                   .addOnCompleteListener(task -> {
                                       setEnabledVisibility();
                                       if (task.isSuccessful()) {
                                           startActivity(new Intent(getBaseContext(), MainActivity.class));
                                           finish();
                                       } else {
                                           binding.pinView.setLineColor(getResources().getColor(R.color.baby_red));
                                           AppUtility.showSnackbar(binding.getRoot(),task.getException().getMessage());
                                           Log.e("VerificationActivityLOG","   =====>  "+task.getException().getMessage().toString());
                                       }
                                   });



                       }
                   }


               });
           }else {
               Log.d("froWhere","inside: "+fromlogin);
               binding.tvHintCode.setText(getString(R.string.secret_code) + " +970 " + getIntent().getStringExtra("number"));
               binding.btnLogin.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {

                       setEnabledVisibility();
                       Log.e("VerificationActivityLOG","Click");
                       if (TextUtils.isEmpty(binding.pinView.getText().toString())) {
                           binding.pinView.setError("Enter verification code");
                           binding.pinView.setLineColor(getResources().getColor(R.color.baby_red));
                           AppUtility.showSnackbar(binding.getRoot(),"Enter verification code");
                           Log.e("VerificationActivityLOG", "empty");
                           return;
                       } else {
                           binding.progressBar.setVisibility(View.VISIBLE);
                           binding.btnLogin.setText(R.string.sending);
                           binding.pinView.setEnabled(false);
                           binding.btnLogin.setEnabled(false);

                           code = binding.pinView.getText().toString();
                           if (verificationIdEdit != null) {
                               binding.progressBar.setVisibility(View.VISIBLE);

                               Log.d("inside if","inside");
                               Log.d("verificationIdEdit",getIntent().getStringExtra("verificationIdEdit"));
                               Log.d("code",binding.pinView.toString().trim());

                               presenter.updatePhoneNumber(getIntent().getStringExtra("verificationIdEdit"),binding.pinView.getText().toString(),sp.getString(DRIVER_ID_KEY,null),Integer.parseInt(newNumber));
                                //updatePhoneNumber(getIntent().getStringExtra("verificationIdEdit"),binding.pinView.getText().toString());
                           }
                       }

                   }
               });
           }
        binding.tvResend.setVisibility(View.VISIBLE);

        binding.tvResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.tvResend.setTextColor(getResources().getColor(R.color.dark_gray));
                binding.tvResend.setEnabled(false);
                resendVerificationCode();
            }
        });



    }

    private void setEnabledVisibility(){
        binding.progressBar.setVisibility(View.GONE);
        binding.btnLogin.setText(R.string.verify);
        binding.btnLogin.setEnabled(true);
        binding.pinView.setEnabled(true);
        binding.tvResend.setEnabled(true);
    }


    private void updatePhoneNumber(String verificationId, String verificationCode) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, verificationCode);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            currentUser.updatePhoneNumber(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Phone number update successful

                                AppUtility.showSnackbar(binding.getRoot(),"Phone number updated successfully");
                            } else {
                                // Phone number update failed
                                Log.d("PhoneNum updated failed", task.getException().getMessage());
                                //AppUtility.showSnackbar(binding.getRoot(),"Failed to update phone number: " + task.getException().getMessage());

                                Toast.makeText(getApplicationContext(), "Failed to update phone number: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    @Override
    public void onChangeNumberSuccess() {

        AppUtility.showSnackbar(binding.getRoot(),getString(R.string.num_change_success));
        startActivity(new Intent(getBaseContext(), MainActivity.class));
        finish();
    }



    private void resendVerificationCode() {
        if (token != null) {

            Log.d("teeeeat1","+970" + LoginActivity.mobile);
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    "+970" + LoginActivity.mobile,
                    60,
                    TimeUnit.SECONDS,
                    VerificationActivity.this,
                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                        @Override
                        public void onVerificationCompleted(PhoneAuthCredential credential) {
                            // This method will be called when verification is completed automatically
                            signInWithPhoneAuthCredential(credential);
                        }

                        @Override
                        public void onVerificationFailed(FirebaseException e) {
                            Toast.makeText(VerificationActivity.this, "Verification failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken resendingToken) {
                            storedVerificationId = verificationId;
                            token = resendingToken;
                            startCountdownTimer();
                            Toast.makeText(VerificationActivity.this, "Verification code sent", Toast.LENGTH_SHORT).show();
                        }
                    },
                    token
            );
        } else {
            Toast.makeText(VerificationActivity.this, "Resending not supported", Toast.LENGTH_SHORT).show();
        }
    }

    private void startCountdownTimer() {
        binding.tvResend.setEnabled(false);
        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                binding.tvResend.setText("Resend in " + millisUntilFinished / 1000 + "s");
            }

            public void onFinish() {
                binding.tvResend.setEnabled(true);
                binding.tvResend.setText("Resend");
            }
        }.start();
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(VerificationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(VerificationActivity.this, "Verification successful", Toast.LENGTH_SHORT).show();
                            AuthResult authResult = task.getResult();
                            // Perform additional actions if needed
                        } else {
                            Toast.makeText(VerificationActivity.this, "Verification failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    public void onChangeNumberFailure(Exception e) {
        AppUtility.showSnackbar(binding.getRoot(),e.getMessage());
        Log.d("FailureUpdating",e.getMessage());
        binding.pinView.setError(getString(R.string.num_change_error));
        binding.pinView.setLineColor(getResources().getColor(R.color.baby_red));

    }
}
