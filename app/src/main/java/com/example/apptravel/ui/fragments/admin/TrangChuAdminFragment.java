package com.example.apptravel.ui.fragments.admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.apptravel.R;
import com.example.apptravel.data.adapters.HoatDongAdapter;
import com.example.apptravel.data.api.ApiClient;
import com.example.apptravel.data.models.HoatDong;
import com.example.apptravel.data.repository.HoatDongRepository;
import com.example.apptravel.ui.activitys.admin.QuanLyNguoiDungActivity;
import com.example.apptravel.util.QuanLyDangNhap;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrangChuAdminFragment extends Fragment {

    private ImageView img_avatar;
    private TextView txt_name;
    private QuanLyDangNhap quanLyDangNhap;
    private RecyclerView recyclerView;
    private HoatDongRepository hoatDongRepository;
    private List<HoatDong> listHoatDong;
    private ImageView btnQuanLyNguoiDung;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trang_chu_admin, container, false);

        quanLyDangNhap = new QuanLyDangNhap(getContext());
        img_avatar = view.findViewById(R.id.admin_avatar);
        txt_name = view.findViewById(R.id.admin_name);
        recyclerView = view.findViewById(R.id.rcv_hoatdong);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        hoatDongRepository = new HoatDongRepository(getContext());

        // --- BẮT ĐẦU PHẦN THÊM MỚI ---
        // 1. Ánh xạ nút từ layout
        btnQuanLyNguoiDung = view.findViewById(R.id.btn_quanlynguoidung);

        // 2. Gán sự kiện click cho nút
        btnQuanLyNguoiDung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo một Intent để mở QuanLyNguoiDungActivity
                Intent intent = new Intent(getActivity(), QuanLyNguoiDungActivity.class);
                startActivity(intent);
            }
        });
        // --- KẾT THÚC PHẦN THÊM MỚI ---

        LoadData();

        return view;
    }

    private void LoadData() {
        txt_name.setText(quanLyDangNhap.LayHoTen());

        String tenFileAnh = quanLyDangNhap.LayAnhDaiDien();

        if (tenFileAnh != null && !tenFileAnh.isEmpty()) {
            String fullUrl = ApiClient.getFullImageUrl(tenFileAnh);

            Glide.with(this)
                    .load(fullUrl)
                    .placeholder(R.drawable.nen)
                    .error(R.drawable.ic_launcher_background)
                    .timeout(60000)
                    .into(img_avatar);
        }

        hoatDongRepository.getAllHoatDong().enqueue(new Callback<List<HoatDong>>() {
            @Override
            public void onResponse(Call<List<HoatDong>> call, Response<List<HoatDong>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    HoatDongAdapter hoatDongAdapter = new HoatDongAdapter(getContext(), response.body());
                    recyclerView.setAdapter(hoatDongAdapter);
                }
            }
            @Override
            public void onFailure(Call<List<HoatDong>> call, Throwable t) {
                if (isAdded() && getActivity() != null) {
                    android.util.Log.e("LoiAPI", "Lỗi lấy hoạt động: " + t.getMessage());
                    Toast.makeText(getContext(), "Lỗi tải hoạt động!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}