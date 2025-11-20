package com.example.apptravel.models;

public class BookedTour {
    private int imageResId;
    private String title;
    private String date;
    private String location;

    public BookedTour(int imageResId, String title, String date, String location) {
        this.imageResId = imageResId;
        this.title = title;
        this.date = date;
        this.location = location;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }
}
