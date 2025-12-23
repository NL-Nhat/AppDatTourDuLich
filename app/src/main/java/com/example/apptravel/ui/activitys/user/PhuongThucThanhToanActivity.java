package com.example.apptravel.ui.activitys.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apptravel.R;
import com.example.apptravel.data.api.ApiClient;
import com.example.apptravel.data.api.ApiService;
import com.example.apptravel.data.models.ThanhToanRequest;

import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhuongThucThanhToanActivity extends AppCompatActivity {

    private Button btnThanhToan;
    private ImageView btn_back;
    private RadioGroup rgPhuongThuc;
    private TextView txtTongTien;
    private int maDatTour;
    private double tongTien;
    private ThanhToanRequest thanhToanRequest;
    private ApiService apiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chonthanhtoan);

        thanhToanRequest = new ThanhToanRequest();
        apiService = ApiClient.getClient(this).create(ApiService.class);

        anhXa();
        layDuLieuIntent();
        xuLySuKien();
    }

    private void anhXa() {
        btn_back = findViewById(R.id.btnBack);
        btnThanhToan = findViewById(R.id.btnThanhToan);
        rgPhuongThuc = findViewById(R.id.rgPhuongThuc);
        txtTongTien = findViewById(R.id.txtTongTien);
    }

    private void layDuLieuIntent() {
        maDatTour = getIntent().getIntExtra("maDatTour", -1);
        tongTien = getIntent().getDoubleExtra("tongTien", 0);
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        txtTongTien.setText(decimalFormat.format(getIntent().getDoubleExtra("tongTien", 0)) + " VNĐ");
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
            String phuongThuc2 = "";
            if (selectedId == R.id.rbChuyenKhoan) {
                phuongThuc = "ChuyenKhoan";
                phuongThuc2 = "Chuyển khoản";
            }
            else if (selectedId == R.id.rbViDienTu) {
                phuongThuc = "ViDienTu";
                phuongThuc2 = "Ví điện tử";
            }
            else if (selectedId == R.id.rbTheTinDung) {
                phuongThuc = "TheTinDung";
                phuongThuc2 = "Thẻ tín dụng";
            }

            thanhToanRequest.setMaDatTour(maDatTour);
            thanhToanRequest.setPhuongThuc(phuongThuc);
            thanhToanRequest.setSoTien(tongTien);
            taoThanhToan(thanhToanRequest, phuongThuc2);
        });
    }

    private void taoThanhToan(ThanhToanRequest thanhToanRequest, String phuongThuc2) {
        btnThanhToan.setEnabled(false); // Khóa nút
        btnThanhToan.setText("Đang xử lý...");

        apiService.createThanhToan(thanhToanRequest).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                btnThanhToan.setEnabled(true); // Mở lại nút
                btnThanhToan.setText("ĐẶT NGAY");
                if (response.isSuccessful()) {

                    long timeMillis = System.currentTimeMillis();

                    Intent intent = new Intent(PhuongThucThanhToanActivity.this, KetQuaThanhToanActivity.class);
                    intent.putExtra("maDatTour", maDatTour);
                    intent.putExtra("tongTien", tongTien);
                    intent.putExtra("phuongThuc", phuongThuc2);
                    intent.putExtra("ngayGioThanhToan", timeMillis);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(
                            PhuongThucThanhToanActivity.this,
                            "Thanh toán thất bại",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                btnThanhToan.setEnabled(true); // Mở lại nút
                btnThanhToan.setText("ĐẶT NGAY");
                Toast.makeText(PhuongThucThanhToanActivity.this, "Lỗi kết nối API", Toast.LENGTH_SHORT).show();
            }
        });
    }
}