package com.reload.grandstore.Search;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reload.grandstore.R;
import com.reload.grandstore.viewHolder.ProductsAdapter;
import com.reload.grandstore.viewHolder.ProductsModel;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    View view ;
   private RecyclerView mRecyclerView;
   private ArrayList<ProductsModel> mList;
   private ProductsAdapter mProductAdapt;
   private DatabaseReference mDatabaseReference;
   TextView mSearch_Et ;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search, container, false);

        initProductsViews();
        getDataFromFB();

        return view ;
    }



    private void initProductsViews() {

        mRecyclerView = view.findViewById(R.id.products_List_RV);
        mList = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mSearch_Et = view.findViewById(R.id.search_et);
        mSearch_Et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                filter(s.toString());
            }
        });

    }


    private void getDataFromFB() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Products");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ProductsModel model = dataSnapshot1.getValue(ProductsModel.class);
                    mList.add(model);
                }
                mProductAdapt = new ProductsAdapter(mList , getActivity());
                mRecyclerView.setAdapter(mProductAdapt);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "something is wrong ....", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void filter(String text) {
        ArrayList<ProductsModel> filteredList = new ArrayList<>();

        for (ProductsModel model : mList){
            if (model.getName().toLowerCase().contains(text.toLowerCase()) || model.getCategory().toLowerCase().contains(text.toLowerCase()) ){
                filteredList.add(model);
            }
        }

        mProductAdapt.filterList(filteredList);

    }



}
