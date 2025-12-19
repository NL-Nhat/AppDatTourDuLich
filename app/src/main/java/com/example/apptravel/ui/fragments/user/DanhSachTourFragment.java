package com.example.apptravel.ui.fragments.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptravel.R;
import com.example.apptravel.data.adapters.TourAdapter;
import com.example.apptravel.data.models.Tour;
import com.example.apptravel.data.repository.TourRepository;
import com.example.apptravel.ui.activitys.user.TourDetailActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DanhSachTourFragment extends Fragment implements TourAdapter.OnTourClickListener {

    private RecyclerView recyclerView;
    private TourAdapter tourAdapter;
    private TourRepository tourRepository;
    private List<Tour> listTour = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_danh_sach_tour, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_tours);
        //đặt LayoutManager (Grid 2 cột)
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        tourAdapter = new TourAdapter(getContext(), TourAdapter.TYPE_NORMAL);
        tourRepository = new TourRepository(getContext());

        tourAdapter.setOnTourClickListener(this);
        recyclerView.setAdapter(tourAdapter);

        loadData();

        return view;
    }

    private void loadData() {
        tourRepository.getAllTours().enqueue(new Callback<List<Tour>>() {
            @Override
            public void onResponse(Call<List<Tour>> call, Response<List<Tour>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listTour = response.body(); // Lưu vào biến toàn cục
                    tourAdapter.setTourList(listTour); // Đẩy vào adapter
                }
            }

            @Override
            public void onFailure(Call<List<Tour>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onTourClick(Tour tour) {
        Intent intent = new Intent(getActivity(), TourDetailActivity.class);
        intent.putExtra("object_tour", tour);
        startActivity(intent);
    }
}
