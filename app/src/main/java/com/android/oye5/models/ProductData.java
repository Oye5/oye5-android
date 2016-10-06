package com.android.oye5.models;

import java.io.Serializable;

public class ProductData implements Serializable {
    private String name = "";
    private int price = 0;
    private int favorites = 0;
    private int distance = 0;
    private int imgResId = 0;

    public ProductData() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getFavorites() {
        return favorites;
    }

    public void setFavorites(int favorites) {
        this.favorites = favorites;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getImgResId() {
        return imgResId;
    }

    public void setImgResId(int imgResId) {
        this.imgResId = imgResId;
    }
}
