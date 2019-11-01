package com.legitzxdevelopment.cookietracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference().child("User").child("1");

        // Widgets
        Button loginButton = (Button) findViewById(R.id.loginButton);
        
        // Listeners
        loginButton.setOnClickListener(new View.OnClickListener() { // Login Handler
            public void onClick(View v) {
                TextView username = (TextView) findViewById(R.id.username);
                TextView password = (TextView) findViewById(R.id.password);

                final String usernameStr = username.getText().toString(); // Get users submitted username
                final String passwordStr = password.getText().toString(); // Gets users submitted password

                // Firebase

                myRef.addValueEventListener(new ValueEventListener() { // Iterates through the database
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) { // Automatic method created
                        String username = dataSnapshot.child("username").getValue().toString(); // Grabs `username` from the database
                        String password = dataSnapshot.child("password").getValue().toString(); // Grabs `password` from the database
                        String isAdmin = dataSnapshot.child("admin").getValue().toString(); // Grabs `admin` from the databse


                        if(username.equals(usernameStr) && password.equals(passwordStr)) { // Check to see if a user with this username/password combo exists
                            Toast.makeText(MainActivity.this, "Logged in!", Toast.LENGTH_SHORT).show();

                            if(isAdmin.equals("true")) { // Check to see if this user is a teacher
                                // Launch admin panel
                            } else { // Regular student
                                // Launch regular panel
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Wrong username or password!", Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

}

