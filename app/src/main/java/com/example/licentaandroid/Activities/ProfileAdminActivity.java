package com.example.licentaandroid.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.licentaandroid.R;

public class ProfileAdminActivity extends AppCompatActivity implements View.OnClickListener {
    TextView textView;
    private CardView log, change, message, comment, add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_admin);


        textView = findViewById(R.id.username);
        message = findViewById(R.id.PrivateMessage);
        log = findViewById(R.id.Logout);
        change = findViewById(R.id.PassChange);
        add = findViewById(R.id.Add);

        message.setOnClickListener(this);
        log.setOnClickListener(this);
        change.setOnClickListener(this);
        add.setOnClickListener(this);


        textView.setText("Popescu Toni");
    }

    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()) {

            case R.id.Add:
                i = new Intent(ProfileAdminActivity.this, AdminPanelActivity.class);
                startActivity(i);
                break;


            case R.id.PassChange:
                i = new Intent(ProfileAdminActivity.this, ForgotPasswordActivity.class);
                startActivity(i);
                break;

            case R.id.Logout:
                i = new Intent(ProfileAdminActivity.this, LoginActivity.class);
                startActivity(i);
                break;

            case R.id.PrivateMessage:
                i = new Intent(ProfileAdminActivity.this, ViewUsersActivity.class);
                startActivity(i);
                break;



            default:
                break;
        }
    }
}