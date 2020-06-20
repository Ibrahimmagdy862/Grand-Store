package com.reload.grandstore.viewHolder;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reload.grandstore.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriesFragment extends Fragment {
    View view;
    RecyclerView mRecyclerView;
    ArrayList<ProductsModel> mList;
    ProductsAdapter mProductAdapt;
    DatabaseReference mDatabaseReference;

    public CategoriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_categories, container, false);

        initProductsViews();
        getDataFromFB();

        return view;
    }

    private void initProductsViews() {
        mRecyclerView = view.findViewById(R.id.product_RV);
        mList = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void getDataFromFB() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Products");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        ProductsModel model = dataSnapshot1.getValue(ProductsModel.class);

                        if (model.getState().equalsIgnoreCase("Not Approved")){
                           // Toast.makeText(getActivity(), "no Orders", Toast.LENGTH_SHORT).show();
                        }else {
                            mList.add(model);
                        }
                    }
                    mProductAdapt = new ProductsAdapter(mList, getActivity());
                    mRecyclerView.setAdapter(mProductAdapt);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "something is wrong ....", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
