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
import com.example.driveroutreach.model.AttendanceConfirmation;
import com.example.driveroutreach.model.Benefeciares;
import com.example.driveroutreach.ui.app_utility.AppUtility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

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


        binding.tvDate.setText(AppUtility.getDate());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("AttendanceConfirmation")
                .child(date).child(driverId).child(journeyId);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("valuess",dataSnapshot.toString());

                if (dataSnapshot.exists()) {


                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {

                        AttendanceConfirmation attendance =  childSnapshot.getValue(AttendanceConfirmation.class);


                    firestore.collection("Beneficiaries").whereEqualTo("documentId",attendance.getUserId()).get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                    if (task.isSuccessful()){
                                        ArrayList< Benefeciares> benf = (ArrayList<Benefeciares>) task.getResult().toObjects(Benefeciares.class);

                                        ArrayList<String> name = new ArrayList<>();
                                        for (Benefeciares b: task.getResult().toObjects(Benefeciares.class)){

                                            name.add(b.getName());
                                        }


                                        binding.rvAttendings.setAdapter(new bottomSheetAdapter(name));
                                        binding.rvAttendings.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));

                                    }else {
                                        Log.d("Failure",task.getException().getMessage());
                                    }


                                }
                            });


                    }




                } else {
                    Log.d("DataReturned", "No data available");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("onFailure", databaseError.getMessage());
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
