package com.reload.grandstore.seller;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.reload.grandstore.R;
import com.reload.grandstore.admins.AdminCategoryActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellerCategoryFragment extends Fragment implements View.OnClickListener {
    View view ;

    ImageView mT_Shirt, mT_Shirt_Sports, mDress, mSweater;
    ImageView mGlasses, mBag, mHat, mShoes;
    ImageView mHead_Phones, mLaptop, mWatches, mMobile;

    public SellerCategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_seller_category, container, false);

        initViews();

        return view ;
    }


    private void initViews() {
        mT_Shirt = view.findViewById(R.id.t_shirts);
        mT_Shirt_Sports = view.findViewById(R.id.sports_t_shirts);
        mDress = view.findViewById(R.id.female_dresses);
        mSweater = view.findViewById(R.id.sweaters);
        mGlasses = view.findViewById(R.id.glasses);
        mBag = view.findViewById(R.id.bags);
        mHat = view.findViewById(R.id.hats);
        mShoes = view.findViewById(R.id.shoes);
        mHead_Phones = view.findViewById(R.id.headphones);
        mLaptop = view.findViewById(R.id.laptop);
        mWatches = view.findViewById(R.id.watches);
        mMobile = view.findViewById(R.id.mobiles);

        mT_Shirt.setOnClickListener(this);
        mT_Shirt_Sports.setOnClickListener(this);
        mDress.setOnClickListener(this);
        mSweater.setOnClickListener(this);
        mGlasses.setOnClickListener(this);
        mBag.setOnClickListener(this);
        mHat.setOnClickListener(this);
        mShoes.setOnClickListener(this);
        mHead_Phones.setOnClickListener(this);
        mLaptop.setOnClickListener(this);
        mWatches.setOnClickListener(this);
        mMobile.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.t_shirts:
                Intent i = new Intent(getActivity() , SellerAddNewProductActivity.class);
                i.putExtra("Category", "T_Shirts");
                startActivity(i);
                break;

            case R.id.sports_t_shirts:
                Intent u = new Intent(getActivity() , SellerAddNewProductActivity.class);
                u.putExtra("Category", "sports_T_Shirts");
                startActivity(u);
                break;

            case R.id.female_dresses:
                Intent e = new Intent(getActivity() , SellerAddNewProductActivity.class);
                e.putExtra("Category", "female_dress");
                startActivity(e);
                break;

            case R.id.sweaters:
                Intent a = new Intent(getActivity() , SellerAddNewProductActivity.class);
                a.putExtra("Category", "sweater");
                startActivity(a);
                break;


            case R.id.glasses:
                Intent p = new Intent(getActivity() , SellerAddNewProductActivity.class);
                p.putExtra("Category", "Glasses");
                startActivity(p);
                break;

            case R.id.bags:
                Intent y = new Intent(getActivity() , SellerAddNewProductActivity.class);
                y.putExtra("Category", "Bags");
                startActivity(y);
                break;

            case R.id.hats:
                Intent k = new Intent(getActivity() , SellerAddNewProductActivity.class);
                k.putExtra("Category", "Hats");
                startActivity(k);
                break;

            case R.id.shoes:
                Intent t = new Intent(getActivity() , SellerAddNewProductActivity.class);
                t.putExtra("Category", "Shoes");
                startActivity(t);
                break;

            case R.id.headphones:
                Intent q = new Intent(getActivity() , SellerAddNewProductActivity.class);
                q.putExtra("Category", "HeadPhones");
                startActivity(q);
                break;

            case R.id.laptop:
                Intent j = new Intent(getActivity() , SellerAddNewProductActivity.class);
                j.putExtra("Category", "Laptop");
                startActivity(j);
                break;

            case R.id.watches:
                Intent w = new Intent(getActivity() , SellerAddNewProductActivity.class);
                w.putExtra("Category", "Watches");
                startActivity(w);
                break;

            case R.id.mobiles:
                Intent m = new Intent(getActivity() , SellerAddNewProductActivity.class);
                m.putExtra("Category", "Mobiles");
                startActivity(m);
                break;
        }


    }
}
