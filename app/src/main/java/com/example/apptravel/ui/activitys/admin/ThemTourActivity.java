package com.example.apptravel.ui.activitys.admin;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptravel.R;
import com.example.apptravel.data.adapters.LichKhoiHanhAdminAdapter;
import com.example.apptravel.data.api.ApiClient;
import com.example.apptravel.data.api.ApiService;
import com.example.apptravel.data.models.DiemDen;
import com.example.apptravel.data.models.HuongDanVienResponse;
import com.example.apptravel.data.models.TourRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThemTourActivity extends AppCompatActivity {

    private ImageView imgTour, btnBack;
    private EditText txtTourName, txt_moTa, txt_giaNguoiLon, txt_giaTreEm;
    private TextView txtChuaCoLich;
    private AutoCompleteTextView spinnerDiemDen;
    private RadioGroup rdGroupTrangThai;
    private RadioButton rd_dang_mo, rd_tamDung;
    private MaterialButton btn_them_lichKH, btn_cancel, btn_save;
    private LinearLayout btn_upload_img;
    private ApiService apiService;
    private List<DiemDen> listDiemDen = new ArrayList<>();
    private ArrayAdapter<DiemDen> adapterDiemDen;
    private TourRequest tourRequest;
    private Uri selectedImageUri; // Lưu URI của ảnh đã chọn
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    private RecyclerView rvLichKhoiHanh;
    private LichKhoiHanhAdminAdapter lichAdapter;
    private List<TourRequest.LichKhoiHanhDTO> listLKH = new ArrayList<>();
    private List<HuongDanVienResponse> listHDV = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_tour);

        apiService = ApiClient.getClient(this).create(ApiService.class);
        tourRequest = new TourRequest();

        loadDiemDen();
        loadHuongDanVien();
        anhXa();
        xuLySuKien();

        // Khởi tạo bộ chọn ảnh
        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                selectedImageUri = uri;
                imgTour.setImageURI(uri); // Hiển thị ảnh lên ImageView
                btn_upload_img.setAlpha(0.5f); // Làm mờ nút chọn để thấy ảnh rõ hơn (tùy chọn)
            } else {
                Toast.makeText(this, "Chưa chọn ảnh", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void anhXa() {
        imgTour = findViewById(R.id.img_tour);
        btnBack = findViewById(R.id.btn_back);
        txtTourName = findViewById(R.id.txt_tour_name);
        txtChuaCoLich = findViewById(R.id.txt_chua_co_lich);
        txt_moTa = findViewById(R.id.txt_MoTa);
        txt_giaNguoiLon = findViewById(R.id.txt_giaNguoiLon);
        txt_giaTreEm = findViewById(R.id.txt_giaTreEm);
        spinnerDiemDen = findViewById(R.id.spinner_diem_den);
        rdGroupTrangThai = findViewById(R.id.rd_group_trangThai);
        rd_dang_mo = findViewById(R.id.rb_dang_mo);
        rd_tamDung = findViewById(R.id.rd_tamDung);
        btn_them_lichKH = findViewById(R.id.btn_them_lichKH);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_save = findViewById(R.id.btn_save);
        btn_upload_img = findViewById(R.id.btn_upload_img);
        rvLichKhoiHanh = findViewById(R.id.recycler_lich_kh);

        if (rd_dang_mo.isChecked()) {
            tourRequest.setTrangThai("DangMo");
        } else {
            tourRequest.setTrangThai("TamDung");
        }

        // Cài đặt RecyclerView
        lichAdapter = new LichKhoiHanhAdminAdapter(listLKH, new LichKhoiHanhAdminAdapter.OnItemClickListener() {
            @Override
            public void onEdit(int position, TourRequest.LichKhoiHanhDTO item) {
                showDialogLichKhoiHanh(true, position, item);
            }

            @Override
            public void onDelete(int position) {
                listLKH.remove(position);
                lichAdapter.notifyItemRemoved(position);
                updateLichVisibility();
            }
        });
        rvLichKhoiHanh.setLayoutManager(new LinearLayoutManager(this));
        rvLichKhoiHanh.setAdapter(lichAdapter);

        updateLichVisibility();
    }

    private void xuLySuKien() {
        btn_cancel.setOnClickListener(v -> finish());
        btnBack.setOnClickListener(v -> finish());

        spinnerDiemDen.setOnItemClickListener((parent, view, position, id) -> {
            DiemDen selected = (DiemDen) parent.getItemAtPosition(position);
            tourRequest.setMaDiemDen(selected.getMaDiemDen());
        });

        // Xử lý chọn Trạng Thái (RadioGroup)
        rdGroupTrangThai.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_dang_mo) {
                tourRequest.setTrangThai("DangMo");
            } else if (checkedId == R.id.rd_tamDung) {
                tourRequest.setTrangThai("TamDung");
            }
        });

        ///load ảnh
        btn_upload_img.setOnClickListener(v -> {
            // Mở trình chọn ảnh của hệ thống
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });

        btn_them_lichKH.setOnClickListener(v -> {
            showDialogLichKhoiHanh(false, -1, null);
        });

        btn_save.setOnClickListener(v -> {
            tourRequest.setTenTour(txtTourName.getText().toString());
            tourRequest.setMoTa(txt_moTa.getText().toString());
            tourRequest.setGiaNguoiLon(Double.parseDouble(txt_giaNguoiLon.getText().toString()));
            tourRequest.setGiaTreEm(Double.parseDouble(txt_giaTreEm.getText().toString()));

            saveTour();
        });
    }

    private void loadDiemDen() {
        apiService.getDiemDens().enqueue(new Callback<List<DiemDen>>() {
            @Override
            public void onResponse(Call<List<DiemDen>> call, Response<List<DiemDen>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listDiemDen = response.body();

                    // Tạo adapter
                    adapterDiemDen = new ArrayAdapter<>(
                            ThemTourActivity.this,
                            android.R.layout.simple_dropdown_item_1line,
                            listDiemDen
                    );

                    spinnerDiemDen.setAdapter(adapterDiemDen);
                }
            }

            @Override
            public void onFailure(Call<List<DiemDen>> call, Throwable t) {
                Toast.makeText(ThemTourActivity.this, "Lỗi tải điểm đến: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Hàm lưu tạm file ảnh
    private File getFileFromUri(Uri uri) {
        try {
            File tempFile = new File(getCacheDir(), "temp_image.jpg");
            InputStream inputStream = getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(tempFile);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            return tempFile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveTour() {
        String tenTour = txtTourName.getText().toString().trim();
        String moTa = txt_moTa.getText().toString().trim();
        String giaNLStr = txt_giaNguoiLon.getText().toString().trim();
        String giaTEStr = txt_giaTreEm.getText().toString().trim();

        if (tenTour.isEmpty()) { txtTourName.setError("Nhập tên tour"); return; }
        if (giaNLStr.isEmpty()) { txt_giaNguoiLon.setError("Nhập giá người lớn"); return; }
        if (tourRequest.getMaDiemDen() == 0) {
            Toast.makeText(this, "Vui lòng chọn điểm đến!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedImageUri == null) {
            Toast.makeText(this, "Vui lòng chọn ảnh đại diện cho tour!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (listLKH.isEmpty()) {
            Toast.makeText(this, "Tour phải có ít nhất một lịch khởi hành!", Toast.LENGTH_SHORT).show();
            return;
        }

        tourRequest.setTenTour(tenTour);
        tourRequest.setMoTa(moTa);
        tourRequest.setGiaNguoiLon(Double.parseDouble(giaNLStr));
        tourRequest.setGiaTreEm(giaTEStr.isEmpty() ? 0.0 : Double.parseDouble(giaTEStr));
        // listLKH đã được gán vào tourRequest trong showDialogLichKhoiHanh thông qua tourRequest.setLichKhoiHanhs(listLKH)

        btn_save.setEnabled(false);
        btn_save.setText("Đang lưu...");

        Gson gson = new Gson();
        String jsonTour = gson.toJson(tourRequest);
        RequestBody tourPart = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                jsonTour
        );

        File file = getFileFromUri(selectedImageUri);
        if (file == null) return;

        RequestBody filePart = RequestBody.create(
                MediaType.parse("image/*"),
                file
        );
        MultipartBody.Part fileMultipart = MultipartBody.Part.createFormData("file", file.getName(), filePart);

        apiService.addFullTour(tourPart, fileMultipart).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                btn_save.setEnabled(true);
                btn_save.setText("Lưu Tour");

                if (response.isSuccessful()) {
                    Toast.makeText(ThemTourActivity.this, "Thêm tour và lịch trình thành công!", Toast.LENGTH_LONG).show();
                    finish(); // Đóng màn hình sau khi lưu thành công
                } else {
                    try {
                        String errorLog = response.errorBody() != null ? response.errorBody().string() : "Lỗi không xác định";
                        Log.e("SAVE_TOUR_ERR", errorLog);
                        Toast.makeText(ThemTourActivity.this, "Lỗi server: " + errorLog, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                btn_save.setEnabled(true);
                btn_save.setText("Lưu Tour");
                Log.e("SAVE_TOUR_FAIL", t.getMessage());
                Toast.makeText(ThemTourActivity.this, "Lỗi kết nối mạng!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDialogLichKhoiHanh(boolean isEdit, int position, TourRequest.LichKhoiHanhDTO editItem) {
        Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.dialog_themlichkhoihanh_admin);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextInputEditText txtNgayKH = dialog.findViewById(R.id.txt_ngay_khoi_hanh);
        TextInputEditText txtNgayKT = dialog.findViewById(R.id.txt_ngay_ket_thuc);
        TextInputEditText txtGioKH = dialog.findViewById(R.id.txt_gio_khoi_hanh);
        AutoCompleteTextView cmbHDV = dialog.findViewById(R.id.cmb_huong_dan_vien);
        TextInputEditText txtSoLuong = dialog.findViewById(R.id.txt_so_luong_khach);
        MaterialButton btnConfirm = dialog.findViewById(R.id.btn_confirm_dialog);
        MaterialButton btnHuy = dialog.findViewById(R.id.btn_huy);

        final int[] selectedMaHDV = {-1};
        final String[] selectedTenHDV = {""};

        if (isEdit) {
            btnConfirm.setText("Cập nhật");
            txtNgayKH.setText(editItem.getNgayKhoiHanh().split(" ")[0]);
            txtGioKH.setText(editItem.getNgayKhoiHanh().split(" ")[1]);
            txtNgayKT.setText(editItem.getNgayKetThuc().split(" ")[0]);
            txtSoLuong.setText(String.valueOf(editItem.getSoLuongKhachToiDa()));
            cmbHDV.setText(editItem.getTenHDV(), false);
            selectedMaHDV[0] = editItem.getMaHDV();
            selectedTenHDV[0] = editItem.getTenHDV();
        }

        //Thiết lập Adapter cho HDV
        ArrayAdapter<HuongDanVienResponse> hdvAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listHDV);
        cmbHDV.setAdapter(hdvAdapter);

        cmbHDV.setOnItemClickListener((parent, view, pos, id) -> {
            HuongDanVienResponse selected = (HuongDanVienResponse) parent.getItemAtPosition(pos);
            selectedMaHDV[0] = selected.getMaHuongDanVien();
            selectedTenHDV[0] = selected.getTenHuongDanVien();
        });

        // Xử lý chọn Ngày/Giờ (Sử dụng DatePickerDialog & TimePickerDialog)
        txtNgayKH.setOnClickListener(v -> showDatePicker(txtNgayKH));
        txtNgayKT.setOnClickListener(v -> showDatePicker(txtNgayKT));
        txtGioKH.setOnClickListener(v -> showTimePicker(txtGioKH));

        btnHuy.setOnClickListener(v -> dialog.dismiss());

        btnConfirm.setOnClickListener(v -> {
            String sNgayKH = txtNgayKH.getText().toString().trim();
            String sNgayKT = txtNgayKT.getText().toString().trim();
            String sGioKH = txtGioKH.getText().toString().trim();
            String sSoLuong = txtSoLuong.getText().toString().trim();

            // Kiểm tra trống
            if (sNgayKH.isEmpty() || sNgayKT.isEmpty() || sGioKH.isEmpty() || sSoLuong.isEmpty() || selectedMaHDV[0] == -1) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin có dấu (*)", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra số lượng
            int soLuong = Integer.parseInt(sSoLuong);
            if (soLuong <= 0) {
                Toast.makeText(this, "Số lượng khách phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra logic ngày (Ngày kết thúc không được trước ngày khởi hành)
            if (sNgayKT.compareTo(sNgayKH) < 0) {
                Toast.makeText(this, "Ngày kết thúc không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tạo DTO và lưu
            String fullNgayKH = sNgayKH + " " + sGioKH;
            String fullNgayKT = sNgayKT + " 23:59:59";
            TourRequest.LichKhoiHanhDTO dto = new TourRequest.LichKhoiHanhDTO(fullNgayKH, fullNgayKT, soLuong);
            dto.setMaHDV(selectedMaHDV[0]);
            dto.setTenHDV(selectedTenHDV[0]);

            if (isEdit) {
                listLKH.set(position, dto);
            } else {
                listLKH.add(dto);
            }

            lichAdapter.notifyDataSetChanged();
            tourRequest.setLichKhoiHanhs(listLKH);
            updateLichVisibility();
            dialog.dismiss();
        });

        dialog.show();
    }

    // Hàm bổ trợ chọn ngày
    private void showDatePicker(EditText editText) {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String date = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
            editText.setText(date);
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    // Hàm bổ trợ chọn giờ
    private void showTimePicker(EditText editText) {
        Calendar c = Calendar.getInstance();
        new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            editText.setText(String.format("%02d:%02d:00", hourOfDay, minute));
        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
    }

    private void loadHuongDanVien() {
        apiService.getHuongDanViens().enqueue(new Callback<List<HuongDanVienResponse>>() {
            @Override
            public void onResponse(Call<List<HuongDanVienResponse>> call, Response<List<HuongDanVienResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listHDV = response.body();
                }
            }
            @Override
            public void onFailure(Call<List<HuongDanVienResponse>> call, Throwable t) {
                Log.e("API_ERROR", t.getMessage());
            }
        });
    }

    private void updateLichVisibility() {
        if (listLKH == null || listLKH.isEmpty()) {
            txtChuaCoLich.setVisibility(android.view.View.VISIBLE);
            rvLichKhoiHanh.setVisibility(android.view.View.GONE);
        } else {
            txtChuaCoLich.setVisibility(android.view.View.GONE);
            rvLichKhoiHanh.setVisibility(android.view.View.VISIBLE);
        }
    }
}
