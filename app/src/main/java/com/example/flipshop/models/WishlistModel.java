package com.example.flipshop.models;

public class WishlistModel {
    private String userID;
    private String productID;
    private String productName;
    private String pImage;
    private String pPrice;
    private String pCartkey;
    int colorcode=0;

    public WishlistModel(){

    }

    public WishlistModel(String user,String pId, String pName, String pimg, String pprice, String pcartKey,int colorcode){
        userID=user;
        productID=pId;
        productName=pName;
        pImage=pimg;
        pPrice=pprice;
        pCartkey=pcartKey;
        this.colorcode=colorcode;

    }

    public int getColorcode() {
        return colorcode;
    }

    public void setColorcode(int colorcode) {
        this.colorcode = colorcode;
    }

    public String getpCartkey() {
        return pCartkey;
    }

    public void setpCartkey(String pCartkey) {
        this.pCartkey = pCartkey;
    }

    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }

    public String getpPrice() {
        return pPrice;
    }

    public void setpPrice(String pPrice) {
        this.pPrice = pPrice;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}