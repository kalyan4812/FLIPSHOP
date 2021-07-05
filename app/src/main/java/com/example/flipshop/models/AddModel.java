package com.example.flipshop.models;

public class AddModel {
    private String cName;
    private String cImageUri;
    private String subtitle="";

    public AddModel(String cName, String cImageUri,String subtitle) {
        this.cName = cName;
        this.cImageUri = cImageUri;
        this.subtitle=subtitle;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
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
