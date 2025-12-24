package com.example.apptravel.ui.activitys.admin;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apptravel.R;
import com.example.apptravel.data.api.ApiClient;
import com.example.apptravel.data.api.ApiService;
import com.example.apptravel.data.models.NguoiDung;
import com.example.apptravel.data.models.RegisterRequest;

import java.util.Calendar;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThemTaiKhoanActivity extends AppCompatActivity {
    private EditText editTenDangNhap, editHoTen, editEmail, editSoDienThoai,
            editDiaChi, editNgaySinh, editMatKhau;
    private RadioGroup nhomVaiTro, nhomGioiTinh;
    private ImageView nutLich, nutAnHienMatKhau;
    private Button nutTaoTaiKhoan;

    private boolean laHienMatKhau = false;
    private ApiService apiService;
    private NguoiDung nguoiDungDangSua;
    private boolean laCheDoSua = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_tai_khoan);

        anhXaGiaoDien();
        kiemTraCheDoSua();
        caiDatSuKien();
    }
    private void kiemTraCheDoSua() {
        if (getIntent().hasExtra("NGUOI_DUNG_EDIT")) {
            nguoiDungDangSua = (NguoiDung) getIntent().getSerializableExtra("NGUOI_DUNG_EDIT");
            laCheDoSua = true;
            nutTaoTaiKhoan.setText("CẬP NHẬT TÀI KHOẢN");

            editTenDangNhap.setText(nguoiDungDangSua.getTenDangNhap());
            editTenDangNhap.setEnabled(false);

            editHoTen.setText(nguoiDungDangSua.getHoTen());
            editEmail.setText(nguoiDungDangSua.getEmail());
            editSoDienThoai.setText(nguoiDungDangSua.getSoDienThoai());
            editDiaChi.setText(nguoiDungDangSua.getDiaChi());
            editNgaySinh.setText(nguoiDungDangSua.getNgaySinh());

            editMatKhau.setHint("Để trống nếu không muốn đổi mật khẩu");

            if ("Nu".equalsIgnoreCase(nguoiDungDangSua.getGioiTinh()) || "Nữ".equals(nguoiDungDangSua.getGioiTinh())) {
                nhomGioiTinh.check(R.id.radio_nu);
            } else {
                nhomGioiTinh.check(R.id.radio_nam);
            }

            if ("Admin".equalsIgnoreCase(nguoiDungDangSua.getVaiTro())) {
                nhomVaiTro.check(R.id.radio_quantrivien);
            } else if ("HuongDanVien".equalsIgnoreCase(nguoiDungDangSua.getVaiTro())) {
                nhomVaiTro.check(R.id.radio_huongdanvien);
            } else {
                nhomVaiTro.check(R.id.radio_khachhang);
            }
        }
    }

    private void anhXaGiaoDien() {
        editTenDangNhap = findViewById(R.id.edit_tendangnhap);
        editHoTen = findViewById(R.id.edit_hoten);
        editEmail = findViewById(R.id.edit_email);
        editSoDienThoai = findViewById(R.id.edit_sodienthoai);
        editDiaChi = findViewById(R.id.edit_diachi);
        editNgaySinh = findViewById(R.id.edit_ngaysinh);
        editMatKhau = findViewById(R.id.edit_password);

        nhomVaiTro = findViewById(R.id.radio_group_vaitro);
        nhomGioiTinh = findViewById(R.id.nhom_gioi_tinh);

        nutLich = findViewById(R.id.btn_calendar);
        nutAnHienMatKhau = findViewById(R.id.btn_toggle_password);
        nutTaoTaiKhoan = findViewById(R.id.btn_luu);

        apiService = ApiClient.getClient(this).create(ApiService.class);
    }

    private void caiDatSuKien() {
        nutLich.setOnClickListener(v -> moBoChonNgay());
        editNgaySinh.setOnClickListener(v -> moBoChonNgay());

        nutAnHienMatKhau.setOnClickListener(v -> {
            if (laHienMatKhau) {
                editMatKhau.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                nutAnHienMatKhau.setImageResource(R.drawable.ic_visibility_off);
            } else {
                editMatKhau.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                nutAnHienMatKhau.setImageResource(R.drawable.ic_visibility);
            }
            laHienMatKhau = !laHienMatKhau;
            editMatKhau.setSelection(editMatKhau.getText().length());
        });

        // Sự kiện nút Tạo tài khoản
        nutTaoTaiKhoan.setOnClickListener(v -> xuLyTaoTaiKhoan());
    }

    private void moBoChonNgay() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, yearSelected, monthOfYear, dayOfMonth) -> {
                    String date = String.format(Locale.getDefault(), "%04d-%02d-%02d", yearSelected, monthOfYear + 1, dayOfMonth);
                    editNgaySinh.setText(date);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void thucHienThemMoi(RegisterRequest request) {
        apiService.register(request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ThemTaiKhoanActivity.this, "Tạo tài khoản thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ThemTaiKhoanActivity.this, "Lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ThemTaiKhoanActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void thucHienCapNhat(NguoiDung user) {
        String idStr = String.valueOf(user.getMaNguoiDung());

        apiService.updateNguoiDung(idStr, user).enqueue(new Callback<NguoiDung>() {
            @Override
            public void onResponse(Call<NguoiDung> call, Response<NguoiDung> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ThemTaiKhoanActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ThemTaiKhoanActivity.this, "Lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NguoiDung> call, Throwable t) {
                Toast.makeText(ThemTaiKhoanActivity.this, "Lỗi kết nối Server!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void xuLyTaoTaiKhoan() {
        String tenDN = editTenDangNhap.getText().toString().trim();
        String matKhau = editMatKhau.getText().toString().trim();
        String hoTen = editHoTen.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String sdt = editSoDienThoai.getText().toString().trim();
        String diaChi = editDiaChi.getText().toString().trim();
        String ngaySinh = editNgaySinh.getText().toString().trim();

        String gioiTinhStr = "Nam";
        int gioiTinhId = nhomGioiTinh.getCheckedRadioButtonId();
        if (gioiTinhId == R.id.radio_nu) {
            gioiTinhStr = "Nu";
        } else if (gioiTinhId == R.id.radio_nam) {
            gioiTinhStr = "Nam";
        }

        String vaiTroStr = "KhachHang";
        int vaiTroId = nhomVaiTro.getCheckedRadioButtonId();
        if (vaiTroId == R.id.radio_huongdanvien) {
            vaiTroStr = "HuongDanVien";
        } else if (vaiTroId == R.id.radio_quantrivien) {
            vaiTroStr = "Admin";
        } else if (vaiTroId == R.id.radio_khachhang) {
            vaiTroStr = "KhachHang";
        }

        // Phân luồng
        if (laCheDoSua) {
            nguoiDungDangSua.setHoTen(hoTen);
            nguoiDungDangSua.setEmail(email);
            nguoiDungDangSua.setSoDienThoai(sdt);
            nguoiDungDangSua.setDiaChi(diaChi);
            nguoiDungDangSua.setNgaySinh(ngaySinh);
            nguoiDungDangSua.setGioiTinh(gioiTinhStr);
            nguoiDungDangSua.setVaiTro(vaiTroStr);

            if (!matKhau.isEmpty()) {
                nguoiDungDangSua.setMatKhau(matKhau);
            }

            thucHienCapNhat(nguoiDungDangSua);
        } else {
            RegisterRequest newRequest = new RegisterRequest(tenDN, matKhau, hoTen, email, sdt, diaChi, ngaySinh, vaiTroStr, gioiTinhStr);
            thucHienThemMoi(newRequest);
        }
    }
}