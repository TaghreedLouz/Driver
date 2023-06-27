package com.example.driveroutreach.ui.fragments.Profile;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.driveroutreach.R;
import com.example.driveroutreach.databinding.FragmentProfileBinding;
import com.example.driveroutreach.model.DriverProfile;
import com.example.driveroutreach.ui.activities.Login.LoginActivity;
import com.example.driveroutreach.ui.activities.about_us.AboutUsActivity;
import com.example.driveroutreach.ui.activities.contact_us.ContactUsActivity;
import com.example.driveroutreach.ui.activities.edit_profile.EditProfileActivity;
import com.example.driveroutreach.ui.activities.notification.NotificationActivity;
import com.example.driveroutreach.ui.activities.settings.SettingsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    SharedPreferences sp;
    public final String DRIVER_ID_KEY = "driverId";


    DriverProfile driverProfileObject;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentProfileBinding binding = FragmentProfileBinding.inflate(inflater,container,false);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        sp = getActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);

        String driverId= sp.getString(DRIVER_ID_KEY,null);

           firestore.collection("Driver").document(driverId).get()
                           .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                               @Override
                               public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                   Log.d("Driver",task.getResult().toString());
                                   if (task.isSuccessful()){


                                     driverProfileObject = task.getResult().toObject(DriverProfile.class);
                                     Log.d("Driver",task.getResult().toString());
                                     binding.tvName.setText(driverProfileObject.getName());
                                     binding.tvNumber.setText("+972".concat(String.valueOf(driverProfileObject.getMobile())));

                                       } else {

                                       Log.d("Driver's info", task.getException().getMessage());

                                   }

                               }
                           });

                binding.linLayoutContactUs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getActivity(), ContactUsActivity.class));
                    }
                });

                binding.linLayoutNotification.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getActivity(), NotificationActivity.class));
                    }
                });

                binding.linLayoutAboutUs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getActivity(), AboutUsActivity.class));
                    }
                });

                binding.linLayoutSettings.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getActivity(), SettingsActivity.class));


                    }
                });


                binding.imgEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                      startActivity(new Intent(getActivity(), EditProfileActivity.class).putExtra("Driver_Profile",driverProfileObject));
                    }
                });

                binding.linLayoutLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                    }
                });

        return binding.getRoot();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

//        sendData =(sendData) context;
    }
}