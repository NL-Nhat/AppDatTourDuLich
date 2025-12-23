package com.example.apptravel.ui.activitys.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.example.apptravel.data.api.ApiClient;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.apptravel.R;
import com.example.apptravel.data.models.AdminBookingItem;
import com.example.apptravel.data.repository.AdminBookingRepository;

import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminBookingDetailActivity extends AppCompatActivity {

    public static final String EXTRA_MA_DAT_TOUR = "maDatTour";

    private ProgressBar progress;

    private ImageView ivTourImage;
    private ImageView ivCustomerAvatar;

    private TextView tvMaDatTour;
    private TextView chipTrangThaiDatTour;
    private TextView chipTrangThaiThanhToan;

    private TextView tvKhachHang;
    private TextView tvEmail;
    private TextView tvSoDienThoai;

    private TextView tvTenTour;
    private TextView tvDiaDiem;
    private TextView tvNgay;
    private TextView tvSoNguoi;
    private TextView tvTongTien;

    private TextView tvNgayDat;
    private TextView tvNgayHuy;
    private TextView tvLyDoHuy;

    private AdminBookingRepository repo;

    private Button btnConfirm;
    private Button btnCancel;

    private Integer currentMaDatTour;
    private AdminBookingItem currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_booking_detail);

        progress = findViewById(R.id.progress);

        ivTourImage = findViewById(R.id.iv_tour_image);
        ivCustomerAvatar = findViewById(R.id.iv_customer_avatar);

        tvMaDatTour = findViewById(R.id.tv_ma_dat_tour);
        chipTrangThaiDatTour = findViewById(R.id.chip_trang_thai_dat_tour);
        chipTrangThaiThanhToan = findViewById(R.id.chip_trang_thai_thanh_toan);

        tvKhachHang = findViewById(R.id.tv_khach_hang);
        tvEmail = findViewById(R.id.tv_email);
        tvSoDienThoai = findViewById(R.id.tv_so_dien_thoai);

        tvTenTour = findViewById(R.id.tv_ten_tour);
        tvDiaDiem = findViewById(R.id.tv_dia_diem);
        tvNgay = findViewById(R.id.tv_ngay);
        tvSoNguoi = findViewById(R.id.tv_so_nguoi);
        tvTongTien = findViewById(R.id.tv_tong_tien);

        tvNgayDat = findViewById(R.id.tv_ngay_dat);
        tvNgayHuy = findViewById(R.id.tv_ngay_huy);
        tvLyDoHuy = findViewById(R.id.tv_ly_do_huy);

        repo = new AdminBookingRepository(this);

        btnConfirm = findViewById(R.id.btn_confirm);
        btnCancel = findViewById(R.id.btn_cancel);

        int maDatTour = getIntent().getIntExtra(EXTRA_MA_DAT_TOUR, -1);
        if (maDatTour <= 0) {
            Toast.makeText(this, "Thiếu mã đặt tour", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        currentMaDatTour = maDatTour;

        btnConfirm.setOnClickListener(v -> {
            if (currentItem != null) confirmBooking(currentItem);
        });
        btnCancel.setOnClickListener(v -> {
            if (currentItem != null) showCancelDialog(currentItem);
        });

        loadDetail(maDatTour);
    }

    private void loadDetail(int maDatTour) {
        progress.setVisibility(View.VISIBLE);
        repo.getBookingDetail(maDatTour).enqueue(new Callback<AdminBookingItem>() {
            @Override
            public void onResponse(@NonNull Call<AdminBookingItem> call, @NonNull Response<AdminBookingItem> response) {
                progress.setVisibility(View.GONE);
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(AdminBookingDetailActivity.this, "Không tải được chi tiết", Toast.LENGTH_SHORT).show();
                    return;
                }
                currentItem = response.body();
                bind(currentItem);
                updateActionButtons(currentItem);
            }

            @Override
            public void onFailure(@NonNull Call<AdminBookingItem> call, @NonNull Throwable t) {
                progress.setVisibility(View.GONE);
                Toast.makeText(AdminBookingDetailActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateActionButtons(AdminBookingItem item) {
        String status = item != null ? item.getTrangThaiDatTour() : null;
        boolean canConfirm = status != null && status.equalsIgnoreCase("ChoXacNhan");
        boolean canCancel = status == null || !status.equalsIgnoreCase("DaHuy");

        if (btnConfirm != null) btnConfirm.setVisibility(canConfirm ? View.VISIBLE : View.GONE);
        if (btnCancel != null) btnCancel.setVisibility(canCancel ? View.VISIBLE : View.GONE);
    }

    private void bind(AdminBookingItem item) {
        tvMaDatTour.setText("Đơn #" + item.getMaDatTour());

        // Status chip
        bindBookingStatusChip(item.getTrangThaiDatTour());
        bindPaymentStatusChip(item.getTrangThaiThanhToan());

        // Customer
        tvKhachHang.setText(safe(item.getHoTen()));
        tvEmail.setText(safe(item.getEmail()));
        tvSoDienThoai.setText(safe(item.getSoDienThoai()));

        // Avatar
        String avatar = item.getAnhDaiDien();
        if (avatar != null && !avatar.isEmpty()) {
            String url = ApiClient.getFullImageUrl(this, avatar);
            Glide.with(this)
                    .load(url)
                    .placeholder(R.drawable.nen)
                    .error(R.drawable.nen)
                    .timeout(60000)
                    .into(ivCustomerAvatar);
        } else {
            ivCustomerAvatar.setImageResource(R.drawable.nen);
        }

        // Tour
        tvTenTour.setText(safe(item.getTenTour()));
        tvDiaDiem.setText(safe(item.getDiaDiem()));

        // Tour image
        String tourImage = item.getUrlHinhAnhChinh();
        if (tourImage != null && !tourImage.isEmpty()) {
            String url = ApiClient.getFullImageUrl(this, tourImage);
            Glide.with(this)
                    .load(url)
                    .placeholder(R.drawable.nen)
                    .error(R.drawable.nen)
                    .timeout(60000)
                    .into(ivTourImage);
        } else {
            ivTourImage.setImageResource(R.drawable.nen);
        }

        tvNgay.setText("Ngày: " + formatDateRange(item.getNgayKhoiHanh(), item.getNgayKetThuc()));

        int adults = item.getSoNguoiLon() != null ? item.getSoNguoiLon() : 0;
        int kids = item.getSoTreEm() != null ? item.getSoTreEm() : 0;
        tvSoNguoi.setText("Số người: " + adults + " người lớn, " + kids + " trẻ em");

        tvTongTien.setText("Tổng tiền: " + formatMoney(item.getTongTien()));

        tvNgayDat.setText("Ngày đặt: " + compactDate(item.getNgayDat()));

        String ngayHuy = compactDate(item.getNgayHuy());
        if (!ngayHuy.isEmpty()) {
            tvNgayHuy.setVisibility(View.VISIBLE);
            tvNgayHuy.setText("Ngày hủy: " + ngayHuy);
        } else {
            tvNgayHuy.setVisibility(View.GONE);
        }

        String lyDo = safe(item.getLyDoHuy());
        if (!lyDo.isEmpty()) {
            tvLyDoHuy.setVisibility(View.VISIBLE);
            tvLyDoHuy.setText("Lý do hủy: " + lyDo);
        } else {
            tvLyDoHuy.setVisibility(View.GONE);
        }
    }

    private void bindBookingStatusChip(String status) {
        String s = status == null ? "" : status;
        if (s.equalsIgnoreCase("ChoXacNhan")) {
            chipTrangThaiDatTour.setText("Chờ xác nhận");
            chipTrangThaiDatTour.setBackgroundResource(R.drawable.bg_chip_pending);
        } else if (s.equalsIgnoreCase("DaXacNhan")) {
            chipTrangThaiDatTour.setText("Đã xác nhận");
            chipTrangThaiDatTour.setBackgroundResource(R.drawable.bg_chip_confirmed);
        } else if (s.equalsIgnoreCase("DaHuy")) {
            chipTrangThaiDatTour.setText("Đã hủy");
            chipTrangThaiDatTour.setBackgroundResource(R.drawable.bg_chip_cancelled);
        } else {
            chipTrangThaiDatTour.setText(s.isEmpty() ? "Không rõ" : s);
            chipTrangThaiDatTour.setBackgroundResource(R.drawable.bg_chip_pending);
        }
    }

    private void bindPaymentStatusChip(String status) {
        String s = status == null ? "" : status;
        if (s.equalsIgnoreCase("DaThanhToan")) {
            chipTrangThaiThanhToan.setText("Đã thanh toán");
            chipTrangThaiThanhToan.setBackgroundResource(R.drawable.bg_chip_paid);
        } else {
            chipTrangThaiThanhToan.setText(s.isEmpty() ? "Chưa thanh toán" : s);
            chipTrangThaiThanhToan.setBackgroundResource(R.drawable.bg_chip_paid);
        }
    }

    private String formatDateRange(String start, String end) {
        String s = compactDate(start);
        String e = compactDate(end);
        if (s.isEmpty() && e.isEmpty()) return "";
        if (e.isEmpty()) return s;
        if (s.isEmpty()) return e;
        return s + " - " + e;
    }

    private String compactDate(String iso) {
        if (iso == null) return "";
        int tIdx = iso.indexOf('T');
        if (tIdx > 0) return iso.substring(0, tIdx);
        int spaceIdx = iso.indexOf(' ');
        if (spaceIdx > 0) return iso.substring(0, spaceIdx);
        return iso;
    }

    private String formatMoney(Double amount) {
        if (amount == null) return "0 VND";
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        return nf.format(amount) + " VND";
    }

    private void confirmBooking(AdminBookingItem item) {
        if (item.getMaDatTour() == null) return;
        int id = item.getMaDatTour();

        new MaterialAlertDialogBuilder(this)
                .setTitle("Xác nhận đơn")
                .setMessage("Bạn chắc chắn muốn xác nhận đơn #" + id + " ?")
                .setNegativeButton("Đóng", (d, w) -> d.dismiss())
                .setPositiveButton("Xác nhận", (d, w) -> {
                    repo.confirmBooking(id).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                            if (!response.isSuccessful()) {
                                Toast.makeText(AdminBookingDetailActivity.this, "Xác nhận thất bại (HTTP " + response.code() + ")", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Toast.makeText(AdminBookingDetailActivity.this, "Đã xác nhận đơn", Toast.LENGTH_SHORT).show();
                            if (currentMaDatTour != null) loadDetail(currentMaDatTour);
                        }

                        @Override
                        public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                            Toast.makeText(AdminBookingDetailActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .show();
    }

    private void showCancelDialog(AdminBookingItem item) {
        if (item.getMaDatTour() == null) return;

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_admin_booking_cancel, null);
        TextInputLayout til = dialogView.findViewById(R.id.til_cancel_reason);
        TextInputEditText et = dialogView.findViewById(R.id.et_cancel_reason);

        AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                .setTitle("Hủy đơn")
                .setMessage("Vui lòng nhập lý do hủy.")
                .setView(dialogView)
                .setNegativeButton("Đóng", (d, w) -> d.dismiss())
                .setPositiveButton("Hủy đơn", null)
                .create();

        dialog.setOnShowListener(d -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String reason = et != null && et.getText() != null ? et.getText().toString().trim() : "";
            if (reason.isEmpty()) {
                til.setError("Vui lòng nhập lý do hủy");
                return;
            }
            til.setError(null);
            dialog.dismiss();
            callCancelBooking(item.getMaDatTour(), reason);
        }));

        dialog.show();
    }

    private void callCancelBooking(int maDatTour, String reason) {
        repo.cancelBooking(maDatTour, reason).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(AdminBookingDetailActivity.this, "Hủy thất bại (HTTP " + response.code() + ")", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(AdminBookingDetailActivity.this, "Đã hủy đơn", Toast.LENGTH_SHORT).show();
                if (currentMaDatTour != null) loadDetail(currentMaDatTour);
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(AdminBookingDetailActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }
}
