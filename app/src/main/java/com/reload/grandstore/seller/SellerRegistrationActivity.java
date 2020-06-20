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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.reload.grandstore.R;

import java.util.HashMap;

public class SellerRegistrationActivity extends AppCompatActivity {

    EditText mSeller_Name, mSeller_Phone, mSeller_Email, mSeller_Password, mSeller_PasswordAgain, mSeller_Shop_Address;
    Button mSeller_Registration_btn, mSeller_HaveAccount_Btn;

    ProgressDialog mLoadingDialog ;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration);

        initViews();

        mLoadingDialog = new ProgressDialog(SellerRegistrationActivity.this);

    }

    private void initViews() {
        mAuth = FirebaseAuth.getInstance();

        mSeller_Name = findViewById(R.id.seller_Name);
        mSeller_Phone = findViewById(R.id.seller_Phone);
        mSeller_Email = findViewById(R.id.seller_Email);
        mSeller_Password = findViewById(R.id.seller_Password);
        mSeller_PasswordAgain = findViewById(R.id.seller_PasswordAgain);
        mSeller_Shop_Address = findViewById(R.id.seller_Shop_address);
        mSeller_Registration_btn = findViewById(R.id.seller_register_btn);
        mSeller_HaveAccount_Btn = findViewById(R.id.seller_HaveAccount_btn);


        mSeller_HaveAccount_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerRegistrationActivity.this, SellerLoginActivity.class);
                startActivity(intent);

            }
        });


        mSeller_Registration_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
            }
        });


    }

    private void validation() {
        if (mSeller_Name.getText().toString().isEmpty()) {
            Toast.makeText(SellerRegistrationActivity.this, "Please Enter Your Name", Toast.LENGTH_SHORT).show();
        } else if (mSeller_Phone.getText().toString().isEmpty()) {
            Toast.makeText(SellerRegistrationActivity.this, "Please Enter Your Phone Number", Toast.LENGTH_SHORT).show();
        } else if (mSeller_Phone.getText().toString().length() < 11 || mSeller_Phone.getText().toString().length() > 11) {
            Toast.makeText(SellerRegistrationActivity.this, "Please Enter Your Correct Phone Number", Toast.LENGTH_SHORT).show();
        } else if (mSeller_Email.getText().toString().isEmpty()) {
            Toast.makeText(SellerRegistrationActivity.this, "Please Enter Your Email", Toast.LENGTH_SHORT).show();
        } else if (mSeller_Password.getText().toString().isEmpty()) {
            Toast.makeText(SellerRegistrationActivity.this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();
        } else if (mSeller_Password.getText().toString().length() <= 8) {
            Toast.makeText(SellerRegistrationActivity.this, "Password must be more than 8 ", Toast.LENGTH_SHORT).show();
        } else if (mSeller_PasswordAgain.getText().toString().isEmpty()) {
            Toast.makeText(SellerRegistrationActivity.this, "Please Enter Your Password Again", Toast.LENGTH_SHORT).show();
        } else if (mSeller_PasswordAgain.getText().toString().length() <= 8) {
            Toast.makeText(SellerRegistrationActivity.this, "Password must be more than 8 ", Toast.LENGTH_SHORT).show();
        } else if (mSeller_Shop_Address.getText().toString().isEmpty()) {
            Toast.makeText(SellerRegistrationActivity.this, "Please Enter Your Shop - Business Address ", Toast.LENGTH_SHORT).show();
        } else if (!(mSeller_Password.getText().toString().equalsIgnoreCase(mSeller_PasswordAgain.getText().toString()))) {
            Toast.makeText(SellerRegistrationActivity.this, "Two Passwords Not identical ", Toast.LENGTH_SHORT).show();
        } else {
            mLoadingDialog.setTitle("Create Seller Account");
            mLoadingDialog.setMessage("Please Wait , While We are Checking the Credentials.");
            mLoadingDialog.setCanceledOnTouchOutside(false);
            mLoadingDialog.show();

            saveDataInFB();
        }

    }

    private void saveDataInFB() {
        mAuth.createUserWithEmailAndPassword(mSeller_Email.getText().toString(), mSeller_Password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

                            String sid = mAuth.getCurrentUser().getUid();

                            HashMap<String, Object> sellerMap = new HashMap<>();
                            sellerMap.put("sid", sid);
                            sellerMap.put("phone", mSeller_Phone.getText().toString());
                            sellerMap.put("email",  mSeller_Email.getText().toString());
                            sellerMap.put("address",  mSeller_Shop_Address.getText().toString());
                            sellerMap.put("name",  mSeller_Name.getText().toString());

                            mRootRef.child("Sellers").child(sid).updateChildren(sellerMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                           if (task.isSuccessful()) {
                                               mLoadingDialog.dismiss();
                                               Toast.makeText(SellerRegistrationActivity.this, "You are Registered Successfully", Toast.LENGTH_SHORT).show();
                                               Intent i = new Intent(SellerRegistrationActivity.this , SellerLoginActivity.class);
                                               startActivity(i);
                                           }else{
                                               Toast.makeText(SellerRegistrationActivity.this, "NetWork Error : Please Try Again After Some Time", Toast.LENGTH_SHORT).show();
                                           }


                                        }
                                    });

                        }


                    }
                });


    }
}
