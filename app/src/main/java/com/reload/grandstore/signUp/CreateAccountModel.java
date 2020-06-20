package com.reload.grandstore.signUp;

public class CreateAccountModel {

   String User_Name ;
   String User_Phone ;
   String User_Password ;
   String User_Address ;

    public CreateAccountModel(String user_Name, String user_Phone, String user_Password, String user_Address) {
        User_Name = user_Name;
        User_Phone = user_Phone;
        User_Password = user_Password;
        User_Address = user_Address;
    }

    public String getUser_Name() {
        return User_Name;
    }

    public String getUser_Phone() {
        return User_Phone;
    }

    public String getUser_Password() {
        return User_Password;
    }

    public String getUser_Address() {
        return User_Address;
    }
}
