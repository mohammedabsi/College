package com.example.universityapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.universityapp.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface {

    private ActivityMainBinding binding;

    FirebaseFirestore firestore;
    FirebaseAuth mAuth;


    private MainPostsAdaptar mainPostsAdaptar;
    private ArrayList<College> placesArrayList;
    ;
    LinearLayoutManager layoutManager;

    private boolean x = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        initRecycler();
        RetrieveNewsData();


        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (x == false) {
                    binding.addbutton.setVisibility(View.VISIBLE);
                    binding.textInputLayout2.setVisibility(View.VISIBLE);
                    x = true;
                } else {
                    binding.addbutton.setVisibility(View.GONE);
                    binding.textInputLayout2.setVisibility(View.GONE);
                    x = false;
                }

            }
        });

        binding.addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddCollege();
            }
        });
    }

    private void AddCollege() {
        String coll = binding.addcol.getText().toString().trim();

        College college = new College(null, null);

        if (!coll.isEmpty()) {


            firestore.collection("College").document(coll).set(college);

        } else {
            binding.addcol.setError("enter College");
            binding.addcol.requestFocus();
        }
    }

    private void initRecycler() {
        placesArrayList = new ArrayList<College>();
        layoutManager = new LinearLayoutManager(MainActivity.this);
        binding.collegerecycler.setLayoutManager(layoutManager);
        binding.collegerecycler.setHasFixedSize(true);
        mainPostsAdaptar = new MainPostsAdaptar(placesArrayList, getApplicationContext(), this);
        binding.collegerecycler.setAdapter(mainPostsAdaptar);
    }

    public void RetrieveNewsData() {

        firestore.collection("College").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {

                    Log.d("fireStore Error", error.getMessage().toString());

                    return;
                }
                for (DocumentChange documentChange : value.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        College college = documentChange.getDocument().toObject(College.class);

                        college.setCollege(documentChange.getDocument().getId());
                        placesArrayList.add(college);

                        Log.d("aTAG", "onEvent: " + documentChange.getDocument().getId());


                    }

                    mainPostsAdaptar.notifyDataSetChanged();

                    if (!placesArrayList.isEmpty()) {
                        binding.hidetxt.setVisibility(View.INVISIBLE);
                    } else {
                        binding.hidetxt.setVisibility(View.VISIBLE);

                    }


                }


            }
        });

    }

    @Override
    public void onItemClick(Integer position) {

        Intent intent = new Intent(this, ClubActivity.class);
        intent.putExtra("colTags", placesArrayList.get(position).getCollege());

        startActivity(intent);

        // Toast.makeText(this, "click"+placesArrayList.get(position).getCollege() , Toast.LENGTH_SHORT).show();

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
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;

            case R.id.action_home:

                startActivity(new Intent(MainActivity.this, MainActivity.class));
                finish();
                break;

            case R.id.action_std:
                startActivity(new Intent(MainActivity.this, StdActivity.class));
                break;


        }
        return super.onOptionsItemSelected(item);

    }
}