package com.example.apptravel.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DistrictResponse {
    @SerializedName("name")
    private String provinceName;

    @SerializedName("code")
    private int provinceCode;

    @SerializedName("districts")
    private List<District> districts;

    public List<District> getDistricts() { return districts; }
    public String getProvinceName() { return provinceName; }
}
