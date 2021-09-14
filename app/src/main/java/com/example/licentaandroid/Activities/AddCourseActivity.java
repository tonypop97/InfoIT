package com.example.licentaandroid.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.licentaandroid.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class AddCourseActivity extends AppCompatActivity {
    private EditText title, text, course, homework;
    private Button add, cancel, view;
    private FirebaseAuth id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);


        id = FirebaseAuth.getInstance();


        course = findViewById(R.id.course);
        title = findViewById(R.id.title);
        text = findViewById(R.id.text);
        homework = findViewById(R.id.homework);
        add = findViewById(R.id.Select);
        cancel = findViewById(R.id.Cancel);
        view = findViewById(R.id.View);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(AddCourseActivity.this, ListCoursesTeacherActivity.class);
                startActivity(i);
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddCourseActivity.this, ProfileTeacherActivity.class);
                startActivity(i);
            }
        });
    }


    public void insert() {
        Map<String, Object> map = new HashMap<>();
        map.put("title", title.getText().toString());
        map.put("text", text.getText().toString());
        map.put("homework", homework.getText().toString());
        map.put("id", id.getCurrentUser().getUid());
        FirebaseDatabase.getInstance().getReference().child("Courses").push()
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        title.setText("");
                        text.setText("");
                        homework.setText("");
                        Toast.makeText(AddCourseActivity.this, "Inserted Successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddCourseActivity.this, "Couldn't insert ", Toast.LENGTH_SHORT).show();
            }
        });
    }


}