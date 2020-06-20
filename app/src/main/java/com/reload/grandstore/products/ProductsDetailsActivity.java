package com.reload.grandstore.products;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reload.grandstore.R;
import com.reload.grandstore.sharedPerformances.UserSession;
import com.reload.grandstore.viewHolder.ProductsModel;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductsDetailsActivity extends AppCompatActivity {
    TextView mP_Name, mP_Descript, mP_Price;
    ImageView mP_Image;
    String product_Id = "", state = "Normal";
    ElegantNumberButton mE_Num_Btn;
    String num;

    UserSession mSession;

    Button mAddToCartBtn;

    boolean buttonChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_details);

        mSession = new UserSession(ProductsDetailsActivity.this);
        mP_Name = findViewById(R.id.p_Name);
        mP_Descript = findViewById(R.id.p_Description);
        mP_Price = findViewById(R.id.p_Price);
        mP_Image = findViewById(R.id.p_Image);
        mE_Num_Btn = findViewById(R.id.number_button);
        mAddToCartBtn = findViewById(R.id.add_product_to_cart_btn);

        product_Id = getIntent().getStringExtra("pid");

        getProductDetails(product_Id);

        mE_Num_Btn.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                num = mE_Num_Btn.getNumber();
                buttonChanged = true;
                // Toast.makeText(ProductsDetailsActivity.this,num, Toast.LENGTH_SHORT).show();
            }
        });


        mAddToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (state.equals("Order Shipped") || state.equals("Order Placed")) {
                    Toast.makeText(ProductsDetailsActivity.this, "You can purchase more Products , Once Your order is shipped or confirmed", Toast.LENGTH_SHORT).show();
                } else {

                    if (buttonChanged == true) {
                        addingtoCartList();
                    } else {
                        num = "1";
                        addingtoCartList();
                    }


                }

            }
        });


    }

    @Override
    protected void onStart() {
        CheckOrderState();
        super.onStart();
    }

    private void addingtoCartList() {
        String saveCurrentDate, saveCurrentTime;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd , yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", product_Id);
        cartMap.put("P_name", mP_Name.getText().toString());
        cartMap.put("price", mP_Price.getText().toString());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity", num);
        cartMap.put("discount", "");

        cartListRef.child("User View").child(mSession.getUserData().get(mSession.KEY_PHONE))
                .child("Products").child(product_Id)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            cartListRef.child("Admin View").child(mSession.getUserData().get(mSession.KEY_PHONE))
                                    .child("Products").child(product_Id)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                Toast.makeText(ProductsDetailsActivity.this, "Added To Cart List", Toast.LENGTH_SHORT).show();

//                                                onBackPressed();
                                                finish();


                                            }

                                        }
                                    });


                        }

                    }
                });

    }

    private void getProductDetails(String product_id) {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productsRef.child(product_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ProductsModel productsModel = dataSnapshot.getValue(ProductsModel.class);
                    mP_Name.setText(productsModel.getName());
                    mP_Descript.setText(productsModel.getDescription());
                    mP_Price.setText(productsModel.getPrice());
                    Picasso.get().load(productsModel.getImage()).into(mP_Image);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void CheckOrderState() {
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(mSession.getUserData().get(mSession.KEY_PHONE));

        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String shippingState = dataSnapshot.child("state").getValue().toString();

                    if (shippingState.equals("shipped")) {

                        state = "Order Shipped";

                    } else if (shippingState.equals("not shipped")) {

                        state = "Order Placed";

                    }


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}
