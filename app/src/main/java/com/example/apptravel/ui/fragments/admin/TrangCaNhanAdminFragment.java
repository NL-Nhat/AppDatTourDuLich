package com.example.apptravel.ui.fragments.admin;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.apptravel.data.api.ApiClient;
import com.example.apptravel.data.api.ApiService;
import com.example.apptravel.data.models.ViDienTuResponse;
import com.example.apptravel.ui.DangNhapActivity;
import com.example.apptravel.R;
import com.example.apptravel.ui.activitys.user.DoiMatKhauActivity;
import com.example.apptravel.ui.activitys.user.ThongTinCaNhanActivity;
import com.example.apptravel.util.QuanLyDangNhap;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrangCaNhanAdminFragment extends Fragment {

    private LinearLayout menu_editInfo, menu_doiMk, menu_logout, menu_qr;
    private QuanLyDangNhap quanLyDangNhap;
    private ImageView anhDaiDien;
    private TextView txtTen, txtEmail;
    private ApiService apiService;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trang_ca_nhan_admin, container, false);

        quanLyDangNhap = new QuanLyDangNhap(getContext());
        apiService = ApiClient.getClient(getContext()).create(ApiService.class);

        anhXa(view);
        GanThongTin();
        sukienClick();

        return view;
    }

    private void anhXa(View view) {
        menu_editInfo = view.findViewById(R.id.menu_edit_info);
        menu_doiMk = view.findViewById(R.id.menu_doiMK);
        menu_logout = view.findViewById(R.id.menu_logout);
        menu_qr = view.findViewById(R.id.menu_qr);
        txtEmail = view.findViewById(R.id.admin_email);
        txtTen = view.findViewById(R.id.admin_name);
        anhDaiDien = view.findViewById(R.id.admin_avatar);
    }
    private void GanThongTin() {

        txtTen.setText(quanLyDangNhap.LayHoTen());
        txtEmail.setText(quanLyDangNhap.LayEmail());

        String tenFileAnh = quanLyDangNhap.LayAnhDaiDien();

        if (tenFileAnh != null && !tenFileAnh.isEmpty()) {
            String duongDanAnh = "avatar/" + tenFileAnh;
            String fullUrl = ApiClient.getFullImageUrl(getContext(), duongDanAnh);

            Glide.with(getContext())
                    .load(fullUrl)
                    .placeholder(R.drawable.nen)
                    .error(R.drawable.ic_launcher_background)
                    .timeout(60000)
                    .into(anhDaiDien); // Load vào UI
        }
    }

    private void sukienClick() {

        menu_editInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ThongTinCaNhanActivity.class);
                startActivity(intent);
            }
        });

        menu_doiMk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DoiMatKhauActivity.class);
                startActivity(intent);
            }
        });

        menu_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quanLyDangNhap.DangXuat();
                Intent intent = new Intent(getActivity(), DangNhapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });

        menu_qr.setOnClickListener(v -> quetQR());
    }

    private void quetQR() {
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Quét mã QR trên vé của khách hàng");
        integrator.setCameraId(0);  // Sử dụng camera sau
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getContext(), "Đã hủy quét", Toast.LENGTH_LONG).show();
            } else {
                // ĐÂY LÀ NƠI NHẬN ĐƯỢC MÃ
                String ma = result.getContents();
                kiemTra(ma);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void kiemTra(String maDatTour) {

        try {
            int ma = Integer.parseInt(maDatTour);

            apiService.getLayThongTinVi(ma).enqueue(new Callback<ViDienTuResponse>() {
                @Override
                public void onResponse(Call<ViDienTuResponse> call, Response<ViDienTuResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ViDienTuResponse data = response.body();

                        // kiểm tra trạng thái thanh toán
                        if ("DaThanhToan".equals(data.getTrangThaiThanhToan())) {
                            hienThiDialogThanhCong(data);
                        } else {
                            Toast.makeText(getContext(), "Khách hàng này chưa thanh toán!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Mã vé không hợp lệ!", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<ViDienTuResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            // Nếu người dùng quét mã QR không phải là số
            Toast.makeText(getContext(), "Mã QR không đúng định dạng vé tour!", Toast.LENGTH_LONG).show();
        }
    }

    private void hienThiDialogThanhCong(ViDienTuResponse data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("✅ VÉ HỢP LỆ");
        builder.setMessage("Khách hàng: " + data.getTenKhachHang() +
                "\nTour: " + data.getTentour() +
                "\nSố người: " + data.getSoNguoiLon() + " Lớn, " + data.getSoTreEm() + " Trẻ em" +
                "\n\n👉 TRẠNG THÁI: ĐÃ THANH TOÁN");

        builder.setPositiveButton("Quét tiếp", (dialog, which) -> {
            dialog.dismiss();
            quetQR();
        });

        builder.setNegativeButton("Đóng", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    // Cập nhật lại giao diện mỗi khi Fragment được hiển thị lại
    @Override
    public void onResume() {
        super.onResume();
        GanThongTin();
    }
}