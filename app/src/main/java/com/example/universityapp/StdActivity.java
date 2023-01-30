package com.example.universityapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.universityapp.databinding.ActivityStdBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class StdActivity extends AppCompatActivity implements RecyclerViewInterface {

    private ActivityStdBinding binding;

    FirebaseFirestore firestore;
    FirebaseAuth mAuth;


    private StdAdaptar stdAdaptar;
    private ArrayList<User> userArrayListArrayList;
    ;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStdBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        initRecycler();
        RetrieveNewsData();

    }

    private void initRecycler() {
        userArrayListArrayList = new ArrayList<User>();
        layoutManager = new LinearLayoutManager(StdActivity.this);
        binding.stdlist.setLayoutManager(layoutManager);
        binding.stdlist.setHasFixedSize(true);
        stdAdaptar = new StdAdaptar(userArrayListArrayList, getApplicationContext(), this);
        binding.stdlist.setAdapter(stdAdaptar);
    }

    public void RetrieveNewsData() {

        firestore.collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {

                    Log.d("fireStore Error", error.getMessage().toString());

                    return;
                }
                for (DocumentChange documentChange : value.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        User college = documentChange.getDocument().toObject(User.class);

                        userArrayListArrayList.add(college);



                    }

                    stdAdaptar.notifyDataSetChanged();




                }


            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menulayout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(StdActivity.this, LoginActivity.class));
                finish();
                break;

            case R.id.action_home:

                startActivity(new Intent(StdActivity.this, MainActivity.class));
                finish();
                break;

            case R.id.action_std:
                startActivity(new Intent(StdActivity.this, StdActivity.class));
                break;


        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onItemClick(Integer position) {

    }

    @Override
    public void onDeleteClick(Integer position) {
        userArrayListArrayList.remove(position);
        stdAdaptar.notifyItemRemoved(position);
    }
}