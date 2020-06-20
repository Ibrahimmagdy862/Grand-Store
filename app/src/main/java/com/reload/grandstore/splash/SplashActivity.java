package com.reload.grandstore.splash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reload.grandstore.R;
import com.reload.grandstore.admins.AdminCategoryActivity;
import com.reload.grandstore.home.HomeActivity;
import com.reload.grandstore.prevalent.Prevalent;
import com.reload.grandstore.signin.SignInActivity;
import com.reload.grandstore.signin.Users;

import io.paperdb.Paper;

public class SplashActivity extends AppCompatActivity {

    String UserPhoneKey;
    String UserPasswordKey;

    ProgressDialog mProgressDia;
    private String parentDBName = "Users";
    private String parentDBName2 = "Admins";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splashTimer();
        mProgressDia = new ProgressDialog(this);

        Paper.init(this);

        UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);

        if (UserPhoneKey != "" && UserPasswordKey != "") {

            if (!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey)) {

                mProgressDia.setTitle("Already Logged in");
                mProgressDia.setMessage("Please Wait ........");
                mProgressDia.setCanceledOnTouchOutside(false);
//                mProgressDia.show();

                allowAccess(UserPhoneKey, UserPasswordKey);
            } else {
                startActivity(new Intent(SplashActivity.this, SignInActivity.class));
            }

        }

    }

    private void allowAccess(final String Phone, final String Pass) {

        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDBName).child(Phone).exists()) {
                    Users mUsersData = dataSnapshot.child(parentDBName).child(Phone).getValue(Users.class);
                    if (mUsersData.getUser_Phone().equals(Phone)) {
                        if (mUsersData.getUser_Password().equals(Pass)) {
//                            mProgressDia.dismiss();
                            Toast.makeText(SplashActivity.this, "Please Wait, You are Already Logged In", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SplashActivity.this, HomeActivity.class));

                        } else {
                            // Toast.makeText(SplashActivity.this, "Your Password is inCorrect ", Toast.LENGTH_SHORT).show();
                            mProgressDia.dismiss();
                            Prevalent.currentOnlineUser = mUsersData;
                            startActivity(new Intent(SplashActivity.this, SignInActivity.class));
                            finish();
                        }
                    }

                    // Toast.makeText(SignInActivity.this, "يا اخى ويلكم هههههه", Toast.LENGTH_SHORT).show();

                } else if (dataSnapshot.child(parentDBName2).child(Phone).exists()) {
                    Users mUsersData = dataSnapshot.child(parentDBName2).child(Phone).getValue(Users.class);
                    if (mUsersData.getUser_Phone().equals(Phone)) {
                        if (mUsersData.getUser_Password().equals(Pass)) {
                            mProgressDia.dismiss();
                            Toast.makeText(SplashActivity.this, "Please Wait, You are Already Logged In", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SplashActivity.this, AdminCategoryActivity.class));
                            finish();
                        } else {
                            // Toast.makeText(SplashActivity.this, "Your Password is inCorrect ", Toast.LENGTH_SHORT).show();
                            mProgressDia.dismiss();
                            Prevalent.currentOnlineUser = mUsersData;
                            startActivity(new Intent(SplashActivity.this, SignInActivity.class));
                            finish();
                        }
                    }


                } else {
                    startActivity(new Intent(SplashActivity.this, SignInActivity.class));
                    finish();
                    // Toast.makeText(SplashActivity.this, "Account With this " +Phone+ "don't exist", Toast.LENGTH_SHORT).show();
                    mProgressDia.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void splashTimer() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // startActivity(new Intent(SplashActivity.this , SignInActivity.class));
                finish();
            }
        }, 3000);
    }
}
