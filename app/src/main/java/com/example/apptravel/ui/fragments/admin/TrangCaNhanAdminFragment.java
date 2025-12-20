package com.example.apptravel.ui.fragments.admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.apptravel.data.api.ApiClient;
import com.example.apptravel.ui.DangNhapActivity;
import com.example.apptravel.R;
import com.example.apptravel.ui.activitys.user.DoiMatKhauActivity;
import com.example.apptravel.ui.activitys.user.ThongTinCaNhanActivity;
import com.example.apptravel.util.QuanLyDangNhap;

public class TrangCaNhanAdminFragment extends Fragment {

    private LinearLayout menu_editInfo;
    private LinearLayout menu_doiMk;
    private LinearLayout menu_logout;
    private QuanLyDangNhap quanLyDangNhap;
    private ImageView anhDaiDien;
    private TextView txtTen, txtEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trang_ca_nhan_admin, container, false);

        menu_editInfo = view.findViewById(R.id.menu_edit_info);
        menu_doiMk = view.findViewById(R.id.menu_doiMK);
        menu_logout = view.findViewById(R.id.menu_logout);
        quanLyDangNhap = new QuanLyDangNhap(getContext());
        txtEmail = view.findViewById(R.id.admin_email);
        txtTen = view.findViewById(R.id.admin_name);
        anhDaiDien = view.findViewById(R.id.admin_avatar);

        GanThongTin();

        menu_editInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ThongTinCaNhanActivity.class);
                startActivity(intent);
            }
        });

        menu_doiMk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DoiMatKhauActivity.class);
                startActivity(intent);
            }
        });

        menu_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DangNhapActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void GanThongTin() {

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

    // Cập nhật lại giao diện mỗi khi Fragment được hiển thị lại
    @Override
    public void onResume() {
        super.onResume();
        GanThongTin();
    }
}