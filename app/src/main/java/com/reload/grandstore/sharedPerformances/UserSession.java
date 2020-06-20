package com.reload.grandstore.sharedPerformances;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class UserSession {

    SharedPreferences mSharedPreferences ;
    SharedPreferences.Editor mEditor ;
    Context mContext ;

    private String FILENAME = "Grand Store" ;
    public String KEY_NAME = "user_name" ;
    public String KEY_PHONE = "user_Phone" ;
    public String KEY_PASSWORD = "user_pass" ;
    public String KEY_ADDRESS = "user_Address" ;


    public UserSession(Context mContext) {
        this.mContext = mContext;
        mSharedPreferences = mContext.getSharedPreferences(FILENAME , Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public void SaveData(String name , String phone , String pass , String address ){
        mEditor.putString(KEY_NAME , name);
        mEditor.putString(KEY_PHONE , phone);
        mEditor.putString(KEY_PASSWORD , pass);
        mEditor.putString(KEY_ADDRESS , address);
        mEditor.apply();
    }

    public HashMap<String , String> getUserData(){
        HashMap<String , String> users = new HashMap<>();
        users.put(KEY_NAME , mSharedPreferences.getString(KEY_NAME , null));
        users.put(KEY_PHONE , mSharedPreferences.getString(KEY_PHONE , null));
        users.put(KEY_PASSWORD , mSharedPreferences.getString(KEY_PASSWORD , null));
        users.put(KEY_ADDRESS , mSharedPreferences.getString(KEY_ADDRESS , null));
        return users ;
    }

}
