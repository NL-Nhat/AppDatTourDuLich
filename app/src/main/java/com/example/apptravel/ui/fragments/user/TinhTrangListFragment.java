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
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import com.example.apptravel.data.models.DatTourHistoryItem;
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
    private final List<DatTourHistoryItem> bookingList = new ArrayList<>();

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

        boolean showCancel = "CHO_XAC_NHAN".equals(trangThai);
        adapter = new TinhTrangAdapter(getContext(), bookingList, showCancel, this::showCancelDialog);
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
            bookingList.clear();
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

                bookingList.clear();
                bookingList.addAll(body);

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

    private void showCancelDialog(DatTourHistoryItem item) {
        if (getContext() == null) return;

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_cancel_booking, null, false);
        TextInputLayout til = dialogView.findViewById(R.id.til_cancel_reason);
        TextInputEditText et = dialogView.findViewById(R.id.et_cancel_reason);

        AlertDialog dialog = new MaterialAlertDialogBuilder(getContext())
                .setTitle("Hủy đặt tour")
                .setMessage("Vui lòng cho biết lý do hủy.")
                .setView(dialogView)
                .setNegativeButton("Đóng", (d, w) -> d.dismiss())
                // set null listener để tự xử lý validate và KHÔNG auto dismiss khi lỗi
                .setPositiveButton("Hủy tour", null)
                .create();

        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                String reason = et.getText() != null ? et.getText().toString().trim() : "";

                if (reason.isEmpty()) {
                    til.setError("Vui lòng nhập lý do hủy");
                    return;
                }

                til.setError(null);
                dialog.dismiss();
                callCancelBooking(item.getMaDatTour(), reason);
            });
        });

        dialog.show();
    }

    private void callCancelBooking(int maDatTour, String reason) {
        if (getContext() == null) return;

        DatTourRepository repo = new DatTourRepository(getContext());
        repo.cancelBooking(maDatTour, reason).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "Hủy thất bại (HTTP " + response.code() + ")", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getContext(), "Đã hủy đặt tour", Toast.LENGTH_SHORT).show();
                loadBookings();
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

