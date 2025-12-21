package com.example.apptravel.data.models;

public class LichTrinhYeuCau implements DisplayableItem{
    private int imageResId;
    private String imageUrl;
    private String title;
    private String date;
    private String location;
    private boolean isSelected;

    public LichTrinhYeuCau(int imageResId, String title, String date, String location, boolean isSelected) {
        this.imageResId = imageResId;
        this.title = title;
        this.date = date;
        this.location = location;
        this.isSelected = isSelected;
    }

    public LichTrinhYeuCau(String imageUrl, String title, String date, String location, boolean isSelected) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.date = date;
        this.location = location;
        this.isSelected = isSelected;
    }

    // Getters
    public int getImageResId() {
        return imageResId;
    }

    public String getImageUrl() {
        return imageUrl;
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

    public boolean isSelected() {
        return isSelected;
    }

    // Setters
    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}

