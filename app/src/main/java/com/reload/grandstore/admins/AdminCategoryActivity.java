package com.reload.grandstore.admins;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.reload.grandstore.admins.checkNewProducts.CheckNewProductsActivity;
import com.reload.grandstore.home.HomeActivity;
import com.reload.grandstore.logOut.LogOutFragment;
import com.reload.grandstore.R;
import com.reload.grandstore.adminsNewOrders.AdminNewOrderActivity;
import com.reload.grandstore.sharedPerformances.TotalAmount_TypeSession;

public class AdminCategoryActivity extends AppCompatActivity implements View.OnClickListener {

    Button mMaintain_Btn, mCheckNewOrders_Btn, mCheck_and_Approve_NewProducts , mLog_Out_Admin_Btn;

    TotalAmount_TypeSession mTotalAmount_typeSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        initViews();
    }

    private void initViews() {

        mMaintain_Btn = findViewById(R.id.maintain_btn);
        mCheckNewOrders_Btn = findViewById(R.id.check_new_orders_btn);
        mCheck_and_Approve_NewProducts = findViewById(R.id.check_and_Approve_new_Products_btn);
        mLog_Out_Admin_Btn = findViewById(R.id.log_out_admin_btn);

        mMaintain_Btn.setOnClickListener(this);
        mCheckNewOrders_Btn.setOnClickListener(this);
        mCheck_and_Approve_NewProducts.setOnClickListener(this);
        mLog_Out_Admin_Btn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.maintain_btn:
                mTotalAmount_typeSession = new TotalAmount_TypeSession(this);
                mTotalAmount_typeSession.SaveTotalAmount("0", "Admin");
                startActivity(new Intent(AdminCategoryActivity.this, HomeActivity.class));
                break;

            case R.id.check_new_orders_btn:
                Intent r = new Intent(AdminCategoryActivity.this, AdminNewOrderActivity.class);
                startActivity(r);
                break;

            case R.id.check_and_Approve_new_Products_btn:
                Intent p = new Intent(AdminCategoryActivity.this, CheckNewProductsActivity.class);
                startActivity(p);
                break;

            case R.id.log_out_admin_btn:
                LogOutFragment logOutFragment = new LogOutFragment();
                logOutFragment.show(getSupportFragmentManager(), "Dialog");

               /* Intent z = new Intent(AdminCategoryActivity.this, SignInActivity.class);
                z.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                Paper.book().destroy();
                startActivity(z);
                finish();*/
                break;
        }
    }
}
