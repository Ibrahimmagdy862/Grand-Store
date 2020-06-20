package com.reload.grandstore.signUp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.reload.grandstore.signin.SignInActivity;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    EditText mName , mPhone_SignUp , mPass_SignUp , mAddress_SignUp ;
    Button mSignUp_btn ;
    TextView mSignIn_tv , mSignUp_Admin_Text, mSignUp_Not_Admin_text ;

    ProgressDialog mProgressDialog ;
    private String parentDBName = "Users";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initViews();
    }

    private void initViews() {
        mName = findViewById(R.id.name_et_signUp);
        mPhone_SignUp = findViewById(R.id.phone_et_signUp);
        mPass_SignUp = findViewById(R.id.passsword_et_signUp);
        mAddress_SignUp = findViewById(R.id.address_et_signUp);
        mSignUp_Admin_Text = findViewById(R.id.signUp_admin_text);
        mSignUp_Not_Admin_text = findViewById(R.id.signUp_notAdmin_text);
        mSignIn_tv = findViewById(R.id.go_signIn);
        mSignUp_btn = findViewById(R.id.signUp_btn);

        mProgressDialog = new ProgressDialog(SignUpActivity.this);
        mSignIn_tv.setOnClickListener(this);
        mSignUp_btn.setOnClickListener(this);
        mSignUp_Admin_Text.setOnClickListener(this);
        mSignUp_Not_Admin_text.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.go_signIn:
                startActivity(new Intent(SignUpActivity.this , SignInActivity.class));
                finish();
                break;

            case R.id.signUp_admin_text:
                mSignUp_Not_Admin_text.setVisibility(View.VISIBLE);
                mSignUp_Admin_Text.setVisibility(View.GONE);
                mSignUp_btn.setText("Create Admin Account");
                parentDBName = "Admins" ;
                break;

            case R.id.signUp_notAdmin_text:
                mSignUp_Not_Admin_text.setVisibility(View.GONE);
                mSignUp_Admin_Text.setVisibility(View.VISIBLE);
                mSignUp_btn.setText("Create Account");
                parentDBName = "Users" ;
                break;

            case R.id.signUp_btn:
                createAccount();
                break;
        }
    }

    private void createAccount() {
        if (mName.getText().toString().isEmpty()){
            Toast.makeText(this, " please enter your name ", Toast.LENGTH_SHORT).show();
        }else if (mPhone_SignUp.getText().toString().isEmpty()){
            Toast.makeText(this, " please enter your Phone Number ", Toast.LENGTH_SHORT).show();
        }else if (mPhone_SignUp.getText().toString().length() < 11 || mPhone_SignUp.getText().toString().length() > 11 ){
            Toast.makeText(this, " please enter your Correct Phone Number ", Toast.LENGTH_SHORT).show();
        }else if (mPass_SignUp.getText().toString().isEmpty()){
            Toast.makeText(this, " please enter your Password ", Toast.LENGTH_SHORT).show();
        }else if (mPass_SignUp.getText().toString().length() < 8 || mPass_SignUp.getText().toString().length() == 8 ){
            Toast.makeText(this, " your Password must be more than 8", Toast.LENGTH_SHORT).show();
        }else {

            mProgressDialog.setTitle("Create Account");
            mProgressDialog.setMessage("Please Wait , While We are Checking the Credentials.");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();

            validatePhoneNumber(mName.getText().toString() , mPhone_SignUp.getText().toString() , mPass_SignUp.getText().toString() , mAddress_SignUp.getText().toString());

        }

    }

    private void validatePhoneNumber(final String Name, final String Phone, final String Pass , final String Addrss) {

        final DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!(dataSnapshot.child(parentDBName).child(Phone).exists())){

                    CreateAccountModel createAccountModel = new CreateAccountModel(Name , Phone , Pass , Addrss);

                    mDatabaseRef.child(parentDBName).child(Phone).setValue(createAccountModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(SignUpActivity.this, "Congratulation Your Account has been Created", Toast.LENGTH_SHORT).show();
                                        mProgressDialog.dismiss();
                                        startActivity(new Intent(SignUpActivity.this , SignInActivity.class));
                                        finish();
                                    }else {
                                        Toast.makeText(SignUpActivity.this, "NetWork Error : Please Try Again After Some Time", Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });

                }else{
                    Toast.makeText(SignUpActivity.this, "this" + Phone + "is already used ", Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


}
