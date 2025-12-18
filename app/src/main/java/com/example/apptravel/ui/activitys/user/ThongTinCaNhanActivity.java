package com.example.apptravel.ui.activitys.user;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.apptravel.R;
import com.example.apptravel.data.api.ApiClient;
import com.example.apptravel.util.QuanLyDangNhap;
import java.util.Calendar;

public class ThongTinCaNhanActivity extends AppCompatActivity {

    private ImageView btnBack;
    private Button btnHuy;
    private Button btnLuu;
    private EditText txtHoTen, txtEmail, txtSDT, txtNgaySinh, txtDiaChi;
    private Spinner cmbGioiTinh;
    private QuanLyDangNhap quanLyDangNhap;
    private ImageView anhDaiDien;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_ca_nhan);

        btnBack = findViewById(R.id.btn_back);
        btnHuy = findViewById(R.id.btn_cancel);
        btnLuu = findViewById(R.id.btn_save);
        quanLyDangNhap = new QuanLyDangNhap(this);
        txtHoTen = findViewById(R.id.txtHoTen);
        txtEmail = findViewById(R.id.txtEmail);
        txtSDT = findViewById(R.id.txtSDT);
        anhDaiDien = findViewById(R.id.profile_image);
        txtDiaChi = findViewById(R.id.txtDiaChi);
        txtNgaySinh = findViewById(R.id.txtNgaySinh);
        cmbGioiTinh = findViewById(R.id.cmbGioiTinh);

        //Xử lý Spinner Giới tính
        String[] genders = {"Nam", "Nữ"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genders);
        cmbGioiTinh.setAdapter(adapter);

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

        GanThongTin();

        btnBack.setOnClickListener(v -> finish());
        btnHuy.setOnClickListener(v -> finish());
    }

    private void GanThongTin() {
        txtHoTen.setText(quanLyDangNhap.LayHoTen());
        txtEmail.setText(quanLyDangNhap.LayEmail());
        txtSDT.setText(quanLyDangNhap.LaySoDienThoai());
        txtNgaySinh.setText(quanLyDangNhap.LayNgaySinh());
        txtDiaChi.setText(quanLyDangNhap.LayDiaChi());

        // --- Xử lý Spinner Giới tính ---
        String gioiTinhDaLuu = quanLyDangNhap.LayGioiTinh();
        if (gioiTinhDaLuu != null) {
            if (gioiTinhDaLuu.equals("Nam")) {
                cmbGioiTinh.setSelection(0);
            } else if (gioiTinhDaLuu.equals("Nữ")) {
                cmbGioiTinh.setSelection(1);
            }
        }

        // --- Xử lý Ảnh đại diện ---
        String tenFileAnh = quanLyDangNhap.LayAnhDaiDien();
        if (tenFileAnh != null && !tenFileAnh.isEmpty()) {
            String duongDanAnh = "avatar/" + tenFileAnh;
            String fullUrl = ApiClient.getFullImageUrl(this, duongDanAnh);

            Glide.with(this)
                    .load(fullUrl)
                    .placeholder(R.drawable.nen)
                    .error(R.drawable.ic_launcher_background)
                    .into(anhDaiDien);
        }
    }
}