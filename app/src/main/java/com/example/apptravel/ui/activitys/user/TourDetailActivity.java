package com.example.apptravel.ui.activitys.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apptravel.R;

public class TourDetailActivity extends AppCompatActivity {

    private Button btnDatNgay;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_detail);

        btnDatNgay = (Button) findViewById(R.id.btn_datNgay);
        btnBack = (ImageView) findViewById(R.id.btn_back);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnDatNgay.setOnClickListener(v -> {
            Intent intent = new Intent(TourDetailActivity.this, NhapThongTinActivity.class);
            startActivity(intent);
        });
    }
}
