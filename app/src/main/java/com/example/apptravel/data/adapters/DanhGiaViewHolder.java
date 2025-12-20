package com.example.apptravel.data.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptravel.R;

public class DanhGiaViewHolder extends RecyclerView.ViewHolder {

    ImageView imgAvatar;
    TextView txtName, txtDate, txtContent;

    public DanhGiaViewHolder(@NonNull View itemView) {
        super(itemView);
        imgAvatar = itemView.findViewById(R.id.user_avatar);
        txtName = itemView.findViewById(R.id.txt_user_name);
        txtDate = itemView.findViewById(R.id.txt_ngayDanhGia);
        txtContent = itemView.findViewById(R.id.txt_noiDung);
    }
}
