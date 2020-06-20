package com.reload.grandstore.viewHolder;

public class ProductsModel {
    String category , date , description , Image , Name , Price, pid , time , state , sellerName , sellerAddress , sellerPhone , sellerEmail ;

    public ProductsModel(){

    }

    public ProductsModel(String category, String date, String description, String image, String name, String price, String pid, String time, String state, String sellerName, String sellerAddress, String sellerPhone, String sellerEmail) {
        this.category = category;
        this.date = date;
        this.description = description;
        Image = image;
        Name = name;
        Price = price;
        this.pid = pid;
        this.time = time;
        this.state = state;
        this.sellerName = sellerName;
        this.sellerAddress = sellerAddress;
        this.sellerPhone = sellerPhone;
        this.sellerEmail = sellerEmail;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerAddress() {
        return sellerAddress;
    }

    public void setSellerAddress(String sellerAddress) {
        this.sellerAddress = sellerAddress;
    }

    public String getSellerPhone() {
        return sellerPhone;
    }

    public void setSellerPhone(String sellerPhone) {
        this.sellerPhone = sellerPhone;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }
}
