package com.example.apptravel.data.api;

import com.example.apptravel.data.models.LoginRequest;
import com.example.apptravel.data.models.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);
}
