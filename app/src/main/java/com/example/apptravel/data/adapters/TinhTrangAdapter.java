package com.example.apptravel.data.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptravel.R;
import com.example.apptravel.data.models.LichTrinhYeuCau;

import java.util.List;

/**
 * Adapter này hiển thị danh sách các tour trong các tab của TinhTrangXacNhanFragment.
 * Nó sử dụng layout 'item_lich_trinh_yeu_cau.xml' để hiển thị thông tin chi tiết
 * và ẩn đi CheckBox không cần thiết.
 */
public class TinhTrangAdapter extends RecyclerView.Adapter<TinhTrangAdapter.ViewHolder> {

    private final Context context;
    private final List<LichTrinhYeuCau> tourList;

    public TinhTrangAdapter(Context context, List<LichTrinhYeuCau> tourList) {
        this.context = context;
        this.tourList = tourList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Sử dụng layout item_lich_trinh_yeu_cau để có đầy đủ thông tin
        View view = LayoutInflater.from(context).inflate(R.layout.item_lich_trinh_yeu_cau, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LichTrinhYeuCau currentTour = tourList.get(position);

        holder.ivImage.setImageResource(currentTour.getImageResId());
        holder.tvTitle.setText(currentTour.getTitle());
        holder.tvLocation.setText(currentTour.getLocation());

        // Giả sử LichTrinhYeuCau có một trường cho ngày tháng.
        // Nếu không có, bạn có thể bỏ dòng này hoặc cập nhật model.
        // holder.tvDate.setText(currentTour.getDate()); // Ví dụ
    }

    @Override
    public int getItemCount() {
        return tourList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView ivImage;
        final TextView tvTitle;
        final TextView tvDate;
        final TextView tvLocation;
        final CheckBox cbChonTour;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ các view từ layout item_lich_trinh_yeu_cau.xml
            ivImage = itemView.findViewById(R.id.iv_lich_trinh_image);
            tvTitle = itemView.findViewById(R.id.tv_lich_trinh_title);
            tvDate = itemView.findViewById(R.id.tv_lich_trinh_date);
            tvLocation = itemView.findViewById(R.id.tv_lich_trinh_location);
            cbChonTour = itemView.findViewById(R.id.cb_chon_tour);

            if (cbChonTour != null) {
                cbChonTour.setVisibility(View.GONE);
            }
        }
    }
}
