package com.reload.grandstore.cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.reload.grandstore.R;
import com.reload.grandstore.home.HomeActivity;
import com.reload.grandstore.sharedPerformances.TotalAmount_TypeSession;
import com.reload.grandstore.sharedPerformances.UserSession;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {
    EditText mEditTextName, mEditTextPhoneNum, mEditTextHomeAddress, mEditTextCity;
    Button mConfirmOrderBtn;

    UserSession mSession;
    private String totalAmount = "" ;

    TotalAmount_TypeSession mTotalAmountSession ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        initViews();
    }

    private void initViews() {
        mEditTextName = findViewById(R.id.shipment_name);
        mEditTextPhoneNum = findViewById(R.id.shipment_PhoneNumber);
        mEditTextHomeAddress = findViewById(R.id.shipment_Address);
        mEditTextCity = findViewById(R.id.shipment_CityName);
        mConfirmOrderBtn = findViewById(R.id.confirm_btn);

        mSession = new UserSession(this);
        mTotalAmountSession = new TotalAmount_TypeSession(this);

        totalAmount = getIntent().getStringExtra("Total Price");

        mConfirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if (totalAmount.equalsIgnoreCase("0")){
                     Toast.makeText(ConfirmFinalOrderActivity.this, "The Shipment is empty " , Toast.LENGTH_SHORT).show();
                 }else{
                     validation();

                 }

            }
        });
    }

    private void validation() {
        if (mEditTextName.getText().toString().isEmpty()) {
            Toast.makeText(this, " Please Enter Your Name ", Toast.LENGTH_SHORT).show();
        } else if (mEditTextPhoneNum.getText().toString().isEmpty()) {
            Toast.makeText(this, " Please Enter Your Phone Number ", Toast.LENGTH_SHORT).show();
        } else if (mEditTextPhoneNum.getText().toString().length() < 11 || mEditTextPhoneNum.getText().toString().length() > 11) {
            Toast.makeText(this, " Please Enter Your Correct Phone Number ", Toast.LENGTH_SHORT).show();
        } else if (mEditTextHomeAddress.getText().toString().isEmpty()) {
            Toast.makeText(this, " Please Enter Your Home Address ", Toast.LENGTH_SHORT).show();
        } else if (mEditTextCity.getText().toString().isEmpty()) {
            Toast.makeText(this, " Please Enter Your City ", Toast.LENGTH_SHORT).show();
        } else {
            confirmOrder();
        }


    }

    private void confirmOrder() {

        final String saveCurrentDate, saveCurrentTime;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd , yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(mSession.getUserData().get(mSession.KEY_PHONE));


        HashMap<String , Object> ordersMap = new HashMap<>();
        ordersMap.put("totalAmount" , totalAmount);
        ordersMap.put("name" , mEditTextName.getText().toString());
        ordersMap.put("Phone" , mEditTextPhoneNum.getText().toString());
        ordersMap.put("Address" , mEditTextHomeAddress.getText().toString());
        ordersMap.put("City" , mEditTextCity.getText().toString());
        ordersMap.put("date" , saveCurrentDate);
        ordersMap.put("time" , saveCurrentTime);
        ordersMap.put("state" , "not shipped");

        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference().child("Cart List")
                            .child("User View")
                            .child(mSession.getUserData().get(mSession.KEY_PHONE))
                            .removeValue();

                }


            }
        }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(ConfirmFinalOrderActivity.this, "Your final Order has been Successfully ...", Toast.LENGTH_SHORT).show();
                Intent o = new Intent(ConfirmFinalOrderActivity.this , HomeActivity.class);
                mTotalAmountSession.SaveTotalAmount("0" , "");
                startActivity(o);
                finish();
            }
        });


    }


}
