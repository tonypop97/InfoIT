package com.example.licentaandroid.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.licentaandroid.JavaClasses.FetchData;
import com.example.licentaandroid.Adapters.HelperAdapter;
import com.example.licentaandroid.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CourseActivity extends AppCompatActivity {
    private List<FetchData> fetchData;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        TextView title = findViewById(R.id.title);
        TextView text = findViewById(R.id.text);
        TextView home = findViewById(R.id.Home);
        fetchData = new ArrayList<>();


        String Title = "Title is not set";
        String Text = "Text is not set";
        String Home = "Text";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Title = extras.getString("title");
            Text = extras.getString("text");
            Home = extras.getString("homework");
        }
        title.setText(Title);
        text.setText(Text);
        home.setText(Home);


        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CourseActivity.this, AssigmentActivity.class));
            }


        });

    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                Intent i = new Intent(this, AssigmentActivity.class);
                break;

        }

    }


}