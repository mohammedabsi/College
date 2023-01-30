package com.example.universityapp;

import android.content.Context;
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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class StdAdaptar extends RecyclerView.Adapter<StdAdaptar.MainViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    private ArrayList<User> mainList;
    private Context mContext;

    public StdAdaptar(ArrayList<User> mainList, Context mContext, RecyclerViewInterface recyclerViewInterface) {
        this.mainList = mainList;
        this.mContext = mContext;
        this.recyclerViewInterface = recyclerViewInterface;
    }



    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.stdelement, parent, false);


        return new MainViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        User maincollege = mainList.get(position);
        holder.stdemail.setText(maincollege.getEmail());
        holder.stdpass.setText(maincollege.getPassword());
        holder.stdclub.setText(maincollege.getCategory());
        holder.stdcollege.setText(maincollege.getCollege());
        holder.stddelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                // Get auth credentials from the user for re-authentication. The example below shows
                // email and password credentials but there are multiple possible providers,
                // such as GoogleAuthProvider or FacebookAuthProvider.
                AuthCredential credential = EmailAuthProvider
                        .getCredential(maincollege.getEmail(), maincollege.getPassword());

                // Prompt the user to re-provide their sign-in credentials
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                user.delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    FirebaseFirestore.getInstance().collection("Users").document(maincollege.getEmail()).delete();

                                                    int pos = holder.getAdapterPosition();
                                                    if (pos !=RecyclerView.NO_POSITION){
                                                        mainList.remove(pos);
                                                        notifyItemRemoved(pos);
                                                        recyclerViewInterface.onDeleteClick(pos);

                                                    }
                                                    Toast.makeText(mContext, "User account deleted.", Toast.LENGTH_SHORT).show();
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
        });


    }

    @Override
    public int getItemCount() {
        return mainList.size();
    }

    public void filterList(ArrayList<User> filteredList) {
        mainList = filteredList;
        notifyDataSetChanged();
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        TextView stdemail , stdpass , stdclub ,stdcollege ;

        ImageView stddelete;


        public MainViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            stdemail = itemView.findViewById(R.id.stdemail);
            stdpass = itemView.findViewById(R.id.stdpass);
            stdcollege = itemView.findViewById(R.id.stdcollege);
            stdclub = itemView.findViewById(R.id.stdclub);
            stddelete = itemView.findViewById(R.id.stddelete);

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
