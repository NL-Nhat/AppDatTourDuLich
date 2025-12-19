package com.example.apptravel.data.repository;

import android.content.Context;

import com.example.apptravel.data.api.ApiClient;
import com.example.apptravel.data.api.ApiService;
import com.example.apptravel.data.models.HoatDong;
import java.util.List;
import retrofit2.Call;

public class HoatDongRepository {
    private ApiService api;

    public HoatDongRepository(Context context) {
        api = ApiClient.getClient(context).create(ApiService.class);
    }

    public Call<List<HoatDong>> getAllHoatDong() {
        return api.getHoatDong();
    }
}
