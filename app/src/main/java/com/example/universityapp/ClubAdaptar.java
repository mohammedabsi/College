package com.example.universityapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ClubAdaptar extends RecyclerView.Adapter<ClubAdaptar.MainViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    private ArrayList<College> mainList;
    private Context mContext;

    public ClubAdaptar(ArrayList<College> mainList, Context mContext, RecyclerViewInterface recyclerViewInterface) {
        this.mainList = mainList;
        this.mContext = mContext;
        this.recyclerViewInterface = recyclerViewInterface;
    }


    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.element2, parent, false);


        return new MainViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        College club = mainList.get(position);
        holder.single_element2.setText(club.getClub());



        holder.icondelete2.setOnClickListener(new View.OnClickListener() {
                                                         @Override
                                                         public void onClick(View view) {
                                                             FirebaseFirestore.getInstance()
                                                                     .collection("College")
                                                                     .document(club.getCollege())
                                                                     .collection("clubs")
                                                                     .document(club.getClub())
                                                                     .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                         @Override
                                                                         public void onComplete(@NonNull Task<Void> task) {
                                                                             Toast.makeText(mContext, "Club deleted", Toast.LENGTH_SHORT).show();
                                                                             int pos = holder.getAdapterPosition();
                                                                             if (pos !=RecyclerView.NO_POSITION){
                                                                                 mainList.remove(pos);
                                                                                 notifyItemRemoved(pos);
                                                                                 recyclerViewInterface.onDeleteClick(pos);

                                                                             }
                                                                         }
                                                                     }).addOnFailureListener(new OnFailureListener() {
                                                                         @Override
                                                                         public void onFailure(@NonNull Exception e) {
                                                                             Toast.makeText(mContext, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                                                         }
                                                                     });
                                                         }
                                                     });


    }

    @Override
    public int getItemCount() {
        return mainList.size();
    }

    public void filterList(ArrayList<College> filteredList) {
        mainList = filteredList;
        notifyDataSetChanged();
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        TextView single_element2;
        ImageView icondelete2;


        public MainViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            single_element2 = itemView.findViewById(R.id.element2);
            icondelete2 = itemView.findViewById(R.id.icondelete2);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });

        }
    }
}
