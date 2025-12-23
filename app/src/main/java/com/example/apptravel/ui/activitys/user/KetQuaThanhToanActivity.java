package com.example.apptravel.ui.activitys.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apptravel.R;
import com.example.apptravel.util.QuanLyDangNhap;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class KetQuaThanhToanActivity extends AppCompatActivity {

    private Button btn_xemVeDienTu, btn_trangChu;
    private TextView txtTongTien, txtTenKhachHang, txtPhuongThuc, txtNgayGio;
    private int maDatTour;
    private QuanLyDangNhap quanLyDangNhap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ket_qua_thanh_toan);

        quanLyDangNhap = new QuanLyDangNhap(this);
        anhXa();
        txtTenKhachHang.setText(quanLyDangNhap.LayHoTen());
        layDuLieuIntent();
        xuLySuKien();
    }

    private void anhXa() {
        btn_xemVeDienTu = findViewById(R.id.btn_xemVeDienTu);
        btn_trangChu = findViewById(R.id.btn_trangChu);
        txtNgayGio = findViewById(R.id.txtNgayGio);
        txtPhuongThuc = findViewById(R.id.txtPhuongThuc);
        txtTenKhachHang = findViewById(R.id.txtTenKhachHang);
        txtTongTien = findViewById(R.id.txtTongTien);
    }

    private void layDuLieuIntent() {
        maDatTour = getIntent().getIntExtra("maDatTour", -1);

        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        txtTongTien.setText(decimalFormat.format(getIntent().getDoubleExtra("tongTien", 0)) + " VNĐ");

        txtPhuongThuc.setText(getIntent().getStringExtra("phuongThuc"));

        long ngayGio = getIntent().getLongExtra("ngayGioThanhToan", 0);
        txtNgayGio.setText(new SimpleDateFormat(
                "dd/MM/yyyy - HH:mm", Locale.getDefault()
        ).format(new Date(ngayGio)));

    }

    private void xuLySuKien() {

        btn_trangChu.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            finish();
        });

        btn_xemVeDienTu.setOnClickListener(v -> {
            Intent intent = new Intent(this, VeDienTuActivity.class);
            intent.putExtra("maDatTour", maDatTour);
            intent.putExtra("tenKhachHang", quanLyDangNhap.LayHoTen());
            startActivity(intent);
            finish();
        });



    }
}