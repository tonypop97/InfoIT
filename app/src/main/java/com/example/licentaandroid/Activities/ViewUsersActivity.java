package com.example.licentaandroid.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.example.licentaandroid.Adapters.ViewUsersAdapter;
import com.example.licentaandroid.JavaClasses.Teachers;
import com.example.licentaandroid.JavaClasses.Users;
import com.example.licentaandroid.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class ViewUsersActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ViewUsersAdapter teachersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_teachers);

        recyclerView = findViewById(R.id.View);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Users"), Users.class)
                        .build();


        teachersAdapter = new ViewUsersAdapter(options, getApplicationContext());
        recyclerView.setAdapter(teachersAdapter);
    }


    @Override
    protected void onStart() {
        super.onStart();
        teachersAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        teachersAdapter.startListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.searchmenu, menu);

        MenuItem item = menu.findItem(R.id.search);

        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                processsearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                processsearch(s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    private void processsearch(String s) {
        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("fullname").startAt(s).endAt(s + "\uf8ff"), Users.class)
                        .build();

        teachersAdapter = new ViewUsersAdapter(options, getApplicationContext());
        teachersAdapter.startListening();
        recyclerView.setAdapter(teachersAdapter);

    }
}