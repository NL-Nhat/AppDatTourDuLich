package com.example.apptravel.ui.activitys.admin;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
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
import com.example.apptravel.data.models.LichKhoiHanh;
import com.example.apptravel.data.models.NguoiDung;
import com.example.apptravel.data.models.Tour;
import com.example.apptravel.data.models.TourRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuaTourActivity extends AppCompatActivity implements LichKhoiHanhAdminAdapter.OnItemClickListener {
    private TextInputEditText edt_tour_name, edt_mo_ta, edt_gia_nguoi_lon, edt_gia_tre_em;
    private AutoCompleteTextView spinner_diem_den;
    private MaterialButton btn_edit_mode, btn_save;
    private ImageView tour_image;
    private RecyclerView recycler_schedule;
    private RadioGroup rg_trang_thai;
    private RadioButton rb_dang_mo, rb_tam_dung;
    private TextView btn_them_lichKH;

    private Tour tourHienTai;
    private Integer maDiemDenSelected;
    
    // Biến lưu tên ảnh trên server (vd: ha_long.jpg)
    private String hinhAnhServer; 
    
    private boolean isEditMode = false;
    private List<DiemDen> listDiemDenEntity = new ArrayList<>();
    private List<NguoiDung> listHuongDanVien = new ArrayList<>();
    private List<TourRequest.LichKhoiHanhDTO> listLichTrinhDTO = new ArrayList<>();
    private LichKhoiHanhAdminAdapter lichKhoiHanhAdminAdapter;
    private ApiService apiService;
    
    // Biến lưu ảnh chọn từ điện thoại để preview
    private Uri selectedImageUri = null;

    private final ActivityResultLauncher<String> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    // Chỉ hiển thị để xem trước
                    Glide.with(this).load(uri).into(tour_image);
                    Toast.makeText(this, "Đã chọn ảnh (Lưu ý: Ảnh này sẽ chưa được lưu lên server)", Toast.LENGTH_LONG).show();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_tour);

        apiService = ApiClient.getClient(this).create(ApiService.class);

        initViews();
        setupEvents();

        toggleEditMode(false);
        loadDiemDenFromServer();
        loadHuongDanVienFromServer();

        if (getIntent().hasExtra("TOUR_OBJECT")) {
            Tour tourTomTat = (Tour) getIntent().getSerializableExtra("TOUR_OBJECT");
            if (tourTomTat != null) {
                tourHienTai = tourTomTat;
                fillDataSafe();
                loadFullTourDetails(tourTomTat.getMaTour());
            }
        }
    }

    private void initViews() {
        edt_tour_name = findViewById(R.id.edt_tour_name);
        edt_mo_ta = findViewById(R.id.edt_mo_ta);
        edt_gia_nguoi_lon = findViewById(R.id.edt_gia_nguoi_lon);
        edt_gia_tre_em = findViewById(R.id.edt_gia_tre_em);
        spinner_diem_den = findViewById(R.id.spinner_diem_den);
        btn_edit_mode = findViewById(R.id.btn_edit_mode);
        btn_save = findViewById(R.id.btn_save);
        tour_image = findViewById(R.id.tour_image);
        recycler_schedule = findViewById(R.id.recycler_schedule);
        rg_trang_thai = findViewById(R.id.rg_trang_thai);
        rb_dang_mo = findViewById(R.id.rb_dang_mo);
        rb_tam_dung = findViewById(R.id.rb_tam_dung);
        btn_them_lichKH = findViewById(R.id.btn_them_lichKH);

        lichKhoiHanhAdminAdapter = new LichKhoiHanhAdminAdapter(this, listLichTrinhDTO, this);
        recycler_schedule.setLayoutManager(new LinearLayoutManager(this));
        recycler_schedule.setAdapter(lichKhoiHanhAdminAdapter);
    }

    private void setupEvents() {
        btn_edit_mode.setOnClickListener(v -> {
            isEditMode = !isEditMode;
            toggleEditMode(isEditMode);
        });

        btn_save.setOnClickListener(v -> updateTourToServer());
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        tour_image.setOnClickListener(v -> {
            if (isEditMode) {
                pickImageLauncher.launch("image/*");
            }
        });

        btn_them_lichKH.setOnClickListener(v -> showScheduleDialog(null, -1));
    }

    private void showScheduleDialog(final TourRequest.LichKhoiHanhDTO lich, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_themlichkhoihanh_admin, null);
        builder.setView(dialogView);

        final TextView tvTitle = dialogView.findViewById(R.id.tv_title);
        final TextInputEditText edtNgayDi = dialogView.findViewById(R.id.txt_ngay_khoi_hanh);
        final TextInputEditText edtNgayVe = dialogView.findViewById(R.id.txt_ngay_ket_thuc);
        final TextInputEditText edtGioKH = dialogView.findViewById(R.id.txt_gio_khoi_hanh);
        final TextInputEditText edtSoLuong = dialogView.findViewById(R.id.txt_so_luong_khach);
        final AutoCompleteTextView spinnerHDV = dialogView.findViewById(R.id.cmb_huong_dan_vien);
        final MaterialButton btnConfirm = dialogView.findViewById(R.id.btn_confirm_dialog);
        final MaterialButton btnHuy = dialogView.findViewById(R.id.btn_huy);

        final AlertDialog dialog = builder.create();

        tvTitle.setText(lich == null ? "Thêm lịch khởi hành" : "Sửa lịch khởi hành");

        edtNgayDi.setOnClickListener(v -> showDatePicker(edtNgayDi));
        edtNgayVe.setOnClickListener(v -> showDatePicker(edtNgayVe));
        edtGioKH.setOnClickListener(v -> showTimePicker(edtGioKH));
        spinnerHDV.setOnClickListener(v -> spinnerHDV.showDropDown());
        btnHuy.setOnClickListener(v -> dialog.dismiss());

        List<String> tenHDV = listHuongDanVien.stream().map(NguoiDung::getHoTen).collect(Collectors.toList());
        ArrayAdapter<String> hdvAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, tenHDV);
        spinnerHDV.setAdapter(hdvAdapter);

        final Integer[] selectedHDVId = {null};
        final String[] selectedHDVName = {null};

        spinnerHDV.setOnItemClickListener((p, v, pos, id) -> {
            selectedHDVName[0] = (String) p.getItemAtPosition(pos);
            for (NguoiDung n : listHuongDanVien) {
                if (n.getHoTen().equals(selectedHDVName[0])) {
                    selectedHDVId[0] = n.getMaNguoiDung();
                    break;
                }
            }
        });

        if (lich != null) {
            if (lich.getNgayKhoiHanh() != null && lich.getNgayKhoiHanh().length() >= 16) {
                 edtNgayDi.setText(lich.getNgayKhoiHanh().substring(0, 10));
                 edtGioKH.setText(lich.getNgayKhoiHanh().substring(11, 16));
            } else if (lich.getNgayKhoiHanh() != null) {
                 edtNgayDi.setText(lich.getNgayKhoiHanh());
            }
            
            if (lich.getNgayKetThuc() != null && lich.getNgayKetThuc().length() >= 10) {
                 edtNgayVe.setText(lich.getNgayKetThuc().substring(0, 10));
            } else {
                 edtNgayVe.setText(lich.getNgayKetThuc());
            }

            edtSoLuong.setText(String.valueOf(lich.getSoLuongKhachToiDa()));
            
            if (lich.getTenHDV() != null) {
                selectedHDVId[0] = lich.getMaHDV();
                selectedHDVName[0] = lich.getTenHDV();
                spinnerHDV.setText(lich.getTenHDV(), false);
            }
            btnConfirm.setText("Cập nhật");
        }

        btnConfirm.setOnClickListener(v -> {
            String ngayDi = edtNgayDi.getText().toString();
            String ngayVe = edtNgayVe.getText().toString();
            String gioKH = edtGioKH.getText().toString();
            String soLuongStr = edtSoLuong.getText().toString();

            if (ngayDi.isEmpty() || gioKH.isEmpty() || soLuongStr.isEmpty() || selectedHDVId[0] == null) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (ngayVe.isEmpty()) ngayVe = ngayDi;

            String startDateTime = ngayDi + "T" + gioKH + ":00";
            String endDateTime = ngayVe.contains("T") ? ngayVe : (ngayVe + "T23:59:59");

            int soLuong = Integer.parseInt(soLuongStr);
            TourRequest.LichKhoiHanhDTO newLich = new TourRequest.LichKhoiHanhDTO(startDateTime, endDateTime, soLuong);
            newLich.setMaHDV(selectedHDVId[0]);
            newLich.setTenHDV(selectedHDVName[0]);

            if (position != -1) {
                newLich.setMaLichKhoiHanh(listLichTrinhDTO.get(position).getMaLichKhoiHanh());
            }

            if (position == -1) {
                listLichTrinhDTO.add(newLich);
            } else {
                listLichTrinhDTO.set(position, newLich);
            }
            lichKhoiHanhAdminAdapter.notifyDataSetChanged();
            dialog.dismiss();
        });

        dialog.show();
    }

    @Override
    public void onEditClick(int position) {
        showScheduleDialog(listLichTrinhDTO.get(position), position);
    }

    @Override
    public void onDeleteClick(int position) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa lịch trình này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    listLichTrinhDTO.remove(position);
                    lichKhoiHanhAdminAdapter.notifyItemRemoved(position);
                    lichKhoiHanhAdminAdapter.notifyItemRangeChanged(position, listLichTrinhDTO.size());
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
    
    private void showDatePicker(TextInputEditText editText) {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    String date = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                    editText.setText(date);
                },
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void showTimePicker(TextInputEditText editText) {
        Calendar c = Calendar.getInstance();
        new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    String time = String.format("%02d:%02d", hourOfDay, minute);
                    editText.setText(time);
                },
                c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true)
                .show();
    }

    private void loadFullTourDetails(int tourId) {
        apiService.getTourDetails(tourId).enqueue(new Callback<Tour>() {
            @Override
            public void onResponse(Call<Tour> call, Response<Tour> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tourHienTai = response.body();
                    fillDataSafe();
                    setupScheduleList();
                } else {
                    Toast.makeText(SuaTourActivity.this, "Lỗi tải chi tiết tour", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Tour> call, Throwable t) {
                Log.e("SuaTour", "API Error: " + t.getMessage());
            }
        });
    }

    private void fillDataSafe() {
        if (tourHienTai == null) return;
        edt_tour_name.setText(tourHienTai.getTenTour());
        edt_mo_ta.setText(tourHienTai.getMoTa());

        DecimalFormat df = new DecimalFormat("#,###");
        if (tourHienTai.getGiaNguoiLon() != null) edt_gia_nguoi_lon.setText(df.format(tourHienTai.getGiaNguoiLon()));
        if (tourHienTai.getGiaTreEm() != null) edt_gia_tre_em.setText(df.format(tourHienTai.getGiaTreEm()));

        hinhAnhServer = tourHienTai.getUrlHinhAnhChinh();

        // Ưu tiên hiển thị ảnh chọn từ điện thoại để preview
        if (selectedImageUri != null) {
            Glide.with(this).load(selectedImageUri).into(tour_image);
        } else if (hinhAnhServer != null && !hinhAnhServer.isEmpty()) {
            String imageUrl = ApiClient.getFullImageUrl(this, "tour/" + hinhAnhServer);
            Glide.with(this).load(imageUrl).error(R.drawable.quang_ninh).into(tour_image);
        } else {
            tour_image.setImageResource(R.drawable.quang_ninh);
        }

        if (tourHienTai.getDiemDen() != null) {
            spinner_diem_den.setText(tourHienTai.getDiemDen().getTenDiemDen(), false);
            maDiemDenSelected = tourHienTai.getDiemDen().getMaDiemDen();
        }

        if ("DangMo".equalsIgnoreCase(tourHienTai.getTrangThai())) {
            rb_dang_mo.setChecked(true);
        } else {
            rb_tam_dung.setChecked(true);
        }
    }

    private void setupScheduleList() {
        listLichTrinhDTO.clear();
        if (tourHienTai != null && tourHienTai.getLichKhoiHanhs() != null) {
            for (LichKhoiHanh model : tourHienTai.getLichKhoiHanhs()) {
                if (model != null) {
                    TourRequest.LichKhoiHanhDTO dto = new TourRequest.LichKhoiHanhDTO(
                            model.getNgayKhoiHanh(),
                            model.getNgayKetThuc(),
                            model.getSoLuongKhachToiDa()
                    );
                    dto.setMaLichKhoiHanh(model.getMaLichKhoiHanh());
                    
                    if (model.getHuongDanVien() != null) {
                        dto.setMaHDV(model.getHuongDanVien().getMaNguoiDung());
                        dto.setTenHDV(model.getHuongDanVien().getHoTen());
                    }
                    listLichTrinhDTO.add(dto);
                }
            }
        }
        lichKhoiHanhAdminAdapter.notifyDataSetChanged();
    }

    private void loadDiemDenFromServer() {
        apiService.getDiemDens().enqueue(new Callback<List<DiemDen>>() {
            @Override
            public void onResponse(Call<List<DiemDen>> call, Response<List<DiemDen>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listDiemDenEntity.clear();
                    listDiemDenEntity.addAll(response.body());
                    List<String> names = listDiemDenEntity.stream().map(DiemDen::getTenDiemDen).collect(Collectors.toList());
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(SuaTourActivity.this, android.R.layout.simple_dropdown_item_1line, names);
                    spinner_diem_den.setAdapter(adapter);
                }
            }
            @Override public void onFailure(Call<List<DiemDen>> call, Throwable t) {}
        });

        spinner_diem_den.setOnItemClickListener((parent, view, position, id) -> {
            String selectedName = (String) parent.getItemAtPosition(position);
            for (DiemDen d : listDiemDenEntity) {
                if (d.getTenDiemDen().equals(selectedName)) {
                    maDiemDenSelected = d.getMaDiemDen();
                    break;
                }
            }
        });
    }

    private void loadHuongDanVienFromServer() {
        apiService.getHuongDanViens().enqueue(new Callback<List<NguoiDung>>() {
            @Override
            public void onResponse(Call<List<NguoiDung>> call, Response<List<NguoiDung>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listHuongDanVien.clear();
                    listHuongDanVien.addAll(response.body());
                }
            }
            @Override public void onFailure(Call<List<NguoiDung>> call, Throwable t) {}
        });
    }

    private void toggleEditMode(boolean enable) {
        isEditMode = enable;
        edt_tour_name.setEnabled(enable);
        edt_mo_ta.setEnabled(enable);
        edt_gia_nguoi_lon.setEnabled(enable);
        edt_gia_tre_em.setEnabled(enable);
        spinner_diem_den.setEnabled(enable);
        for (int i = 0; i < rg_trang_thai.getChildCount(); i++) {
            rg_trang_thai.getChildAt(i).setEnabled(enable);
        }

        btn_save.setVisibility(enable ? View.VISIBLE : View.GONE);
        btn_them_lichKH.setVisibility(enable ? View.VISIBLE : View.GONE);
        if (lichKhoiHanhAdminAdapter != null) {
            lichKhoiHanhAdminAdapter.setEditMode(enable);
        }

        if (enable) {
            btn_edit_mode.setText("Hủy");
            btn_edit_mode.setIconResource(R.drawable.ic_delete);
        } else {
            btn_edit_mode.setText("Chỉnh sửa");
            btn_edit_mode.setIconResource(R.drawable.ic_edit);
            if (tourHienTai != null) fillDataSafe();
        }
    }

    private void updateTourToServer() {
        TourRequest request = new TourRequest();
        request.setTenTour(edt_tour_name.getText().toString());
        request.setMoTa(edt_mo_ta.getText().toString());
        request.setMaDiemDen(maDiemDenSelected);
        
        // SỬA LỖI: Luôn gửi tên ảnh cũ trên server, không gửi đường dẫn local content://
        request.setUrlHinhAnhChinh(hinhAnhServer);

        if (rb_dang_mo.isChecked()) {
            request.setTrangThai("DangMo");
        } else {
            request.setTrangThai("TamDung");
        }

        try {
            String gNL = edt_gia_nguoi_lon.getText().toString().replaceAll("[^\\d]", "");
            String gTE = edt_gia_tre_em.getText().toString().replaceAll("[^\\d]", "");
            request.setGiaNguoiLon(new BigDecimal(gNL.isEmpty() ? "0" : gNL));
            request.setGiaTreEm(new BigDecimal(gTE.isEmpty() ? "0" : gTE));
        } catch (Exception e) {
            Toast.makeText(this, "Giá tiền không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        request.setLichKhoiHanhs(listLichTrinhDTO);

        if (tourHienTai != null) {
            apiService.updateTour(tourHienTai.getMaTour(), request).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        String msg = "Cập nhật thành công!";
                        if (selectedImageUri != null) {
                            msg += "\n(Ảnh mới chưa được lưu do server đang bảo trì)";
                        }
                        Toast.makeText(SuaTourActivity.this, msg, Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(SuaTourActivity.this, "Lỗi Server: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(SuaTourActivity.this, "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
