package com.example.apptravel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PhuongThucThanhToanActivity extends AppCompatActivity {

    private Button btnThanhToan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chonthanhtoan);

        // Ẩn Action Bar & set Status bar đen
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        // 1. Nhận dữ liệu tiền từ Activity trước
        long basePrice = getIntent().getLongExtra("FINAL_TOTAL", 910000); // Mặc định nếu không có
        long discount = 50000; // Giả sử giảm giá cố định
        long finalPrice = basePrice - discount;
        String  tourName = getIntent().getStringExtra("TOUR_NAME_TT"); // Nhận tên tour
        // 2. Ánh xạ View
        TextView tvBase = findViewById(R.id.tvBasePrice);
        TextView tvFinal = findViewById(R.id.tvFinalTotal);
        View btnBack = findViewById(R.id.btnBack);
        // 3. Hiển thị dữ liệu
        tvBase.setText(String.format("%,d VNĐ", basePrice));
        tvFinal.setText(String.format("%,d VNĐ", finalPrice));

        // 4. Xử lý sự kiện nút bấm
        btnBack.setOnClickListener(v -> finish());

        btnThanhToan = findViewById(R.id.btnConfirmPayment);

        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhuongThucThanhToanActivity.this, KetQuaThanhToanActivity.class);
                startActivity(intent);
            }
        });
    }
}