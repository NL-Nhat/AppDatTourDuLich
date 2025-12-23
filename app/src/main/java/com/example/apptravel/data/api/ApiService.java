package com.example.apptravel.data.api;

import com.example.apptravel.data.models.CancelBookingRequest;
import com.example.apptravel.data.models.ChangePasswordRequest;
import com.example.apptravel.data.models.DanhGia;
import com.example.apptravel.data.models.DatTourHistoryItem;
import com.example.apptravel.data.models.BookingRequest;
import com.example.apptravel.data.models.BookingResponse;
import com.example.apptravel.data.models.DiemDen;
import com.example.apptravel.data.models.DistrictResponse;
import com.example.apptravel.data.models.HoatDong;
import com.example.apptravel.data.models.LichKhoiHanh;
import com.example.apptravel.data.models.LoginRequest;
import com.example.apptravel.data.models.LoginResponse;
import com.example.apptravel.data.models.NguoiDung;
import com.example.apptravel.data.models.Province;
import com.example.apptravel.data.models.NguoiDung;
import com.example.apptravel.data.models.ThanhToanRequest;
import com.example.apptravel.data.models.Tour;
import com.example.apptravel.data.models.AdminBookingItem;
import com.example.apptravel.data.models.TourRequest;
import com.example.apptravel.data.models.ViDienTuResponse;
import com.example.apptravel.data.models.WardResponse;
import com.example.apptravel.data.models.RegisterRequest;
import com.example.apptravel.ui.activitys.user.KetQuaThanhToanActivity;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // SỬA LỖI: Xóa dấu / ở đầu
    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);
    @GET("api/tour/all")
    Call<List<Tour>> getAllTours();
    @GET("api/tour/{id}/lich-khoi-hanh")
    Call<List<LichKhoiHanh>> getLichKhoiHanh(@Path("id") int maTour);
    @GET("api/tour/{id}/danh-gia")
    Call<List<DanhGia>> getDanhGia(@Path("id") int maTour);
    @GET("api/admin/hoatdong")
    Call<List<HoatDong>> getHoatDong();

    // SỬA LỖI: Xóa dấu / ở đầu
    @GET("api/address/provinces")
    Call<List<Province>> getProvinces();
    
    // SỬA LỖI: Xóa dấu / ở đầu
    @GET("api/address/districts/{code}")
    Call<DistrictResponse> getDistricts(@Path("code") String code);

    // SỬA LỖI: Xóa dấu / ở đầu
    @GET("api/address/wards/{code}")
    Call<WardResponse> getWards(@Path("code") String code);
    //User
    @GET("api/auth/user/{id}")
    Call<NguoiDung> getNguoiDungById(@Path("id") String maNguoiDung);
    @Headers("No-Authentication: true")
    @PUT("api/auth/user/{id}")
    Call<NguoiDung> updateNguoiDung(@Path("id") String userId, @Body NguoiDung nguoiDung);
    @Multipart
    @POST("api/auth/uploadAnhDaiDien")
    Call<Map<String, String>> uploadAnhDaiDien(@Part MultipartBody.Part file);
    @GET("api/user/bookings")
    Call<List<DatTourHistoryItem>> getUserBookings(@Query("status") String status);

    @POST("api/user/bookings/{id}/cancel")
    Call<Void> cancelUserBooking(@Path("id") int maDatTour, @Body CancelBookingRequest request);
    @PUT("api/auth/user/{id}/doiMatKhau")
    Call<ResponseBody> doiMatKhau(@Path("id") String userId, @Body ChangePasswordRequest request);
    // Admin - bookings
    @GET("api/admin/bookings")
    Call<List<AdminBookingItem>> getAdminBookings(@Query("status") String status, @Query("q") String q);

    @GET("api/admin/bookings/{id}")
    Call<AdminBookingItem> getAdminBookingDetail(@Path("id") int maDatTour);

    @POST("api/admin/bookings/{id}/confirm")
    Call<Void> confirmAdminBooking(@Path("id") int maDatTour);

    @POST("api/admin/bookings/{id}/cancel")
    Call<Void> cancelAdminBooking(@Path("id") int maDatTour, @Body CancelBookingRequest request);

    // SỬA LỖI: Xóa dấu / ở đầu
    @POST("api/bookings/create")
    Call<BookingResponse> createBooking(@Body BookingRequest bookingRequest);

    @POST("api/auth/register")
    Call<ResponseBody> register(@Body RegisterRequest request);
  
    @GET("/api/bookings/{id}")
    Call<ViDienTuResponse> getLayThongTinVi(@Path("id") int maDatTour);

    @POST("/api/thanhtoan/create")
    Call<Void> createThanhToan(@Body ThanhToanRequest thanhToanRequest);

    @GET("api/admin/tours")
    Call<List<Tour>> getAdminTours();

    @GET("api/admin/tours/{id}")
    Call<Tour> getTourDetails(@Path("id") int tourId);

    @POST("api/admin/tours/add-full")
    Call<ResponseBody> addFullTour(@Body TourRequest request);

    @GET("api/admin/tours/huong-dan-vien")
    Call<List<NguoiDung>> getHuongDanViens();

    @DELETE("api/admin/tours/{id}")
    Call<ResponseBody> deleteTour(@Path("id") int maTour);

    @GET("api/admin/diem-den")
    Call<List<DiemDen>> getDiemDens();

    @PUT("api/admin/tours/{id}")
    Call<ResponseBody> updateTour(@Path("id") Integer id, @Body TourRequest request);
}
