package com.example.apptravel.data.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.apptravel.R;
import com.example.apptravel.data.api.ApiClient;
import com.example.apptravel.data.models.DanhGia;

import java.util.List;

public class DanhGiaAdapter extends RecyclerView.Adapter<DanhGiaViewHolder>{

    private Context context;
    private List<DanhGia> list;

    public DanhGiaAdapter(Context context, List<DanhGia> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public DanhGiaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_danhgia_chitiettour, parent, false);
        return new DanhGiaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DanhGiaViewHolder holder, int position) {
        DanhGia item = list.get(position);

        if (item.getNguoiDung() != null) {
            holder.txtName.setText(item.getNguoiDung().getHoTen());

            String duongDanAnh = "avatar/" + item.getNguoiDung().getAnhDaiDien();
            //Tạo URL đầy đủ
            String fullUrl = ApiClient.getFullImageUrl(duongDanAnh);
            // Load ảnh vào ImageView (biến anhDaiDien)
            Glide.with(context)
                    .load(fullUrl)
                    .placeholder(R.drawable.nen)
                    .error(R.drawable.ic_launcher_background)
                    .timeout(60000)
                    .into(holder.imgAvatar); // Load vào UI
        }

        // Xử lý ngày đánh giá
        String ngayDG = item.getThoiGianTao();
        if(ngayDG != null && ngayDG.length() >= 10) ngayDG = ngayDG.substring(0, 10);

        holder.txtDate.setText(ngayDG);
        holder.txtContent.setText(item.getBinhLuan());

    }

    @Override
    public int getItemCount() { return list == null ? 0 : list.size(); }

}
