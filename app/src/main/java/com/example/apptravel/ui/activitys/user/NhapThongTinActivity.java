package com.example.apptravel.ui.activitys.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.apptravel.R;

public class NhapThongTinActivity extends AppCompatActivity {

    private Button btnInfoBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhapthongtin);

        // Ẩn Action Bar mặc định
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Đổi màu icon status bar sang màu đen
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        // Nhận tiền từ màn hình trước
        long totalAmount = getIntent().getLongExtra("TOTAL_AMOUNT", 0);

        // Cập nhật vào Footer
        TextView tvPrice = findViewById(R.id.tvInfoPrice);
        if (totalAmount > 0) {
            tvPrice.setText(String.format("%,d VNĐ", totalAmount));
        }

        // Nút Back
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        btnInfoBook = findViewById(R.id.btnInfoBook);
        btnInfoBook.setOnClickListener(v -> {
            Intent intent = new Intent(NhapThongTinActivity.this, PhuongThucThanhToanActivity.class);
            startActivity(intent);
        });
    }
}