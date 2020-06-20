package com.reload.grandstore.admins;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reload.grandstore.R;
import com.reload.grandstore.viewHolder.CategoriesFragment;
import com.reload.grandstore.viewHolder.ProductsModel;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class Admin_Maintain_ProductsActivity extends AppCompatActivity {

    ImageView mChane_Image;
    EditText mChange_PName, mChange_PDescript, mChange_PPrice;
    Button mApplyChanges_Btn , mDelete_this_Product ;

    String mProduct_id ;
    DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__maintain__products);

        initViews();

    }


    private void initViews() {
        mChane_Image = findViewById(R.id.apply_p_Image);
        mChange_PName = findViewById(R.id.apply_p_Name);
        mChange_PDescript = findViewById(R.id.apply_p_Description);
        mChange_PPrice = findViewById(R.id.apply_p_Price);

        mApplyChanges_Btn = findViewById(R.id.applyChanges_btn);
        mDelete_this_Product = findViewById(R.id.delete_this_product_btn);

        mProduct_id = getIntent().getStringExtra("pid");

        getProductDetails(mProduct_id);

        mApplyChanges_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validation();
            }
        });

        mDelete_this_Product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productsRef.child(mProduct_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(Admin_Maintain_ProductsActivity.this, "Product is deleted successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Admin_Maintain_ProductsActivity.this , AdminCategoryActivity.class));
                        finish();
                    }
                });

            }
        });

    }

    private void getProductDetails(String mProduct_id) {

        productsRef.child(mProduct_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ProductsModel productsModel = dataSnapshot.getValue(ProductsModel.class);
                    mChange_PName.setText(productsModel.getName());
                    mChange_PDescript.setText(productsModel.getDescription());
                    mChange_PPrice.setText(productsModel.getPrice());
                    Picasso.get().load(productsModel.getImage()).into(mChane_Image);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void Validation() {
        if (mChange_PName.getText().toString().isEmpty()){
            Toast.makeText(this, "Please Enter Your Product Name", Toast.LENGTH_SHORT).show();
        }else if (mChange_PDescript.getText().toString().isEmpty()){
            Toast.makeText(this, "Please Enter Your Product Description", Toast.LENGTH_SHORT).show();
        }else if (mChange_PPrice.getText().toString().isEmpty()){
            Toast.makeText(this, "Please Enter Your Product Price", Toast.LENGTH_SHORT).show();
        }else{
            ApplyChanes();
        }

    }

    private void ApplyChanes() {
        HashMap<String , Object> productMap = new HashMap<>();
        productMap.put("pid" , mProduct_id);
        productMap.put("name" , mChange_PName.getText().toString());
        productMap.put("description" , mChange_PDescript.getText().toString());
        productMap.put("price" , mChange_PPrice.getText().toString());

        productsRef.child(mProduct_id).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(Admin_Maintain_ProductsActivity.this, "Changes Applied Successfully ", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Admin_Maintain_ProductsActivity.this , AdminCategoryActivity.class));
                    finish();
                }
                
            }
        });




    }


}
