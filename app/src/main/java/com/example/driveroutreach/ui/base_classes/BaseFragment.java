package com.example.driveroutreach.ui.base_classes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;

public class BaseFragment extends Fragment {
FirebaseFirestore firestore;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

      firestore = FirebaseFirestore.getInstance();

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
