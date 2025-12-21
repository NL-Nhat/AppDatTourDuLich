package com.example.apptravel.ui.activitys.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apptravel.R;

public class PhuongThucThanhToanActivity extends AppCompatActivity {

    private Button btnThanhToan;
    private ImageView btn_back;
    private RadioGroup rgPhuongThuc;
    private TextView txtTongTien;
    private int maDatTour;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chonthanhtoan);

        maDatTour = getIntent().getIntExtra("maDatTour", -1);

        anhXa();
        xuLySuKien();
    }

    private void anhXa() {
        btn_back = findViewById(R.id.btnBack);
        btnThanhToan = findViewById(R.id.btnThanhToan);
        rgPhuongThuc = findViewById(R.id.rgPhuongThuc);
        txtTongTien = findViewById(R.id.txtTongTien);
    }

    private void xuLySuKien() {

        btn_back.setOnClickListener(v -> {
            finish();
        });

        btnThanhToan.setOnClickListener(v -> {
            int selectedId = rgPhuongThuc.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(this, "Vui lòng chọn phương thức!", Toast.LENGTH_SHORT).show();
                return;
            }

            String phuongThuc = "";
            if (selectedId == R.id.rbChuyenKhoan) phuongThuc = "ChuyenKhoan";
            else if (selectedId == R.id.rbViDienTu) phuongThuc = "ViDienTu";
            else if (selectedId == R.id.rbTheTinDung) phuongThuc = "TheTinDung";

            guiThanhToanLenServer(phuongThuc);
        });
    }

    private void guiThanhToanLenServer(String method) {

    }
}