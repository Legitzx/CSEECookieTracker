package com.legitzxdevelopment.cookietracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class AdminMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        User user = (User) getIntent().getSerializableExtra("User");

        final Button addStudent = findViewById(R.id.addStudentButton);
        TextView welcome = findViewById(R.id.adminWelcomeTextView);

        welcome.setText("Logged in as: " + user.getName());

        addStudent.setOnClickListener(new View.OnClickListener() { // When add student is pressed
            @Override
            public void onClick(View v) {
                // Send to new activity
                openAddStudent();
            }
        });
    }

    public void openAddStudent() {
        Intent intent = new Intent(this, AdminAddStudentActivity.class);
        startActivity(intent);
    }
}
