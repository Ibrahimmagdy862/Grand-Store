package com.reload.grandstore.admins.checkNewProducts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reload.grandstore.R;
import com.reload.grandstore.admins.AdminCategoryActivity;
import com.reload.grandstore.home.HomeActivity;
import com.reload.grandstore.products.ProductsDetailsActivity;
import com.reload.grandstore.viewHolder.ProductsAdapter;
import com.reload.grandstore.viewHolder.ProductsModel;

import java.util.ArrayList;

public class CheckNewProductsActivity extends AppCompatActivity implements CheckNewProductsAdapter.OnCheckNewProductsClick {

    private RecyclerView mRecyclerView;
    private ArrayList<CheckNewProductsModel> mList;
    private CheckNewProductsAdapter mCheckProductAdapt;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_new_products);


        initProductsViews();
        getNotApprovedProductsFromFB();

    }


    private void initProductsViews() {
        mRecyclerView = findViewById(R.id.adminCheck_List);
        mList = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(CheckNewProductsActivity.this));
    }


    private void getNotApprovedProductsFromFB() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Products");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    CheckNewProductsModel model = dataSnapshot1.getValue(CheckNewProductsModel.class);

                    if (model.getState().equalsIgnoreCase("Approved")){
                        // Toast.makeText(getActivity(), "no Orders", Toast.LENGTH_SHORT).show();
                    }else {
                        mList.add(model);
                    }
                }
                mCheckProductAdapt = new CheckNewProductsAdapter(mList, CheckNewProductsActivity.this);
                mRecyclerView.setAdapter(mCheckProductAdapt);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CheckNewProductsActivity.this , "something is wrong ....", Toast.LENGTH_SHORT).show();

            }
        });


    }


    @Override
    public void onItemClick(final String itemPid) {

        CharSequence options[] = new CharSequence[]
                {
                        "Yes" , "No"
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(CheckNewProductsActivity.this);
        builder.setTitle("Do you want to Approved this Product. Are you sure ?");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){
                    changeProductState(itemPid);

                }else if (which == 1){


                }


            }
        });

        builder.show();

    }

    private void changeProductState(String itemPid) {
        mDatabaseReference.child(itemPid).child("state").setValue("Approved")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(CheckNewProductsActivity.this, "This item has been Approved, and it is now available for sale from the seller ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CheckNewProductsActivity.this , AdminCategoryActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });


    }


}
