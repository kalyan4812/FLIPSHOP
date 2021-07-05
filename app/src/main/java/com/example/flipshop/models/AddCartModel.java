package com.example.flipshop.models;

import java.io.Serializable;

public class AddCartModel  implements Serializable {
    private String userID;
    private String productID;
    private String productName;
    private String pImage;
    private String pPrice;
    private String pCartkey;
    private Integer count;

    public AddCartModel(){

    }

    public AddCartModel(String user,String pId, String pName, String pimg, String pprice, String pcartKey){
        userID=user;
        productID=pId;
        productName=pName;
        pImage=pimg;
        pPrice=pprice;
        pCartkey=pcartKey;


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
