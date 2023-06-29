package com.example.driveroutreach.ui.fragments.Profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.driveroutreach.R;
import com.example.driveroutreach.databinding.FragmentProfileBinding;
import com.example.driveroutreach.model.DriverProfile;
import com.example.driveroutreach.ui.activities.Login.LoginActivity;
import com.example.driveroutreach.ui.activities.contact_us.ContactUsActivity;
import com.example.driveroutreach.ui.activities.edit_profile.EditProfileActivity;
import com.example.driveroutreach.ui.activities.notification.NotificationActivity;
import com.example.driveroutreach.ui.activities.settings.SettingsActivity;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements ProfileView{

    SharedPreferences sp;
    public final String DRIVER_ID_KEY = "driverId";
    FragmentProfileBinding binding;

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
         binding = FragmentProfileBinding.inflate(inflater,container,false);

        sp = getActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);
        String driverId= sp.getString(DRIVER_ID_KEY,null);

        ProfilePresenter profilePresenter = new ProfilePresenter(this);

        profilePresenter.driverInfo(driverId);

        profilePresenter.gettingProfileImage(driverId);


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
//                        Intent intent = new Intent(getActivity(), Abou.class);
//                        startActivity(intent);
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
    public void onDriverInfoFailure(Exception e) {
        Log.d(getClass().getName(),e.getMessage());
    }

    @Override
    public void onDriverInfoSuccess(DriverProfile driverProfile) {

        driverProfileObject = driverProfile;
        binding.tvName.setText(driverProfile.getName());
        binding.tvNumber.setText("+972".concat(String.valueOf(driverProfile.getMobile())));

    }

    @Override
    public void onGettingImgeSuccess(String img) {
        Glide.with(getActivity()).load(img)
                .into(binding.imgProfile);

        Log.d("Success",img);
    }

    @Override
    public void onGettingImgFailure(Exception e) {
        binding.imgProfile.setImageResource(R.drawable.profile_avtar);
        Log.d("FailureImg",e.getMessage());
    }
}