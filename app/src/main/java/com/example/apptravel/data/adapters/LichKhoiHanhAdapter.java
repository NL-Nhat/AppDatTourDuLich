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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LichKhoiHanhAdapter extends RecyclerView.Adapter<LichKhoiHanhViewHolder>{

    private Context context;
    private List<LichKhoiHanh> list;

    private final SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

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

        try{
            Date ngayDi = inputFormat.parse(item.getNgayKhoiHanh());
            Date ngayVe = inputFormat.parse(item.getNgayKetThuc());

            holder.txtDate.setText(
                    dateFormat.format(ngayDi) + " - " + dateFormat.format(ngayVe)
            );

            holder.txtTime.setText(timeFormat.format(ngayDi));

        }catch (Exception e){
            holder.txtDate.setText("—");
            holder.txtTime.setText("—");
        }

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
