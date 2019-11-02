package com.legitzxdevelopment.cookietracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminAddStudentActivity extends AppCompatActivity {

    private long maxid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_student);

        final TextView username = findViewById(R.id.AddStudentUsername);
        final TextView password = findViewById(R.id.AddStudentPassword);
        final TextView name = findViewById(R.id.AddStudentName);
        final TextView grade = findViewById(R.id.AddStudentGrade);
        final TextView addId = findViewById(R.id.AddStudentID);
        final CheckBox isAdmin = findViewById(R.id.AddStudentIsAdmin);
        final Button createStudent = findViewById(R.id.AddStudentCreateUserButton);

        final TextView removeId = findViewById(R.id.RemoveStudentID);
        final CheckBox confirmRemove = findViewById(R.id.removeStudentsVerification);
        final Button removeStudent = findViewById(R.id.RemoveStudentButton);

        // Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference().child("User");

        myRef.addValueEventListener(new ValueEventListener() { // Used to get the last child, then when we finally push to the database, the child will be auto-incremented
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    maxid = (dataSnapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final User user = new User();

        createStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = true;
                if(TextUtils.isEmpty(username.getText().toString())) { // Makes sure the user didnt leave it blank
                    username.setError("Please enter a username!");
                    check = false;
                }
                if(TextUtils.isEmpty(password.getText().toString())) {
                    password.setError("Please enter a password!");
                    check = false;
                }
                if(TextUtils.isEmpty(grade.getText().toString())) {
                    grade.setError("Please enter a grade!");
                    check = false;
                }
                if(TextUtils.isEmpty(addId.getText().toString())) {
                    addId.setError("Please enter a id!");
                    check = false;
                }
                if(TextUtils.isEmpty(name.getText().toString())) {
                    name.setError("Please enter a name!");
                    check = false;
                }
                if(check) { // Runs if all fields are NOT empty
                    user.setAdmin(isAdmin.isChecked());
                    user.setUsername(username.getText().toString());
                    user.setPassword(password.getText().toString());
                    user.setGrade(grade.getText().toString());
                    user.setId(addId.getText().toString());
                    user.setName(name.getText().toString());

                    user.setBoost(0);
                    user.setQuizPass(0);
                    user.setHwPass(0);
                    user.setCookies(0);

                    myRef.child(String.valueOf(maxid + 1)).setValue(user); // max id is for autoincrementing the child
                    Toast.makeText(AdminAddStudentActivity.this, "User Created Successfully", Toast.LENGTH_SHORT).show();

                    username.setText(""); // Resets the inputs for easier handling
                    password.setText("");
                    grade.setText("");
                    addId.setText("");
                    name.setText("");
                }
            }
        });

        removeStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = true;

                if(TextUtils.isEmpty(removeId.getText().toString())) { // Makes sure all info is filled out
                    check = false;
                    removeId.setError("Please enter a id!");
                }
                if(!confirmRemove.isChecked()) { // If the user failed to check the checkbox...
                    confirmRemove.setError("Check this if you want to remove the user");
                    check = false;
                }

                final DatabaseReference reff = database.getReference(); // Created another reference so I can use a different path

                if(check) { // All steps passed -> continue with user deletion
                    reff.child("User")
                            .addListenerForSingleValueEvent(new ValueEventListener() { // This will iterate through each child of User
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        String id = snapshot.child("id").getValue().toString();

                                        if(id.equals(removeId.getText().toString())) {
                                            snapshot.getRef().removeValue();

                                            Toast.makeText(AdminAddStudentActivity.this, "Student removed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                }
            }
        });
    }

    public void deleteStudent(String studentId) {
        DatabaseReference dStudent = FirebaseDatabase.getInstance().getReference("User").child(studentId);

        dStudent.removeValue();

        Toast.makeText(this, "Student removed", Toast.LENGTH_LONG).show();
    }
}
