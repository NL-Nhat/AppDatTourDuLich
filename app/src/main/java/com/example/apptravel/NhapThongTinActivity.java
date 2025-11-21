package com.example.apptravel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class NhapThongTinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhapthongtin);

        // Ẩn Action Bar mặc định vì mình đã tự làm cái custom đẹp hơn
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Đổi màu icon status bar sang màu đen
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        // Nhận tiền từ màn hình trước
        long totalAmount = getIntent().getLongExtra("TOTAL_AMOUNT", 0);
        String  tourName = getIntent().getStringExtra("TOUR_NAME"); // Nhận tên tour

        TextView tv_tourName = findViewById(R.id.tv_name_tour);
        tv_tourName.setText(tourName);
        // Cập nhật vào Footer
        TextView tvPrice = findViewById(R.id.tvInfoPrice);
        if (totalAmount > 0) {
            tvPrice.setText(String.format("%,d VNĐ", totalAmount));
        }
        TextView tv_tname = findViewById(R.id.tv_name_tour);
        String textName = tv_tname.getText().toString();

        // Nút Back
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        Button btnBookNow = findViewById(R.id.btnInfoBook);
        btnBookNow.setOnClickListener(v -> {
            Intent intent = new Intent(NhapThongTinActivity.this, PhuongThucThanhToanActivity.class);
            intent.putExtra("FINAL_TOTAL", totalAmount);
            intent.putExtra("TOUR_NAME_TT", textName);
            startActivity(intent);
        });
    }
}