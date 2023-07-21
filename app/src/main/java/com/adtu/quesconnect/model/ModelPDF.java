package com.adtu.quesconnect.model;

import com.google.firebase.database.Exclude;

public class ModelPDF {
    String name,price,pdfurl;
    String key;
    public ModelPDF() {
    }

    public ModelPDF(String name,String price, String pdfurl) {
        this.name = name;
        this.price = price;
        this.pdfurl = pdfurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPdfurl() {
        return pdfurl;
    }

    public void setPdfurl(String pdfurl) {
        this.pdfurl = pdfurl;
    }
    @Exclude
    public String getKey() {
        return key;
    }
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }
}

