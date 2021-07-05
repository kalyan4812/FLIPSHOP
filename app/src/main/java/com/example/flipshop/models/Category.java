package com.example.flipshop.models;

public class Category {
    String cName, cImageUri;

    public Category(){

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
