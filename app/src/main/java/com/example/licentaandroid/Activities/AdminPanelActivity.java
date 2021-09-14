package com.example.licentaandroid.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.licentaandroid.R;
import com.example.licentaandroid.JavaClasses.Teachers;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminPanelActivity extends AppCompatActivity {

    EditText fullname, email, password;
    Button btn1, btn2, btn3;
    boolean valid = true;
    FirebaseAuth fauth;
    FirebaseFirestore fstore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        fauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        fullname = findViewById(R.id.course);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        btn1 = findViewById(R.id.Add);
        btn2 = findViewById(R.id.Cancel);
        btn3 = findViewById(R.id.View);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valid) {
                    fauth.createUserWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseUser user = fauth.getCurrentUser();
                            String name = fullname.getText().toString().trim();
                            String id = fauth.getUid();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                            user.updateProfile(profileUpdates);
                            String Email = email.getText().toString().trim();
                            String imageurl = "null";
                            Toast.makeText(AdminPanelActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                            DocumentReference df = fstore.collection("User").document(user.getUid());
                            Map<String, Object> userinfo = new HashMap<>();
                            userinfo.put("FullName", name);
                            userinfo.put("Email", Email);
                            userinfo.put("ImageUrl", imageurl);
                            userinfo.put("ID", id);
                            userinfo.put("isTeacher", "1");

                            df.set(userinfo);

                            Teachers teachers = new Teachers(Email, name, imageurl, id);
                            FirebaseDatabase.getInstance().getReference("Teachers")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(teachers);

                            if (!(password.getText().toString().isEmpty() || Email.isEmpty() || name.isEmpty())) {
                                password.setText("");
                                email.setText("");
                                fullname.setText("");
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AdminPanelActivity.this, "Failed to create account", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ProfileAdminActivity.class));
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ViewUsersActivity.class));
            }
        });

    }


}