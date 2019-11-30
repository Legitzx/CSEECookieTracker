package com.legitzxdevelopment.cookietracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class AdminMainActivity extends AppCompatActivity {
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference myRef = database.getReference();

    private boolean accessible = false; // Checks is a user is loaded
    private String currentId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        User user = (User) getIntent().getSerializableExtra("User");

        // Add Student
        final Button addStudent = findViewById(R.id.addStudentButton);

        // Add/Remove Cookies
        final EditText studentId = findViewById(R.id.studentId);
        final Button getStudent = findViewById(R.id.studentGet);


        final FloatingActionButton addCookies = findViewById(R.id.addCookiesAdminMain);
        final FloatingActionButton removeCookies = findViewById(R.id.removeCookies);


        final FloatingActionButton addHomework = findViewById(R.id.addHomework);
        final FloatingActionButton removeHomework = findViewById(R.id.removeHomework);


        final FloatingActionButton addQuiz = findViewById(R.id.addQuiz);
        final FloatingActionButton removeQuiz = findViewById(R.id.removeQuiz);

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

        getStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = true;
                if(TextUtils.isEmpty(studentId.getText().toString())) {
                    studentId.setError("Please enter an id");
                    check = false;
                }

                if(check) {
                    getStudent(studentId.getText().toString());
                }
            }
        });

        // FloatingButtons

        addCookies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(accessible) {
                    editCookies(1, currentId, true);
                }
            }
        });

        removeCookies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(accessible) {
                    editCookies(1, currentId, false);
                }
            }
        });

        addHomework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(accessible) {
                    editHomework(1, currentId, true);
                }
            }
        });

        removeHomework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(accessible) {
                    editHomework(1, currentId, false);
                }
            }
        });

        addQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(accessible) {
                    editQuiz(1, currentId, true);
                }
            }
        });

        removeQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(accessible) {
                    editQuiz(1, currentId, false);
                }
            }
        });

    }

    public void getStudent(final String realId) {
        final TextView amountCookies = findViewById(R.id.amountCookies);
        final TextView amountHomework = findViewById(R.id.amountHomework);
        final TextView amountQuiz = findViewById(R.id.amountQuiz);
        final ImageView getStudentVisual = findViewById(R.id.getStudentVisual);

        myRef.child("User")
                .addListenerForSingleValueEvent(new ValueEventListener() { // This will iterate through each child of User
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String id = snapshot.child("id").getValue().toString(); // Grabs `username` from the database
                            String cookies = snapshot.child("cookies").getValue().toString();
                            String hw = snapshot.child("hwPass").getValue().toString();
                            String quiz = snapshot.child("quizPass").getValue().toString();


                            if(id.equals(realId)) { // Check to see if a user with this username/password combo exists
                                getStudentVisual.setImageResource(R.drawable.checkmark);
                                amountCookies.setText("Cookies: " + cookies);
                                amountHomework.setText("HW Passes: " + hw);
                                amountQuiz.setText("Quiz Passes: " + quiz);
                                accessible = true; // We are ready to add/remove

                                currentId = realId;
                                break;
                            }

                            Toast.makeText(AdminMainActivity.this, "Student with the id of: " + realId + " does not exist!", Toast.LENGTH_SHORT).show();
                            getStudentVisual.setImageResource(R.drawable.cross);
                            amountCookies.setText("Cookies: " + "");
                            amountHomework.setText("HW Passes: " + "");
                            amountQuiz.setText("Quiz Passes: " + "");
                            accessible = false; // Not ready to add/remove
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void editCookies(final int numCookies, final String realId, final boolean addCookies) {
        final TextView amountCookies = findViewById(R.id.amountCookies);

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

                                    amountCookies.setText("Cookies: " + totalCookies);

                                    snapshot.getRef().child("cookies").setValue(totalCookies);
                                    Toast.makeText(AdminMainActivity.this, "Success: Added " + numCookies + " cookie(s) to " + name, Toast.LENGTH_SHORT).show();
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

                                    amountCookies.setText("Cookies: " + totalCookies);

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

    public void editHomework(final int numHomework, final String realId, final boolean addHomework) {
        final TextView amountHomework = findViewById(R.id.amountHomework);

        if(addHomework) { // Add Homework passes from id
            // Grab amount of homework passes from user
            // Then we will parseInt and add numHomework to it
            // finally we will update the value

            myRef.child("User")
                    .addListenerForSingleValueEvent(new ValueEventListener() { // This will iterate through each child of User
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            boolean check = false;
                            int totalHomework = 0;
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String id = snapshot.child("id").getValue().toString();
                                int oldHomework = Integer.parseInt(snapshot.child("hwPass").getValue().toString());
                                String name = snapshot.child("name").getValue().toString();

                                if(id.equals(realId)) {
                                    totalHomework = oldHomework + numHomework;

                                    amountHomework.setText("HW Passes: " + totalHomework);

                                    snapshot.getRef().child("hwPass").setValue(totalHomework);
                                    Toast.makeText(AdminMainActivity.this, "Success: Added " + numHomework + " homework pass(es) to " + name, Toast.LENGTH_SHORT).show();
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

        } else { // Remove Homework
            myRef.child("User")
                    .addListenerForSingleValueEvent(new ValueEventListener() { // This will iterate through each child of User
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            boolean check = false;
                            int totalHomework = 0;
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String id = snapshot.child("id").getValue().toString();
                                int oldHomework = Integer.parseInt(snapshot.child("hwPass").getValue().toString());
                                String name = snapshot.child("name").getValue().toString();

                                if(id.equals(realId)) {
                                    totalHomework = oldHomework - numHomework;

                                    amountHomework.setText("HW Passes: " + totalHomework);

                                    snapshot.getRef().child("hwPass").setValue(totalHomework);
                                    Toast.makeText(AdminMainActivity.this, "Success: Removed " + numHomework + " homework pass(es) from " + name, Toast.LENGTH_SHORT).show();
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

    public void editQuiz(final int numQuiz, final String realId, final boolean addQuiz) {
        final TextView amountQuiz = findViewById(R.id.amountQuiz);

        if(addQuiz) { // Add Homework passes from id
            // Grab amount of homework passes from user
            // Then we will parseInt and add numHomework to it
            // finally we will update the value

            myRef.child("User")
                    .addListenerForSingleValueEvent(new ValueEventListener() { // This will iterate through each child of User
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            boolean check = false;
                            int totalQuiz = 0;
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String id = snapshot.child("id").getValue().toString();
                                int oldHomework = Integer.parseInt(snapshot.child("quizPass").getValue().toString());
                                String name = snapshot.child("name").getValue().toString();

                                if(id.equals(realId)) {
                                    totalQuiz = oldHomework + numQuiz;

                                    amountQuiz.setText("Quiz Passes: " + totalQuiz);

                                    snapshot.getRef().child("quizPass").setValue(totalQuiz);
                                    Toast.makeText(AdminMainActivity.this, "Success: Added " + numQuiz + " quiz pass(es) to " + name, Toast.LENGTH_SHORT).show();
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

        } else { // Remove Homework
            myRef.child("User")
                    .addListenerForSingleValueEvent(new ValueEventListener() { // This will iterate through each child of User
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            boolean check = false;
                            int totalQuiz = 0;
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String id = snapshot.child("id").getValue().toString();
                                int oldHomework = Integer.parseInt(snapshot.child("quizPass").getValue().toString());
                                String name = snapshot.child("name").getValue().toString();

                                if(id.equals(realId)) {
                                    totalQuiz = oldHomework - numQuiz;

                                    amountQuiz.setText("Quiz Passes: " + totalQuiz);

                                    snapshot.getRef().child("quizPass").setValue(totalQuiz);
                                    Toast.makeText(AdminMainActivity.this, "Success: Removed " + numQuiz + " quiz pass(es) from " + name, Toast.LENGTH_SHORT).show();
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
