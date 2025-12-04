package com.example.apptravel.ui.fragments.user;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.apptravel.data.api.ApiClient;
import com.example.apptravel.ui.DangNhapActivity;
import com.example.apptravel.R;
import com.example.apptravel.ui.activitys.user.CaiDatActivity;
import com.example.apptravel.ui.activitys.user.ThongTinCaNhanActivity;
import com.example.apptravel.util.QuanLyDangNhap;

import java.util.Map;

public class TrangCaNhanFragment extends Fragment {

    private LinearLayout userInfo;
    private LinearLayout logout;
    private LinearLayout caiDat;
    private TextView txtTen, txtEmail;
    private QuanLyDangNhap quanLyDangNhap;
    private ImageView anhDaiDien;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trang_ca_nhan, container, false);

        quanLyDangNhap = new QuanLyDangNhap(getContext());
        userInfo = view.findViewById(R.id.user_info);
        logout = view.findViewById(R.id.logout);
        caiDat = view.findViewById(R.id.caiDat);
        txtTen = view.findViewById(R.id.txtTen);
        txtEmail = view.findViewById(R.id.txtEmail);
        anhDaiDien = view.findViewById(R.id.profile_image);

        GanThongTin();

        userInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ThongTinCaNhanActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quanLyDangNhap.DangXuat();
                Intent intent = new Intent(getActivity(), DangNhapActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        caiDat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CaiDatActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void GanThongTin() {
        if (quanLyDangNhap.isLoggedIn()) {
            // 1. Hiển thị tên và email
            txtTen.setText(quanLyDangNhap.LayHoTen());
            txtEmail.setText(quanLyDangNhap.LayEmail());

            // 2. Lấy tên file ảnh từ SharedPreferences (VD: "hinh1.jpg")
            String tenFileAnh = quanLyDangNhap.LayAnhDaiDien();

            // 3. Kiểm tra xem có tên ảnh không để tránh lỗi null
            if (tenFileAnh != null && !tenFileAnh.isEmpty()) {

                // 4. Ghép thư mục server: "avatar/" + "hinh1.jpg"
                String duongDanAnh = "avatar/" + tenFileAnh;

                // 5. Tạo URL đầy đủ
                String fullUrl = ApiClient.getFullImageUrl(getContext(), duongDanAnh);

                // 6. Load ảnh vào ImageView (biến anhDaiDien)
                Glide.with(getContext())
                        .load(fullUrl)
                        .placeholder(R.drawable.nen)
                        .error(R.drawable.ic_launcher_background)
                        .into(anhDaiDien); // Load vào UI
            }
        }
    }

    // Cập nhật lại giao diện mỗi khi Fragment được hiển thị lại
    @Override
    public void onResume() {
        super.onResume();
        GanThongTin();
    }
}