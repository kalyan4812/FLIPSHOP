package com.example.flipshop.models;

public class OrdersModel {
    private String userID;
    private String productID;
    private String productName;
    private String pImage;
    private String pPrice;
    private String pOrderkey;
    private String oStatus;
public OrdersModel(){

  }
    public OrdersModel(String userID, String productID, String productName, String pImage, String pPrice, String pOrderkey,String oStatus) {
        this.userID = userID;
        this.productID = productID;
        this.productName = productName;
        this.pImage = pImage;
        this.pPrice = pPrice;
        this.pOrderkey = pOrderkey;
        this.oStatus=oStatus;
    }

    public String getpOrderkey() {
        return pOrderkey;
    }

    public void setpOrderkey(String pOrderkey) {
        this.pOrderkey = pOrderkey;
    }

    public String getoStatus() {
        return oStatus;
    }

    public void setoStatus(String oStatus) {
        this.oStatus = oStatus;
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


}
