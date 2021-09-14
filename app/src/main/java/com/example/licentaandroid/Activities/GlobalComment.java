package com.example.licentaandroid.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.licentaandroid.Adapters.CommentAdapter;
import com.example.licentaandroid.JavaClasses.Comment;
import com.example.licentaandroid.JavaClasses.Teachers;
import com.example.licentaandroid.JavaClasses.Users;
import com.example.licentaandroid.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class GlobalComment extends AppCompatActivity {
    RecyclerView RvComment;
    List<Comment> listComment = new ArrayList<>();
    CommentAdapter commentAdapter;
    ImageButton button;

    private Users users;
    private Teachers teachers;
    private CircleImageView circleImageView;
    FirebaseFirestore fstore;
    EditText editText;
    FirebaseStorage storage;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_comment);

        RvComment = findViewById(R.id.rv_comment);

        button = findViewById(R.id.button);

        editText = findViewById(R.id.edittext);
        fstore = FirebaseFirestore.getInstance();

        circleImageView = findViewById(R.id.profileImage);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();


        DocumentReference df = fstore.collection("User").document(Objects.requireNonNull(firebaseAuth.getUid()));
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("isTeacher") != null) {
                    databaseReference = FirebaseDatabase.getInstance().getReference("Teachers").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            teachers = dataSnapshot.getValue(Teachers.class);
                            assert teachers != null;

                            Glide.with(getApplicationContext()).load(teachers.getImageUrl()).into(circleImageView);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(GlobalComment.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                } else {
                    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            users = dataSnapshot.getValue(Users.class);
                            assert users != null;

                            Glide.with(getApplicationContext()).load(users.getImageUrl()).into(circleImageView);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(GlobalComment.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setVisibility(View.VISIBLE);

                DatabaseReference commentReference = firebaseDatabase.getReference().child("Comments").push();
                String comment_content = editText.getText().toString();
                String userID = firebaseUser.getUid();
                String uname = firebaseUser.getDisplayName();
                String uimg = firebaseUser.getPhotoUrl() != null ? firebaseUser.getPhotoUrl().toString() : null;

                Comment comment = new Comment(comment_content, uname, userID, uimg);


                commentReference.setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        editText.setText("");
                        button.setVisibility(View.VISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });


            }
        });


        iniRvComment();
    }

    private void iniRvComment() {
        RvComment.setLayoutManager(new LinearLayoutManager(this));
        RvComment.setHasFixedSize(true);

        DatabaseReference commentRef = firebaseDatabase.getReference().child("Comments");
        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listComment = new ArrayList<>();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Comment comment = snap.getValue(Comment.class);


                    listComment.add(comment);
                }

                commentAdapter = new CommentAdapter(getApplicationContext(), listComment);
                RvComment.setAdapter(commentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}