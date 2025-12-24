package com.example.apptravel.data.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptravel.R;
import com.example.apptravel.data.api.ApiClient;
import com.example.apptravel.data.api.ApiService;
import com.example.apptravel.data.models.NguoiDung;
import com.example.apptravel.ui.activitys.admin.ThemTaiKhoanActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuanLyNguoiDungAdapter extends RecyclerView.Adapter<QuanLyNguoiDungAdapter.UserViewHolder> {
    private List<NguoiDung> userList;
    private Context context;
    private ApiService apiService;

    public QuanLyNguoiDungAdapter(List<NguoiDung> userList, Context context) {
        this.userList = userList;
        this.context = context;
        this.apiService = ApiClient.getClient(context).create(ApiService.class);
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_management, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        NguoiDung user = userList.get(position);

        holder.txtId.setText(user.getMaNguoiDung().toString());
        holder.txtName.setText(user.getHoTen());

        holder.itemView.setOnClickListener(v -> {
            StringBuilder sb = new StringBuilder();
            sb.append("🆔 ID: ").append(user.getMaNguoiDung()).append("\n\n");
            sb.append("👤 Họ tên: ").append(user.getHoTen()).append("\n\n");
            sb.append("📧 Email: ").append(user.getEmail()).append("\n\n");
            sb.append("📞 Số điện thoại: ").append(user.getSoDienThoai()).append("\n\n");
            sb.append("🏠 Địa chỉ: ").append(user.getDiaChi() != null ? user.getDiaChi() : "Chưa cập nhật").append("\n\n");
            sb.append("📅 Ngày sinh: ").append(user.getNgaySinh()).append("\n\n");
            sb.append("👫 Giới tính: ").append(user.getGioiTinh()).append("\n\n");
            sb.append("🛡️ Vai trò: ").append(user.getVaiTro());

            new AlertDialog.Builder(context)
                    .setTitle("Chi Tiết Tài Khoản")
                    .setMessage(sb.toString())
                    .setCancelable(true)
                    .setPositiveButton("Đóng", (dialog, which) -> dialog.dismiss())
                    .create().show();
        });

        holder.btnSua.setOnClickListener(v -> {
            Intent intent = new Intent(context, ThemTaiKhoanActivity.class);
            // Truyền đối tượng người dùng sang màn hình sửa
            intent.putExtra("NGUOI_DUNG_EDIT", user);
            context.startActivity(intent);
        });

        boolean isLocked = "Khoa".equalsIgnoreCase(user.getTrangThai());

        holder.btnKhoa.setText(isLocked ? "Mở Khóa" : "Khóa");
        holder.btnKhoa.setBackgroundTintList(ColorStateList.valueOf(isLocked ? Color.GRAY : Color.RED));

        holder.btnKhoa.setOnClickListener(v -> {
            int currentPos = holder.getAdapterPosition();
            if (currentPos == RecyclerView.NO_POSITION) return;

            String newStatus = isLocked ? "HoatDong" : "Khoa";
            String actionText = isLocked ? "Mở khóa" : "Khoa";

            new AlertDialog.Builder(context)
                    .setTitle("Xác nhận")
                    .setMessage("Bạn có chắc chắn muốn " + actionText + " tài khoản này?")
                    .setPositiveButton("Đồng ý", (dialog, which) -> {
                        NguoiDung updateReq = new NguoiDung();
                        updateReq.setTrangThai(newStatus);

                        apiService.updateNguoiDung(String.valueOf(user.getMaNguoiDung()), updateReq)
                                .enqueue(new Callback<NguoiDung>() {
                                    @Override
                                    public void onResponse(Call<NguoiDung> call, Response<NguoiDung> response) {
                                        if (response.isSuccessful()) {
                                            user.setTrangThai(newStatus);
                                            notifyItemChanged(currentPos);
                                            Toast.makeText(context, actionText + " thành công!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(context, "Lỗi server: " + response.code(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<NguoiDung> call, Throwable t) {
                                        Toast.makeText(context, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return userList != null ? userList.size() : 0;
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView txtId, txtName;
        Button btnSua, btnKhoa;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            txtId = itemView.findViewById(R.id.txt_user_id);
            txtName = itemView.findViewById(R.id.txt_user_name);
            btnSua = itemView.findViewById(R.id.btn_sua);
            btnKhoa = itemView.findViewById(R.id.btn_xoa);
        }
    }

    public void updateList(List<NguoiDung> newList) {
        this.userList = newList;
        notifyDataSetChanged();
    }
}