package com.example.apptravel.data.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptravel.R;
import com.example.apptravel.data.models.TourRequest;

import java.util.List;

public class LichKhoiHanhAdminAdapter extends RecyclerView.Adapter<LichKhoiHanhAdminAdapter.ViewHolder> {

    private final Context context;
    private final List<TourRequest.LichKhoiHanhDTO> list;
    private final OnItemClickListener listener;
    private boolean isEditMode = false;

    public interface OnItemClickListener {
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    public LichKhoiHanhAdminAdapter(Context context, List<TourRequest.LichKhoiHanhDTO> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    public void setEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_schedule_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TourRequest.LichKhoiHanhDTO lich = list.get(position);

        if (lich.getNgayKhoiHanh() != null && lich.getNgayKhoiHanh().length() >= 10) {
            holder.tvNgayDi.setText(lich.getNgayKhoiHanh().substring(0, 10));
        }
        if (lich.getNgayKetThuc() != null && lich.getNgayKetThuc().length() >= 10) {
            holder.tvNgayVe.setText(lich.getNgayKetThuc().substring(0, 10));
        }
        
        holder.tvSoLuong.setText("Tối đa: " + lich.getSoLuongKhachToiDa());

        // SỬA LỖI: HIển thị tên HDV
        if (lich.getTenHDV() != null && !lich.getTenHDV().isEmpty()) {
            holder.tvHDV.setText(lich.getTenHDV());
        } else if (lich.getMaHDV() != null) {
            holder.tvHDV.setText("HDV (ID: " + lich.getMaHDV() + ")"); // Dự phòng nếu chỉ có mã
        } else {
            holder.tvHDV.setText("Chưa chọn HDV");
        }

        if (isEditMode) {
            holder.btnEdit.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.VISIBLE);
        } else {
            holder.btnEdit.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.GONE);
        }

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEditClick(position);
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDeleteClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNgayDi, tvNgayVe, tvSoLuong, tvHDV;
        ImageView btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNgayDi = itemView.findViewById(R.id.tv_ngay_di);
            tvNgayVe = itemView.findViewById(R.id.tv_ngay_ve);
            tvSoLuong = itemView.findViewById(R.id.tv_so_luong_khach);
            tvHDV = itemView.findViewById(R.id.tv_huong_dan_vien);
            btnEdit = itemView.findViewById(R.id.btn_edit_schedule);
            btnDelete = itemView.findViewById(R.id.btn_delete_schedule);
        }
    }
}
