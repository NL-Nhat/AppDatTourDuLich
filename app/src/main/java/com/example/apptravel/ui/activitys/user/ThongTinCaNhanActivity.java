package com.example.apptravel.ui.activitys.user;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.apptravel.R;
import com.example.apptravel.data.api.ApiClient;
import com.example.apptravel.data.api.ApiService;
import com.example.apptravel.data.models.NguoiDung;
import com.example.apptravel.util.QuanLyDangNhap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Map;
//import java.util.jar.Manifest;
import android.Manifest;
import android.widget.Toast;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThongTinCaNhanActivity extends AppCompatActivity {

    private ImageView btnBack;
    private Button btnHuy;
    private Button btnLuu;
    private EditText txtHoTen, txtEmail, txtSDT, txtNgaySinh, txtDiaChi;
    private Spinner cmbGioiTinh;
    private QuanLyDangNhap quanLyDangNhap;
    private ImageView anhDaiDien;
    private ImageView btnEditAnhDaiDien;


    private ApiService apiService;
    private NguoiDung currentNguoiDung;
    private String userId;

    private Uri imageUri;
    private ActivityResultLauncher<String[]> requestPermissionLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<Uri> cameraLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_ca_nhan);
        anhXaViews();


        apiService = ApiClient.getClient(this).create(ApiService.class);
        quanLyDangNhap = new QuanLyDangNhap(this);
        int idInt = quanLyDangNhap.LayMaNguoiDung();
        if (idInt != 0) {
            userId = String.valueOf(idInt);
        }
        // Đăng ký các launchers cho việc chọn ảnh và xin quyền
        registerActivityLaunchers();

        setupUIComponents();
        GanThongTin();
        loadUserProfileFromServer();
        btnBack.setOnClickListener(v -> finish());
        btnHuy.setOnClickListener(v -> finish());
        btnLuu.setOnClickListener(v -> {
            saveUserProfile();
        });
        btnEditAnhDaiDien.setOnClickListener(v -> showImagePickDialog());
        loadUserProfileFromServer();
    }
    private void anhXaViews() {
        btnBack = findViewById(R.id.btn_back);
        btnHuy = findViewById(R.id.btn_cancel);
        btnLuu = findViewById(R.id.btn_save);
        txtHoTen = findViewById(R.id.txtHoTen);
        txtEmail = findViewById(R.id.txtEmail);
        txtSDT = findViewById(R.id.txtSDT);
        anhDaiDien = findViewById(R.id.profile_image);
        txtDiaChi = findViewById(R.id.txtDiaChi);
        txtNgaySinh = findViewById(R.id.txtNgaySinh);
        cmbGioiTinh = findViewById(R.id.cmbGioiTinh);
        btnEditAnhDaiDien = findViewById(R.id.btn_edit_anhdaidien); // THÊM MỚI
    }
    private void setupUIComponents() {
        //Xử lý Spinner Giới tính
        String[] genders = {"Nam", "Nu"};
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
    }

    private void registerActivityLaunchers() {
        // Launcher xin quyền
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                permissions -> {
                    boolean cameraGranted = permissions.getOrDefault(Manifest.permission.CAMERA, false);
                    String storagePermission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ? Manifest.permission.READ_MEDIA_IMAGES : Manifest.permission.READ_EXTERNAL_STORAGE;
                    boolean storageGranted = permissions.getOrDefault(storagePermission, false);

                    if (cameraGranted) {
                        openCamera();
                    } else if (storageGranted) {
                        openGallery();
                    } else {
                        Toast.makeText(this, "Quyền truy cập bị từ chối", Toast.LENGTH_SHORT).show();
                    }
                });

        // Launcher mở thư viện
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            imageUri = selectedImageUri;
                            Glide.with(this).load(imageUri).into(anhDaiDien);
                            uploadAvatarToServer(imageUri);
                        }
                    }
                });

        // Launcher mở camera
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                success -> {
                    if (success) {
                        Glide.with(this).load(imageUri).into(anhDaiDien);
                        uploadAvatarToServer(imageUri);
                    }
                });
    }

    private void showImagePickDialog() {
        String[] options = {"Chụp ảnh mới", "Chọn từ thư viện"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thay đổi ảnh đại diện");
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                checkCameraPermissionAndOpenCamera();
            } else {
                checkStoragePermissionAndOpenGallery();
            }
        });
        builder.create().show();
    }

    private void checkStoragePermissionAndOpenGallery() {
        String permission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                ? android.Manifest.permission.READ_MEDIA_IMAGES
                : android.Manifest.permission.READ_EXTERNAL_STORAGE;

        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            requestPermissionLauncher.launch(new String[]{permission});
        }
    }

    private void checkCameraPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {
            requestPermissionLauncher.launch(new String[]{Manifest.permission.CAMERA});
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        galleryLauncher.launch(intent);
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Avatar");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        cameraLauncher.launch(imageUri);
    }

    private void uploadAvatarToServer(Uri imageUriToUpload) {
        if (imageUriToUpload == null) return;

        File file = createTempFileFromUri(imageUriToUpload);
        if (file == null) return;

        RequestBody requestFile = RequestBody.create(okhttp3.MediaType.parse(getContentResolver().getType(imageUriToUpload)), file);;
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        Toast.makeText(this, "Đang tải ảnh lên...", Toast.LENGTH_SHORT).show();

        apiService.uploadAnhDaiDien(body).enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String newFileName = response.body().get("fileName");
                    quanLyDangNhap.LuuAnhDaiDien(newFileName);

                    Toast.makeText(ThongTinCaNhanActivity.this, "Tải ảnh thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ThongTinCaNhanActivity.this, "Lỗi server khi tải ảnh", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Log.e(TAG, "Lỗi upload: " + t.getMessage());
            }
        });
    }

    private File createTempFileFromUri(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            File tempFile = File.createTempFile("avatar", ".jpg", getCacheDir());
            tempFile.deleteOnExit();
            try (OutputStream out = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
            inputStream.close();
            return tempFile;
        } catch (Exception e) {
            Log.e(TAG, "Lỗi tạo file tạm: ", e);
            return null;
        }
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
            } else if (gioiTinhDaLuu.equals("Nu")) {
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
    private void loadUserProfileFromServer() {
        Log.i(TAG, "Bắt đầu tải thông tin người dùng với ID: " + userId);

        apiService.getNguoiDungById(userId).enqueue(new Callback<NguoiDung>() {
            @Override
            public void onResponse(Call<NguoiDung> call, Response<NguoiDung> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentNguoiDung = response.body();
                    displayUserData();
                }
            }
            @Override
            public void onFailure(Call<NguoiDung> call, Throwable t) {
                Log.e(TAG, "Lỗi: " + t.getMessage());
            }
        });
    }
    private void displayUserData() {
        if (currentNguoiDung == null) return;

        txtHoTen.setText(currentNguoiDung.getHoTen());
        txtNgaySinh.setText(currentNguoiDung.getNgaySinh());
        String tenFileAnh = currentNguoiDung.getAnhDaiDien();
    }

    private void saveUserProfile() {
        NguoiDung updateInfo = new NguoiDung();
        updateInfo.setHoTen(txtHoTen.getText().toString().trim());
        updateInfo.setSoDienThoai(txtSDT.getText().toString().trim());
        updateInfo.setNgaySinh(txtNgaySinh.getText().toString().trim());
        updateInfo.setEmail(txtEmail.getText().toString().trim());
        updateInfo.setDiaChi(txtDiaChi.getText().toString().trim());
        updateInfo.setGioiTinh(cmbGioiTinh.getSelectedItem().toString());
        updateInfo.setAnhDaiDien(quanLyDangNhap.LayAnhDaiDien());

        apiService.updateNguoiDung(userId, updateInfo).enqueue(new Callback<NguoiDung>() {
            @Override
            public void onResponse(Call<NguoiDung> call, Response<NguoiDung> response) {
                if (response.isSuccessful() && response.body() != null) {
                    NguoiDung updatedUser = response.body();

                    quanLyDangNhap.LuuDangNhap(
                            quanLyDangNhap.LayToken(),
                            updatedUser.getMaNguoiDung(),
                            true,
                            updatedUser.getHoTen(),
                            updatedUser.getEmail(),
                            updatedUser.getAnhDaiDien(),
                            updatedUser.getVaiTro(),
                            updatedUser.getSoDienThoai(),
                            updatedUser.getDiaChi(),
                            updatedUser.getGioiTinh(),
                            updatedUser.getNgaySinh()
                    );

                    Toast.makeText(ThongTinCaNhanActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(ThongTinCaNhanActivity.this, "Cập nhật thất bại (Lỗi " + response.code() + ")", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NguoiDung> call, Throwable t) {
                Toast.makeText(ThongTinCaNhanActivity.this, "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}