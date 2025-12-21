package com.example.apptravel.ui.activitys.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apptravel.data.api.ApiClient;
import com.example.apptravel.data.api.ApiService;
import com.example.apptravel.data.models.ViDienTuResponse;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import com.example.apptravel.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VeDienTuActivity extends AppCompatActivity {

    private TextView txtTenKhachHang, txtTourName, txtNgayGio, txtSoNguoi;
    private ImageView imgQRCode, btnBack;
    private int maDatTour;
    private ApiService apiService;
    private ViDienTuResponse viDienTuResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ve_dien_tu);

        apiService = ApiClient.getClient(this).create(ApiService.class);
        viDienTuResponse = new ViDienTuResponse();

        anhXa();
        layDuLieuIntent();
        layDuLieuAPI();
        generateQRCode(String.valueOf(maDatTour));
        xuLySuKien();
    }

    private void anhXa() {
        txtTourName = findViewById(R.id.txtTourName);
        txtTenKhachHang = findViewById(R.id.txtTenKhachHang);
        txtNgayGio = findViewById(R.id.txtNgayGio);
        txtSoNguoi = findViewById(R.id.txtSoNguoi);
        imgQRCode = findViewById(R.id.imgQRCode);
        btnBack = findViewById(R.id.btnBack);
    }

    private void layDuLieuIntent() {
        maDatTour = getIntent().getIntExtra("maDatTour", -1);
        txtTenKhachHang.setText(getIntent().getStringExtra("tenKhachHang"));
    }

    private void layDuLieuAPI() {
        apiService.getLayThongTinVi(maDatTour).enqueue(new Callback<ViDienTuResponse>() {
            @Override
            public void onResponse(Call<ViDienTuResponse> call, Response<ViDienTuResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    viDienTuResponse = response.body();
                    txtTourName.setText(viDienTuResponse.getTentour());

                    //định dạng ngày giờ
                    try {
                        SimpleDateFormat input =
                                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                        SimpleDateFormat output =
                                new SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault());

                        Date date = input.parse(viDienTuResponse.getNgayDi());
                        txtNgayGio.setText(output.format(date));

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    txtSoNguoi.setText(viDienTuResponse.getSoNguoiLon() + " người lớn, " + viDienTuResponse.getSoTreEm() + " trẻ em");
                }
                else {
                    Toast.makeText(VeDienTuActivity.this, "Lấy thông tin thất bại", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ViDienTuResponse> call, Throwable t) {
                Toast.makeText(VeDienTuActivity.this, "Lỗi kết nối API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void xuLySuKien() {
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(VeDienTuActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    // HÀM TẠO MÃ QR
    private void generateQRCode(String data) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            // Tạo ma trận bit từ chuỗi dữ liệu (ví dụ: mã đặt tour)
            BitMatrix bitMatrix = multiFormatWriter.encode(data, BarcodeFormat.QR_CODE, 500, 500);
            // Chuyển ma trận thành Bitmap để hiển thị lên ImageView
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imgQRCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}