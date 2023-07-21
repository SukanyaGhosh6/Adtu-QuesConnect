package com.adtu.quesconnect.model;

import com.google.firebase.database.Exclude;

public class User {
    public String name,email,phone;
    String key;
    String uuids;

    public User() {
    }
    public User(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
   public User(String name, String email, String phone, String uuids) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.uuids = uuids;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getUuids() {
        return uuids;
    }

    public void setUuids(String uuids) {
        this.uuids = uuids;
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
