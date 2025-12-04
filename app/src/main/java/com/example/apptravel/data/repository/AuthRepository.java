package com.example.apptravel.data.repository;

import android.content.Context;

import com.example.apptravel.data.api.ApiClient;
import com.example.apptravel.data.api.ApiService;
import com.example.apptravel.data.models.LoginRequest;
import com.example.apptravel.data.models.LoginResponse;

import retrofit2.Call;

public class AuthRepository {

    private ApiService api;

    public AuthRepository(Context context) {
        api = ApiClient.getClient(context).create(ApiService.class);
    }

    public Call<LoginResponse> login(LoginRequest request) {
        return api.login(request);
    }
}
