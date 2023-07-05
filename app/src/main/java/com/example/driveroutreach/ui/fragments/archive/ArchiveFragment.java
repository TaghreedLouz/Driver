package com.example.driveroutreach.ui.fragments.archive;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.driveroutreach.adapters.ArchiveAdapter;
import com.example.driveroutreach.databinding.FragmentArchiveBinding;
import com.example.driveroutreach.model.ArichivedJourney;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArchiveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArchiveFragment extends Fragment implements ArichiveView {
    ArchivePresenter presenter;

    SharedPreferences sp;
    SharedPreferences.Editor edit;
    public final String DRIVER_ID_KEY = "driverId";
    FragmentArchiveBinding binding;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ArchiveFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ArchiveFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ArchiveFragment newInstance(String param1, String param2) {
        ArchiveFragment fragment = new ArchiveFragment();
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
         binding = FragmentArchiveBinding.inflate(inflater,container,false);

        presenter = new ArchivePresenter(this);
        sp = getActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);

        String driverId= sp.getString(DRIVER_ID_KEY,null);
        edit = sp.edit();


        presenter.gettingArchivedJourneys(driverId);


        return binding.getRoot();
    }

    @Override
    public void onGettingArchivedJourneysSuccess(ArrayList<ArichivedJourney> journeys) {


        binding.rvArchive.setAdapter(new ArchiveAdapter(journeys));
        binding.rvArchive.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
    }

    @Override
    public void onGettingArchivedJourneysFailure(Exception e) {
        Log.d("failure",e.getMessage());
    }
}