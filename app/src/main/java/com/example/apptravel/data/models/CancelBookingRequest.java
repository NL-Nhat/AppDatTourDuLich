package com.example.apptravel.data.models;

import com.google.gson.annotations.SerializedName;

public class CancelBookingRequest {
    @SerializedName("lyDoHuy")
    private final String lyDoHuy;

    public CancelBookingRequest(String lyDoHuy) {
        this.lyDoHuy = lyDoHuy;
    }

    public String getLyDoHuy() {
        return lyDoHuy;
    }
}
