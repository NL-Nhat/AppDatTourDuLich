package com.example.apptravel.ui.activitys.user;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.apptravel.R;
import com.example.apptravel.data.api.ApiClient;
import com.example.apptravel.data.api.ApiService;
import com.example.apptravel.data.models.ChangePasswordRequest;
import com.example.apptravel.util.QuanLyDangNhap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoiMatKhauActivity extends AppCompatActivity {

    private ImageView btnBack;
    private Button btnHuy, btnDoiMatKhau;
    private EditText editCurrentPassword, editNewPassword, editConfirmPassword;
    private ImageView btnToggleCurrent, btnToggleNew, btnToggleConfirm;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doi_mat_khau);

        btnBack = findViewById(R.id.btn_back);
        btnHuy = findViewById(R.id.btn_Huy);
        editCurrentPassword = findViewById(R.id.edit_current_password);
        btnToggleCurrent = findViewById(R.id.btn_toggle_current);
        editNewPassword = findViewById(R.id.edit_new_password);
        btnToggleNew = findViewById(R.id.btn_toggle_new);
        editConfirmPassword = findViewById(R.id.edit_confirm_password);
        btnToggleConfirm = findViewById(R.id.btn_toggle_confirm);
        Button btnDoiMatKhauActual = findViewById(R.id.btn_doiMatKhau);

        btnDoiMatKhauActual.setOnClickListener(v -> handleDoiMatKhau());

        // Thêm sự kiện click cho nút ẩn/hiện mật khẩu hiện tại
        btnToggleCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editCurrentPassword.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                    editCurrentPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    btnToggleCurrent.setImageResource(R.drawable.ic_visibility);
                } else {
                    editCurrentPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    btnToggleCurrent.setImageResource(R.drawable.ic_visibility_off);
                }
                editCurrentPassword.setSelection(editCurrentPassword.getText().length());
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void handleDoiMatKhau() {
        String currentPass = editCurrentPassword.getText().toString().trim();
        String newPass = editNewPassword.getText().toString().trim();
        String confirmPass = editConfirmPassword.getText().toString().trim();
        // --- Kiểm tra Validation ---
        if (currentPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        if (newPass.length() < 6) {
            Toast.makeText(this, "Mật khẩu mới phải từ 6 ký tự trở lên", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!newPass.equals(confirmPass)) {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
            return;
        }
        if (newPass.equals(currentPass)) {
            Toast.makeText(this, "Mật khẩu mới không được trùng mật khẩu cũ", Toast.LENGTH_SHORT).show();
            return;
        }
        thucHienDoiMatKhauTrenServer(currentPass, newPass);
    }
    private void thucHienDoiMatKhauTrenServer(String oldPass, String newPass) {
        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        QuanLyDangNhap quanLyDangNhap = new QuanLyDangNhap(this);
        String userId = String.valueOf(quanLyDangNhap.LayMaNguoiDung());

        ChangePasswordRequest request = new ChangePasswordRequest(oldPass, newPass);
        request.setOldPassword(oldPass);
        request.setNewPassword(newPass);

        apiService.doiMatKhau(userId, request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DoiMatKhauActivity.this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(DoiMatKhauActivity.this, "Mật khẩu hiện tại không đúng", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(DoiMatKhauActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}