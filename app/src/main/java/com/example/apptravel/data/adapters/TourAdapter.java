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
import com.example.apptravel.data.models.Tour;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TourAdapter extends RecyclerView.Adapter<TourViewHolder> {

    private Context context;
    private List<Tour> tourList;
    private OnTourClickListener onTourClickListener;

    public interface OnTourClickListener {
        void onTourClick(int position);
    }

    public void setOnTourClickListener(OnTourClickListener listener) {
        this.onTourClickListener = listener;
    }

    public TourAdapter(Context context) {
        this.context = context;
        this.tourList = new ArrayList<>();
    }

    public void setTourList(List<Tour> tourList) {
        this.tourList = tourList;
        notifyDataSetChanged(); // Cập nhật lại giao diện khi có dữ liệu mới
    }

    @NonNull
    @Override
    public TourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tour, parent, false);
        return new TourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TourViewHolder holder, int position) {
        Tour tour = tourList.get(position);
        if (tour == null) return;

        // 1. Gán Tên Tour
        holder.tourTitle.setText(tour.getTenTour());

        // 2. Gán Giá (Format về dạng tiền tệ dễ đọc, ví dụ: 1,000,000 đ)
        // Lưu ý: Code model của bạn là Double, cần check null để tránh lỗi
        double price = tour.getGiaNguoiLon() != null ? tour.getGiaNguoiLon() : 0;
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        holder.tourPrice.setText(currencyFormat.format(price));

        // 3. Gán Rating
        double rating = tour.getDiemDanhGiaTrungBinh() != null ? tour.getDiemDanhGiaTrungBinh() : 0.0;
        holder.tourRating.setText(String.valueOf(rating));

        String duongDanAnh = "tour/" + tour.getUrlHinhAnhChinh();

        //Tạo URL đầy đủ
        String fullUrl = ApiClient.getFullImageUrl(context, duongDanAnh);

        // Load ảnh vào ImageView (biến anhDaiDien)
        Glide.with(context)
                .load(fullUrl)
                .placeholder(R.drawable.nen)
                .error(R.drawable.ic_launcher_background)
                .into(holder.tourImage); // Load vào UI

        holder.itemView.setOnClickListener(v -> {
            if (onTourClickListener != null) {
                onTourClickListener.onTourClick(holder.getBindingAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return tourList != null ? tourList.size() : 0;
    }

}
