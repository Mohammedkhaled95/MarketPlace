package com.example.khaled.shopz.Model;

import java.util.ArrayList;
import java.util.List;

public class SuperMarket {


    private String image;
    private String location;
    private String ownerName;
    private String phone;
    private String rate;

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    private String deviceToken;
    private String superMarketname;
    private List<Item> items;


    public SuperMarket() {

    }

    public SuperMarket(String image, String location, String ownerName, String phone, String rate, String superMarketname, List<Item> items) {
        this.image = image;
        this.location = location;
        this.ownerName = ownerName;
        this.phone = phone;
        this.rate = rate;
        this.superMarketname = superMarketname;
        this.items = items;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getSuperMarketname() {
        return superMarketname;
    }

    public void setSuperMarketname(String superMarketname) {
        this.superMarketname = superMarketname;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}