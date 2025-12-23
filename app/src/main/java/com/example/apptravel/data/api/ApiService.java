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

    //Đăng nhập
    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    //Lấy danh sách tour
    @GET("api/tour/all")
    Call<List<Tour>> getAllTours();

    //Lấy danh sách lịch khởi hàn theo tour
    @GET("api/tour/{id}/lich-khoi-hanh")
    Call<List<LichKhoiHanh>> getLichKhoiHanh(@Path("id") int maTour);

    //Lấy danh sách đánh giá theo tour
    @GET("api/tour/{id}/danh-gia")
    Call<List<DanhGia>> getDanhGia(@Path("id") int maTour);

    //Lấy hoạt động hệ thống (phần trang chủ admin)
    @GET("api/admin/hoatdong")
    Call<List<HoatDong>> getHoatDong();

    //Lấy danh sách tỉnh thành
    @GET("api/address/provinces")
    Call<List<Province>> getProvinces();

    //Lấy danh sách quận huyện
    @GET("api/address/districts/{code}")
    Call<DistrictResponse> getDistricts(@Path("code") String code);

    //Lấy danh sách phường xã
    @GET("api/address/wards/{code}")
    Call<WardResponse> getWards(@Path("code") String code);

    //Lấy thông tin người dùng
    @GET("api/auth/user/{id}")
    Call<NguoiDung> getNguoiDungById(@Path("id") String maNguoiDung);

    //Cập nhật thông tin người dùng
    @PUT("api/auth/user/{id}")
    Call<NguoiDung> updateNguoiDung(@Path("id") String userId, @Body NguoiDung nguoiDung);

    //Cập nhật ảnh đại diện
    @Multipart
    @POST("api/auth/uploadAnhDaiDien")
    Call<Map<String, String>> uploadAnhDaiDien(@Part MultipartBody.Part file);

    //Lấy danh sách tour đã đặt
    @GET("api/user/bookings")
    Call<List<DatTourHistoryItem>> getUserBookings(@Query("status") String status);

    //Hủy tour đã đặt
    @POST("api/user/bookings/{id}/cancel")
    Call<Void> cancelUserBooking(@Path("id") int maDatTour, @Body CancelBookingRequest request);

    // Đôi mật khẩu
    @PUT("api/auth/user/{id}/doiMatKhau")
    Call<ResponseBody> doiMatKhau(@Path("id") String userId, @Body ChangePasswordRequest request);

    //Lấy danh sách tour đã đặt phần admin
    @GET("api/admin/bookings")
    Call<List<AdminBookingItem>> getAdminBookings(@Query("status") String status, @Query("q") String q);

    //Lây thông tin chi tiết tour đã đặt phần admin
    @GET("api/admin/bookings/{id}")
    Call<AdminBookingItem> getAdminBookingDetail(@Path("id") int maDatTour);

    //Xác nhận tour đã đặt phần admin
    @POST("api/admin/bookings/{id}/confirm")
    Call<Void> confirmAdminBooking(@Path("id") int maDatTour);

    // Hủy tour đã đặt phần admin
    @POST("api/admin/bookings/{id}/cancel")
    Call<Void> cancelAdminBooking(@Path("id") int maDatTour, @Body CancelBookingRequest request);

    //Đăt tour
    @POST("api/bookings/create")
    Call<BookingResponse> createBooking(@Body BookingRequest bookingRequest);

    //Đăng ký
    @POST("api/auth/register")
    Call<ResponseBody> register(@Body RegisterRequest request);

    //Lấy thông tin đặt tour, kiểm tra trạng thái thanh toán
    @GET("api/bookings/{id}")
    Call<ViDienTuResponse> getLayThongTinVi(@Path("id") int maDatTour);

    //Thanh toán
    @POST("api/thanhtoan/create")
    Call<Void> createThanhToan(@Body ThanhToanRequest thanhToanRequest);

    // Lấy danh sách người dùng
    @GET("api/auth/users")
    Call<List<NguoiDung>> getAllUsers();

    // Lấy danh sách tour phần admin
    @GET("api/admin/tours")
    Call<List<Tour>> getAdminTours();

    // Xem chi tiết tour phần admin
    @GET("api/admin/tours/{id}")
    Call<Tour> getTourDetails(@Path("id") int tourId);

    // Thêm tour
    @POST("api/admin/tours/add-full")
    Call<ResponseBody> addFullTour(@Body TourRequest request);

    // Lấy danh sách hướng dẫn viên
    @GET("api/admin/tours/huong-dan-vien")
    Call<List<NguoiDung>> getHuongDanViens();

    // Xóa tour
    @DELETE("api/admin/tours/{id}")
    Call<ResponseBody> deleteTour(@Path("id") int maTour);

    //Lấy danh sách điểm đến
    @GET("api/admin/diem-den")
    Call<List<DiemDen>> getDiemDens();

    //Cập nhật tour
    @PUT("api/admin/tours/{id}")
    Call<ResponseBody> updateTour(@Path("id") Integer id, @Body TourRequest request);
}
