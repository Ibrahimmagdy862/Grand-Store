package com.reload.grandstore.cart;

public class CartModel {

    String P_name , date , discount , pid , price , quantity , time ;

    public CartModel(){

    }

    public CartModel(String p_name, String date, String discount, String pid, String price, String quantity, String time) {
        P_name = p_name;
        this.date = date;
        this.discount = discount;
        this.pid = pid;
        this.price = price;
        this.quantity = quantity;
        this.time = time;
    }

    public String getP_name() {
        return P_name;
    }

    public void setP_name(String p_name) {
        P_name = p_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
