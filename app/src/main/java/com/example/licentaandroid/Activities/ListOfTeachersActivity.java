package com.example.licentaandroid.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.licentaandroid.Adapters.TeacherAdapter;
import com.example.licentaandroid.JavaClasses.Teachers;
import com.example.licentaandroid.JavaClasses.Users;
import com.example.licentaandroid.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ListOfTeachersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TeacherAdapter teacherAdapter;
    private List<Teachers> mTeachers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_teachers);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        mTeachers = new ArrayList<>();


        readTeachers();

    }

    private void readTeachers() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Teachers");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot datasnapshot) {
                mTeachers.clear();
                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                    Teachers teachers = snapshot.getValue(Teachers.class);

                    assert teachers != null;
                    assert firebaseUser != null;
                    if (!teachers.getId().equals(firebaseUser.getUid())) {
                        mTeachers.add(teachers);
                    }
                }

                teacherAdapter = new TeacherAdapter(getBaseContext(), mTeachers);
                recyclerView.setAdapter(teacherAdapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }


}