package com.example.apptravel.data.repository;

import android.content.Context;

import com.example.apptravel.data.api.ApiClient;
import com.example.apptravel.data.api.ApiService;
import com.example.apptravel.data.models.DatTourHistoryItem;

import java.util.List;

import retrofit2.Call;

public class DatTourRepository {

    private final ApiService api;

    public DatTourRepository(Context context) {
        api = ApiClient.getClient(context).create(ApiService.class);
    }

    public Call<List<DatTourHistoryItem>> getLichSuDatTour(String status) {
        return api.getUserBookings(status);
    }
}
