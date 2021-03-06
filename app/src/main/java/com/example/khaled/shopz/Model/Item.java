package com.example.khaled.shopz.Model;

import android.net.Uri;

public class Item {


    private String description;
    private String image;
    private String name;
    private String price;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String key;




    public Item() {
    }

    public Item(String description, String image, String name, String price) {
        this.description = description;
        this.image = image;
        this.name = name;
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
}
