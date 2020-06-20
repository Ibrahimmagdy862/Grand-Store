package com.reload.grandstore.seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.reload.grandstore.R;
import com.reload.grandstore.sharedPerformances.UserSession;

public class SellerLoginActivity extends AppCompatActivity {

    EditText mSeller_Email_Login, mSeller_Password_Login;
    Button mSeller_Login_btn;

    private ProgressDialog mLoadingDialog;
    private FirebaseAuth mAuth;

    UserSession mSellerSession ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);

        initViews();
        mSellerSession = new UserSession(this);
        mLoadingDialog = new ProgressDialog(SellerLoginActivity.this);

    }

    private void initViews() {
        mAuth = FirebaseAuth.getInstance();

        mSeller_Email_Login = findViewById(R.id.seller_Email_Login);
        mSeller_Password_Login = findViewById(R.id.seller_Password_Login);
        mSeller_Login_btn = findViewById(R.id.seller_Login_btn);

        mSeller_Login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
            }
        });

    }

    private void validation() {
        if (mSeller_Email_Login.getText().toString().isEmpty()) {
            Toast.makeText(SellerLoginActivity.this, "Please Enter Your Email ", Toast.LENGTH_SHORT).show();
        } else if (mSeller_Password_Login.getText().toString().isEmpty()) {
            Toast.makeText(SellerLoginActivity.this, "Please Enter Your Password ", Toast.LENGTH_SHORT).show();
        } else if (mSeller_Password_Login.getText().toString().length() <= 8) {
            Toast.makeText(SellerLoginActivity.this, "Please Enter Your Correct Password ", Toast.LENGTH_SHORT).show();
        } else {

            mLoadingDialog.setTitle("Login Seller Account");
            mLoadingDialog.setMessage("Please Wait , While We are Checking the Credentials.");
            mLoadingDialog.setCanceledOnTouchOutside(false);
            mLoadingDialog.show();

            loginSeller();
        }

    }

    private void loginSeller() {

        mAuth.signInWithEmailAndPassword(mSeller_Email_Login.getText().toString() , mSeller_Password_Login.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mLoadingDialog.dismiss();
                            Toast.makeText(SellerLoginActivity.this, "You already Logged In Successfully", Toast.LENGTH_SHORT).show();
                            mSellerSession.SaveData(mSeller_Email_Login.getText().toString() , "" , "" , "");
                            Intent i = new Intent(SellerLoginActivity.this , SellersHomeActivity.class);
                            startActivity(i);
                        }else{
                            mLoadingDialog.dismiss();
                            Toast.makeText(SellerLoginActivity.this, "Your Email Or Password is Error", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


    }
}
