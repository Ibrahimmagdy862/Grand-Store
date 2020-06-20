package com.reload.grandstore.adminsNewOrders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reload.grandstore.R;
import com.reload.grandstore.admins.AdminCategoryActivity;

import java.util.ArrayList;

public class AdminNewOrderActivity extends AppCompatActivity  implements OrdersAdminAdapter.OnOrderClicked{

    RecyclerView mOrdersRecyclerView ;
    ArrayList<OrdersModel>mOrderList ;
    OrdersAdminAdapter mOrdersAdminAdapter ;
    DatabaseReference mOrderRef ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_order);

        initViews();
        getOrdersFromDB();
    }

    private void initViews() {

        mOrdersRecyclerView = findViewById(R.id.orders_List);
        mOrderList = new ArrayList<>() ;
        mOrdersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void getOrdersFromDB() {
        mOrderRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        mOrderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                    OrdersModel ordersModel = dataSnapshot2.getValue(OrdersModel.class);
                    mOrderList.add(ordersModel);
                }
                mOrdersAdminAdapter = new OrdersAdminAdapter(mOrderList , AdminNewOrderActivity.this , AdminNewOrderActivity.this);
                mOrdersRecyclerView.setAdapter(mOrdersAdminAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminNewOrderActivity.this , "something is wrong ....", Toast.LENGTH_SHORT).show();

            }
        });

    }


    @Override
    public void orderItemClick(final String orderItem) {
        CharSequence options[] = new CharSequence[]
                {
                        "Yes" , "No"
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Have You Shipped this Order Products ? ");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){

                    RemoveOrder(orderItem);

                }else if (which == 1){

                }

            }
        });
        builder.show();

    }

    private void RemoveOrder(String orderItem) {
        mOrderRef.child(orderItem).removeValue();
        startActivity(new Intent(AdminNewOrderActivity.this , AdminCategoryActivity.class));
        finish();
    }
}
