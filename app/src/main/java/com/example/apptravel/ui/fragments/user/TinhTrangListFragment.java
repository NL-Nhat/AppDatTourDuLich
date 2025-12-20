package com.example.apptravel.ui.fragments.user;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptravel.R;
import com.example.apptravel.data.adapters.TinhTrangAdapter;
import com.example.apptravel.data.models.DatTourHistoryItem;
import com.example.apptravel.data.models.LichTrinhYeuCau;
import com.example.apptravel.data.repository.DatTourRepository;
import com.example.apptravel.util.QuanLyDangNhap;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TinhTrangListFragment extends Fragment {

    private static final String TAG = "TinhTrangListFragment";

    private static final String ARG_TRANG_THAI = "TRANG_THAI";

    private RecyclerView recyclerView;
    private TinhTrangAdapter adapter;
    private final List<LichTrinhYeuCau> tourList = new ArrayList<>();

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

        adapter = new TinhTrangAdapter(getContext(), tourList);
        recyclerView.setAdapter(adapter);

        loadBookings();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadBookings();
    }

    private void loadBookings() {
        if (getContext() == null) return;

        QuanLyDangNhap session = new QuanLyDangNhap(getContext());
        if (!session.isLoggedIn()) {
            Log.w(TAG, "User not logged in -> clear list");
            Toast.makeText(getContext(), "Chưa đăng nhập - không tải được lịch sử", Toast.LENGTH_SHORT).show();
            tourList.clear();
            if (adapter != null) adapter.notifyDataSetChanged();
            return;
        }

        String statusParam = mapTrangThaiToApiStatus(trangThai);
        Log.d(TAG, "Loading bookings for tab=" + trangThai + " => statusParam=" + statusParam);

        DatTourRepository repo = new DatTourRepository(getContext());
        repo.getLichSuDatTour(statusParam).enqueue(new Callback<List<DatTourHistoryItem>>() {
            @Override
            public void onResponse(@NonNull Call<List<DatTourHistoryItem>> call, @NonNull Response<List<DatTourHistoryItem>> response) {
                Log.d(TAG, "HTTP " + response.code() + " for statusParam=" + statusParam);

                if (!response.isSuccessful()) {
                    String msg = "Không tải được lịch sử (HTTP " + response.code() + ")";
                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                    Log.w(TAG, msg);
                    return;
                }

                List<DatTourHistoryItem> body = response.body();
                if (body == null) {
                    Toast.makeText(getContext(), "Dữ liệu trả về rỗng (body=null)", Toast.LENGTH_SHORT).show();
                    Log.w(TAG, "Response body is null");
                    return;
                }

                Log.d(TAG, "Received " + body.size() + " bookings");
                if (body.isEmpty()) {
                    Toast.makeText(getContext(), "Không có dữ liệu cho trạng thái này", Toast.LENGTH_SHORT).show();
                }

                tourList.clear();
                for (DatTourHistoryItem item : body) {
                    String title = item.getTenTour() != null ? item.getTenTour() : "";
                    String location = item.getDiaDiem() != null ? item.getDiaDiem() : "";
                    String date = formatDateRange(item.getNgayKhoiHanh(), item.getNgayKetThuc());
                    String imageUrl = item.getUrlHinhAnhChinh();

                    // Dùng ảnh URL nếu có, fallback drawable mặc định
                    LichTrinhYeuCau uiItem;
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        uiItem = new LichTrinhYeuCau(imageUrl, title, date, location, false);
                    } else {
                        uiItem = new LichTrinhYeuCau(R.drawable.nen, title, date, location, false);
                    }
                    tourList.add(uiItem);
                }

                if (adapter != null) adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<List<DatTourHistoryItem>> call, @NonNull Throwable t) {
                String msg = "Lỗi kết nối: " + t.getMessage();
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                Log.e(TAG, msg, t);
            }
        });
    }

    private String mapTrangThaiToApiStatus(String trangThaiTab) {
        if (trangThaiTab == null) return null;

        switch (trangThaiTab) {
            case "DA_DUYET":
                return "DaXacNhan";
            case "CHO_XAC_NHAN":
                return "ChoXacNhan";
            case "DA_HUY":
                return "DaHuy";
            default:
                return null;
        }
    }

    private String formatDateRange(String start, String end) {
        String s = compactDate(start);
        String e = compactDate(end);
        if (s.isEmpty() && e.isEmpty()) return "";
        if (e.isEmpty()) return s;
        if (s.isEmpty()) return e;
        return s + " - " + e;
    }

    private String compactDate(String iso) {
        if (iso == null) return "";
        // backend thường trả ISO: 2025-11-20T08:00:00
        int tIdx = iso.indexOf('T');
        if (tIdx > 0) {
            return iso.substring(0, tIdx);
        }
        // nếu có khoảng trắng: 2025-11-20 08:00:00
        int spaceIdx = iso.indexOf(' ');
        if (spaceIdx > 0) {
            return iso.substring(0, spaceIdx);
        }
        return iso;
    }
}
