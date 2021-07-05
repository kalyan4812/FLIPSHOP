package com.example.flipshop.models;

import android.text.TextUtils;

public class ProductUpload {
    private String key, category, brand, name, mrp, price, description, stock, color, size;
    private String cImageUri1, cImageUri2, cImageUri3, cImageUri4;
    private long num_of_people_rated;
    private double rating;
    private String colorcode;
    private String uploadId;

    public long getNum_of_people_rated() {
        return num_of_people_rated;
    }

    public void setNum_of_people_rated(long num_of_people_rated) {
        this.num_of_people_rated = num_of_people_rated;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public ProductUpload() {

    }

    public String getColorcode() {
        return colorcode;
    }

    public void setColorcode(String colorcode) {
        this.colorcode = colorcode;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ProductUpload(long num_of_people_rated,double rating,String colorcode, String key, String category, String brand, String name, String mrp, String price, String description, String stock, String color, String size, String cImageUri1, String cImageUri2, String cImageUri3, String cImageUri4) {

        if (TextUtils.isEmpty(category)) {
            this.name = "no category";
        }
        if (TextUtils.isEmpty(name)) {
            this.name = "no name";
        }
        if (TextUtils.isEmpty(brand)) {
            this.brand = "no name";
        }
        if (TextUtils.isEmpty(price)) {
            this.price = "NOT MENTIONED";
        }
        if (TextUtils.isEmpty(mrp)) {
            this.mrp = "NOT MENTIONED";
        }
        if (TextUtils.isEmpty(stock)) {
            this.stock = "NO INFORMATION";
        }
        if (TextUtils.isEmpty(color)) {
            this.color = "NO INFORMATION";
        }
        if (TextUtils.isEmpty(size)) {
            this.size = "NOT MENTIONED";
        }
        if (TextUtils.isEmpty(description)) {
            this.description = "NO DESCRIPTION";
        }
        this.brand = brand;
        this.name = name;
        this.mrp = mrp;
        this.price = price;
        this.description = description;
        this.stock = stock;
        this.color = color;
        this.size = size;
        this.cImageUri1 = cImageUri1;
        this.cImageUri2 = cImageUri2;
        this.cImageUri3 = cImageUri3;
        this.cImageUri4 = cImageUri4;
        this.category = category;
        this.key = key;
        this.colorcode = colorcode;
        this.num_of_people_rated=0;
        this.rating=0.0;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setcImageUri1(String cImageUri1) {
        this.cImageUri1 = cImageUri1;
    }

    public void setcImageUri2(String cImageUri2) {
        this.cImageUri2 = cImageUri2;
    }

    public void setcImageUri3(String cImageUri3) {
        this.cImageUri3 = cImageUri3;
    }

    public void setcImageUri4(String cImageUri4) {
        this.cImageUri4 = cImageUri4;
    }

    public String getBrand() {
        return brand;
    }

    public String getName() {
        return name;
    }

    public String getMrp() {
        return mrp;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getStock() {
        return stock;
    }

    public String getColor() {
        return color;
    }

    public String getSize() {
        return size;
    }

    public String getcImageUri1() {
        return cImageUri1;
    }

    public String getcImageUri2() {
        return cImageUri2;
    }

    public String getcImageUri3() {
        return cImageUri3;
    }

    public String getcImageUri4() {
        return cImageUri4;
    }
}
