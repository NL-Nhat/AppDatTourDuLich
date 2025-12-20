package com.example.apptravel.data.api;

import com.example.apptravel.data.models.BookingRequest;
import com.example.apptravel.data.models.BookingResponse;
import com.example.apptravel.data.models.DanhGia;
import com.example.apptravel.data.models.DistrictResponse;
import com.example.apptravel.data.models.HoatDong;
import com.example.apptravel.data.models.LichKhoiHanh;
import com.example.apptravel.data.models.LoginRequest;
import com.example.apptravel.data.models.LoginResponse;
import com.example.apptravel.data.models.Province;
import com.example.apptravel.data.models.Tour;
import com.example.apptravel.data.models.WardResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

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
    //Lấy tỉnh
    @GET("/api/address/provinces")
    Call<List<Province>> getProvinces();
    //Lấy quận huyện theo tỉnh
    @GET("/api/address/districts/{code}")
    Call<DistrictResponse> getDistricts(@Path("code") String code);
    //Lấy phường xã theo quận huyện
    @GET("/api/address/wards/{code}")
    Call<WardResponse> getWards(@Path("code") String code);

    @POST("/api/bookings/create")
    Call<BookingResponse> createBooking(@Body BookingRequest bookingRequest);
}
