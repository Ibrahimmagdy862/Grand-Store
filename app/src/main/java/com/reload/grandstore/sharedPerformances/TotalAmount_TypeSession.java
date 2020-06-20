package com.reload.grandstore.sharedPerformances;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class TotalAmount_TypeSession {

    SharedPreferences mSharedPreferences ;
    SharedPreferences.Editor mEditor ;
    Context mContext ;

    private String FILENAME = "Grand Store TotalAmount" ;
    public String KEY_TotalAmount = "total_amount" ;
    public  String KEY_Type = "TYPE_ADMIN" ;


    public TotalAmount_TypeSession(Context mContext) {
        this.mContext = mContext;
        mSharedPreferences = mContext.getSharedPreferences(FILENAME , Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public void SaveTotalAmount(String totalAmount , String typeAdmin ){
        mEditor.putString(KEY_TotalAmount , totalAmount);
        mEditor.putString(KEY_Type , typeAdmin) ;
        mEditor.apply();
    }

    public HashMap<String , String> getTotalAmountData(){
        HashMap<String , String> users = new HashMap<>();
        users.put(KEY_TotalAmount , mSharedPreferences.getString(KEY_TotalAmount , null));
        users.put(KEY_Type , mSharedPreferences.getString(KEY_Type , null));
        return users ;
    }

}
