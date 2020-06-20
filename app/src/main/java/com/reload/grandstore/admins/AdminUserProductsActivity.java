package com.reload.grandstore.admins;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
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
import com.reload.grandstore.cart.CartAdapter;
import com.reload.grandstore.cart.CartFragment;
import com.reload.grandstore.cart.CartModel;

import java.util.ArrayList;

public class AdminUserProductsActivity extends AppCompatActivity implements CartAdapter.OncartItemclick {

    RecyclerView mUser_Admin_Products_RecyclerView;
    ArrayList<CartModel> mProductsList;
    CartAdapter mProductsAdapt;
    DatabaseReference mProductsRef;
    private String mUser_Id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_products);

        mUser_Id = getIntent().getStringExtra("uid");
        initViews();
        getDataFromDBase();

    }

    private void initViews() {

        mUser_Admin_Products_RecyclerView = findViewById(R.id.admin_user_products_List);
        mUser_Admin_Products_RecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProductsList = new ArrayList<>();

    }

    private void getDataFromDBase() {
        mProductsRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View").child(mUser_Id).child("Products");
        mProductsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    CartModel cartModel = dataSnapshot1.getValue(CartModel.class);
                    mProductsList.add(cartModel);
                }

                mProductsAdapt = new CartAdapter(mProductsList ,AdminUserProductsActivity.this , AdminUserProductsActivity.this);
                mUser_Admin_Products_RecyclerView.setAdapter(mProductsAdapt);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminUserProductsActivity.this, "something is wrong ....", Toast.LENGTH_SHORT).show();

            }
        });


    }


    @Override
    public void onCartItemClick(String itemPid) {

    }
}
