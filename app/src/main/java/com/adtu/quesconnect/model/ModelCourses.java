package com.adtu.quesconnect.model;

public class ModelCourses{
    String name,duration,description,price,discount,tax,key;

    public ModelCourses() {
    }



    public ModelCourses(String name, String duration, String description, String price, String discount, String tax, String key) {
        this.name = name;
        this.duration = duration;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.tax = tax;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}