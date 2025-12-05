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

        // 1. Ánh xạ RecyclerView (Kiểm tra ID trong file XML của bạn)
        recyclerView = view.findViewById(R.id.recycler_view_tours); // Đảm bảo ID này đúng với layout XML

        // 2. Cài đặt LayoutManager (Grid 2 cột)
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        // 3. Khởi tạo Adapter và Repository
        tourAdapter = new TourAdapter(getContext());
        tourRepository = new TourRepository(getContext());

        // 4. Gán sự kiện click (Quan trọng)
        tourAdapter.setOnTourClickListener(this);

        // 5. Gán Adapter cho RecyclerView
        recyclerView.setAdapter(tourAdapter);

        // 6. Gọi API lấy dữ liệu
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
    public void onTourClick(int position) {
        // Lấy đối tượng tour tại vị trí được click
        Tour selectedTour = listTour.get(position);

        // Chuyển màn hình và gửi dữ liệu
        Intent intent = new Intent(getActivity(), TourDetailActivity.class);

        //Class Tour phải "implements Serializable" mới gửi được kiểu này
        intent.putExtra("object_tour", selectedTour);

        startActivity(intent);
    }
}
