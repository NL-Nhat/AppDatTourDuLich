package com.example.apptravel.ui.fragments.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptravel.R;
import com.example.apptravel.data.adapters.TourAdminAdapter;
import com.example.apptravel.data.api.ApiClient;
import com.example.apptravel.data.api.ApiService;
import com.example.apptravel.data.models.Tour;
import com.example.apptravel.ui.activitys.admin.ThemTourActivity;
import com.example.apptravel.util.QuanLyDangNhap; // Import thêm
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuanLyTourAdminFragment extends Fragment {

    private RecyclerView rcvTours;
    private TourAdminAdapter adapter;
    private List<Tour> tourList = new ArrayList<>();

    private TextView tvTotalTours, tvActiveTours, tvInactiveTours;
    private FloatingActionButton btnAddTour;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quan_ly_tour_admin, container, false);

        rcvTours = view.findViewById(R.id.recycler_tours);
        tvTotalTours = view.findViewById(R.id.stat_total_tours);
        tvActiveTours = view.findViewById(R.id.stat_active_tours);
        tvInactiveTours = view.findViewById(R.id.stat_inactive_tours);
        btnAddTour = view.findViewById(R.id.ic_them_tour);

        rcvTours.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TourAdminAdapter(tourList, getContext());
        rcvTours.setAdapter(adapter);

        btnAddTour.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ThemTourActivity.class);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadToursFromServer();
    }

    private void loadToursFromServer() {
        if (getContext() == null) return;
        
        ApiService apiService = ApiClient.getClient(getContext()).create(ApiService.class);
        apiService.getAdminTours().enqueue(new Callback<List<Tour>>() {
            @Override
            public void onResponse(Call<List<Tour>> call, Response<List<Tour>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tourList.clear();
                    tourList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    updateStatistics();
                }
            }

            @Override
            public void onFailure(Call<List<Tour>> call, Throwable t) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Lỗi tải danh sách: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateStatistics() {
        int total = tourList.size();
        int active = 0;
        int inactive = 0;

        for (Tour tour : tourList) {
            if ("DangMo".equalsIgnoreCase(tour.getTrangThai())) {
                active++;
            } else {
                inactive++;
            }
        }

        tvTotalTours.setText(String.valueOf(total));
        tvActiveTours.setText(String.valueOf(active));
        tvInactiveTours.setText(String.valueOf(inactive));
    }
}
