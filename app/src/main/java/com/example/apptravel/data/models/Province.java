package com.example.apptravel.data.models;

import com.google.gson.annotations.SerializedName;

public class Province {
    @SerializedName("name")
    private String name;

    @SerializedName("code")
    private int code;

    public Province(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public String getName() { return name; }
    public int getCode() { return code; }

    // Ghi đè toString để hiển thị tên trên Spinner/List thuận tiện hơn
    @Override
    public String toString() { return name; }
}
