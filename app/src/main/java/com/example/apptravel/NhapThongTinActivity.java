package com.example.apptravel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
<<<<<<< Updated upstream
=======
import android.widget.Button;
import android.widget.ImageView;
>>>>>>> Stashed changes
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

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

        // Cập nhật vào Footer
        TextView tvPrice = findViewById(R.id.tvInfoPrice);
        if (totalAmount > 0) {
            tvPrice.setText(String.format("%,d VNĐ", totalAmount));
        }

        // Nút Back
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
<<<<<<< Updated upstream

=======
        Button btnBookNow = findViewById(R.id.btnInfoBook);
        btnBookNow.setOnClickListener(v -> {
            Intent intent = new Intent(NhapThongTinActivity.this, PhuongThucThanhToanActivity.class);
            intent.putExtra("FINAL_TOTAL", totalAmount);
            intent.putExtra("TOUR_NAME_TT", textName);
            startActivity(intent);
        });
        setupCounter(
                findViewById(R.id.btnPlusAdult),
                findViewById(R.id.btnMinusAdult),
                findViewById(R.id.tvCountAdult),
                1 // Adult min = 1
        );

        setupCounter(
                findViewById(R.id.btnPlusChild),
                findViewById(R.id.btnMinusChild),
                findViewById(R.id.tvCountChild),
                0 // Child min = 0
        );
>>>>>>> Stashed changes

    }
    private void setupCounter(ImageView btnPlus, ImageView btnMinus, TextView tvCount, int minValue) {
        btnPlus.setOnClickListener(v -> {
            int count = Integer.parseInt(tvCount.getText().toString());
            count++;
            tvCount.setText(String.valueOf(count));
        });

        btnMinus.setOnClickListener(v -> {
            int count = Integer.parseInt(tvCount.getText().toString());
            if (count > minValue) count--;
            tvCount.setText(String.valueOf(count));
        });
    }

}