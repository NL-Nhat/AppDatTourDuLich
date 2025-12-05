package com.example.apptravel.data.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptravel.R;
import com.example.apptravel.data.models.LichKhoiHanh;

import java.util.List;

public class LichKhoiHanhAdapter extends RecyclerView.Adapter<LichKhoiHanhViewHolder>{

    private Context context;
    private List<LichKhoiHanh> list;

    public LichKhoiHanhAdapter(Context context, List<LichKhoiHanh> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public LichKhoiHanhViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lichkhoihanh, parent, false);
        return new LichKhoiHanhViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LichKhoiHanhViewHolder holder, int position) {
        LichKhoiHanh item = list.get(position);

        //Lấy ngày đi và ngày về
        String ngayDi = item.getNgayKhoiHanh();
        String ngayVe = item.getNgayKetThuc();

        // lấy 10 ký tự đầu (2025-12-01)
        if(ngayDi != null && ngayDi.length() >= 10) ngayDi = ngayDi.substring(0, 10);
        if(ngayVe != null && ngayVe.length() >= 10) ngayVe = ngayVe.substring(0, 10);

        holder.txtDate.setText(ngayDi + " - " + ngayVe);

        // Kiểm tra null cho Hướng dẫn viên để tránh Crash
        if (item.getHuongDanVien() != null) {
            holder.txtHDV.setText("HDV: " + item.getHuongDanVien().getHoTen());
        } else {
            holder.txtHDV.setText("HDV: Đang cập nhật");
        }

        holder.txtSlots.setText(item.getSoLuongKhachDaDat() + "/" + item.getSoLuongKhachToiDa() + " chỗ");

        if (item.getSoLuongKhachDaDat() >= item.getSoLuongKhachToiDa()) {
            holder.txtTrangThai.setText("Hết chỗ");
            holder.txtTrangThai.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        } else {
            holder.txtTrangThai.setText("Còn chỗ");
            holder.txtTrangThai.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        }
    }

    @Override
    public int getItemCount() { return list == null ? 0 : list.size(); }

}
