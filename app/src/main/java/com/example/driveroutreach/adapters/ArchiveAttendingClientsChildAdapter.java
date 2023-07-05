package com.example.driveroutreach.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.driveroutreach.databinding.ItemArchiveDetailsBinding;
import com.example.driveroutreach.model.Benefeciares;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ArchiveAttendingClientsChildAdapter  extends RecyclerView.Adapter<ArchiveAttendingClientsChildAdapter.ChildVH> {

    ArrayList<String> clientsId;
    Context context;
    FirebaseFirestore firestore;

    public ArchiveAttendingClientsChildAdapter(ArrayList<String> clientsId,Context context) {
        this.clientsId = clientsId;
        this.context = context;
        firestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ChildVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemArchiveDetailsBinding binding = ItemArchiveDetailsBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ChildVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildVH holder, int position) {

        String id = clientsId.get(position);

        firestore.collection("Beneficiaries").whereEqualTo("documentId",id)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()){
                            ArrayList<Benefeciares> benf    = (ArrayList<Benefeciares>) task.getResult().toObjects(Benefeciares.class);

                            for (Benefeciares b : benf){
                                holder.name.setText(b.getName());

                                Glide.with(context).load(b.getImgUrl()).into(holder.clientImg);
                            }

                        } else {
                            Log.d("faildToGetClientInfo",task.getException().getMessage());
                        }




                    }
                });

    }

    @Override
    public int getItemCount() {
        return clientsId != null? clientsId.size() : 0;
    }

    class ChildVH extends RecyclerView.ViewHolder {

        TextView name;
        ImageView clientImg;

        public ChildVH(@NonNull ItemArchiveDetailsBinding binding) {
            super(binding.getRoot());

            name = binding.tvName;
            clientImg=binding.imgProfile;

        }
    }
}
