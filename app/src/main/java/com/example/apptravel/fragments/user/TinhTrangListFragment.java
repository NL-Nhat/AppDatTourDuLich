package com.example.apptravel.fragments.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptravel.R;
import com.example.apptravel.adapters.TinhTrangAdapter;
import com.example.apptravel.models.LichTrinhYeuCau;
import com.example.apptravel.models.TourDataManager;

import java.util.ArrayList;
import java.util.List;

public class TinhTrangListFragment extends Fragment {

    private static final String ARG_TRANG_THAI = "TRANG_THAI";
    private RecyclerView recyclerView;
    private TinhTrangAdapter adapter;
    private List<LichTrinhYeuCau> tourList;
    private String trangThai;

    public static TinhTrangListFragment newInstance(String trangThai) {
        TinhTrangListFragment fragment = new TinhTrangListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TRANG_THAI, trangThai);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            trangThai = getArguments().getString(ARG_TRANG_THAI);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page_layout, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_page);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadDataByTrangThai();

        adapter = new TinhTrangAdapter(getContext(), tourList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void loadDataByTrangThai() {
        tourList = new ArrayList<>();
        switch (trangThai) {
            case "CHO_XAC_NHAN":
                tourList.addAll(TourDataManager.getSelectedTours());
                break;
            case "DA_DUYET":
                // TODO: Tải dữ liệu các tour đã duyệt từ API hoặc database
                tourList.add(new LichTrinhYeuCau(R.drawable.da_nang, "Tour Du Lịch - Tham Quan, Check in tại Đà Nẵng City (1 Ngày 1 Đêm)", "", "Đà Nẵng", false));
                break;
            case "DA_HUY":
                // TODO: Tải dữ liệu các tour đã hủy từ API hoặc database
                tourList.add(new LichTrinhYeuCau(R.drawable.cao_bang, "Tour Du Lịch - Tham Quan, Cứu trợ tại Cao Bằng (2 Ngày 1 Đêm)", "Ngày 28 - Ngày 30/09/2025", "Cao Bằng, Việt Nam", true));
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ("CHO_XAC_NHAN".equals(trangThai) && adapter != null) {
            tourList.clear();
            tourList.addAll(TourDataManager.getSelectedTours());
            adapter.notifyDataSetChanged();
        }
    }
}
