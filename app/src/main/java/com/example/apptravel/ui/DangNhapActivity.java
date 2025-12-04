package com.example.apptravel.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apptravel.R;
import com.example.apptravel.data.models.LoginRequest;
import com.example.apptravel.data.models.LoginResponse;
import com.example.apptravel.data.repository.AuthRepository;
import com.example.apptravel.ui.activitys.admin.MainAdminActivity;
import com.example.apptravel.ui.activitys.user.DangKyActivity;
import com.example.apptravel.ui.activitys.user.MainActivity;
import com.example.apptravel.util.QuanLyDangNhap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DangNhapActivity extends AppCompatActivity {
    private TextView txtDangKy;
    private Button btnDangNhap;
    private EditText editUsername, editPassword;
    private AuthRepository authRepository;

    private QuanLyDangNhap quanLyDangNhap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangnhap);

        editUsername = findViewById(R.id.edit_username);
        editPassword = findViewById(R.id.edit_password);
        txtDangKy = findViewById(R.id.txtdangky);
        btnDangNhap = findViewById(R.id.btndangnhap);
        authRepository = new AuthRepository(this);


        txtDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DangNhapActivity.this, DangKyActivity.class);
                startActivity(intent);
            }
        });

        quanLyDangNhap = new QuanLyDangNhap(this);
        btnDangNhap.setOnClickListener(v -> login());

    }

    private void login() {
        String user = editUsername.getText().toString().trim();
        String pass = editPassword.getText().toString().trim();

        // Kiểm tra dữ liệu
        if (user.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo request
        LoginRequest request = new LoginRequest(user, pass);

        // Gọi API backend
        authRepository.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse res = response.body();

                    if (res.getVaiTro().equalsIgnoreCase("ADMIN")) {
                        quanLyDangNhap.LuuDangNhap(res.getMaNguoiDung(),true ,res.getHoTen(), res.getEmail(), res.getAnhDaiDien());
                        Toast.makeText(DangNhapActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(DangNhapActivity.this, MainAdminActivity.class));

                    } else if(res.getVaiTro().equalsIgnoreCase("KhachHang")){
                        quanLyDangNhap.LuuDangNhap(res.getMaNguoiDung(),true, res.getHoTen(), res.getEmail(), res.getAnhDaiDien());
                        Toast.makeText(DangNhapActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();

                        // CHUYỂN SANG MainActivity VÀ CHỈ ĐỊNH MỞ TRANG CÁ NHÂN
                        Intent intent = new Intent(DangNhapActivity.this, MainActivity.class);
                        // 1. Chỉ định Fragment cần mở
                        intent.putExtra("fragment_name", "TrangCaNhanFragment");
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(DangNhapActivity.this, "Không có trang nhân viên", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    finish();
                } else {
                    Toast.makeText(DangNhapActivity.this, "Sai tài khoản hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(DangNhapActivity.this, "Không thể kết nối server!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
