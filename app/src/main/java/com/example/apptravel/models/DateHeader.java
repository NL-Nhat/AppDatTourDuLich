package com.example.apptravel.models;

public class DateHeader implements DisplayableItem{
    private String date;

    public DateHeader(String date) {
        this.date = date;
    }

    public String getDate() {
        return "Đã tạo ngày " + date;
    }
}
