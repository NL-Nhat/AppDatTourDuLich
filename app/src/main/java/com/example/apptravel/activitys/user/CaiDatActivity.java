package com.example.apptravel.activitys.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.apptravel.R;

public class CaiDatActivity extends AppCompatActivity {

    private ImageView btnBack;
    private LinearLayout thongTinTaiKhoan;
    private LinearLayout doiMatKhau;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cai_dat);

        btnBack = findViewById(R.id.btn_back);
        thongTinTaiKhoan = findViewById(R.id.thong_tin_tai_khoan);
        doiMatKhau =findViewById(R.id.doiMatKhau);

        // Xử lý nút back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        thongTinTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CaiDatActivity.this, ThongTinCaNhanActivity.class);
                startActivity(intent);
            }
        });

        doiMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CaiDatActivity.this, DoiMatKhauActivity.class);
                startActivity(intent);
            }
        });
    }
}