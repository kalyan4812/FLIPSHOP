package com.example.flipshop.models;

import android.text.TextUtils;

public class CategoryUpload {
    private String cName;
    private String cImageUri;

    public CategoryUpload(){

    }

    public CategoryUpload(String name, String imageuri){
        if (TextUtils.isEmpty(name)){
            name="no name";
        }
        cName=name;
        cImageUri=imageuri;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getcImageUri() {
        return cImageUri;
    }

    public void setcImageUri(String cImageUri) {
        this.cImageUri = cImageUri;
    }
}
