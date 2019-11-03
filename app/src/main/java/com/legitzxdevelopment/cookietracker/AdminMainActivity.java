package com.legitzxdevelopment.cookietracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class AdminMainActivity extends AppCompatActivity {
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference myRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        User user = (User) getIntent().getSerializableExtra("User");

        // Firebase


        // Add Student
        final Button addStudent = findViewById(R.id.addStudentButton);

        // Add/Remove Cookies
        final TextView studentId = findViewById(R.id.studentIdAdmin);
        final TextView numCookies = findViewById(R.id.numberCookiesAdmin); // Dont mix up with numCookiesAdmin
        final Button addCookies = findViewById(R.id.addCookiesButton);
        final Button removeCookies = findViewById(R.id.removeCookiesButton);

        // Welcome message at bottom left
        TextView welcome = findViewById(R.id.adminWelcomeTextView);
        welcome.setText("Logged in as: " + user.getName());

        // Listeners
        addStudent.setOnClickListener(new View.OnClickListener() { // When add student is pressed
            @Override
            public void onClick(View v) {
                // Send to new activity
                openAddStudent();
            }
        });

        addCookies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = true;
                if(TextUtils.isEmpty(studentId.getText().toString())) {
                    studentId.setError("Please enter an id");
                    check = false;
                }
                if(TextUtils.isEmpty(numCookies.getText().toString())) {
                    numCookies.setError("Please enter how many cookies");
                    check = false;
                }

                final int cookies = Integer.parseInt(numCookies.getText().toString());
                if(check) {
                    editCookies(cookies, studentId.getText().toString(), true);
                }
            }
        });

        removeCookies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = true;
                if(TextUtils.isEmpty(studentId.getText().toString())) {
                    studentId.setError("Please enter an id");
                    check = false;
                }
                if(TextUtils.isEmpty(numCookies.getText().toString())) {
                    numCookies.setError("Please enter how many cookies");
                    check = false;
                }
                if(check) {
                    editCookies(Integer.parseInt(numCookies.getText().toString()), studentId.getText().toString(), false);
                }
            }
        });
    }

    public void editCookies(final int numCookies, final String realId, final boolean addCookies) {
        if(addCookies) { // Add Cookies from id
            // Grab amount of cookies from user
            // Then we will parseInt and add numCookies to it
            // finally we will update the value


            myRef.child("User")
                    .addListenerForSingleValueEvent(new ValueEventListener() { // This will iterate through each child of User
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            boolean check = false;
                            int totalCookies = 0;
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String id = snapshot.child("id").getValue().toString();
                                int oldCookies = Integer.parseInt(snapshot.child("cookies").getValue().toString());
                                String name = snapshot.child("name").getValue().toString();

                                if(id.equals(realId)) {
                                    totalCookies = oldCookies + numCookies;

                                    snapshot.getRef().child("cookies").setValue(totalCookies);
                                    Toast.makeText(AdminMainActivity.this, "Success: Added " + numCookies + " cookie(s) from " + name, Toast.LENGTH_SHORT).show();
                                    check = true;
                                    break;
                                }
                            }

                            if(!check) {
                                Toast.makeText(AdminMainActivity.this, "Student not found", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

        } else { // Remove Cookies
            myRef.child("User")
                    .addListenerForSingleValueEvent(new ValueEventListener() { // This will iterate through each child of User
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            boolean check = false;
                            int totalCookies = 0;
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String id = snapshot.child("id").getValue().toString();
                                int oldCookies = Integer.parseInt(snapshot.child("cookies").getValue().toString());
                                String name = snapshot.child("name").getValue().toString();

                                if(id.equals(realId)) {
                                    totalCookies = oldCookies - numCookies;

                                    snapshot.getRef().child("cookies").setValue(totalCookies);
                                    Toast.makeText(AdminMainActivity.this, "Success: Removed " + numCookies + " cookie(s) from " + name, Toast.LENGTH_SHORT).show();
                                    check = true;
                                    break;
                                }
                            }

                            if(!check) {
                                Toast.makeText(AdminMainActivity.this, "Student not found", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
    }

    public void openAddStudent() {
        Intent intent = new Intent(this, AdminAddStudentActivity.class);
        startActivity(intent);
    }
}
