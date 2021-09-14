package com.example.licentaandroid.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.example.licentaandroid.Adapters.HelperAdapter;
import com.example.licentaandroid.JavaClasses.FetchData;
import com.example.licentaandroid.R;
import com.example.licentaandroid.Adapters.myadapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {
    private List<FetchData> fetchData;
    private RecyclerView recyclerView;
    private HelperAdapter helperAdapter;
    private com.example.licentaandroid.Adapters.myadapter myadapter;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private HelperAdapter.RecyclerViewClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fetchData = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Courses");
        setOnClickListener();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    FetchData data = ds.getValue(FetchData.class);
                    fetchData.add(data);
                }
                helperAdapter = new HelperAdapter(fetchData, listener);
                recyclerView.setAdapter(helperAdapter);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void setOnClickListener() {
        listener = new HelperAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent i = new Intent(MenuActivity.this, CourseActivity.class);
                i.putExtra("title", fetchData.get(position).getTitle());
                i.putExtra("text", fetchData.get(position).getText());
                i.putExtra("homework", fetchData.get(position).getHomework());
                startActivity(i);
            }
        };

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

        myadapter = new myadapter(options);
        myadapter.startListening();
        recyclerView.setAdapter(myadapter);

    }
}