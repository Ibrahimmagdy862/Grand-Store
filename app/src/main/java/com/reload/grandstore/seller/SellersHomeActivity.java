package com.reload.grandstore.seller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.reload.grandstore.R;
import com.reload.grandstore.logOut.LogOutFragment;

public class SellersHomeActivity extends AppCompatActivity {

    RelativeLayout mContainer ;
    RelativeLayout mHome_relative , mAdd_relative , mLogOut_relative ;

    public static int navItemIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellers_home);

        initViews();
        LoadFragment(new SellerHomeFragment(), 0);

    }

    private void initViews() {

        mContainer = findViewById(R.id.container);

        mHome_relative = findViewById(R.id.home_seller_relative);
        mAdd_relative  = findViewById(R.id.add_seller_relative);
        mLogOut_relative = findViewById(R.id.log_out_seller_relative);

        mHome_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadFragment(new SellerHomeFragment(), 0);

            }
        });

        mAdd_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadFragment(new SellerCategoryFragment() , 1);

            }
        });

        mLogOut_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogOutFragment logOutFragment = new LogOutFragment();
                logOutFragment.show(getSupportFragmentManager() , "Dialog" );

            }
        });


    }



    void LoadFragment(Fragment fragment, int index) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        navItemIndex = index;
    }


    @Override
    public void onBackPressed() {
        if (navItemIndex != 0) {

            navItemIndex = 0;
            LoadFragment(new SellerHomeFragment(), 0);

            return;
        }

        super.onBackPressed();
    }
}
