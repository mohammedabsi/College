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
import android.widget.Toast;

import com.example.universityapp.databinding.ActivityStudentBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class StudentActivity extends AppCompatActivity implements RecyclerViewInterface {

    private ActivityStudentBinding binding;

    private PostsAdaptar postsAdaptar;
    private ArrayList<Post> postsArrayList;
    ;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    binding.collegetitle.setText(task.getResult().getString("college"));
                    binding.clubtitle.setText(task.getResult().getString("category"));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(StudentActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        initRecycler();
        RetrieveNewsData();
    }

    private void initRecycler() {
        postsArrayList = new ArrayList<Post>();
        layoutManager = new LinearLayoutManager(StudentActivity.this);
        binding.postrecycler.setLayoutManager(layoutManager);
        binding.postrecycler.setHasFixedSize(true);
        postsAdaptar = new PostsAdaptar(postsArrayList, getApplicationContext(), this);
        binding.postrecycler.setAdapter(postsAdaptar);
    }

    public void RetrieveNewsData() {
        FirebaseFirestore.getInstance()
                .collection("Users")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            FirebaseFirestore.getInstance()
                                    .collection("College")
                                    .document(task.getResult().getString("college"))
                                    .collection("clubs")
                                    .document(task.getResult().getString("category"))
                                    .collection("Posts")
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                    if (error != null) {

                                        Log.d("fireStore Error", error.getMessage().toString());

                                        return;
                                    }
                                    for (DocumentChange documentChange : value.getDocumentChanges()) {
                                        if (documentChange.getType() == DocumentChange.Type.ADDED) {
                                            Post post = documentChange.getDocument().toObject(Post.class);
                                            postsArrayList.add(post);

                                            Log.d("asTAG", "onEvent: " + documentChange.getDocument().getId());


                                        }

                                        postsAdaptar.notifyDataSetChanged();




                                    }


                                }
                            });


                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(StudentActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.stdmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.std_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(StudentActivity.this,LoginActivity.class));
                finish();
                break;




        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onItemClick(Integer position) {

    }
}