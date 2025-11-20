package com.example.apptravel.models;

public class Tour {
    private String title;
    private String price;
    private float rating;
    private int imageResId;
    private boolean isFavorite;

    public Tour(String title, String price, float rating, int imageResId, boolean isFavorite) {
        this.title = title;
        this.price = price;
        this.rating = rating;
        this.imageResId = imageResId;
        this.isFavorite = isFavorite;
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
}