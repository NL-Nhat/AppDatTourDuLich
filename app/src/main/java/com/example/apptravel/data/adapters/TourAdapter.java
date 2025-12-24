package com.example.apptravel.data.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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

    private final Context context;
    private List<Tour> tourList;
    private OnTourClickListener onTourClickListener;

    // Khai báo các hằng số ViewType
    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_HOME = 2;
    private int currentType = TYPE_NORMAL; // Mặc định
    final boolean[] isFavorite = {false};

    public interface OnTourClickListener {
        void onTourClick(Tour tour);
    }

    public void setOnTourClickListener(OnTourClickListener listener) {
        this.onTourClickListener = listener;
    }

    //  nhận vào loại layout muốn dùng, để sử dụng 2 item khác nhau cùng thuộc tính
    public TourAdapter(Context context, int viewType) {
        this.context = context;
        this.tourList = new ArrayList<>();
        this.currentType = viewType;
    }

    //Ghi đè phương thức này để báo cho RecyclerView biết dùng type nào
    @Override
    public int getItemViewType(int position) {
        return currentType;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTourList(List<Tour> tourList) {
        this.tourList = tourList;
        notifyDataSetChanged(); // Cập nhật lại giao diện khi có dữ liệu mới
    }

    @NonNull
    @Override
    public TourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = (viewType == TYPE_HOME) ? R.layout.item_tour_trangchu : R.layout.item_tour;
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new TourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TourViewHolder holder, int position) {
        // Dùng phép chia lấy dư để lấy đúng dữ liệu từ list thực tế để làm cuộn tròn
        int actualPosition = position % tourList.size();
        Tour tour = tourList.get(actualPosition);

        if (tour == null) return;

        holder.tourTitle.setText(tour.getTenTour());
        double price = tour.getGiaNguoiLon() != null ? tour.getGiaNguoiLon().doubleValue() : 0.0;
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        holder.tourPrice.setText(currencyFormat.format(price));

        double rating = tour.getDiemDanhGiaTrungBinh() != null ? tour.getDiemDanhGiaTrungBinh() : 0.0;

        // Kiểm tra ViewType của Item hiện tại
        if (getItemViewType(position) == TYPE_HOME) {
            int reviewCount = tour.getSoLuongDanhGia() != null ? tour.getSoLuongDanhGia() : 0;
            // Định dạng: "4.5 | 162 đánh giá"
            String ratingText = String.format(Locale.getDefault(), "%.1f | %d đánh giá", rating, reviewCount);
            holder.tourRating.setText(ratingText);
        } else {
            holder.tourRating.setText(String.valueOf(rating));
        }

        String fullUrl = ApiClient.getFullImageUrl(tour.getUrlHinhAnhChinh());
        // Load ảnh vào ImageView
        Glide.with(context)
                .load(fullUrl)
                .placeholder(R.drawable.nen)
                .error(R.drawable.ic_launcher_background)
                .timeout(60000)
                .into(holder.tourImage);

        holder.itemView.setOnClickListener(v -> {
            if (onTourClickListener != null) {
                onTourClickListener.onTourClick(tour);
            }
        });

        holder.favoriteIcon.setOnClickListener(v -> {
            isFavorite[0] = !isFavorite[0];

            if (isFavorite[0]) {
                holder.favoriteIcon.setColorFilter(
                        ContextCompat.getColor(context, R.color.red),
                        PorterDuff.Mode.SRC_IN
                );
            } else {
                holder.favoriteIcon.setColorFilter(
                        ContextCompat.getColor(context, R.color.white),
                        PorterDuff.Mode.SRC_IN
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        if(tourList == null || tourList.isEmpty()) return 0;

        if(currentType == TYPE_HOME) {
            return Integer.MAX_VALUE;
        }
        return tourList.size();
    }

}
