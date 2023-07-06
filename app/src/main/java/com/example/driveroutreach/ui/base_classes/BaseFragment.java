package com.example.driveroutreach.ui.base_classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;

public class BaseFragment extends Fragment {
    public FirebaseFirestore firestore;
    public SharedPreferences sp;

    public SharedPreferences.Editor edit;
    public final String DRIVER_ID_KEY = "driverId";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);
        edit= sp.edit();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        firestore = FirebaseFirestore.getInstance();
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
