package com.example.driveroutreach.ui.activities.edit_profile;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.driveroutreach.databinding.ActivityEditProfileBinding;
import com.example.driveroutreach.model.DriverProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    ActivityEditProfileBinding binding;
    FirebaseStorage firebaseStorage;
    FirebaseFirestore firestore;
    SharedPreferences sp;

    public final String DRIVER_ID_KEY = "driverId";

    Uri  imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseStorage =firebaseStorage = FirebaseStorage.getInstance();
        firestore=FirebaseFirestore.getInstance();
        sp = getSharedPreferences("sp", MODE_PRIVATE);

        DriverProfile driver_profile = getIntent().getParcelableExtra("Driver_Profile");

        binding.tvName.setText(String.valueOf(driver_profile.getName()));
        binding.etMobile.setText(String.valueOf(driver_profile.getMobile()));
        binding.tvVechileId.setText(String.valueOf(driver_profile.getVichuleId()));
        binding.tvDayOff.setText(driver_profile.getDayOff());
        binding.tvRegion.setText(driver_profile.getRegion());





        binding.imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inetent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(inetent, 10);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10 && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri img_uri = data.getData();
            binding.imgProfile.setImageURI(img_uri);

            // todo: remove the comment when Taghreed finishes the login
            if (img_uri != null) uploadImage(img_uri);
        }
    }
 //todo: make in the firebase var for img url .. test this function ..
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
        Map<String , Object> setLocation = new HashMap<>();
        setLocation.put("ImgUrl",  imgUrl.toString());

        firestore.collection("Beneficiaries").document("1").update(setLocation)
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
}

