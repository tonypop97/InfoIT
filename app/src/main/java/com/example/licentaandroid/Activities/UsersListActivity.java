package com.example.licentaandroid.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.licentaandroid.Adapters.DownloadAdapter;
import com.example.licentaandroid.Adapters.HelperAdapter;
import com.example.licentaandroid.Adapters.ListUsersAdapter;
import com.example.licentaandroid.Adapters.UserAdapter;
import com.example.licentaandroid.Adapters.UsersAdapter;
import com.example.licentaandroid.JavaClasses.Uploads;
import com.example.licentaandroid.JavaClasses.Users;
import com.example.licentaandroid.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class UsersListActivity extends AppCompatActivity {
    DatabaseReference df;
    RecyclerView recyclerView;

    private ListUsersAdapter listUsersAdapter;
    private List<Users> mUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        recyclerView = findViewById(R.id.userslist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mUsers = new ArrayList<>();

        readUsers();

    }

    private void readUsers() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot datasnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                    Users users = snapshot.getValue(Users.class);

                    assert users != null;
                    assert firebaseUser != null;
                    if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(firebaseUser.getUid())) {
                        mUsers.add(users);
                    }
                }

                listUsersAdapter = new ListUsersAdapter(getBaseContext(), mUsers);
                recyclerView.setAdapter(listUsersAdapter);

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

}