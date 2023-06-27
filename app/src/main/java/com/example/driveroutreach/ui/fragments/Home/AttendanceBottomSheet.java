package com.example.driveroutreach.ui.fragments.Home;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.driveroutreach.adapters.bottomSheetAdapter;
import com.example.driveroutreach.databinding.BottomSheetDialogBinding;
import com.example.driveroutreach.model.Benefeciares;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AttendanceBottomSheet extends BottomSheetDialogFragment {

    BottomSheetBehavior bottomSheetBehavior;

    BottomSheetDialogBinding binding;

    Task<DataSnapshot> reference;
    SharedPreferences sp;
    public final String DRIVER_ID_KEY = "driverId";
    String journeyId;
    String date;
    ArrayList<String> benf;
    ArrayList<String> names;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetDialogBinding.inflate(inflater,container,false);

        sp = getActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);
        String driverId= sp.getString(DRIVER_ID_KEY,null);
         journeyId = sp.getString("journeyId", null);
         date = sp.getString("journeyDate", null);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        names = new ArrayList<>();

        SimpleDateFormat simpleformat = new SimpleDateFormat("dd-MMMM-yyyy");
        date=simpleformat.format(Calendar.getInstance().getTime());

        binding.tvDate.setText(date);

        reference = FirebaseDatabase.getInstance().getReference("AttendanceConfirmation").child("31-May-2023")
                .child(driverId).child(journeyId).get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {

                        if (task.isSuccessful()) {
                            for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {


                                benf =(ArrayList<String>) dataSnapshot.getValue();

                                Log.d("DataReturned", benf.toString());

                            }

                            if (benf != null){
                                for (int i = 0; i < benf.size(); i++) {

                                    firestore.collection("Beneficiaries").document(benf.get(i)).get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        Benefeciares benefeciares = task.getResult().toObject(Benefeciares.class);

                                                        Log.d("locationTag",benefeciares.getLocation().toString());


                                                       benefeciares.getName();
                                                       names.add(benefeciares.getName());
                                                        binding.rvAttendings.setAdapter(new bottomSheetAdapter(names));
                                                        binding.rvAttendings.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

                                                    } else {
                                                        Log.d("exception",task.getException().getMessage());
                                                    }

                                                }
                                            });

                                }
                            }

                        } else {
                            Log.d("realtimeDatabase", task.getException().getMessage());
                        }
                    }
                });





        return binding.getRoot();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog1) {
                FrameLayout bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);

                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setSkipCollapsed(true);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        return dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }
}
