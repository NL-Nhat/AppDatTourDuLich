package com.example.apptravel.ui.activitys.user;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.apptravel.R;
import com.example.apptravel.data.api.ApiClient;
import com.example.apptravel.data.api.ApiService;
import com.example.apptravel.data.models.BookingRequest;
import com.example.apptravel.data.models.BookingResponse;
import com.example.apptravel.data.models.District;
import com.example.apptravel.data.models.DistrictResponse;
import com.example.apptravel.data.models.LichKhoiHanh;
import com.example.apptravel.data.models.Province;
import com.example.apptravel.data.models.Tour;
import com.example.apptravel.data.models.WardResponse;
import com.example.apptravel.util.QuanLyDangNhap;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NhapThongTinActivity extends AppCompatActivity {

    private TextView txtHoten, txtNgaySinh, txtSoDienThoai, txtTenTour, txtNgayDi, txtGioDi, txtTongTien,
            txtSoNguoiLon, txtSoTreEm, txtGiaNguoiLon, txtGiaTreEm;
    private Button btnDatNgay;
    private RadioGroup radioGroup;
    private ImageButton btn_tru_adult, btn_cong_adult, btn_tru_child, btn_cong_child;
    private Tour tour;
    private LichKhoiHanh lich;
    private QuanLyDangNhap quanLyDangNhap;
    private final SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private int soNguoiLon = 1, soTreEm = 0;
    private double tongTien = 0;
    private ApiService apiService;
    private EditText etTinhThanh, etQuanHuyen, etPhuongXa, etSoNha;
    private String selectedProvinceCode = "";
    private String selectedDistrictCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhapthongtin);

        apiService = ApiClient.getClient(this).create(ApiService.class);

        anhXa();
        layDuLieuIntent();
        xuLySuKien();
    }

    private void anhXa() {
        txtHoten = findViewById(R.id.txt_hoTen);
        txtNgaySinh = findViewById(R.id.txt_ngaySinh);
        txtSoDienThoai = findViewById(R.id.txt_soDienThoai);
        txtTenTour = findViewById(R.id.txt_tenTour);
        txtNgayDi = findViewById(R.id.txt_ngayDi);
        txtGioDi = findViewById(R.id.txt_gioDi);
        txtTongTien = findViewById(R.id.txt_tongTien);
        txtSoTreEm = findViewById(R.id.txt_soTreEm);
        txtSoNguoiLon = findViewById(R.id.txt_soNguoiLon);
        txtGiaNguoiLon = findViewById(R.id.txt_giaNguoiLon);
        txtGiaTreEm = findViewById(R.id.txt_giaTreEm);
        btn_tru_adult = findViewById(R.id.btn_tru_adult);
        btn_cong_adult = findViewById(R.id.btn_cong_adult);
        btn_cong_child = findViewById(R.id.btn_cong_child);
        btn_tru_child = findViewById(R.id.btn_tru_child);
        btnDatNgay = findViewById(R.id.btn_datNgay);
        etTinhThanh = findViewById(R.id.et_tinhThanh);
        etQuanHuyen = findViewById(R.id.et_quanHuyen);
        etPhuongXa = findViewById(R.id.et_phuongXa);
        etSoNha = findViewById(R.id.et_soNha);
        radioGroup = findViewById(R.id.rg_gioiTinh);
        quanLyDangNhap = new QuanLyDangNhap(this);
    }

    private void layDuLieuIntent() {
        tour = (Tour) getIntent().getSerializableExtra("object_tour");
        lich = (LichKhoiHanh) getIntent().getSerializableExtra("object_lich");
        btn_tru_adult.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray));
        btn_tru_child.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray));

        if(tour != null) {
            DecimalFormat decimalFormat = new DecimalFormat("#,###");
            txtGiaNguoiLon.setText(decimalFormat.format(tour.getGiaNguoiLon()) + " VNĐ");
            txtGiaTreEm.setText(decimalFormat.format(tour.getGiaTreEm()) + " VNĐ");
            txtTenTour.setText(tour.getTenTour());
        }

        if(lich != null) {
            try{
                Date ngayDi = inputFormat.parse(lich.getNgayKhoiHanh());
                txtNgayDi.setText("Bắt đầu từ " + dateFormat.format(ngayDi));
                txtGioDi.setText("lúc " + timeFormat.format( ngayDi));

            }catch (Exception e){
                txtNgayDi.setText("—");
                txtGioDi.setText("—");
            }
        }
        tinhTongTien();
    }

    private void xuLySuKien() {

        //Xử lý DatePicker cho Ngày sinh
        txtNgaySinh.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
                // Định dạng dd/MM/yyyy
                String date = String.format("%02d/%02d/%d", dayOfMonth, month1 + 1, year1);
                txtNgaySinh.setText(date);
            }, year, month, day);
            datePickerDialog.show();
        });

        // Nút Back
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        btn_tru_adult.setOnClickListener(v -> {
            if(soNguoiLon > 1) {
                if(soNguoiLon == 2)
                    btn_tru_adult.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray));
                soNguoiLon--;
                txtSoNguoiLon.setText(String.valueOf(soNguoiLon));
                tinhTongTien();
            }
        });
        btn_tru_child.setOnClickListener(v -> {
           if(soTreEm > 0) {
               if(soTreEm == 1)
                   btn_tru_child.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray));
               soTreEm--;
               txtSoTreEm.setText(String.valueOf(soTreEm));
               tinhTongTien();
           }
        });
        btn_cong_adult.setOnClickListener(v -> {
            soNguoiLon++;
            txtSoNguoiLon.setText(String.valueOf(soNguoiLon));
            btn_tru_adult.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.black));
            tinhTongTien();
        });
        btn_cong_child.setOnClickListener(v -> {
            soTreEm++;
            txtSoTreEm.setText(String.valueOf(soTreEm));
            btn_tru_child.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.black));
            tinhTongTien();
        });

        etTinhThanh.setOnClickListener(v -> loadProvinces());

        // Sự kiện chọn Quận/Huyện
        etQuanHuyen.setOnClickListener(v -> {
            if (selectedProvinceCode.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn Tỉnh/Thành phố trước", Toast.LENGTH_SHORT).show();
            } else {
                loadDistricts(selectedProvinceCode);
            }
        });

        // Sự kiện chọn Phường/Xã
        etPhuongXa.setOnClickListener(v -> {
            if (selectedDistrictCode.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn Quận/Huyện trước", Toast.LENGTH_SHORT).show();
            } else {
                loadWards(selectedDistrictCode);
            }
        });

        // đặt tour
        btnDatNgay.setOnClickListener(v -> {
            String hoTen = txtHoten.getText().toString().trim();
            String sdt = txtSoDienThoai.getText().toString().trim();
            String tinh = etTinhThanh.getText().toString();
            String quan = etQuanHuyen.getText().toString();
            String phuong = etPhuongXa.getText().toString();
            String soNha = etSoNha.getText().toString().trim();

            if (hoTen.isEmpty() || sdt.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin bắt buộc", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton radioButton = findViewById(radioGroup.getCheckedRadioButtonId());
            String gioiTinh = radioButton.getText().toString();

            // Chuyển đổi ngày sinh sang dạng yyyy-MM-dd
            String ngaySinhHienTai = txtNgaySinh.getText().toString().trim();
            String ngaySinhGuiApi = null;
            if(!ngaySinhHienTai.isEmpty() && ngaySinhHienTai != null) {
                try {
                    Date dateObj = dateFormat.parse(ngaySinhHienTai);
                    SimpleDateFormat apiDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    ngaySinhGuiApi = apiDateFormat.format(dateObj);
                } catch (ParseException e) {
                    Toast.makeText(this, "Ngày sinh không đúng định dạng", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            //=================

            // Nối chuỗi địa chỉ
            List<String> parts = new ArrayList<>();
            if (!soNha.isEmpty()) parts.add(soNha);
            if (!phuong.isEmpty()) parts.add(phuong);
            if (!quan.isEmpty()) parts.add(quan);
            if (!tinh.isEmpty()) parts.add(tinh);

            //  Kiểm tra và nối chuỗi
            String diaChiDayDu;
            if (parts.isEmpty()) {
                diaChiDayDu = null; // Nếu tất cả đều trống
            } else {
                // Nối các phần tử bằng dấu phẩy và khoảng trắng
                diaChiDayDu = android.text.TextUtils.join(", ", parts);
            }

            BookingRequest bookingRequest = new BookingRequest();
            bookingRequest.setMaLichKhoiHanh(lich.getMaLichKhoiHanh());
            bookingRequest.setSoNguoiLon(soNguoiLon);
            bookingRequest.setSoTreEm(soTreEm);
            bookingRequest.setHoTen(hoTen);
            bookingRequest.setSoDienThoai(sdt);
            bookingRequest.setGioiTinh(gioiTinh);
            bookingRequest.setDiaChi(diaChiDayDu);
            bookingRequest.setNgaySinh(ngaySinhGuiApi);
            bookingRequest.setMaNguoiDung(quanLyDangNhap.LayMaNguoiDung());

            datTour(bookingRequest);
        });
    }

    private void tinhTongTien() {
        tongTien = tour.getGiaNguoiLon() * soNguoiLon + tour.getGiaTreEm() * soTreEm;
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        txtTongTien.setText(decimalFormat.format(tongTien) + " VNĐ");
    }

    private void loadProvinces() {
        apiService.getProvinces().enqueue(new Callback<List<Province>>() {
            @Override
            public void onResponse(Call<List<Province>> call, Response<List<Province>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    showProvinceDialog(response.body());
                }
            }
            @Override
            public void onFailure(Call<List<Province>> call, Throwable t) {
                Toast.makeText(NhapThongTinActivity.this, "Lỗi kết nối API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showProvinceDialog(List<Province> list) {
        String[] names = new String[list.size()];
        for (int i = 0; i < list.size(); i++) names[i] = list.get(i).getName();

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Chọn Tỉnh / Thành phố")
                .setItems(names, (dialog, i) -> {
                    Province selected = list.get(i);
                    etTinhThanh.setText(selected.getName());
                    selectedProvinceCode = String.valueOf(selected.getCode());

                    // Reset các cấp dưới khi đổi Tỉnh
                    selectedDistrictCode = "";
                    etQuanHuyen.setText("");
                    etPhuongXa.setText("");
                }).show();
    }

    // Sau khi người dùng chọn Tỉnh, lấy code gọi tiếp:
    private void loadDistricts(String provinceCode) {
        apiService.getDistricts(provinceCode).enqueue(new Callback<DistrictResponse>() {
            @Override
            public void onResponse(Call<DistrictResponse> call, Response<DistrictResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    showDistrictDialog(response.body().getDistricts());
                }
            }
            @Override
            public void onFailure(Call<DistrictResponse> call, Throwable t) {}
        });
    }

    private void showDistrictDialog(List<District> list) {
        String[] names = new String[list.size()];
        for (int i = 0; i < list.size(); i++) names[i] = list.get(i).getName();

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Chọn Quận / Huyện")
                .setItems(names, (dialog, i) -> {
                    District selected = list.get(i);
                    etQuanHuyen.setText(selected.getName());
                    selectedDistrictCode = String.valueOf(selected.getCode());

                    // Reset cấp dưới khi đổi Quận
                    etPhuongXa.setText("");
                }).show();
    }

    // --- PHẦN PHƯỜNG XÃ ---
    private void loadWards(String districtCode) {
        apiService.getWards(districtCode).enqueue(new Callback<WardResponse>() {
            @Override
            public void onResponse(Call<WardResponse> call, Response<WardResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    showWardDialog(response.body().getWards());
                }
            }
            @Override public void onFailure(Call<WardResponse> call, Throwable t) {}
        });
    }

    private void showWardDialog(List<com.example.apptravel.data.models.Ward> list) {
        String[] names = new String[list.size()];
        for (int i = 0; i < list.size(); i++) names[i] = list.get(i).getName();

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Chọn Phường / Xã")
                .setItems(names, (dialog, i) -> {
                    etPhuongXa.setText(list.get(i).getName());
                }).show();
    }

    private void datTour(BookingRequest bookingRequest) {
        btnDatNgay.setEnabled(false); // Khóa nút
        btnDatNgay.setText("Đang xử lý...");
        apiService.createBooking(bookingRequest).enqueue(new Callback<BookingResponse>() {
            @Override
            public void onResponse(Call<BookingResponse> call, Response<BookingResponse> response) {
                btnDatNgay.setEnabled(true); // Mở lại nút
                btnDatNgay.setText("ĐẶT NGAY");
                if (response.isSuccessful() && response.body() != null) {
                    int maDatTour = response.body().getMaDatTour();
                    hienThiDialogThanhCong(maDatTour);
                } else {
                    // LẤY MÃ LỖI VÀ NỘI DUNG LỖI
                    int statusCode = response.code();
                    String errorBody = "";
                    try {
                        if (response.errorBody() != null) {
                            errorBody = response.errorBody().string();
                        }
                    } catch (IOException e) { e.printStackTrace(); }

                    Log.e("API_ERROR", "Status Code: " + statusCode + " - Message: " + errorBody);
                    Toast.makeText(NhapThongTinActivity.this, "Lỗi " + statusCode + ": " + errorBody, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<BookingResponse> call, Throwable t) {
                btnDatNgay.setEnabled(true); // Mở lại nút
                btnDatNgay.setText("ĐẶT NGAY");
                Toast.makeText(NhapThongTinActivity.this, "Lỗi kết nối API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hienThiDialogThanhCong(int maDatTour) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Đặt Tour Thành Công! 🎉");
        builder.setMessage("Đơn hàng của bạn đã được ghi nhận.\n\nBạn có muốn thực hiện thanh toán ngay để giữ chỗ không?");
        builder.setCancelable(false); // Không cho phép thoát khi bấm ra ngoài

        // Nút Thanh toán ngay
        builder.setPositiveButton("Thanh toán ngay", (dialog, which) -> {
            Intent intent = new Intent(NhapThongTinActivity.this, PhuongThucThanhToanActivity.class);
            intent.putExtra("maDatTour", maDatTour);
            intent.putExtra("tongTien", tongTien);
            startActivity(intent);
            finish();
        });

        // Nút Để sau
        builder.setNegativeButton("Để sau", (dialog, which) -> {
            Toast.makeText(this, "Bạn có thể thanh toán sau trong mục Lịch sử đặt tour", Toast.LENGTH_LONG).show();
            // Quay về màn hình chính hoặc đóng Activity
            finish();
        });

        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }
}