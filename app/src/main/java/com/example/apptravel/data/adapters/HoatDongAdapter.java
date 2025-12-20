package com.example.apptravel.data.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.apptravel.R;
import com.example.apptravel.data.models.HoatDong;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HoatDongAdapter extends RecyclerView.Adapter<HoatDongViewHolder> {

    private List<HoatDong> list;
    private Context context;
    private final SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    public HoatDongAdapter(Context context, List<HoatDong> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public HoatDongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hoadong_admin, parent, false);
        return new HoatDongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HoatDongViewHolder holder, int position) {
        HoatDong item = list.get(position);
        holder.txt_tenHoatDong.setText(item.getTieuDe());
        holder.txt_noiDung.setText(item.getMoTa());

        try {
            Date ngay = inputFormat.parse(item.getThoiGian());
            holder.txt_thoiGian.setText(dateFormat.format(ngay));

        } catch (Exception e) {
            holder.txt_thoiGian.setText("—");
        }

        // Phân loại màu sắc/icon theo loại hoạt động
        switch (item.getLoai()) {
            case "BOOKING":
                setActivityStyle(holder, R.drawable.ic_list, "#2196F3"); // Blue
                break;
            case "REVIEW":
                setActivityStyle(holder, R.drawable.ic_star, "#FF9800"); // Orange
                break;
            case "PAYMENT":
                setActivityStyle(holder, R.drawable.ic_credit_card, "#4CAF50"); // Green
                break;
        }
    }

    private void setActivityStyle(HoatDongViewHolder holder, int iconRes, String colorHex) {
        holder.img_icon.setImageResource(iconRes);
        holder.img_icon.setImageTintList(ColorStateList.valueOf(Color.parseColor(colorHex)));
    }

    @Override
    public int getItemCount() { return list == null ? 0 : list.size(); }
}
