package com.example.driveroutreach.ui.activities.edit_profile;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.driveroutreach.R;
import com.example.driveroutreach.databinding.ActivityEditProfileBinding;
import com.example.driveroutreach.model.DriverProfile;
import com.example.driveroutreach.ui.activities.Verification.VerificationActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class EditProfileActivity extends AppCompatActivity implements EditProfileView {

    ActivityEditProfileBinding binding;
    FirebaseStorage firebaseStorage;
    FirebaseFirestore firestore;
    SharedPreferences sp;
    FirebaseAuth firebaseAuth;
    public final String DRIVER_ID_KEY = "driverId";
    EditProfilePresenter editProfilePresenter;
    String driverId;
    Uri  imgUrl;
    PhoneAuthCredential c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

         editProfilePresenter = new EditProfilePresenter(this);

        firebaseStorage  = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        sp = getSharedPreferences("sp", MODE_PRIVATE);
        driverId =sp.getString(DRIVER_ID_KEY,null);

        DriverProfile driver_profile = getIntent().getParcelableExtra("Driver_Profile");

        binding.tvName.setText(String.valueOf(driver_profile.getName()));
        binding.etMobile.setText(String.valueOf(driver_profile.getMobile()));
        binding.tvVechileId.setText(String.valueOf(driver_profile.getVichuleId()));
        binding.tvDayOff.setText(driver_profile.getDayOff());
        binding.tvRegion.setText(driver_profile.getRegion());

        editProfilePresenter.gettingProfileImage(sp.getString(DRIVER_ID_KEY,null));







       binding.topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        onBackPressed();
    }
});

        binding.imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inetent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(inetent, 10);
            }
        });



        binding.etMobile.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    String newNumber = binding.etMobile.getText().toString().trim();


                    if (!newNumber.isEmpty()){
                           updateNumber(newNumber);
                        return true;
                    } else {
                        Toast.makeText(EditProfileActivity.this, "Enter a new number", Toast.LENGTH_SHORT).show();
                    }
                    }

                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10 && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri img_uri = data.getData();
            binding.imgProfile.setImageURI(img_uri);

            if (img_uri != null) uploadImage(img_uri);
        }
    }


    void uploadImage(Uri profile_img_uri){

        //Setting the progress
        ProgressDialog progressDialog= new ProgressDialog(EditProfileActivity.this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();


        StorageReference storageReference = firebaseStorage.getReference("DriverImages/" + sp.getString(DRIVER_ID_KEY,null) +"/"+profile_img_uri.getLastPathSegment());
        StorageTask<UploadTask.TaskSnapshot> uploadTask = storageReference.putFile(profile_img_uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Image uploaded successfully
                        // Dismiss dialog
                        progressDialog.dismiss();
                        Toast.makeText(getBaseContext(),"Image Uploaded!!", Toast.LENGTH_SHORT).show();



                        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {

                               imgUrl = task.getResult();
                               storeImgInProfile();
                                Log.d("bb",imgUrl.toString());
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();

                        // Error, Image not uploaded
                        progressDialog.dismiss();
                        Log.d("Failure", e.getMessage());
                        Toast.makeText(getBaseContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }) .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                        double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        progressDialog.setMessage("Uploaded " + (int)progress + "%");
                        progressDialog.setProgress((int) progress);
                    }
                });

    }

    void storeImgInProfile(){
        Map<String , Object> setImg = new HashMap<>();
        setImg.put("ImgUrl",  imgUrl.toString());

        firestore.collection("Driver").document(sp.getString(DRIVER_ID_KEY,null)).update(setImg)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Log.d(getClass().getSimpleName(), "Successful");
                        } else {
                            Log.d(getClass().getSimpleName(), task.getException().getMessage());         }
                    }
                });
    }


    void updateNumber(String newNumber) {
        PhoneAuthCredential credentialCompleted ;
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+970" + newNumber,
                60, TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        Log.e("LoginActivityLOG", "done");
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Log.d("verification",e.getMessage());
                    //    setEnabledVisibility();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        super.onCodeSent(verificationId, token);

                        Log.d("verificationIdEdit",verificationId+"  "+"from edit");
                        Intent intent = new Intent(EditProfileActivity.this, VerificationActivity.class);
                        intent.putExtra("verificationIdEdit", verificationId);
                        intent.putExtra("number", newNumber);
                        intent.putExtra("fromWhere",false);
                        startActivity(intent);
                        finish();

                    }
                }
        );



    }


    @Override
    public void onGettingImgeSuccess(String img) {

        Glide.with(getBaseContext()).load(img)
                .into(binding.imgProfile);

        Log.d("Success",img);
    }

    @Override
    public void onGettingImgFailure(Exception e) {
       binding.imgProfile.setImageResource(R.drawable.profile_avtar);
     Log.d("FailureImg",e.getMessage());
    }


}

