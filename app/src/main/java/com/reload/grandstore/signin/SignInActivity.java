package com.reload.grandstore.signin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reload.grandstore.home.HomeActivity;
import com.reload.grandstore.R;
import com.reload.grandstore.admins.AdminCategoryActivity;
import com.reload.grandstore.prevalent.Prevalent;
import com.reload.grandstore.resetPassword.ResetPasswordActivity;
import com.reload.grandstore.seller.SellerRegistrationActivity;
import com.reload.grandstore.sharedPerformances.TotalAmount_TypeSession;
import com.reload.grandstore.sharedPerformances.UserSession;
import com.reload.grandstore.signUp.SignUpActivity;

import io.paperdb.Paper;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    EditText mPhone_SignIn, mPasss_SignIn;
    Button mSignIn_btn ;
    TextView mSignUp_tv, mAdmin_Text, mNot_Admin_text , mForgetPassword_btn , mBecomeSeller_Tv ;

    ProgressDialog mProgressDia;
    private String parentDBName = "Users";
    private CheckBox mChBoxRemmemberMe;

    UserSession mSession ;
    TotalAmount_TypeSession mTotalAmount_typeSession ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        initViews();
    }

    private void initViews() {
        mProgressDia = new ProgressDialog(this);
        mChBoxRemmemberMe = findViewById(R.id.checkBoxRememberMe);

        mSession = new UserSession(this);
        mTotalAmount_typeSession = new TotalAmount_TypeSession(this);

        mPhone_SignIn = findViewById(R.id.phone_et_signIn);
        mPasss_SignIn = findViewById(R.id.passsword_et_signIn);
        mSignUp_tv = findViewById(R.id.go_signUp);
        mAdmin_Text = findViewById(R.id.admin_text);
        mNot_Admin_text = findViewById(R.id.notAdmin_text);
        mSignIn_btn = findViewById(R.id.signIn_btn);
        mForgetPassword_btn = findViewById(R.id.forget_password_btn);
        mBecomeSeller_Tv = findViewById(R.id.becomeSeller_tv);

        mSignUp_tv.setOnClickListener(this);
        mAdmin_Text.setOnClickListener(this);
        mNot_Admin_text.setOnClickListener(this);
        mSignIn_btn.setOnClickListener(this);
        mForgetPassword_btn.setOnClickListener(this);
        mBecomeSeller_Tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.go_signUp:
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
                finish();
                break;

            case R.id.admin_text:
                mSignIn_btn.setText("Login Admin");
                mAdmin_Text.setVisibility(View.GONE);
                mNot_Admin_text.setVisibility(View.VISIBLE);
                parentDBName = "Admins";


                break;

            case R.id.notAdmin_text:
                mSignIn_btn.setText("Sign In");
                mAdmin_Text.setVisibility(View.VISIBLE);
                mNot_Admin_text.setVisibility(View.GONE);
                parentDBName = "Users";

                break;

            case R.id.signIn_btn:
                goShoping();
                break;

            case R.id.forget_password_btn:
                Intent intent = new Intent(SignInActivity.this , ResetPasswordActivity.class);
                intent.putExtra("check" , "login");
                startActivity(intent);
                break;

            case R.id.becomeSeller_tv:
                Intent k = new Intent(SignInActivity.this , SellerRegistrationActivity.class);
                startActivity(k);
                break;
        }
    }

    private void goShoping() {
        if (mPhone_SignIn.getText().toString().isEmpty()) {
            Toast.makeText(this, " please enter your Phone Number ", Toast.LENGTH_SHORT).show();
        } else if (mPhone_SignIn.getText().toString().length() < 11 || mPhone_SignIn.getText().toString().length() > 11) {
            Toast.makeText(this, " please enter your Correct Phone Number ", Toast.LENGTH_SHORT).show();
        } else if (mPasss_SignIn.getText().toString().isEmpty()) {
            Toast.makeText(this, " please enter your Password ", Toast.LENGTH_SHORT).show();
        } else if (mPasss_SignIn.getText().toString().length() < 8 || mPasss_SignIn.getText().toString().length() == 8) {
            Toast.makeText(this, " your Password must be more than 8", Toast.LENGTH_SHORT).show();
        } else {
            mProgressDia.setTitle("Login Account");
            mProgressDia.setMessage("Please Wait , While We are Checking the Credentials.");
            mProgressDia.setCanceledOnTouchOutside(false);
            mProgressDia.show();

            checkFirebase(mPhone_SignIn.getText().toString(), mPasss_SignIn.getText().toString());

        }

    }

    private void checkFirebase(final String Phone, final String Pass) {

        if (mChBoxRemmemberMe.isChecked()) {
            Paper.book().write(Prevalent.UserPhoneKey, Phone);
            Paper.book().write(Prevalent.UserPasswordKey, Pass);
        }


        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDBName).child(Phone).exists()) {
                    Users mUsersData = dataSnapshot.child(parentDBName).child(Phone).getValue(Users.class);
                    if (mUsersData.user_Phone.equals(Phone)) {
                        if (mUsersData.getUser_Password().equals(Pass)) {
                            if (parentDBName.equals("Admins")) {
                                mProgressDia.dismiss();

                                mSession.SaveData(mUsersData.getUser_Name() ,mPhone_SignIn.getText().toString() , mPasss_SignIn.getText().toString() , "");

                                Toast.makeText(SignInActivity.this, "Welcome Admin", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignInActivity.this, AdminCategoryActivity.class));

                                finish();
                            } else if (parentDBName.equals("Users")) {
                                mProgressDia.dismiss();
                                Toast.makeText(SignInActivity.this, "Logged In is Successfully", Toast.LENGTH_SHORT).show();
                                Prevalent.currentOnlineUser = mUsersData ;

                               mTotalAmount_typeSession.SaveTotalAmount("0" , "");
                               mSession.SaveData(mUsersData.getUser_Name() ,mPhone_SignIn.getText().toString() , mPasss_SignIn.getText().toString() , "");

                                startActivity(new Intent(SignInActivity.this, HomeActivity.class));
                                finish();
                            }

                        } else {
                            Toast.makeText(SignInActivity.this, "Your Password is inCorrect ", Toast.LENGTH_SHORT).show();
                            mProgressDia.dismiss();
                        }
                    }

                    // Toast.makeText(SignInActivity.this, "يا اخى ويلكم هههههه", Toast.LENGTH_SHORT).show();


                } else {

                    Toast.makeText(SignInActivity.this, "Account With this "+ " " + Phone +" " + "don't exist", Toast.LENGTH_SHORT).show();
                    mProgressDia.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
