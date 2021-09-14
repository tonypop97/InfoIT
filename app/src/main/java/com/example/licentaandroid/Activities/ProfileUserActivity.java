package com.example.licentaandroid.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.licentaandroid.JavaClasses.ImagesList;
import com.example.licentaandroid.Adapters.ImagesRecyclerAdapter;
import com.example.licentaandroid.R;
import com.example.licentaandroid.JavaClasses.Users;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileUserActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textView;
    private CardView view, log, change, message, comment;
    private CircleImageView circleImageView;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private ImagesRecyclerAdapter imagesRecyclerAdapter;
    private List<ImagesList> imagesList;
    private StorageTask storageTask;
    private Uri imageUri;
    private static final String TAG = "Register";
    private Users users;
    private static final int IMAGE_REQUEST = 1;
    private AlertDialog alertDialog;
    private Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);


        textView = findViewById(R.id.username);
        circleImageView = findViewById(R.id.profileImage);
        message = findViewById(R.id.PrivateMessage);
        comment = findViewById(R.id.GlobalComment);
        log = findViewById(R.id.Logout);
        change = findViewById(R.id.PassChange);
        view = findViewById(R.id.ListView);

        message.setOnClickListener(this);
        comment.setOnClickListener(this);
        log.setOnClickListener(this);
        change.setOnClickListener(this);
        view.setOnClickListener(this);
        imagesList = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        storageReference = FirebaseStorage.getInstance().getReference("profile_images");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users = dataSnapshot.getValue(Users.class);
                assert users != null;
                textView.setText(users.getFullName());


                Glide.with(getApplicationContext()).load(users.getImageUrl()).into(circleImageView);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileUserActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileUserActivity.this);
                builder.setCancelable(true);
                View mView = LayoutInflater.from(ProfileUserActivity.this).inflate(R.layout.select_image, null);
                RecyclerView recyclerView = mView.findViewById(R.id.recyclerView);
                collectOldImage();
                recyclerView.setLayoutManager(new GridLayoutManager(ProfileUserActivity.this, 3));
                recyclerView.setHasFixedSize(true);
                imagesRecyclerAdapter = new ImagesRecyclerAdapter(imagesList, ProfileUserActivity.this);
                recyclerView.setAdapter(imagesRecyclerAdapter);
                imagesRecyclerAdapter.notifyDataSetChanged();
                Button openImage = mView.findViewById(R.id.open_images);
                openImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openImage();
                    }
                });
                builder.setView(mView);
                alertDialog = builder.create();
                alertDialog.show();


            }


        });
    }

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            if (storageTask != null && storageTask.isInProgress()) {
                Toast.makeText(this, "Uploading is in progress", Toast.LENGTH_SHORT).show();
            } else {
                uploadimage();
            }
        }
    }

    private void uploadimage() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading Image");
        progressDialog.show();
        progressDialog.dismiss();

        if (imageUri != null) {
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

            } catch (IOException e) {
                e.printStackTrace();
            }
            final Bitmap finalBitmap = bitmap;
            databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        users = task.getResult().getValue(Users.class);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        finalBitmap.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream);
                        byte[] imageFileToByte = byteArrayOutputStream.toByteArray();
                        final StorageReference imageReference = storageReference.child(users.getFullName() + System.currentTimeMillis() + ".jpg");
                        storageTask = imageReference.putBytes(imageFileToByte);
                        storageTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }
                                return imageReference.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    alertDialog.dismiss();
                                    Uri downloadURi = task.getResult();
                                    String sdownloadURi = downloadURi.toString();
                                    updateUserProfile(sdownloadURi);
                                    Map<String, Object> hasMap = new HashMap<>();
                                    hasMap.put("imageUrl", sdownloadURi);
                                    databaseReference.updateChildren(hasMap);

                                    final DatabaseReference profileImagesReference = FirebaseDatabase.getInstance().getReference("profile_images").child(firebaseUser.getUid());
                                    profileImagesReference.push().setValue(hasMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (!task.isSuccessful()) {
                                                progressDialog.dismiss();

                                            } else {
                                                progressDialog.dismiss();
                                            }
                                        }
                                    });


                                } else {
                                    Toast.makeText(ProfileUserActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            }

                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ProfileUserActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
            });


        }
    }

    private void updateUserProfile(String sdownloadURi) {
        FirebaseUser user = firebaseUser;
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(Uri.parse(sdownloadURi))
                .build();
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {


                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });


    }


    private void collectOldImage() {
        DatabaseReference imageListReference = FirebaseDatabase.getInstance().getReference("profile_images").child(firebaseUser.getUid());
        imageListReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                imagesList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    imagesList.add(snapshot.getValue(ImagesList.class));
                }

                imagesRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileUserActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()) {
            case R.id.ListView:
                i = new Intent(ProfileUserActivity.this, MenuActivity.class);
                startActivity(i);
                break;

            case R.id.PassChange:
                i = new Intent(ProfileUserActivity.this, ForgotPasswordActivity.class);
                startActivity(i);
                break;

            case R.id.Logout:
                firebaseAuth.signOut();
                i = new Intent(ProfileUserActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
                break;

            case R.id.PrivateMessage:
                i = new Intent(ProfileUserActivity.this, ListOfTeachersActivity.class);
                startActivity(i);
                break;

            case R.id.GlobalComment:
                i = new Intent(ProfileUserActivity.this, GlobalComment.class);
                startActivity(i);
                break;

            default:
                break;
        }
    }
}