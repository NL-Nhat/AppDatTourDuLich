package com.example.apptravel.ui.activitys.admin;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
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
import com.example.apptravel.data.models.NguoiDung;
import com.example.apptravel.data.models.TourRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThemTourActivity extends AppCompatActivity {

    private TextInputEditText edt_tour_name, edt_mo_ta, edt_gia_nguoi_lon, edt_gia_tre_em;
    private AutoCompleteTextView spinner_diem_den;
    private RadioGroup radio_group_trang_thai;
    private MaterialButton btn_them_lichKH, btn_save, btn_cancel;
    private RecyclerView recycler_lich_kh;
    private TextView tv_empty_lich;
    private ImageView img_tour_preview;

    private LichKhoiHanhAdminAdapter lichAdapter;
    private List<TourRequest.LichKhoiHanhDTO> listLichTam = new ArrayList<>();
    private List<DiemDen> listDiemDenEntity = new ArrayList<>();
    private List<NguoiDung> listHuongDanVien = new ArrayList<>();

    private Integer maDiemDenSelected = null;
    private String hinhAnhSelected = "ha_long.jpg"; // Mặc định

    // --- KHÔI PHỤC LAUNCHER CHỌN ẢNH TỪ THIẾT BỊ ---
    private final ActivityResultLauncher<String> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    Glide.with(this).load(uri).into(img_tour_preview);
                    hinhAnhSelected = uri.toString(); // Sẽ lưu chuỗi content://... vào DB
                    Toast.makeText(this, "Đã chọn ảnh từ thư viện", Toast.LENGTH_SHORT).show();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_tour);

        initViews();
        loadDuLieuBanDau();
        setupEvents();
        updateLichTrinhUI();
    }

    private void initViews() {
        edt_tour_name = findViewById(R.id.edt_tour_name);
        edt_mo_ta = findViewById(R.id.edt_mo_ta);
        edt_gia_nguoi_lon = findViewById(R.id.edt_gia_nguoi_lon);
        edt_gia_tre_em = findViewById(R.id.edt_gia_tre_em);
        spinner_diem_den = findViewById(R.id.spinner_diem_den);
        radio_group_trang_thai = findViewById(R.id.radio_group_trang_thai);
        btn_them_lichKH = findViewById(R.id.btn_them_lichKH);
        btn_save = findViewById(R.id.btn_save);
        btn_cancel = findViewById(R.id.btn_cancel);
        recycler_lich_kh = findViewById(R.id.recycler_lich_kh);
        tv_empty_lich = findViewById(R.id.tv_empty_lich);
        img_tour_preview = findViewById(R.id.img_tour_preview);

        recycler_lich_kh.setLayoutManager(new LinearLayoutManager(this));
        lichAdapter = new LichKhoiHanhAdminAdapter(this, listLichTam, new LichKhoiHanhAdminAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(int position) {
                showDialogThemLich(listLichTam.get(position), position);
            }

            @Override
            public void onDeleteClick(int position) {
                listLichTam.remove(position);
                updateLichTrinhUI();
            }
        });
        recycler_lich_kh.setAdapter(lichAdapter);
        lichAdapter.setEditMode(true);
    }

    private void setupEvents() {
        btn_them_lichKH.setOnClickListener(v -> showDialogThemLich(null, -1));
        btn_save.setOnClickListener(v -> saveTourToServer());
        btn_cancel.setOnClickListener(v -> finish());
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        // --- KHÔI PHỤC SỰ KIỆN CLICK: MỞ THƯ VIỆN ---
        findViewById(R.id.card_image_picker).setOnClickListener(v -> {
            pickImageLauncher.launch("image/*");
        });

        spinner_diem_den.setOnClickListener(v -> spinner_diem_den.showDropDown());
    }

    private void loadDuLieuBanDau() {
        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);

        apiService.getDiemDens().enqueue(new Callback<List<DiemDen>>() {
            @Override
            public void onResponse(Call<List<DiemDen>> call, Response<List<DiemDen>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listDiemDenEntity.clear();
                    listDiemDenEntity.addAll(response.body());
                    List<String> names = listDiemDenEntity.stream().map(DiemDen::getTenDiemDen).collect(Collectors.toList());
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(ThemTourActivity.this, android.R.layout.simple_dropdown_item_1line, names);
                    spinner_diem_den.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<DiemDen>> call, Throwable t) {
            }
        });

        apiService.getHuongDanViens().enqueue(new Callback<List<NguoiDung>>() {
            @Override
            public void onResponse(Call<List<NguoiDung>> call, Response<List<NguoiDung>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listHuongDanVien.clear();
                    listHuongDanVien.addAll(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<NguoiDung>> call, Throwable t) {
            }
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

    private void showDialogThemLich(final TourRequest.LichKhoiHanhDTO lich, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
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

        List<String> hdvNames = listHuongDanVien.stream().map(NguoiDung::getHoTen).collect(Collectors.toList());
        ArrayAdapter<String> hdvAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, hdvNames);
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
            try {
                String ngayDi = edtNgayDi.getText().toString().trim();
                String ngayVe = edtNgayVe.getText().toString().trim();
                String gioKH = edtGioKH.getText().toString().trim();
                String soLuongStr = edtSoLuong.getText().toString().trim();

                if (ngayDi.isEmpty() || gioKH.isEmpty() || soLuongStr.isEmpty() || selectedHDVId[0] == null) {
                    Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (ngayVe.isEmpty()) ngayVe = ngayDi;

                int soKhach = Integer.parseInt(soLuongStr);
                String startDateTime = ngayDi + "T" + gioKH + ":00";
                String endDateTime = ngayVe.contains("T") ? ngayVe : (ngayVe + "T23:59:59");

                TourRequest.LichKhoiHanhDTO dto = new TourRequest.LichKhoiHanhDTO(startDateTime, endDateTime, soKhach);
                dto.setMaHDV(selectedHDVId[0]);
                dto.setTenHDV(selectedHDVName[0]);

                if (position == -1) {
                    listLichTam.add(dto);
                } else {
                    listLichTam.set(position, dto);
                }
                updateLichTrinhUI();
                dialog.dismiss();
            } catch (Exception e) {
                Toast.makeText(this, "Lỗi khi lưu lịch: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
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

    private void updateLichTrinhUI() {
        if (listLichTam.isEmpty()) {
            tv_empty_lich.setVisibility(View.VISIBLE);
            recycler_lich_kh.setVisibility(View.GONE);
        } else {
            tv_empty_lich.setVisibility(View.GONE);
            recycler_lich_kh.setVisibility(View.VISIBLE);
            lichAdapter.notifyDataSetChanged();
        }
    }

    private void saveTourToServer() {
        String name = edt_tour_name.getText().toString();
        if (name.isEmpty() || maDiemDenSelected == null) {
            Toast.makeText(this, "Vui lòng nhập tên và chọn điểm đến", Toast.LENGTH_SHORT).show();
            return;
        }

        if (listLichTam.isEmpty()) {
            Toast.makeText(this, "Vui lòng thêm ít nhất một lịch trình", Toast.LENGTH_SHORT).show();
            return;
        }

        TourRequest request = new TourRequest();
        request.setTenTour(name);
        request.setMoTa(edt_mo_ta.getText().toString());
        request.setUrlHinhAnhChinh(hinhAnhSelected);
        request.setMaDiemDen(maDiemDenSelected);
        request.setLichKhoiHanhs(listLichTam);

        int selectedStatusId = radio_group_trang_thai.getCheckedRadioButtonId();
        request.setTrangThai(selectedStatusId == R.id.rb_dang_mo ? "DangMo" : "TamDung");

        try {
            String gnl = edt_gia_nguoi_lon.getText().toString();
            String gte = edt_gia_tre_em.getText().toString();
            request.setGiaNguoiLon(gnl.isEmpty() ? BigDecimal.ZERO : new BigDecimal(gnl));
            request.setGiaTreEm(gte.isEmpty() ? BigDecimal.ZERO : new BigDecimal(gte));
        } catch (Exception e) {
            Toast.makeText(this, "Giá tiền không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        apiService.addFullTour(request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ThemTourActivity.this, "Thêm tour thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ThemTourActivity.this, "Lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ThemTourActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
