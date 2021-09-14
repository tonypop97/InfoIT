package com.example.licentaandroid.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.licentaandroid.JavaClasses.Comment;
import com.example.licentaandroid.Adapters.CommentAdapter;
import com.example.licentaandroid.JavaClasses.Uploads;
import com.example.licentaandroid.R;
import com.example.licentaandroid.JavaClasses.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AssigmentActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 234;
    private static final String TAG = "RegisterActivity";
    TextView text;
    EditText editText;
    Button select, upload;

    FirebaseStorage storage;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    Uri pdfuri;
    private DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    private Uri filepath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assigment);


        editText = findViewById(R.id.edittext);
        select = findViewById(R.id.Select);
        upload = findViewById(R.id.Upload);
        text = findViewById(R.id.Text);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();


        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPdf();
            }


        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pdfuri != null)
                    uploadFile(pdfuri);
                else
                    Toast.makeText(AssigmentActivity.this, "Select a file", Toast.LENGTH_SHORT).show();

                if (filepath != null)
                    upload();
                else
                    Toast.makeText(AssigmentActivity.this, "Select a file", Toast.LENGTH_SHORT).show();
            }


        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 9 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectPdf();
            //selectImg();
        } else {
            Toast.makeText(AssigmentActivity.this, "Please provide permission..", Toast.LENGTH_SHORT).show();
        }
    }

    private void selectPdf() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 86);
    }


    private void upload() {
        if (filepath != null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading File ..");
            progressDialog.show();
            StorageReference storageReference = storage.getReference();
            StorageReference riversRef = storageReference.child("images/profile.jpg");
            riversRef.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(AssigmentActivity.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), " ", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage(((int) progress) + "% Uploaded ...");
                }
            });
            progressDialog.dismiss();
        }
    }

    private void uploadFile(Uri pdfuri) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading File ..");
        progressDialog.setProgress(0);
        progressDialog.show();

        final String filename = System.currentTimeMillis() + "";
        StorageReference storageReference = storage.getReference();


        storageReference.child("Uploads").child(filename).putFile(pdfuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                DatabaseReference reference = firebaseDatabase.getReference().child("Uploads").push();
                Task<Uri> url = taskSnapshot.getStorage().getDownloadUrl();
                while (!url.isComplete()) ;
                Uri uri = url.getResult();

                String id = firebaseUser.getUid();
                String name = text.getText().toString();
                String email = firebaseUser.getEmail();


                Uploads uploads = new Uploads(id, name, uri.toString(), email);

                reference.setValue(uploads).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AssigmentActivity.this, "File successfully uploaded", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(AssigmentActivity.this, "File not successfully uploaded", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AssigmentActivity.this, "File not successfully uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                int currentProgress = (int) (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(currentProgress);
            }
        });

    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filepath = data.getData();


        }
        if (requestCode == 86 && resultCode == RESULT_OK && data != null) {
            pdfuri = data.getData();
            text.setText(data.getDataString().substring(data.getDataString().lastIndexOf("/") + 1));
        } else {
            Toast.makeText(AssigmentActivity.this, "Please select a file", Toast.LENGTH_SHORT).show();
        }
    }


}