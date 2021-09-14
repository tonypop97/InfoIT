package com.example.licentaandroid.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.example.licentaandroid.JavaClasses.FetchData;
import com.example.licentaandroid.R;
import com.example.licentaandroid.Adapters.myadapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class ListCoursesTeacherActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    myadapter adapter;
    FetchData fetchData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_courses_teacher);

        recyclerView = findViewById(R.id.View);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(fetchData.getId())){
            FirebaseRecyclerOptions<FetchData> options =
                new FirebaseRecyclerOptions.Builder<FetchData>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Courses"), FetchData.class)
                        .build();
            adapter = new myadapter(options);
            recyclerView.setAdapter(adapter);
        }






    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.startListening();
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
        FirebaseRecyclerOptions<FetchData> options =
                new FirebaseRecyclerOptions.Builder<FetchData>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("courses").orderByChild("title").startAt(s).endAt(s + "\uf8ff"), FetchData.class)
                        .build();

        adapter = new myadapter(options);
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }
}