package com.example.apptravel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.apptravel.models.Tour;
import com.example.apptravel.models.TourData;

import java.util.List;

public class TourDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_detail);
        AppCompatButton btnBookNow = findViewById(R.id.btn_book_now);

        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> onBackPressed());
        }

        int position = getIntent().getIntExtra("tour_position", -1);

        if (position != -1) {
            List<Tour> tourList = TourData.getTourList();
            if (position >= 0 && position < tourList.size()) {
                Tour tour = tourList.get(position);

                ImageView imgTourBanner = findViewById(R.id.img_tour_banner);
                if (imgTourBanner != null) {
                    imgTourBanner.setImageResource(tour.getImageResId());
                }

                TextView tvTourName = findViewById(R.id.tv_tour_name);
                if (tvTourName != null) {
                    tvTourName.setText(tour.getTitle().replace("-\n", " "));
                }

                TextView tvTourLocation = findViewById(R.id.tv_tour_location);
                if (tvTourLocation != null) {
                    tvTourLocation.setText(tour.getLocation());
                }

                TextView tvTourPrice = findViewById(R.id.tv_tour_price);
                if (tvTourPrice != null) {
                    tvTourPrice.setText("Giá từ " + tour.getPrice());
                }

                TextView tvPriceBottom = findViewById(R.id.tv_price_bottom);
                if (tvPriceBottom != null) {
                    tvPriceBottom.setText(tour.getPrice() + " VND");
                }
            }
        }
        TextView tvPrice = findViewById(R.id.tv_price_bottom);
        String priceText = tvPrice.getText().toString();
        String cleanPrice = priceText.replaceAll("[^\\d]", ""); // loại bỏ VNĐ và dấu phẩy
        btnBookNow.setOnClickListener(v -> {
            // Giá vé cơ bản
            long pricePerPerson = Long.parseLong(cleanPrice);

            // Chuyển sang màn hình điền thông tin (InfoActivity)
            Intent intent = new Intent(TourDetailActivity.this, NhapThongTinActivity.class);

            // Truyền giá tiền sang để màn hình sau tính toán
            intent.putExtra("TOTAL_AMOUNT", pricePerPerson);
            startActivity(intent);
        });
    }
}
