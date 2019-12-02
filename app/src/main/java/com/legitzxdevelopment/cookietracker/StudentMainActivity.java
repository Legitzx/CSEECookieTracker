package com.legitzxdevelopment.cookietracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


public class StudentMainActivity extends AppCompatActivity {
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference myRef = database.getReference();

    private String realId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        ImageView refresh = findViewById(R.id.refresh);

        TextView name = findViewById(R.id.tv_name);
        TextView gradeLevel = findViewById(R.id.tv_address);
        TextView cookiesNumber = findViewById(R.id.studentMainCookies);
        TextView examBoost = findViewById(R.id.studentMainExamBoost);
        TextView homeworkPass = findViewById(R.id.studentMainHomeworkPasses);
        TextView quizPass = findViewById(R.id.studentMainQuizPasses);

        Button goToStudentDonate = findViewById(R.id.donateButton);
        Button goToStudentMarketplace = findViewById(R.id.marketplaceButton);

        final User user = (User) getIntent().getSerializableExtra("User");

        // Intial Profile Fetch - Was going to make this a function BUT had issues
        name.setText(user.getName());
        gradeLevel.setText("Grade: " + user.getGrade() + "th");
        cookiesNumber.setText(String.valueOf(user.getCookies()));
        examBoost.setText(user.getBoost() + "%");
        homeworkPass.setText(String.valueOf(user.getHwPass()));
        quizPass.setText(String.valueOf(user.getQuizPass()));
        calculateClassRank(String.valueOf(user.getGrade()), String.valueOf(user.getCookies()));
        realId = user.getId();

        // Refresh Button
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileFetch();
                calculateClassRank(String.valueOf(user.getGrade()), String.valueOf(user.getCookies()));
            }
        });

        // Go To StudentDonate
        goToStudentDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStudentDonate(user);
            }
        });

        // Go to StudentMarketplace
        goToStudentMarketplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStudentMarketplace(user);
            }
        });
    }

    public void openStudentDonate(User user) {
        Intent intent = new Intent(this, StudentDonate.class);
        intent.putExtra("User", user);
        startActivity(intent);
    }

    public void openStudentMarketplace(User user) {
        Intent intent = new Intent(this, StudentMarketplace.class);
        intent.putExtra("User", user);
        startActivity(intent);
    }


    public void profileFetch() {
        final TextView nameTextView = findViewById(R.id.tv_name);
        final TextView gradeLevelTextView = findViewById(R.id.tv_address);
        final TextView cookiesTextView = findViewById(R.id.studentMainCookies);
        final TextView boostTextView = findViewById(R.id.studentMainExamBoost);
        final TextView hwTextView = findViewById(R.id.studentMainHomeworkPasses);
        final TextView quizTextView = findViewById(R.id.studentMainQuizPasses);

        myRef.child("User")
                .addListenerForSingleValueEvent(new ValueEventListener() { // This will iterate through each child of User
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String id = snapshot.child("id").getValue().toString();
                            String name = snapshot.child("name").getValue().toString();
                            String gradeLevel = snapshot.child("grade").getValue().toString();
                            String cookies = snapshot.child("cookies").getValue().toString();
                            String boost = snapshot.child("boost").getValue().toString();
                            String hw = snapshot.child("hwPass").getValue().toString();
                            String quiz = snapshot.child("quizPass").getValue().toString();

                            if(id.equals(realId)) {
                                nameTextView.setText(name);
                                gradeLevelTextView.setText("Grade: " + gradeLevel + "th");
                                cookiesTextView.setText(cookies);
                                boostTextView.setText(boost + "%");
                                hwTextView.setText(hw);
                                quizTextView.setText(quiz);
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void calculateClassRank(final String grade, final String thisCookies) {
        final TextView classRankTextView = findViewById(R.id.studentMainClassRank);

        myRef.child("User")
                .addListenerForSingleValueEvent(new ValueEventListener() { // This will iterate through each child of User
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<Integer> arrayList = new ArrayList<>();
                        int rank = 0;
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String cookies = snapshot.child("cookies").getValue().toString();
                            String gradeLevel = snapshot.child("grade").getValue().toString();

                            if(grade.equals(gradeLevel)) {
                                arrayList.add(Integer.parseInt(cookies));
                            }
                        }

                        Collections.sort(arrayList);
                        Collections.reverse(arrayList);

                        System.out.println(arrayList);

                        for(int x = 0; x < arrayList.size(); x++) {
                            if(arrayList.get(x) == Integer.parseInt(thisCookies)) {
                                rank = (x + 1);
                                break;
                            }
                        }

                        final int total = arrayList.size();

                        if(rank == 1) {
                            classRankTextView.setText(rank + "st place out of " + total + " Students");
                        }
                        if(rank == 2) {
                            classRankTextView.setText(rank + "nd place out of " + total + " Students");
                        }
                        if(rank == 3) {
                            classRankTextView.setText(rank + "rd place out of " + total + " Students");
                        }
                        if(rank > 3) {
                            classRankTextView.setText(rank + "th place out of " + total + " Students");
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

}
