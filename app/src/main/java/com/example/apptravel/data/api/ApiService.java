package com.example.apptravel.data.api;

import com.example.apptravel.data.models.CancelBookingRequest;
import com.example.apptravel.data.models.DanhGia;
import com.example.apptravel.data.models.DatTourHistoryItem;
import com.example.apptravel.data.models.HoatDong;
import com.example.apptravel.data.models.LichKhoiHanh;
import com.example.apptravel.data.models.LoginRequest;
import com.example.apptravel.data.models.LoginResponse;
import com.example.apptravel.data.models.Tour;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("/api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);
    @GET("api/tour/all")
    Call<List<Tour>> getAllTours();
    @GET("api/tour/{id}/lich-khoi-hanh")
    Call<List<LichKhoiHanh>> getLichKhoiHanh(@Path("id") int maTour);
    @GET("api/tour/{id}/danh-gia")
    Call<List<DanhGia>> getDanhGia(@Path("id") int maTour);
    @GET("api/admin/hoatdong")
    Call<List<HoatDong>> getHoatDong();

    @GET("api/user/bookings")
    Call<List<DatTourHistoryItem>> getUserBookings(@Query("status") String status);

    @POST("api/user/bookings/{id}/cancel")
    Call<Void> cancelUserBooking(@Path("id") int maDatTour, @Body CancelBookingRequest request);

}
