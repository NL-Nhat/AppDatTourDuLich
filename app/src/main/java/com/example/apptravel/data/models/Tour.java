package com.example.apptravel.data.models;

import java.io.Serializable;

public class Tour implements Serializable {
    private String title;
    private String price;
    private float rating;
    private int imageResId;
    private boolean isFavorite;
    private String location;

    public Tour(String title, String price, float rating, int imageResId, boolean isFavorite, String location) {
        this.title = title;
        this.price = price;
        this.rating = rating;
        this.imageResId = imageResId;
        this.isFavorite = isFavorite;
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public float getRating() {
        return rating;
    }

    public int getImageResId() {
        return imageResId;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public String getLocation() {
        return location;
    }
}
