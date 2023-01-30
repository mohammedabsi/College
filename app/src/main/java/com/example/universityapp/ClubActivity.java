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
import android.widget.Toast;

import com.example.universityapp.databinding.ActivityClubBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ClubActivity extends AppCompatActivity implements RecyclerViewInterface {

    private ActivityClubBinding binding;

    private ClubAdaptar clubAdaptar;
    private ArrayList<College> placesArrayList;
    ;
    LinearLayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityClubBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.addclubbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddClub();
            }
        });

        initRecycler();
        RetrieveNewsData();



    }

    private void AddClub() {
        String x = getIntent().getStringExtra("colTags");
        String club = binding.addclub.getText().toString().trim();

        College college = new College(x, club);

        if (!club.isEmpty()) {


            FirebaseFirestore.getInstance().collection("College").document(x).collection("clubs").document(club).set(college);

        } else {
            binding.addclub.setError("enter College");
            binding.addclub.requestFocus();
        }
    }

    private void initRecycler() {
        placesArrayList = new ArrayList<College>();
        layoutManager = new LinearLayoutManager(ClubActivity.this);
        binding.clubrecycler.setLayoutManager(layoutManager);
        binding.clubrecycler.setHasFixedSize(true);
        clubAdaptar = new ClubAdaptar(placesArrayList, getApplicationContext(), this);
        binding.clubrecycler.setAdapter(clubAdaptar);
    }

    public void RetrieveNewsData() {
        String x = getIntent().getStringExtra("colTags");


        FirebaseFirestore.getInstance().collection("College").document(x).collection("clubs").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {

                    Log.d("fireStore Error", error.getMessage().toString());

                    return;
                }
                for (DocumentChange documentChange : value.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        College college = documentChange.getDocument().toObject(College.class);

                        college.setClub(documentChange.getDocument().getId());
                        placesArrayList.add(college);

                        Log.d("asTAG", "onEvent: " + documentChange.getDocument().getId());


                    }

                    clubAdaptar.notifyDataSetChanged();




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
                startActivity(new Intent(ClubActivity.this,LoginActivity.class));
                break;
            case R.id.action_home:

                startActivity(new Intent(ClubActivity.this,MainActivity.class));
                finish();
                break;
            case R.id.action_std:
                startActivity(new Intent(ClubActivity.this, StdActivity.class));
                break;


        }
        return super.onOptionsItemSelected(item);

    }


    @Override

    public void onItemClick(Integer position) {

        String x = getIntent().getStringExtra("colTags");
//
        Intent intent = new Intent(this, CreatePostActivity.class);
        intent.putExtra("clubTags", placesArrayList.get(position).getClub());
        intent.putExtra("colTags", x);
        startActivity(intent);


    }

    @Override
    public void onDeleteClick(Integer position) {
        placesArrayList.remove(position);
        clubAdaptar.notifyItemRemoved(position);
    }
}