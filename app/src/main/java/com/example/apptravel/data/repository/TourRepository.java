package com.example.apptravel.data.repository;

import android.content.Context;

import com.example.apptravel.data.api.ApiClient;
import com.example.apptravel.data.api.ApiService;
import com.example.apptravel.data.models.Tour;

import java.util.List;

import retrofit2.Call;

public class TourRepository {
    private ApiService api;

    public TourRepository(Context context) {
        api = ApiClient.getClient(context).create(ApiService.class);
    }

    public Call<List<Tour>> getAllTours() {
        return api.getAllTours();
    }
}
