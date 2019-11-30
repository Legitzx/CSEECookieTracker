package com.legitzxdevelopment.cookietracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
    public User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();


        // Widgets
        Button loginButton = findViewById(R.id.loginButton);

        // Listeners
        loginButton.setOnClickListener(new View.OnClickListener() { // Login Handler
            public void onClick(View v) {
                TextView username = findViewById(R.id.username);
                TextView password = findViewById(R.id.password);

                final String usernameStr = username.getText().toString(); // Get users submitted username
                final String passwordStr = password.getText().toString(); // Gets users submitted password

                password.setText(""); // Removes the password after the login button is pressed (security)

                // Firebase
                final DatabaseReference myRef = database.getReference();


                myRef.child("User")
                        .addListenerForSingleValueEvent(new ValueEventListener() { // This will iterate through each child of User
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                boolean check = false;
                                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String username = snapshot.child("username").getValue().toString(); // Grabs `username` from the database
                                    String password = snapshot.child("password").getValue().toString(); // Grabs `password` from the database
                                    String isAdmin = snapshot.child("admin").getValue().toString(); // Grabs `admin` from the databse


                                    if(username.equals(usernameStr) && password.equals(passwordStr)) { // Check to see if a user with this username/password combo exists
                                        check = true;
                                        user = snapshot.getValue(User.class);

                                        Toast.makeText(MainActivity.this, "Welcome back, " + user.getName() + "!", Toast.LENGTH_SHORT).show();

                                        if(isAdmin.equals("true")) { // Check to see if this user is a teacher
                                            // Launch admin panel
                                            openAdminMenu();
                                        } else { // Regular student
                                            // Launch regular panel
                                            openStudentMenu();
                                        }
                                    }
                                }
                                if(!check) {
                                    Toast.makeText(MainActivity.this, "Wrong username or password", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
        });
    }

    public void openAdminMenu() {
        Intent intent = new Intent(this, AdminMainActivity.class);
        intent.putExtra("User", user);
        startActivity(intent);
    }

    public void openStudentMenu() {
        Intent intent = new Intent(this, StudentMainActivity.class);
        intent.putExtra("User", user);
        startActivity(intent);
    }

}

