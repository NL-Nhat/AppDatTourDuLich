package com.example.apptravel.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WardResponse {
    @SerializedName("name")
    private String districtName;

    @SerializedName("code")
    private int districtCode;

    @SerializedName("wards")
    private List<Ward> wards;

    public List<Ward> getWards() { return wards; }
    public String getDistrictName() { return districtName; }
}
