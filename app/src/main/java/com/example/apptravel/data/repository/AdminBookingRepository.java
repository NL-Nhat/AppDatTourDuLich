package com.example.apptravel.data.repository;

import android.content.Context;

import com.example.apptravel.data.api.ApiClient;
import com.example.apptravel.data.api.ApiService;
import com.example.apptravel.data.models.AdminBookingItem;

import java.util.List;

import retrofit2.Call;

public class AdminBookingRepository {

    private final ApiService api;

    public AdminBookingRepository(Context context) {
        api = ApiClient.getClient(context).create(ApiService.class);
    }

    public Call<List<AdminBookingItem>> getBookings(String status, String q) {
        return api.getAdminBookings(status, q);
    }

    public Call<AdminBookingItem> getBookingDetail(int maDatTour) {
        return api.getAdminBookingDetail(maDatTour);
    }

    public Call<Void> confirmBooking(int maDatTour) {
        return api.confirmAdminBooking(maDatTour);
    }

    public Call<Void> cancelBooking(int maDatTour, String lyDoHuy) {
        return api.cancelAdminBooking(maDatTour, new com.example.apptravel.data.models.CancelBookingRequest(lyDoHuy));
    }
}

