package com.example.flipshop.models;

public class ProductsModel {
    String name, cImageUri1, mrp, price,key;
    private String userID,uploadID;
    private int colorcode;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ProductsModel(){

    }

    public String getUploadID() {
        return uploadID;
    }

    public void setUploadID(String uploadID) {
        this.uploadID = uploadID;
    }

    public int getColorcode() {
        return colorcode;
    }

    public void setColorcode(int colorcode) {
        this.colorcode = colorcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getcImageUri1() {
        return cImageUri1;
    }

    public void setcImageUri1(String cImageUri1) {
        this.cImageUri1 = cImageUri1;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}