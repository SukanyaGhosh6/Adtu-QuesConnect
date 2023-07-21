package com.adtu.quesconnect.model;

import com.google.firebase.database.Exclude;


//************************************************** Quiz sets

public class ModelHotTopic {
    String title;
    private String mImageUrl;
    String pos;
    String key;
    String cat;

    public ModelHotTopic() {
    }

    public ModelHotTopic(String title, String imageUrl, String pos, String cat) {
        this.title = title;
        this.mImageUrl = imageUrl;
        this.pos = pos;
        this.cat = cat;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return mImageUrl;
    }
    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getPos() {
        return pos;
    }
    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
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
