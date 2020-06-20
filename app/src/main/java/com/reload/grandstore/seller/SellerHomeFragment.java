package com.reload.grandstore.seller;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reload.grandstore.R;
import com.reload.grandstore.admins.checkNewProducts.CheckNewProductsAdapter;
import com.reload.grandstore.admins.checkNewProducts.CheckNewProductsModel;
import com.reload.grandstore.sharedPerformances.UserSession;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellerHomeFragment extends Fragment implements CheckNewProductsAdapter.OnCheckNewProductsClick {
    View view;

    private RecyclerView mRecyclerView;
    private ArrayList<CheckNewProductsModel> mList;
    private CheckNewProductsAdapter mCheckProductAdapt;
    private DatabaseReference mDatabaseReference;

    UserSession mSellersSessions;

    public SellerHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_seller_home, container, false);

        initSellerHomeViews();
        getSellerProducts();

        return view;

    }


    private void initSellerHomeViews() {
        mSellersSessions = new UserSession(getActivity());

        mRecyclerView = view.findViewById(R.id.seller_product_RV);
        mList = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void getSellerProducts() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Products");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    CheckNewProductsModel model = dataSnapshot1.getValue(CheckNewProductsModel.class);

                    if (model.getSellerEmail().equalsIgnoreCase(mSellersSessions.getUserData().get(mSellersSessions.KEY_NAME))) {
                        mList.add(model);
                    } else {

                    }
                }
                mCheckProductAdapt = new CheckNewProductsAdapter(mList, SellerHomeFragment.this);
                mRecyclerView.setAdapter(mCheckProductAdapt);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "something is wrong ....", Toast.LENGTH_SHORT).show();

            }
        });


    }

    @Override
    public void onItemClick(final String itemPid) {

        CharSequence options[] = new CharSequence[]
                {
                        "Yes", "No"
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Do you want to delete this product, Are you sure ?");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {

                    mDatabaseReference.child(itemPid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getActivity(), "Product is deleted successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getActivity(), SellersHomeActivity.class));
                            getActivity().finish();
                        }
                    });


                } else if (which == 1) {


                }


            }
        });

        builder.show();


    }
}
