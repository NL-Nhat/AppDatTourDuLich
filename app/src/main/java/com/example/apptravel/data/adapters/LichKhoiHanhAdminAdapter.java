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

    private List<TourRequest.LichKhoiHanhDTO> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEdit(int position, TourRequest.LichKhoiHanhDTO item);
        void onDelete(int position);
    }

    public LichKhoiHanhAdminAdapter(List<TourRequest.LichKhoiHanhDTO> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lichkhoihanh_admin, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TourRequest.LichKhoiHanhDTO item = list.get(position);
        // Ngay khoi hanh - Ngay ket thuc
        holder.tvNgay.setText(item.getNgayKhoiHanh().split(" ")[0] + " - " + item.getNgayKetThuc().split(" ")[0]);
        holder.tvGio.setText(item.getNgayKhoiHanh().split(" ")[1]);
        holder.tvSoLuong.setText("Tối đa: " + item.getSoLuongKhachToiDa());
        holder.tvHDV.setText("HDV: " + (item.getTenHDV() != null ? item.getTenHDV() : "Chưa chọn"));

        holder.btnEdit.setOnClickListener(v -> listener.onEdit(position, item));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(position));
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNgay, tvGio, tvSoLuong, tvHDV;
        ImageView btnEdit, btnDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNgay = itemView.findViewById(R.id.tv_ngay_khoi_hanh);
            tvGio = itemView.findViewById(R.id.tv_gio_khoi_hanh);
            tvSoLuong = itemView.findViewById(R.id.tv_so_luong);
            tvHDV = itemView.findViewById(R.id.tv_hdv);
            btnEdit = itemView.findViewById(R.id.btn_edit_lich);
            btnDelete = itemView.findViewById(R.id.btn_delete_lich);
        }
    }
}
