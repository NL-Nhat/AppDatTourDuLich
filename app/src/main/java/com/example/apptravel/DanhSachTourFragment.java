package com.example.apptravel;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apptravel.adapters.TourAdapter;
import com.example.apptravel.models.Tour;

import java.util.ArrayList;
import java.util.List;

public class DanhSachTourFragment extends Fragment {

    private RecyclerView recyclerView;
    private TourAdapter tourAdapter;
    private List<Tour> tourList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_danh_sach_tour, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_tours);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Khởi tạo dữ liệu mẫu cho hiển thị
        tourList = new ArrayList<>();
        tourList.add(new Tour("Tour du lịch -\nĐà Nẵng", "$100", 5.0f, R.drawable.ic_launcher_background, false));
        tourList.add(new Tour("Tour du lịch -\nHà Nội", "$100", 5.0f, R.drawable.ic_launcher_background, false));
        tourList.add(new Tour("Tour du lịch -\nQuảng Ninh", "$100", 5.0f, R.drawable.ic_launcher_background, false));
        tourList.add(new Tour("Tour du lịch -\nCao Bằng", "$100", 5.0f, R.drawable.ic_launcher_background, false));
        tourList.add(new Tour("Tour du lịch -\nĐà Nẵng", "$100", 5.0f, R.drawable.ic_launcher_background, false));
        tourList.add(new Tour("Tour du lịch -\nHà Nội", "$100", 5.0f, R.drawable.ic_launcher_background, false));
        tourList.add(new Tour("Tour du lịch -\nĐà Nẵng", "$100", 5.0f, R.drawable.ic_launcher_background, false));
        tourList.add(new Tour("Tour du lịch -\nHà Nội", "$100", 5.0f, R.drawable.ic_launcher_background, false));
        tourList.add(new Tour("Tour du lịch -\nQuảng Ninh", "$100", 5.0f, R.drawable.ic_launcher_background, false));
        tourList.add(new Tour("Tour du lịch -\nCao Bằng", "$100", 5.0f, R.drawable.ic_launcher_background, false));
        tourList.add(new Tour("Tour du lịch -\nĐà Nẵng", "$100", 5.0f, R.drawable.ic_launcher_background, false));
        tourList.add(new Tour("Tour du lịch -\nHà Nội", "$100", 5.0f, R.drawable.ic_launcher_background, false));

        tourAdapter = new TourAdapter(getContext(), tourList);
        recyclerView.setAdapter(tourAdapter);

        return view;
    }
}