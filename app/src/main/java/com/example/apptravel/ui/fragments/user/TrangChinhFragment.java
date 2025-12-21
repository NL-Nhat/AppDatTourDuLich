package com.example.apptravel.ui.fragments.user;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptravel.data.adapters.TourAdapter;
import com.example.apptravel.data.models.Tour;
import com.example.apptravel.data.repository.TourRepository;
import com.example.apptravel.ui.activitys.user.MainActivity;
import com.example.apptravel.R;
import com.example.apptravel.ui.activitys.user.TourDetailActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Import các thư viện này
import android.util.Log;

public class TrangChinhFragment extends Fragment implements TourAdapter.OnTourClickListener{
    private TextView txtXemThem;
    private RecyclerView rcv_tour_doc, rcv_tour_ngang;
    private TourAdapter tourAdapter, tourAdapter2;
    private TourRepository tourRepository;
    private List<Tour> listTour = new ArrayList<>();
    private static final String API_TAG = "TrangChinh_API";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trangchinh, container, false);

        txtXemThem = view.findViewById(R.id.txtXemThem);
        rcv_tour_doc = view.findViewById(R.id.rcv_tour_doc);
        rcv_tour_ngang = view.findViewById(R.id.rcv_tour_ngang);

        txtXemThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("fragment_name", "DanhSachTourFragment");
                startActivity(intent);
            }
        });

        // Cài đặt LayoutManager (Grid 2 cột)
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rcv_tour_ngang);

        rcv_tour_ngang.setLayoutManager(layoutManager);
        rcv_tour_doc.setLayoutManager(gridLayoutManager);

        tourAdapter = new TourAdapter(getContext(), TourAdapter.TYPE_NORMAL);
        tourAdapter2 = new TourAdapter(getContext(), TourAdapter.TYPE_HOME);

        tourRepository = new TourRepository(getContext());

        tourAdapter.setOnTourClickListener(this);
        tourAdapter2.setOnTourClickListener(this);

        rcv_tour_ngang.setAdapter(tourAdapter2);
        rcv_tour_doc.setAdapter(tourAdapter);

        loadData();

        return view;
    }

    private void loadData() {
        Log.i(API_TAG, "Bắt đầu gọi API để lấy danh sách tour...");

        tourRepository.getAllTours().enqueue(new Callback<List<Tour>>() {
            @Override
            public void onResponse(Call<List<Tour>> call, Response<List<Tour>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i(API_TAG, "THÀNH CÔNG! Mã HTTP: " + response.code());
                    listTour = response.body();
                    Log.i(API_TAG, "Số lượng tour nhận được: " + listTour.size());

                    listTour = response.body(); // Lưu vào biến toàn cục
                    tourAdapter.setTourList(listTour); // Đẩy vào adapter

                    // Xử lý danh sách cho adapter ngang (chỉ lấy tối đa 10 tour)
                    List<Tour> listTop10 = new ArrayList<>();
                    if (listTour.size() > 10) {
                        listTop10 = new ArrayList<>(listTour.subList(0, 10));
                    } else {
                        listTop10 = new ArrayList<>(listTour);
                    }

                    tourAdapter2.setTourList(listTop10);

                    // --- HIỆU ỨNG VÒNG TRÒN ---
                    if (!listTop10.isEmpty()) {
                        // Đưa vị trí bắt đầu về giữa danh sách
                        int middle = Integer.MAX_VALUE / 2;
                        // Đảm bảo vị trí bắt đầu luôn là item đầu tiên (chia hết cho size)
                        int startPos = middle - (middle % listTop10.size());
                        rcv_tour_ngang.scrollToPosition(startPos);
                    }
                }else{
                    Log.e(API_TAG, "THẤT BẠI! Server phản hồi với mã lỗi HTTP: " + response.code());
                    Log.e(API_TAG, "Thông điệp lỗi: " + response.message());

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
        // Chuyển màn hình và gửi dữ liệu
        Intent intent = new Intent(getActivity(), TourDetailActivity.class);
        intent.putExtra("object_tour", tour);
        startActivity(intent);
    }
}
