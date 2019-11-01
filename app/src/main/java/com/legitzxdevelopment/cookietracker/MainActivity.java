package com.legitzxdevelopment.cookietracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginButton = (Button) findViewById(R.id.loginButton);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference().child("User");

        user = new User();

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView username = (TextView) findViewById(R.id.username);
                TextView password = (TextView) findViewById(R.id.password);

                String usernameStr = username.getText().toString();
                String passwordStr = password.getText().toString();

                user.setBoost(0);
                user.setGrade("0");
                user.setHwPass(0);
                user.setId("Teacher");
                user.setName("Mr.Sep");
                user.setQuizPass(0);
                user.setCookies(5);
                user.setAdmin(true);

                myRef.push().setValue(user);
                Toast.makeText(MainActivity.this, "Logged in!", Toast.LENGTH_SHORT).show();


            }
        });
    }

}

