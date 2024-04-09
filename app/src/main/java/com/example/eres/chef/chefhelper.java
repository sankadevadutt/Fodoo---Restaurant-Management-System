package com.example.eres.chef;

public class chefhelper {
    private String Dishname,Quantity,Description,Price,ImageURL,RandonUUID,phone;

    public String getDishname() {
        return Dishname;
    }

    public void setDishname(String dishname) {
        Dishname = dishname;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getRandonUUID() {
        return RandonUUID;
    }

    public void setRandonUUID(String randonUUID) {
        RandonUUID = randonUUID;
    }

    public chefhelper(){

    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public chefhelper(String dishname, String quantity, String description, String price, String imageURL, String randonUUID, String phone) {
        Dishname = dishname;
        Quantity = quantity;
        Description = description;
        Price = price;
        ImageURL = imageURL;
        RandonUUID = randonUUID;
        this.phone = phone;
    }
}
