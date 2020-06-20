package com.reload.grandstore.resetPassword;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reload.grandstore.R;
import com.reload.grandstore.home.HomeActivity;
import com.reload.grandstore.settings.SettingsFragment;
import com.reload.grandstore.sharedPerformances.UserSession;
import com.reload.grandstore.signin.SignInActivity;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {

    String mGet_Check;
    TextView mPage_Title, mTitle_Questions;
    EditText mReset_PhoneNum, mReset_Question1, mReset_Question2;
    Button mVerify_Btn;

    UserSession mU_Session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mU_Session = new UserSession(this);

        mGet_Check = getIntent().getStringExtra("check");
        mPage_Title = findViewById(R.id.reset_Page_title);
        mTitle_Questions = findViewById(R.id.title_Questions);
        mReset_PhoneNum = findViewById(R.id.reset_PhoneNumber);
        mReset_Question1 = findViewById(R.id.reset_Question1);
        mReset_Question2 = findViewById(R.id.reset_Question2);
        mVerify_Btn = findViewById(R.id.verify_btn);

        if (mGet_Check.equalsIgnoreCase("settings")) {
            mPage_Title.setText("Set Questions");
            mTitle_Questions.setText("Please Set Answers for the following Security Questions. ");
            mReset_PhoneNum.setVisibility(View.GONE);
            mVerify_Btn.setText("Set");

            displayPreviousAnswers();

            mVerify_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAnswersToFB();
                }
            });


        } else if (mGet_Check.equalsIgnoreCase("login")) {

            mReset_PhoneNum.setVisibility(View.VISIBLE);

            mVerify_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Validate();
                }


            });


        }


    }

    private void Validate() {
        if (mReset_PhoneNum.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please Enter Your Phone Number", Toast.LENGTH_SHORT).show();
        } else if (mReset_PhoneNum.getText().toString().length() < 11 || mReset_PhoneNum.getText().toString().length() > 11) {
            Toast.makeText(this, "Please Enter Your Correct Phone Number", Toast.LENGTH_SHORT).show();
        } else if (mReset_Question1.getText().toString().isEmpty() || mReset_Question2.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please Enter Your both Answer", Toast.LENGTH_SHORT).show();
        } else {
            verifyUser();
        }
    }


    private void displayPreviousAnswers() {
        DatabaseReference mDRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mU_Session.getUserData().get(mU_Session.KEY_PHONE));
        mDRef.child("Security Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String answer1 = dataSnapshot.child("Answer1").getValue().toString();
                    String answer2 = dataSnapshot.child("Answer2").getValue().toString();

                    mReset_Question1.setText(answer1);
                    mReset_Question2.setText(answer2);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void setAnswersToFB() {
        if (mReset_Question1.getText().toString().isEmpty() || mReset_Question2.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please Answer both of Questions", Toast.LENGTH_SHORT).show();
        } else {
            DatabaseReference mDRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mU_Session.getUserData().get(mU_Session.KEY_PHONE));

            HashMap<String, Object> userdataMap = new HashMap<>();
            userdataMap.put("Answer1", mReset_Question1.getText().toString());
            userdataMap.put("Answer2", mReset_Question2.getText().toString());

            mDRef.child("Security Questions").updateChildren(userdataMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ResetPasswordActivity.this, "Answers saved Successfully", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(ResetPasswordActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }

                        }
                    });


        }


    }


    private void verifyUser() {

        final DatabaseReference DRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mReset_PhoneNum.getText().toString());

        DRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    if (dataSnapshot.hasChild("Security Questions")) {
                        String ans1 = dataSnapshot.child("Security Questions").child("Answer1").getValue().toString();
                        String ans2 = dataSnapshot.child("Security Questions").child("Answer2").getValue().toString();

                        if (!(ans1.equalsIgnoreCase(mReset_Question1.getText().toString()))) {
                            Toast.makeText(ResetPasswordActivity.this, "Your first answer is wrong", Toast.LENGTH_SHORT).show();
                        } else if (!(ans2.equalsIgnoreCase(mReset_Question2.getText().toString()))) {
                            Toast.makeText(ResetPasswordActivity.this, "Your Second answer is wrong", Toast.LENGTH_SHORT).show();
                        } else {

                            final AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this , R.style.Theme_AppCompat_DayNight_Dialog);
                            builder.setTitle("New Password");

                            final EditText newPassword = new EditText(ResetPasswordActivity.this);
                            newPassword.setHint("Write New Password here....");
                            builder.setView(newPassword);

                            builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (newPassword.getText().toString().isEmpty()){
                                        Toast.makeText(ResetPasswordActivity.this, "Please Enter your new Password", Toast.LENGTH_SHORT).show();
                                    }else {
                                        DRef.child("user_Password").setValue(newPassword.getText().toString())
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            Toast.makeText(ResetPasswordActivity.this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(ResetPasswordActivity.this , SignInActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }


                                                    }
                                                });

                                    }

                                }
                            });

                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();

                                }
                            });


                            builder.show();


                        }

                    } else {
                        Toast.makeText(ResetPasswordActivity.this, "You haven't set the Security Questions. ", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(ResetPasswordActivity.this, "Account with this " + mReset_PhoneNum.getText().toString() + "don't exist", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}
