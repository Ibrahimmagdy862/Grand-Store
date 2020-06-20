package com.reload.grandstore.signin;

public class Users {

    String user_Name ;
    String user_Phone ;
    String user_Password ;
    String user_Image ;
    String user_Address ;

   public Users() {

    }

    public Users(String user_Name, String user_Phone, String user_Password, String user_image, String user_Address) {
        this.user_Name = user_Name;
        this.user_Phone = user_Phone;
        this.user_Password = user_Password;
        this.user_Image = user_image;
        this.user_Address = user_Address;
    }

    public String getUser_Name() {
        return user_Name;
    }

    public void setUser_Name(String user_Name) {
        this.user_Name = user_Name;
    }

    public String getUser_Phone() {
        return user_Phone;
    }

    public void setUser_Phone(String user_Phone) {
        this.user_Phone = user_Phone;
    }

    public String getUser_Password() {
        return user_Password;
    }

    public void setUser_Password(String user_Password) {
        this.user_Password = user_Password;
    }

    public String getUser_image() {
        return user_Image;
    }

    public void setUser_image(String user_image) {
        this.user_Image = user_image;
    }

    public String getUser_Address() {
        return user_Address;
    }

    public void setUser_Address(String user_Address) {
        this.user_Address = user_Address;
    }
}
