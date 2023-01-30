package com.example.universityapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.universityapp.databinding.ActivityCreatePostBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class CreatePostActivity extends AppCompatActivity {

    private ActivityCreatePostBinding binding;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;

    private FirebaseAuth mAuth;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("Images");

    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreatePostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });

        binding.iconaddimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        binding.createpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(binding.iconaddimg);
        }

    }

    private void uploadImage() {

        String college = getIntent().getStringExtra("colTags");
        String club = getIntent().getStringExtra("clubTags");
        binding.progress.setVisibility(View.VISIBLE);
        binding.createpost.setEnabled(false);
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(mImageUri.getLastPathSegment());

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                }
                            }, 500);
                            Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imagePostUrl = uri.toString();
                                    Post post = new Post(binding.postName.getText().toString(), binding.postDesc.getText().toString(), imagePostUrl);
                                    firestore.collection("College")
                                            .document(college)
                                            .collection("clubs")
                                            .document(club)
                                            .collection("Posts")
                                            .document()
                                            .set(post)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(CreatePostActivity.this, "Upload new Post success", Toast.LENGTH_SHORT).show();
                                                    binding.progress.setVisibility(View.INVISIBLE);
                                                    binding.createpost.setEnabled(true);

                                                    Picasso.get().load(R.drawable.ic_addimg).into(binding.iconaddimg);
                                                    binding.iconaddimg.setBackgroundResource(R.drawable.ic_addimg);
                                                    binding.postName.getText().clear();
                                                    binding.postDesc.getText().clear();

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(CreatePostActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                                    binding.progress.setVisibility(View.INVISIBLE);
                                                    binding.createpost.setEnabled(true);


                                                }
                                            });

                                }
                            });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CreatePostActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            binding.progress.setVisibility(View.INVISIBLE);
                            binding.createpost.setEnabled(true);
                        }
                    });


        } else {
            binding.progress.setVisibility(View.INVISIBLE);
            binding.createpost.setEnabled(true);

            Toast.makeText(this, "Please Choose image first", Toast.LENGTH_SHORT).show();
        }

    }

    private void Register() {

        String email = binding.regEmail.getText().toString().trim();
        String password = binding.regPass.getText().toString().trim();
        String college = getIntent().getStringExtra("colTags");
        String club = getIntent().getStringExtra("clubTags");

        binding.register.setEnabled(false);


        if (!email.isEmpty()){
            if (!password.isEmpty()&&password.length()>6){

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                User user = new User("null", email, "null", password, college, club);


                                firestore.collection("College")
                                        .document(college)
                                        .collection("clubs")
                                        .document(club)
                                        .collection("User")
                                        .document(email)
                                        .set(user)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                firestore.collection("Users").document(email).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                    }
                                                });

                                                Toast.makeText(CreatePostActivity.this, "Register success :)", Toast.LENGTH_SHORT).show();
                                                binding.register.setEnabled(true);

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                binding.register.setEnabled(true);
                                                binding.progress.setVisibility(View.INVISIBLE);


                                                Toast.makeText(CreatePostActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            }
                        });

            }else {
                binding.regPass.setError("enter strong pasword");
                binding.regPass.requestFocus();
                binding.register.setEnabled(true);

            }


        }else {
            binding.regEmail.setError("enter email");
            binding.regEmail.requestFocus();
            binding.register.setEnabled(true);

        }



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
                startActivity(new Intent(CreatePostActivity.this, LoginActivity.class));
                break;
            case R.id.action_home:

                startActivity(new Intent(CreatePostActivity.this, MainActivity.class));
                finish();
                break;

            case R.id.action_std:
                startActivity(new Intent(CreatePostActivity.this, StdActivity.class));
                break;


        }
        return super.onOptionsItemSelected(item);

    }


}