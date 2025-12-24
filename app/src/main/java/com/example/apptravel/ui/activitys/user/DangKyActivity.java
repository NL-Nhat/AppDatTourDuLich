package com.example.apptravel.ui.activitys.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apptravel.R;
import com.example.apptravel.data.api.ApiClient;
import com.example.apptravel.data.api.ApiService;
import com.example.apptravel.data.models.RegisterRequest;
import com.example.apptravel.ui.DangNhapActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DangKyActivity extends AppCompatActivity {

    private EditText editUsername, editFullname, editEmail, editPassword, editConfirmPassword;
    private ImageView btnTogglePassword, btnToggleConfirmPassword;
    private CheckBox checkboxTerms;
    private Button btnDangKy;
    private TextView txtDangNhap;

    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangky);

        initViews();
        setupListeners();
    }

    private void initViews() {
        editUsername = findViewById(R.id.edit_username);
        editFullname = findViewById(R.id.edit_fullname);
        editEmail = findViewById(R.id.edit_email);
        editPassword = findViewById(R.id.edit_password);
        editConfirmPassword = findViewById(R.id.edit_confirm_password);
        btnTogglePassword = findViewById(R.id.btn_toggle_password);
        btnToggleConfirmPassword = findViewById(R.id.btn_toggle_confirm_password);
        checkboxTerms = findViewById(R.id.checkbox_terms);
        btnDangKy = findViewById(R.id.btnDangKy);
        txtDangNhap = findViewById(R.id.txtDangNhap);
    }

    private void setupListeners() {
        // Ẩn/Hiện mật khẩu
        btnTogglePassword.setOnClickListener(v -> togglePasswordVisibility());
        btnToggleConfirmPassword.setOnClickListener(v -> toggleConfirmPasswordVisibility());

        // Nút Đăng ký
        btnDangKy.setOnClickListener(v -> validateAndRegister());

        // Link chuyển sang Đăng nhập
        txtDangNhap.setOnClickListener(v -> {
            startActivity(new Intent(this, DangNhapActivity.class));
            finish();
        });
    }

    private void validateAndRegister() {
        String username = editUsername.getText().toString().trim();
        String fullname = editFullname.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String confirmPassword = editConfirmPassword.getText().toString().trim();

        // Kiểm tra dữ liệu
        if (username.isEmpty() || fullname.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!checkboxTerms.isChecked()) {
            Toast.makeText(this, "Bạn phải đồng ý với điều khoản", Toast.LENGTH_SHORT).show();
            return;
        }

        // Gọi API (Giả sử số điện thoại bạn để trống hoặc bổ sung thêm trường)
        RegisterRequest request = new RegisterRequest(username, password, fullname, email);

        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        apiService.register(request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DangKyActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DangKyActivity.this, DangNhapActivity.class));
                    finish();
                } else {
                    Toast.makeText(DangKyActivity.this, "Đăng ký thất bại: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(DangKyActivity.this, "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            editPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            btnTogglePassword.setImageResource(R.drawable.ic_visibility);
        } else {
            editPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            btnTogglePassword.setImageResource(R.drawable.ic_visibility_off);
        }
        isPasswordVisible = !isPasswordVisible;
        editPassword.setSelection(editPassword.length());
    }

    private void toggleConfirmPasswordVisibility() {
        if (isConfirmPasswordVisible) {
            editConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            btnToggleConfirmPassword.setImageResource(R.drawable.ic_visibility);
        } else {
            editConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            btnToggleConfirmPassword.setImageResource(R.drawable.ic_visibility_off);
        }
        isConfirmPasswordVisible = !isConfirmPasswordVisible;
        editConfirmPassword.setSelection(editConfirmPassword.length());
    }
}