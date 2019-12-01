package com.legitzxdevelopment.cookietracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentDonate extends AppCompatActivity {
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference myRef = database.getReference();

    private String id = "";
    private int donorCookies = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_donate);

        final User user = (User) getIntent().getSerializableExtra("User");
        id = user.getId();

        final EditText idRecipient = findViewById(R.id.StudentDonateEditText);
        final TextView cookiesAmount = findViewById(R.id.StudentDonateCookiesAmount);
        ImageView plus = findViewById(R.id.StudentDonatePlus);
        ImageView mins = findViewById(R.id.StudentDonateMinus);
        ImageView backArrow = findViewById(R.id.StudentDonateBackArrow);
        Button donate = findViewById(R.id.StudentDonateButton);

        // Intial
        cookiesAmount.setText("Cookies: " + donorCookies);

        // Listeners
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adjustTextView(true, cookiesAmount);
            }
        });

        mins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adjustTextView(false, cookiesAmount);
            }
        });

        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(id.equals(idRecipient.getText().toString()))) {
                    cookiesHandler(idRecipient.getText().toString());
                } else {
                    Toast.makeText(StudentDonate.this, "You cannot donate to yourself", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStudentMain(user);
            }
        });
    }

    public void adjustTextView(boolean add, TextView tv) {
        if(add) {
            if(!(donorCookies > 49)) {
                donorCookies = donorCookies + 1;
                tv.setText("Cookies: " + donorCookies);
            }
        } else {
            if(donorCookies > 1) {
                donorCookies  = donorCookies - 1;
                tv.setText("Cookies: " + donorCookies);
            }
        }
    }

    public void cookiesHandler(final String recipientId) {
        // This method will make sure the transaction is legit ( IN SHORT: Makes sure the donor has enough cookies, if so -> proceeds to give cookies )

        myRef.child("User") // Checks to see if the recipient exists
                .addListenerForSingleValueEvent(new ValueEventListener() { // This will iterate through each child of User
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean check = false;
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String userId = snapshot.child("id").getValue().toString();

                            if(userId.equals(recipientId)) {
                                check = true;
                                n1(recipientId);
                                break;
                            }
                        }
                        if(!check) {
                            Toast.makeText(StudentDonate.this, "Student with the id of " + recipientId + " does not exist!", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void n1(final String recipientId) {
        myRef.child("User") // Checks to see if the donor has sufficient cookies
                .addListenerForSingleValueEvent(new ValueEventListener() { // This will iterate through each child of User
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean check = false;
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String userId = snapshot.child("id").getValue().toString();
                            int totalCookies = Integer.parseInt(snapshot.child("cookies").getValue().toString());

                            if(userId.equals(id)) {
                                if(donorCookies <= totalCookies) {
                                    check = true;
                                    int newCookies = totalCookies - donorCookies;
                                    snapshot.getRef().child("cookies").setValue(newCookies);
                                    n2(recipientId);
                                }
                                break;
                            }
                        }
                        if(!check) {
                            Toast.makeText(StudentDonate.this, "You do not have enough cookies!", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void n2(final String recipientId) {
        myRef.child("User") // Add Cookies to recipient
                .addListenerForSingleValueEvent(new ValueEventListener() { // This will iterate through each child of User
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String userId = snapshot.child("id").getValue().toString();
                            int totalCookies = Integer.parseInt(snapshot.child("cookies").getValue().toString());

                            if(userId.equals(recipientId)) {
                                int newCookies = totalCookies + donorCookies;
                                snapshot.getRef().child("cookies").setValue(newCookies);
                                Toast.makeText(StudentDonate.this, "Success!", Toast.LENGTH_SHORT).show();
                                System.out.println("BLAHBLAHBLAH");
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void openStudentMain(User user) {
        Intent intent = new Intent(this, StudentMainActivity.class);
        intent.putExtra("User", user);
        startActivity(intent);
    }
}
