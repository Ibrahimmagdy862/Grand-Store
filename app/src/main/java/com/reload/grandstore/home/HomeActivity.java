package com.reload.grandstore.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reload.grandstore.logOut.LogOutFragment;
import com.reload.grandstore.Search.SearchFragment;
import com.reload.grandstore.cart.CartFragment;
import com.reload.grandstore.sharedPerformances.TotalAmount_TypeSession;
import com.reload.grandstore.signin.Users;
import com.reload.grandstore.viewHolder.CategoriesFragment;
import com.reload.grandstore.R;
import com.reload.grandstore.settings.SettingsFragment;
import com.reload.grandstore.sharedPerformances.UserSession;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton mCart_Floating_btn;
    ImageView mMenu, mUser_Image;
    public TextView mShow_Text, mUserName_text;
    LinearLayout mContainer;
    DrawerLayout mDrawerLayout;
    TextView mCart, mSearch, mCategories, mSettings, mLog_Out;
    private UserSession mSession;

    String mImageUri;
    public static int navItemIndex;

    RelativeLayout mMainRelative;
    TotalAmount_TypeSession mTotalAmount_typeSession ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        mSession = new UserSession(this);
        mTotalAmount_typeSession = new TotalAmount_TypeSession(this);
        LoadFragment(new CategoriesFragment(), "Home", 0);
        mCart_Floating_btn.show();
        getImageFromDB();

    }


    private void initViews() {
        mMainRelative = findViewById(R.id.relative1);

        mCart_Floating_btn = findViewById(R.id.cart_floating_btn);
        mMenu = findViewById(R.id.menu);
        mShow_Text = findViewById(R.id.text_show);
        mContainer = findViewById(R.id.container);
        mDrawerLayout = findViewById(R.id.drawer);
        mUser_Image = findViewById(R.id.user_img);
        mUserName_text = findViewById(R.id.userName_text);

//        Picasso.get().load(Prevalent.currentOnlineUser.getUser_image()).placeholder(R.drawable.user_image).into(mUser_Image);

        mCart = findViewById(R.id.cart_tv);
        mSearch = findViewById(R.id.search_tv);
        mCategories = findViewById(R.id.categories_tv);
        mSettings = findViewById(R.id.settings_tv);
        mLog_Out = findViewById(R.id.logOut_tv);

        mMenu.setOnClickListener(this);
        mCart.setOnClickListener(this);
        mCategories.setOnClickListener(this);
        mSearch.setOnClickListener(this);
        mSettings.setOnClickListener(this);
        mLog_Out.setOnClickListener(this);
        mCart_Floating_btn.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu:
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    mUserName_text.setText(mSession.getUserData().get(mSession.KEY_NAME));

                } else {
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                    mUserName_text.setText(mSession.getUserData().get(mSession.KEY_NAME));
                }

                break;

            case R.id.cart_tv:
                if (mTotalAmount_typeSession.getTotalAmountData().get(mTotalAmount_typeSession.KEY_Type).equalsIgnoreCase("Admin")){
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                }else {
                    LoadFragment(new CartFragment(), "Cart", 1);
                    mCart_Floating_btn.hide();
                }
                break;

            case R.id.cart_floating_btn:
                if (mTotalAmount_typeSession.getTotalAmountData().get(mTotalAmount_typeSession.KEY_Type).equalsIgnoreCase("Admin")){

                }else {
                    LoadFragment(new CartFragment(), "Cart", 1);
                    mCart_Floating_btn.hide();
                }
                break;

            case R.id.search_tv:

                LoadFragment(new SearchFragment(), "Search", 3);

                break;

            case R.id.categories_tv:
                LoadFragment(new CategoriesFragment(), "Categories", 0);
                mCart_Floating_btn.show();

                break;

            case R.id.settings_tv:
                if (mTotalAmount_typeSession.getTotalAmountData().get(mTotalAmount_typeSession.KEY_Type).equalsIgnoreCase("Admin")){
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                }else {
                    LoadFragment(new SettingsFragment(), "Settings", 2);
                    mCart_Floating_btn.hide();
                }
                break;

            case R.id.logOut_tv:
                if (mTotalAmount_typeSession.getTotalAmountData().get(mTotalAmount_typeSession.KEY_Type).equalsIgnoreCase("Admin")){
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                }else {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    LogOutFragment logOutFragment = new LogOutFragment();
                    logOutFragment.show(getSupportFragmentManager(), "Dialog");
                }
                break;

        }
    }

    void LoadFragment(Fragment fragment, String text, int index) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        mShow_Text.setText(text);
        mDrawerLayout.closeDrawers();
        navItemIndex = index;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
            mCart_Floating_btn.show();
            return;
        } else if (navItemIndex != 0) {

            navItemIndex = 0;
            LoadFragment(new CategoriesFragment(), "Home", 0);
            mMainRelative.setVisibility(View.VISIBLE);
            mCart_Floating_btn.show();

            return;

        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        mUserName_text.setText(mSession.getUserData().get(mSession.KEY_NAME));
        mMainRelative.setVisibility(View.VISIBLE);
        super.onResume();
    }


    private void getImageFromDB() {
        final DatabaseReference productsR = FirebaseDatabase.getInstance().getReference().child("Users");

        productsR.child(mSession.getUserData().get(mSession.KEY_PHONE)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Users users = dataSnapshot.getValue(Users.class);

                    Picasso.get().load(users.getUser_image()).into(mUser_Image);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
