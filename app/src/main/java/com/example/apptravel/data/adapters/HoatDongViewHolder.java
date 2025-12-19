package com.example.apptravel.data.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptravel.R;

public class HoatDongViewHolder extends RecyclerView.ViewHolder{

    TextView txt_tenHoatDong, txt_noiDung, txt_thoiGian;
    ImageView img_icon;

    public HoatDongViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_noiDung = itemView.findViewById(R.id.txt_noiDung);
        txt_thoiGian = itemView.findViewById(R.id.txt_thoiGian);
        txt_tenHoatDong = itemView.findViewById(R.id.txt_tenHoatDong);
        img_icon = itemView.findViewById(R.id.img_icon);
    }
}
