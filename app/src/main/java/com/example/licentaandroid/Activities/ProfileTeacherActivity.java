package com.example.licentaandroid.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import com.example.licentaandroid.JavaClasses.Teachers;
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
import com.google.firebase.firestore.FirebaseFirestore;
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

public class ProfileTeacherActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RegisterActivity";
    private TextView Username;
    private CircleImageView circleImageView;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private ImagesRecyclerAdapter imagesRecyclerAdapter;
    private DatabaseReference databaseReference;
    private List<ImagesList> imagesList;
    private static final int IMAGE_REQUEST = 1;
    private StorageTask storageTask;
    private Uri imageUri;
    private StorageReference storageReference;
    private Teachers teachers;
    private FirebaseFirestore fstore;


    private CardView view, log, change, message, comment, pdfdw, add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_teacher);

        log = findViewById(R.id.Logout);
        change = findViewById(R.id.PassChange);
        message = findViewById(R.id.PrivateMessage);
        comment = findViewById(R.id.GlobalComment);
        change = findViewById(R.id.PassChange);
        view = findViewById(R.id.ListView);
        add = findViewById(R.id.Add);
        pdfdw = findViewById(R.id.Pdfdw);

        message.setOnClickListener(this);
        comment.setOnClickListener(this);
        log.setOnClickListener(this);
        change.setOnClickListener(this);
        add.setOnClickListener(this);
        view.setOnClickListener(this);
        pdfdw.setOnClickListener(this);

        Username = findViewById(R.id.username);
        circleImageView = findViewById(R.id.profileImage);
        imagesList = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        fstore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("profile_images");
        databaseReference = FirebaseDatabase.getInstance().getReference("Teachers").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                teachers = dataSnapshot.getValue(Teachers.class);
                assert teachers != null;
                Username.setText(teachers.getFullname());

                if (teachers.getImageUrl().equals("null")) {
                    circleImageView.setImageResource(R.drawable.ic_launcher_background);
                } else {
                    Glide.with(getApplicationContext()).load(teachers.getImageUrl()).into(circleImageView);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileTeacherActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileTeacherActivity.this);
                builder.setCancelable(true);
                View mView = LayoutInflater.from(ProfileTeacherActivity.this).inflate(R.layout.select_image, null);
                RecyclerView recyclerView = mView.findViewById(R.id.recyclerView);
                collectOldImage();
                recyclerView.setLayoutManager(new GridLayoutManager(ProfileTeacherActivity.this, 3));
                recyclerView.setHasFixedSize(true);
                imagesRecyclerAdapter = new ImagesRecyclerAdapter(imagesList, ProfileTeacherActivity.this);
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
                AlertDialog alertDialog = builder.create();
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
                        teachers = task.getResult().getValue(Teachers.class);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        finalBitmap.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream);
                        byte[] imageFileToByte = byteArrayOutputStream.toByteArray();
                        final StorageReference imageReference = storageReference.child(teachers.getFullname() + System.currentTimeMillis() + ".jpg");
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
                                    Toast.makeText(ProfileTeacherActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            }

                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ProfileTeacherActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
            });
        }
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
                Toast.makeText(ProfileTeacherActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
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

    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()) {
            case R.id.ListView:
                i = new Intent(ProfileTeacherActivity.this, ListCoursesTeacherActivity.class);
                startActivity(i);
                break;

            case R.id.PassChange:
                i = new Intent(ProfileTeacherActivity.this, ForgotPasswordActivity.class);
                startActivity(i);
                break;

            case R.id.Logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
                break;

            case R.id.Add:
                i = new Intent(ProfileTeacherActivity.this, AddCourseActivity.class);
                startActivity(i);
                break;


            case R.id.PrivateMessage:
                i = new Intent(ProfileTeacherActivity.this, ListOfUsersActivity.class);
                startActivity(i);
                break;

            case R.id.GlobalComment:
                i = new Intent(ProfileTeacherActivity.this, GlobalComment.class);
                startActivity(i);
                break;

            case R.id.Pdfdw:
                i = new Intent(ProfileTeacherActivity.this, UsersListActivity.class);
                startActivity(i);
                break;

            default:
                break;
        }
    }
}