package com.example.apptravel.ui.activitys.admin;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
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

import com.bumptech.glide.Glide;
import com.example.apptravel.R;
import com.example.apptravel.data.adapters.LichKhoiHanhAdminAdapter;
import com.example.apptravel.data.api.ApiClient;
import com.example.apptravel.data.api.ApiService;
import com.example.apptravel.data.models.DiemDen;
import com.example.apptravel.data.models.HuongDanVienResponse;
import com.example.apptravel.data.models.Tour;
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

public class SuaTourActivity extends AppCompatActivity  {

    private ImageView imgTourHeader, btnBack;
    private TextInputEditText edtName, edtGiaNL, edtGiaTE, edtMoTa;
    private AutoCompleteTextView spinnerDiemDen;
    private RadioGroup rgTrangThai;
    private RadioButton rbDangMo, rbTamDung;
    private MaterialButton btnEditMode, btnSave;
    private TextView btnThemLich;
    private RecyclerView rvSchedules;

    private Tour currentTour;
    private ApiService apiService;
    private List<DiemDen> listDiemDen = new ArrayList<>();
    private List<HuongDanVienResponse> listHDV = new ArrayList<>();
    private List<TourRequest.LichKhoiHanhDTO> listLKH = new ArrayList<>();
    private TourRequest tourRequest = new TourRequest();
    private LichKhoiHanhAdminAdapter scheduleAdapter;
    private int selectedMaDiemDen = -1;
    private Uri selectedImageUri = null;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_tour);

        apiService = ApiClient.getClient(this).create(ApiService.class);

        // 1. Nhận dữ liệu từ Intent
        currentTour = (Tour) getIntent().getSerializableExtra("TOUR_OBJECT");

        anhXa();
        setupRecyclerView();
        loadDataToViews();
        loadDiemDenAndHDV();

        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                selectedImageUri = uri;
                imgTourHeader.setImageURI(uri); // Hiển thị ảnh vừa chọn lên header
            }
        });

        setEditMode(false); // Mặc định chỉ xem
        xuLySuKien();
    }

    private void anhXa() {
        imgTourHeader = findViewById(R.id.tour_image);
        btnBack = findViewById(R.id.btn_back);
        edtName = findViewById(R.id.edt_tour_name);
        edtGiaNL = findViewById(R.id.edt_gia_nguoi_lon);
        edtGiaTE = findViewById(R.id.edt_gia_tre_em);
        edtMoTa = findViewById(R.id.edt_mo_ta);
        spinnerDiemDen = findViewById(R.id.spinner_diem_den);
        rgTrangThai = findViewById(R.id.rg_trang_thai);
        rbDangMo = findViewById(R.id.rb_dang_mo);
        rbTamDung = findViewById(R.id.rb_tam_dung);
        btnEditMode = findViewById(R.id.btn_edit_mode);
        btnSave = findViewById(R.id.btn_save);
        btnThemLich = findViewById(R.id.btn_them_lichKH);
        rvSchedules = findViewById(R.id.recycler_schedule);
    }

    private void loadDataToViews() {
        if (currentTour == null) return;

        edtName.setText(currentTour.getTenTour());
        edtGiaNL.setText(String.valueOf(currentTour.getGiaNguoiLon()));
        edtGiaTE.setText(String.valueOf(currentTour.getGiaTreEm()));
        edtMoTa.setText(currentTour.getMoTa());

        if (currentTour.getDiemDen() != null) {
            spinnerDiemDen.setText(currentTour.getDiemDen().getTenDiemDen(), false);
            selectedMaDiemDen = currentTour.getDiemDen().getMaDiemDen();
        }

        if ("DangMo".equals(currentTour.getTrangThai())) rbDangMo.setChecked(true);
        else rbTamDung.setChecked(true);

        Glide.with(this)
                .load(ApiClient.getFullImageUrl(currentTour.getUrlHinhAnhChinh()))
                .into(imgTourHeader);

        // Chuyển đổi list LichKhoiHanh từ Tour model sang DTO cho adapter
        if (currentTour.getLichKhoiHanhs() != null) {
            for (var l : currentTour.getLichKhoiHanhs()) {
                TourRequest.LichKhoiHanhDTO dto = new TourRequest.LichKhoiHanhDTO(
                        l.getNgayKhoiHanh().toString().replace("T", " "),
                        l.getNgayKetThuc().toString().replace("T", " "),
                        l.getSoLuongKhachToiDa()
                );
                if (l.getHuongDanVien() != null) {
                    dto.setMaHDV(l.getHuongDanVien().getMaNguoiDung());
                    dto.setTenHDV(l.getHuongDanVien().getHoTen());
                }
                listLKH.add(dto);
            }
            scheduleAdapter.notifyDataSetChanged();
        }
    }

    private void setupRecyclerView() {
        scheduleAdapter = new LichKhoiHanhAdminAdapter(listLKH, new LichKhoiHanhAdminAdapter.OnItemClickListener() {
            @Override
            public void onEdit(int position, TourRequest.LichKhoiHanhDTO item) {
                // Chỉ cho sửa nếu đang trong chế độ Edit
                if (btnSave.getVisibility() == View.VISIBLE) {
                    showDialogLichKhoiHanh(true, position, item);
                }
            }

            @Override
            public void onDelete(int position) {
                if (btnSave.getVisibility() == View.VISIBLE) {
                    listLKH.remove(position);
                    scheduleAdapter.notifyItemRemoved(position);
                }
            }
        });
        rvSchedules.setLayoutManager(new LinearLayoutManager(this));
        rvSchedules.setAdapter(scheduleAdapter);
    }

    private void setEditMode(boolean isEditing) {
        edtName.setEnabled(isEditing);
        edtGiaNL.setEnabled(isEditing);
        edtGiaTE.setEnabled(isEditing);
        edtMoTa.setEnabled(isEditing);
        spinnerDiemDen.setEnabled(isEditing);
        rbDangMo.setEnabled(isEditing);
        rbTamDung.setEnabled(isEditing);

        btnSave.setVisibility(isEditing ? View.VISIBLE : View.GONE);
        btnThemLich.setVisibility(isEditing ? View.VISIBLE : View.GONE);
        btnEditMode.setText(isEditing ? "Hủy sửa" : "Chỉnh sửa");
    }

    private void xuLySuKien() {
        btnBack.setOnClickListener(v -> finish());

        btnEditMode.setOnClickListener(v -> {
            boolean currentMode = btnSave.getVisibility() == View.VISIBLE;
            setEditMode(!currentMode);
        });

        btnThemLich.setOnClickListener(v -> {
            showDialogLichKhoiHanh(false, -1, null);
        });

        btnSave.setOnClickListener(v -> updateTour());

        spinnerDiemDen.setOnItemClickListener((parent, view, position, id) -> {
            DiemDen d = (DiemDen) parent.getItemAtPosition(position);
            selectedMaDiemDen = d.getMaDiemDen();
        });

        imgTourHeader.setOnClickListener(v -> {
            if (btnSave.getVisibility() == View.VISIBLE) {
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            }
        });
    }

    private void updateTour() {
        String name = edtName.getText().toString().trim();
        if (name.isEmpty()) name = currentTour.getTenTour();

        String moTa = edtMoTa.getText().toString().trim();
        if (moTa.isEmpty()) moTa = currentTour.getMoTa();

        String giaNLStr = edtGiaNL.getText().toString().trim();
        double giaNL = giaNLStr.isEmpty() ? currentTour.getGiaNguoiLon() : Double.parseDouble(giaNLStr);

        String giaTEStr = edtGiaTE.getText().toString().trim();
        double giaTE = giaTEStr.isEmpty() ? currentTour.getGiaTreEm() : Double.parseDouble(giaTEStr);

        int maDD = (selectedMaDiemDen == -1 && currentTour.getDiemDen() != null)
                ? currentTour.getDiemDen().getMaDiemDen()
                : selectedMaDiemDen;

        try {
            // 2. Đóng gói dữ liệu vào TourRequest
            TourRequest request = new TourRequest();
            request.setTenTour(name);
            request.setMoTa(moTa);
            request.setGiaNguoiLon(giaNL);
            request.setGiaTreEm(giaTE);
            request.setMaDiemDen(maDD);
            request.setTrangThai(rbDangMo.isChecked() ? "DangMo" : "TamDung");
            request.setLichKhoiHanhs(listLKH);

            // Chuyển đối tượng request thành JSON RequestBody
            Gson gson = new Gson();
            RequestBody tourPart = RequestBody.create(
                    MediaType.parse("application/json"), gson.toJson(request));

            // 3. Xử lý phần file ảnh (QUAN TRỌNG)
            MultipartBody.Part filePart = null;
            if (selectedImageUri != null) {
                // Nếu có chọn ảnh mới -> Tạo MultipartBody
                File file = getFileFromUri(selectedImageUri);
                if (file != null) {
                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                    filePart = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
                }
            }
            // Nếu filePart == null, Retrofit sẽ gửi một request Multipart mà không có phần "file"
            // Backend của bạn sẽ nhận được file = null và không thực hiện xóa/thay ảnh cũ.

            btnSave.setEnabled(false);
            btnSave.setText("Đang cập nhật...");

            // 4. Gọi API
            apiService.updateTour(currentTour.getMaTour(), tourPart, filePart).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    btnSave.setEnabled(true);
                    btnSave.setText("Lưu thay đổi");
                    if (response.isSuccessful()) {
                        Toast.makeText(SuaTourActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(SuaTourActivity.this, "Lỗi cập nhật: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    btnSave.setEnabled(true);
                    btnSave.setText("Lưu thay đổi");
                    Toast.makeText(SuaTourActivity.this, "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Vui lòng nhập giá tiền hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadDiemDenAndHDV() {
        apiService.getDiemDens().enqueue(new Callback<List<DiemDen>>() {
            @Override
            public void onResponse(Call<List<DiemDen>> call, Response<List<DiemDen>> response) {
                if (response.isSuccessful()) {
                    listDiemDen = response.body();
                    ArrayAdapter<DiemDen> adapter = new ArrayAdapter<>(SuaTourActivity.this,
                            android.R.layout.simple_dropdown_item_1line, listDiemDen);
                    spinnerDiemDen.setAdapter(adapter);
                }
            }
            @Override public void onFailure(Call<List<DiemDen>> call, Throwable t) {}
        });

        apiService.getHuongDanViens().enqueue(new Callback<List<HuongDanVienResponse>>() {
            @Override
            public void onResponse(Call<List<HuongDanVienResponse>> call, Response<List<HuongDanVienResponse>> response) {
                if (response.isSuccessful()) listHDV = response.body();
            }
            @Override public void onFailure(Call<List<HuongDanVienResponse>> call, Throwable t) {}
        });
    }

    private void showDialogLichKhoiHanh(boolean isEdit, int position, TourRequest.LichKhoiHanhDTO editItem) {
        Dialog dialog = new Dialog(this, android.R.style.Theme_Material_Light_Dialog_NoActionBar);
        dialog.setContentView(R.layout.dialog_themlichkhoihanh_admin);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // Ánh xạ các view trong dialog
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
        ArrayAdapter<HuongDanVienResponse> hdvAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, listHDV);
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

            scheduleAdapter.notifyDataSetChanged();
            tourRequest.setLichKhoiHanhs(listLKH);
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

}
