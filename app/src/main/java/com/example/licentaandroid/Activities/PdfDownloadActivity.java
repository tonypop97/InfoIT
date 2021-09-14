package com.example.licentaandroid.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.TextView;

import com.example.licentaandroid.Adapters.DownloadAdapter;
import com.example.licentaandroid.Adapters.HelperAdapter;
import com.example.licentaandroid.JavaClasses.FetchData;
import com.example.licentaandroid.JavaClasses.Uploads;
import com.example.licentaandroid.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PdfDownloadActivity extends AppCompatActivity {


    DatabaseReference df;
    RecyclerView recyclerView;
    ArrayList<Uploads> listuploads = new ArrayList<>();
    DownloadAdapter downloadAdapter;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_download);

        listuploads = new ArrayList<>();

        recyclerView = findViewById(R.id.pdflist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        intent = getIntent();
        final String id = intent.getStringExtra("id");

        df = FirebaseDatabase.getInstance().getReference().child("Uploads");
        df.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listuploads.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Uploads uploads = snap.getValue(Uploads.class);
                    assert uploads != null;

                    if (uploads.getIdUsers().equals(id)) {
                        listuploads.add(uploads);
                    }
                }

                downloadAdapter = new DownloadAdapter(PdfDownloadActivity.this, listuploads);
                recyclerView.setAdapter(downloadAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}