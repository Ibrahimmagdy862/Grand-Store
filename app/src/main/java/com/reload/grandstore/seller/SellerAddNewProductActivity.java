package com.reload.grandstore.seller;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.reload.grandstore.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class SellerAddNewProductActivity extends AppCompatActivity implements View.OnClickListener {

    private String CategoryName , saveCurrentDate ,saveCurrentTime ;
    ImageView mSelectProductImage;
    EditText mProductName, mProductDescription, mProductPrice;
    Button mAddProduct_Btn;

    Uri ImageUri ;

    private static final int GALLERY_PICK = 1;
    private String productRandomKey , downloadImageUrl ;
    private StorageReference productImageRef ;
    private DatabaseReference productRef , sellersRef ;

    String sName , sPhone , sAddress , sEmail , sID ;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_new_product);

        initViews();

        CategoryName = getIntent().getExtras().get("Category").toString();
        Toast.makeText(this, CategoryName, Toast.LENGTH_SHORT).show();
    }

    private void initViews() {
        productImageRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        productRef = FirebaseDatabase.getInstance().getReference().child("Products");

        sellersRef = FirebaseDatabase.getInstance().getReference().child("Sellers");

        mProgressDialog = new ProgressDialog(this);
        mSelectProductImage = findViewById(R.id.select_product_img);
        mProductName = findViewById(R.id.produt_name);
        mProductDescription = findViewById(R.id.product_description);
        mProductPrice = findViewById(R.id.product_Price);
        mAddProduct_Btn = findViewById(R.id.add_Product_btn);

        mSelectProductImage.setOnClickListener(this);
        mAddProduct_Btn.setOnClickListener(this);

        sellersRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){

                            sName = dataSnapshot.child("name").getValue().toString();
                            sAddress = dataSnapshot.child("address").getValue().toString();
                            sPhone = dataSnapshot.child("phone").getValue().toString();
                            sEmail = dataSnapshot.child("email").getValue().toString();
                            sID = dataSnapshot.child("sid").getValue().toString();


                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_product_img:
                openGallery();
                break;

            case R.id.add_Product_btn:
                validation();
                break;
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent , GALLERY_PICK);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GALLERY_PICK && resultCode==RESULT_OK && data!=null){
            ImageUri = data.getData();
            mSelectProductImage.setImageURI(ImageUri);

        }

    }

    private void validation() {
        if (ImageUri == null){
            Toast.makeText(this, "please update your Product Image ", Toast.LENGTH_SHORT).show();
        }else if (mProductName.getText().toString().isEmpty()){
            Toast.makeText(this, "please Enter Your Product Name", Toast.LENGTH_SHORT).show();
        }else if (mProductDescription.getText().toString().isEmpty()){
            Toast.makeText(this, "please Enter Your Product Description", Toast.LENGTH_SHORT).show();
        }else if (mProductPrice.getText().toString().isEmpty()){
            Toast.makeText(this, "please Enter Your Product Price", Toast.LENGTH_SHORT).show();
        }else{
           // Toast.makeText(this, "gogogo", Toast.LENGTH_SHORT).show();
            storeProductinformation();
        }
    }

    private void storeProductinformation() {
        mProgressDialog.setTitle("Add New Product");
        mProgressDialog.setMessage("Dear Seller , Please Wait While We are adding the new Product.");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM  dd, yyyy ");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime ;

        final StorageReference filePath = productImageRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString() ;
                Toast.makeText(SellerAddNewProductActivity.this, "Error: " + message , Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(SellerAddNewProductActivity.this, "Product Image Uploaded Successfully ...... ", Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if (!task.isSuccessful()){

                            throw task.getException();
                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl() ;
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        if (task.isSuccessful()){
                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(SellerAddNewProductActivity.this, "got the Product Image Url Successfully ....", Toast.LENGTH_SHORT).show();
                            saveProductInfoToDataBase();
                        }
                    }
                });
            }
        });

    }

    private void saveProductInfoToDataBase() {
        HashMap<String , Object> productMap = new HashMap<>();
        productMap.put("pid" , productRandomKey);
        productMap.put("date" , saveCurrentDate);
        productMap.put("time" , saveCurrentTime);
        productMap.put("description" , mProductDescription.getText().toString());
        productMap.put("image" , downloadImageUrl);
        productMap.put("category" , CategoryName);
        productMap.put("price" , mProductPrice.getText().toString());
        productMap.put("name" , mProductName.getText().toString());

        productMap.put("sellerName" , sName );
        productMap.put("sellerAddress" , sAddress );
        productMap.put("sellerPhone" , sPhone );
        productMap.put("sellerEmail" , sEmail );
        productMap.put("sID" , sID );

        productMap.put("state" , "Not Approved" );

        productRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            Toast.makeText(SellerAddNewProductActivity.this, "Product is added Successfully ....", Toast.LENGTH_SHORT).show();
                            mProgressDialog.dismiss();
                            startActivity(new Intent(SellerAddNewProductActivity.this, SellersHomeActivity.class));
                            finish();
                        }else{
                            String message = task.getException().toString();
                            Toast.makeText(SellerAddNewProductActivity.this, "Error: " + message , Toast.LENGTH_SHORT).show();
                            mProgressDialog.dismiss();
                        }

                    }
                });

    }

}
