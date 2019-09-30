package com.hfad.mdb_inventory;

import java.io.Serializable;
import java.util.UUID;

public class Model implements Serializable {
    private String price;
    private String location;
    private String item;
    private String imageURL;
    private String description;
    private String date;
    /**
     * UID is used for locating and tracking purchases. When creating a new purchase, generate a random UID
     * when deserializing, restore the UID over this property
     */
    private String uid = UUID.randomUUID().toString();

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
