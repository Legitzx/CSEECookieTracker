package com.legitzxdevelopment.cookietracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentMarketplace extends AppCompatActivity {
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference myRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_marketplace);

        final User user = (User) getIntent().getSerializableExtra("User");

        CardView examCardView = findViewById(R.id.examCardView);
        CardView homeworkCardView = findViewById(R.id.homeworkCardView);
        CardView quizCardView = findViewById(R.id.quizCardView);

        examCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler(1, 1, user);
            }
        });

        homeworkCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler(3, 2, user);
            }
        });

        quizCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler(5, 3, user);
            }
        });
    }

    public void handler(final int price, final int item, final User user) {
        /*
         - Exam Boost: item 1
         - Homework Pass: item 2
          - Quiz Pass: item 3
         */

        myRef.child("User") // Checks to see if the recipient exists
                .addListenerForSingleValueEvent(new ValueEventListener() { // This will iterate through each child of User
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean check = false;
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String userId = snapshot.child("id").getValue().toString();

                            int cookies = Integer.parseInt(snapshot.child("cookies").getValue().toString());
                            int boost = Integer.parseInt(snapshot.child("boost").getValue().toString());
                            int hwPass = Integer.parseInt(snapshot.child("hwPass").getValue().toString());
                            int quizPass = Integer.parseInt(snapshot.child("quizPass").getValue().toString());

                            if(userId.equals(user.getId())) {
                                if(price <= cookies) {
                                    int newCookies = 0;
                                    switch (item) {
                                        case 1: // Exam Boost
                                            //s
                                            newCookies = cookies - price;

                                            snapshot.getRef().child("cookies").setValue(newCookies);
                                            snapshot.getRef().child("boost").setValue((boost + 1));

                                            Toast.makeText(StudentMarketplace.this, "You now have " + (boost + 1) + "% boost on the next exam and a total of " + newCookies + " cookies.", Toast.LENGTH_LONG).show();
                                            check = true;
                                            break;
                                        case 2: // Homework pass
                                            //s
                                            newCookies = cookies - price;

                                            snapshot.getRef().child("cookies").setValue(newCookies);
                                            snapshot.getRef().child("hwPass").setValue((hwPass + 1));

                                            Toast.makeText(StudentMarketplace.this, "You now have " + (hwPass + 1) + " homework passes and a total of " + newCookies + " cookies.", Toast.LENGTH_LONG).show();
                                            check = true;
                                            break;
                                        case 3:
                                            // Quiz pass
                                            newCookies = cookies - price;

                                            snapshot.getRef().child("cookies").setValue(newCookies);
                                            snapshot.getRef().child("quizPass").setValue((quizPass + 1));

                                            Toast.makeText(StudentMarketplace.this, "You now have " + (quizPass + 1) + " quiz passes and a total of " + newCookies + " cookies.", Toast.LENGTH_LONG).show();
                                            check = true;
                                            break;
                                    }
                                }
                                break;
                            }
                        }
                        if(!check) {
                            Toast.makeText(StudentMarketplace.this, "You do not have enough cookies!", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



    }
}
