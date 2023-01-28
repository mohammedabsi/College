package com.example.universityapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        TextView stdemail , stdpass;


        public MainViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            stdemail = itemView.findViewById(R.id.stdemail);
            stdpass = itemView.findViewById(R.id.stdpass);

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
