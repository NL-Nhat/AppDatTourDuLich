package com.example.apptravel.data.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptravel.R;

public class LichKhoiHanhViewHolder extends RecyclerView.ViewHolder{

    TextView txtDate, txtTrangThai, txtHDV, txtSlots, txtTime;

    public LichKhoiHanhViewHolder(@NonNull View itemView) {
        super(itemView);
        txtDate = itemView.findViewById(R.id.schedule_date);
        txtTrangThai = itemView.findViewById(R.id.schedule_status);
        txtHDV = itemView.findViewById(R.id.schedule_guide);
        txtSlots = itemView.findViewById(R.id.schedule_slots);
        txtTime = itemView.findViewById(R.id.schedule_time);
    }
}
