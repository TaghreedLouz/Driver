package com.example.driveroutreach.ui.fragments.schedule.all;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.driveroutreach.R;
import com.example.driveroutreach.adapters.AllTripChildAdapter;
import com.example.driveroutreach.adapters.AllTripsAdapter;
import com.example.driveroutreach.databinding.FragmentAllBinding;
import com.example.driveroutreach.model.JourneyModel;
import com.example.driveroutreach.model.SchedualAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllFragment extends Fragment {
    ArrayList<SchedualAllModel> All_Scheduale_Array;
    ArrayList<String> OrgNames;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AllFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllFragment newInstance(String param1, String param2) {
        AllFragment fragment = new AllFragment();
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
        FragmentAllBinding binding = FragmentAllBinding.inflate(inflater,container,false);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();



        Set<String> OrganizationsNames = new HashSet<String>();
        All_Scheduale_Array = new ArrayList<>();



        firestore.collection("Journey").whereEqualTo("driver","1").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()){

                            for (QueryDocumentSnapshot document : task.getResult()){

                               String org  = document.toObject(JourneyModel.class).getOrganization();

                               OrganizationsNames.add(org);

                               firestore.collection("Journey").whereEqualTo("organization", org)
                                       .whereEqualTo("driver","1")
                                       .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                           @Override
                                           public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                               if (task.isSuccessful()){
                                                   for (QueryDocumentSnapshot document: task.getResult()){

                                                    SchedualAllModel schedualAllModel  = document.toObject(SchedualAllModel.class);


                                                    All_Scheduale_Array.add(schedualAllModel);

                                                    Log.d("all_Rv2",All_Scheduale_Array.toString());


                                                   }

                                               }else {
                                                   Log.d("all",task.getException().getMessage());
                                               }
                                           }
                                       });

                            }

                            //Log.d("test",All_Scheduale_Array.get(0).getOrganization());
                            OrgNames = new ArrayList<>(OrganizationsNames);

                            binding.RVALLTRIPS.setAdapter(new AllTripsAdapter(OrgNames));
                            binding.RVALLTRIPS.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

                        }else {
                            Log.d("journey_all",task.getException().getMessage());
                        }

                    }
                });






        return binding.getRoot();
    }
}