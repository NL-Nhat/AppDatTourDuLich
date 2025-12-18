package com.example.apptravel.ui.activitys.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.apptravel.R;
import com.example.apptravel.data.adapters.DanhGiaAdapter;
import com.example.apptravel.data.adapters.LichKhoiHanhAdapter;
import com.example.apptravel.data.api.ApiClient;
import com.example.apptravel.data.api.ApiService;
import com.example.apptravel.data.models.DanhGia;
import com.example.apptravel.data.models.LichKhoiHanh;
import com.example.apptravel.data.models.Tour;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TourDetailActivity extends AppCompatActivity {

    private ImageView imgTour, btnBack, btnShare, btnFavorite;
    private TextView txtTenTour, txtSoSao, txtDanhGia, txtDiaChi, txtGiaNguoiLon, txtGiaTreEm, txtMoTa, txtGiaTong;
    private RecyclerView rcvLich, rcvDanhGia;
    private Button btnDatNgay;

    private Tour tour; // Object nhận từ Intent
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_detail);

        apiService = ApiClient.getClient(this).create(ApiService.class);

        anhXa();
        layDuLieuIntent();
        suKienClick();
    }

    private void anhXa() {
        imgTour = findViewById(R.id.tour_image);
        btnBack = findViewById(R.id.btn_back);
        btnShare = findViewById(R.id.btn_share);
        btnFavorite = findViewById(R.id.btn_favorite);

        txtTenTour = findViewById(R.id.txt_tour_name);
        txtSoSao = findViewById(R.id.ttx_soSao);
        txtDanhGia = findViewById(R.id.ttx_danhGia);
        txtDiaChi = findViewById(R.id.txt_diaChi);
        txtGiaNguoiLon = findViewById(R.id.ttx_soNguoiLon);
        txtGiaTreEm = findViewById(R.id.ttx_soTreEm);
        txtMoTa = findViewById(R.id.tour_description);
        txtGiaTong = findViewById(R.id.txt_Gia); // Giá ở thanh Bottom

        rcvLich = findViewById(R.id.recycler_schedule);
        rcvDanhGia = findViewById(R.id.recycler_reviews);
        btnDatNgay = findViewById(R.id.btn_datNgay);

        // Setup RecyclerView
        rcvLich.setLayoutManager(new LinearLayoutManager(this));
        rcvDanhGia.setLayoutManager(new LinearLayoutManager(this));
    }

    private void layDuLieuIntent() {
        // Nhận object từ DanhSachTourFragment gửi sang
        tour = (Tour) getIntent().getSerializableExtra("object_tour");

        if (tour != null) {
            // 1. Hiển thị thông tin cơ bản
            txtTenTour.setText(tour.getTenTour());
            txtMoTa.setText(tour.getMoTa());
            txtSoSao.setText(String.valueOf(tour.getDiemDanhGiaTrungBinh()));
            txtDanhGia.setText("(" + tour.getSoLuongDanhGia() + " đánh giá)");
            txtDiaChi.setText(tour.getDiemDen().getThanhPho());

            // Format tiền
            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            txtGiaNguoiLon.setText(format.format(tour.getGiaNguoiLon()));
            txtGiaTreEm.setText(format.format(tour.getGiaTreEm()));
            txtGiaTong.setText(format.format(tour.getGiaNguoiLon())); // Mặc định hiển thị giá người lớn

            String duongDanAnh = "tour/" + tour.getUrlHinhAnhChinh();

            //Tạo URL đầy đủ
            String fullUrl = ApiClient.getFullImageUrl(this, duongDanAnh);

            // Load ảnh vào ImageView (biến anhDaiDien)
            Glide.with(this)
                    .load(fullUrl)
                    .placeholder(R.drawable.nen)
                    .error(R.drawable.ic_launcher_background)
                    .into(imgTour); // Load vào UI

            // 2. Gọi API lấy dữ liệu phụ (Lịch & Đánh giá)
            loadLichKhoiHanh(tour.getMaTour());
            loadDanhGia(tour.getMaTour());
        }
    }

    private void loadLichKhoiHanh(int maTour) {
        apiService.getLichKhoiHanh(maTour).enqueue(new Callback<List<LichKhoiHanh>>() {
            @Override
            public void onResponse(Call<List<LichKhoiHanh>> call, Response<List<LichKhoiHanh>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LichKhoiHanhAdapter adapter = new LichKhoiHanhAdapter(TourDetailActivity.this, response.body());
                    rcvLich.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(Call<List<LichKhoiHanh>> call, Throwable t) {
                android.util.Log.e("LoiAPI", "Lỗi lấy lịch: " + t.getMessage());
                Toast.makeText(TourDetailActivity.this, "Lỗi tải lịch trình!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDanhGia(int maTour) {
        apiService.getDanhGia(maTour).enqueue(new Callback<List<DanhGia>>() {
            @Override
            public void onResponse(Call<List<DanhGia>> call, Response<List<DanhGia>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DanhGiaAdapter adapter = new DanhGiaAdapter(TourDetailActivity.this, response.body());
                    rcvDanhGia.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(Call<List<DanhGia>> call, Throwable t) {
                android.util.Log.e("LoiAPI", "Lỗi lấy đánh giá: " + t.getMessage());
            }
        });
    }

    private void suKienClick() {
        btnBack.setOnClickListener(v -> finish()); // Quay lại màn hình trước

        btnDatNgay.setOnClickListener(v -> {
            Intent intent = new Intent(TourDetailActivity.this, NhapThongTinActivity.class);
            intent.putExtra("object_tour", tour);
            startActivity(intent);
        });
    }
}
