package com.example.apptravel.ui.activitys.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apptravel.ui.DangNhapActivity;
import com.example.apptravel.R;

public class DangKyActivity extends AppCompatActivity {

    private Button btnDangKy;
    private TextView txtDangNhap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangky);

        btnDangKy = (Button) findViewById(R.id.btnDangKy);
        txtDangNhap = (TextView) findViewById(R.id.txtDangNhap);

        btnDangKy.setOnClickListener(v -> {
            Intent intent = new Intent(DangKyActivity.this, DangNhapActivity.class);
            startActivity(intent);
        });

        txtDangNhap.setOnClickListener(v -> {
            Intent intent = new Intent(DangKyActivity.this, DangNhapActivity.class);
            startActivity(intent);
        });
    }
}
