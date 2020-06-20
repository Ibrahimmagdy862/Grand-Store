package com.reload.grandstore.settings;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.reload.grandstore.R;
import com.reload.grandstore.home.HomeActivity;
import com.reload.grandstore.resetPassword.ResetPasswordActivity;
import com.reload.grandstore.sharedPerformances.UserSession;
import com.reload.grandstore.signin.Users;
import com.squareup.picasso.Picasso;

import java.util.HashMap;


import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {
    View view;
    CircleImageView mEdit_User_Image;
    EditText mEdit_Phone_Number, mEdit_FullName, mEdit_User_Address, mEdit_User_Passwo;
    Button mSelect_UserImage, mSave_UserInfo , mSecurity_Question_btn ;

    private String downloadImageUrl ;

    UserSession mSession;

    private static final int GALLERY_PICK = 1;

    Uri mImageUri;
    private String myUrl = "";
    private StorageTask uploadTask;
    private StorageReference storageProfilesPictureRef;
    private String checker = "";

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        storageProfilesPictureRef = FirebaseStorage.getInstance().getReference().child("Users Images");

        mEdit_User_Image = view.findViewById(R.id.editUser_Image);
        mEdit_Phone_Number = view.findViewById(R.id.edit_PhoneNumber);
        mEdit_FullName = view.findViewById(R.id.edit_fullName);
        mEdit_User_Address = view.findViewById(R.id.edit_Address);
        mEdit_User_Passwo = view.findViewById(R.id.edit_Passwo);
        mSelect_UserImage = view.findViewById(R.id.select_userImage_btn);
        mSave_UserInfo = view.findViewById(R.id.save_editBtn);
        mSecurity_Question_btn = view.findViewById(R.id.security_Question_btn);

        mSession = new UserSession(getActivity());

        getProductDetails();

        mEdit_FullName.setText(mSession.getUserData().get(mSession.KEY_NAME));
        mEdit_Phone_Number.setText(mSession.getUserData().get(mSession.KEY_PHONE));
        mEdit_User_Address.setText(mSession.getUserData().get(mSession.KEY_ADDRESS));



        userInfoDisplay(mEdit_User_Image, mEdit_FullName, mEdit_Phone_Number, mEdit_User_Address, mEdit_User_Passwo);

        mSecurity_Question_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , ResetPasswordActivity.class);
                intent.putExtra("check" , "settings");
                startActivity(intent);
            }
        });

        mSelect_UserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker = "clicked";
              /*  CropImage.activity(mImageUri)
                        .setAspectRatio(1, 1)
                        .start(getActivity());

               */
                openGallery();

            }
        });

        mSave_UserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checker.equals("clicked")) {
                    userInfoSaved();

                } else {
                    if (mEdit_Phone_Number.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "please enter your Phone Number", Toast.LENGTH_SHORT).show();
                    } else if (mEdit_FullName.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "please enter your Name", Toast.LENGTH_SHORT).show();
                    } else if (mEdit_User_Address.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "please enter your Address", Toast.LENGTH_SHORT).show();
                    } else {
                        updateOnlyUserInfo();
                    }
                }
            }
        });

        return view;

    }

    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_PICK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       /* if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            mImageUri = result.getUri();
            mEdit_User_Image.setImageURI(mImageUri);
        } else {
            Toast.makeText(getActivity(), "Error Try Again ....", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), HomeActivity.class));

        }*/

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK && data != null) {
            mImageUri = data.getData();
            mEdit_User_Image.setImageURI(mImageUri);

        }

    }


    private void userInfoSaved() {
        if (mEdit_Phone_Number.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "please enter your Phone Number", Toast.LENGTH_SHORT).show();
        } else if (mEdit_FullName.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "please enter your Name", Toast.LENGTH_SHORT).show();
        } else if (mEdit_User_Address.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "please enter your Address", Toast.LENGTH_SHORT).show();
        } else if (checker.equals("clicked")) {
            uploadImage();
            // Toast.makeText(getActivity(), "UPDate this please", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImage() {
        final ProgressDialog mP_Dialog = new ProgressDialog(getActivity());
        mP_Dialog.setTitle("Update Profile");
        mP_Dialog.setMessage("Please Wait , While We are Updating you account information");
        mP_Dialog.setCanceledOnTouchOutside(false);
        mP_Dialog.show();

   /*     if (mImageUri != null) {
            final StorageReference fileRef = storageProfilesPictureRef.child(mSession.getUserData().get(mSession.KEY_PHONE) + ".jpg");

            uploadTask = fileRef.putFile(mImageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {

                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return fileRef.getDownloadUrl();
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                myUrl = downloadUri.toString();

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                                HashMap<String, Object> userMap = new HashMap<>();
                                userMap.put("user_Name", mEdit_FullName.getText().toString());
                                userMap.put("user_Phone", mEdit_Phone_Number.getText().toString());
                                userMap.put("user_Address", mEdit_User_Address.getText().toString());
                                userMap.put("user_Image", myUrl);
                                ref.child(mSession.getUserData().get(mSession.KEY_PHONE)).updateChildren(userMap);

                                mP_Dialog.dismiss();

                                mSession.SaveData(mEdit_FullName.getText().toString(), mEdit_Phone_Number.getText().toString(), mEdit_User_Passwo.getText().toString(), mEdit_User_Address.getText().toString());


                                Toast.makeText(getActivity(), "Profile Info Update Successfully....", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getActivity(), HomeActivity.class));

                            } else {
                                mP_Dialog.dismiss();
                                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });


        } else {
            Toast.makeText(getActivity(), "image is not selected ... ", Toast.LENGTH_SHORT).show();
            mP_Dialog.dismiss();
        }
*/



        if (mImageUri != null) {
            final StorageReference filePath = storageProfilesPictureRef.child(mSession.getUserData().get(mSession.KEY_PHONE) + ".jpg");

            final UploadTask uploadTask = filePath.putFile(mImageUri);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String message = e.toString();
                    Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();
                    mP_Dialog.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getActivity(), "Product Image Uploaded Successfully ...... ", Toast.LENGTH_SHORT).show();

                    Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                            if (!task.isSuccessful()) {

                                throw task.getException();
                            }

                            downloadImageUrl = filePath.getDownloadUrl().toString();
                            return filePath.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {

                            if (task.isSuccessful()) {
                                downloadImageUrl = task.getResult().toString();
                                Uri downloadUri = task.getResult();
                                myUrl = downloadUri.toString();



                                Toast.makeText(getActivity(), "got the Product Image Url Successfully ....", Toast.LENGTH_SHORT).show();
                                mP_Dialog.dismiss();
                                  saveProfileInfoToDataBase();
                            }
                        }
                    });
                }

                private void saveProfileInfoToDataBase() {


                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                    HashMap<String, Object> userMap = new HashMap<>();
                    userMap.put("user_Name", mEdit_FullName.getText().toString());
                    userMap.put("user_Phone", mEdit_Phone_Number.getText().toString());
                    userMap.put("user_Address", mEdit_User_Address.getText().toString());
                    userMap.put("user_Image", myUrl);
                    ref.child(mSession.getUserData().get(mSession.KEY_PHONE)).updateChildren(userMap);

                    mP_Dialog.dismiss();

                    mSession.SaveData(mEdit_FullName.getText().toString(), mEdit_Phone_Number.getText().toString(), mEdit_User_Passwo.getText().toString(), mEdit_User_Address.getText().toString());

                    Toast.makeText(getActivity(), "Profile Info Update Successfully....", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), HomeActivity.class));




                }
            });
        }else {
            Toast.makeText(getActivity(), "image is not selected ... ", Toast.LENGTH_SHORT).show();
            mP_Dialog.dismiss();

        }








    }

    private void updateOnlyUserInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("user_Name", mEdit_FullName.getText().toString());
        userMap.put("user_Phone", mEdit_Phone_Number.getText().toString());
        userMap.put("user_Address", mEdit_User_Address.getText().toString());
        ref.child(mSession.getUserData().get(mSession.KEY_PHONE)).updateChildren(userMap);


        mSession.SaveData(mEdit_FullName.getText().toString(), mEdit_Phone_Number.getText().toString(), mEdit_User_Passwo.getText().toString(), mEdit_User_Address.getText().toString());
        Toast.makeText(getActivity(), "Profile Info Update Successfully....", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getActivity(), HomeActivity.class));

    }

    private void userInfoDisplay(final ImageView mEdit_user_image, final EditText mEdit_FullName, final EditText mEdit_phone_number, final EditText mEdit_user_address, final EditText mEdit_User_Passwo) {

        DatabaseReference UserDReef = FirebaseDatabase.getInstance().getReference().child("Users").child(mSession.getUserData().get(mSession.KEY_PHONE));
        UserDReef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.child("user_image").exists()) {
                        String user_image = dataSnapshot.child("user_image").getValue().toString();
                        String user_Name = dataSnapshot.child("user_Name").getValue().toString();
                        String user_Phone = dataSnapshot.child("user_Phone").getValue().toString();
                        String user_Password = dataSnapshot.child("user_Password").getValue().toString();
                        String user_Address = dataSnapshot.child("user_Address").getValue().toString();

                        Picasso.get().load(user_image).into(mEdit_user_image);

                        mEdit_FullName.setText(user_Name);
                        mEdit_phone_number.setText(user_Phone);
                        mEdit_user_address.setText(user_Address);
                        mEdit_User_Passwo.setText(user_Password);
                    } else {
                        String user_Name = dataSnapshot.child("user_Name").getValue().toString();
                        String user_Phone = dataSnapshot.child("user_Phone").getValue().toString();
                        String user_Address = dataSnapshot.child("user_Address").getValue().toString();
                        String user_Password = dataSnapshot.child("user_Password").getValue().toString();

                        mEdit_FullName.setText(user_Name);
                        mEdit_phone_number.setText(user_Phone);
                        mEdit_user_address.setText(user_Address);
                        mEdit_User_Passwo.setText(user_Password);

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void getProductDetails() {
      final DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Users");

        productsRef.child(mSession.getUserData().get(mSession.KEY_PHONE)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Users users = dataSnapshot.getValue(Users.class);
                    mEdit_FullName.setText(users.getUser_Name());
                    mEdit_Phone_Number.setText(users.getUser_Phone());
                    mEdit_User_Address.setText(users.getUser_Address());
                    Picasso.get().load(users.getUser_image()).into(mEdit_User_Image);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
