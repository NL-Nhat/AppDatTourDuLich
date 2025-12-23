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

import com.bumptech.glide.Glide;
import com.example.apptravel.R;
import com.example.apptravel.data.api.ApiClient;
import com.example.apptravel.data.models.DatTourHistoryItem;
import com.google.android.material.button.MaterialButton;

import java.util.List;

/**
 * Adapter hiển thị lịch sử đặt tour theo trạng thái.
 * Tái sử dụng layout item_lich_trinh_yeu_cau.xml và bổ sung nút Hủy (chỉ hiện khi được phép).
 */
public class TinhTrangAdapter extends RecyclerView.Adapter<TinhTrangAdapter.ViewHolder> {

    public interface OnCancelClickListener {
        void onCancelClick(DatTourHistoryItem item);
    }

    private final Context context;
    private final List<DatTourHistoryItem> bookingList;
    private final boolean showCancelButton;
    private final OnCancelClickListener cancelClickListener;

    public TinhTrangAdapter(Context context,
                            List<DatTourHistoryItem> bookingList,
                            boolean showCancelButton,
                            OnCancelClickListener cancelClickListener) {
        this.context = context;
        this.bookingList = bookingList;
        this.showCancelButton = showCancelButton;
        this.cancelClickListener = cancelClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lich_trinh_yeu_cau, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DatTourHistoryItem item = bookingList.get(position);

        String title = item.getTenTour() != null ? item.getTenTour() : "";
        String location = item.getDiaDiem() != null ? item.getDiaDiem() : "";
        String date = formatDateRange(item.getNgayKhoiHanh(), item.getNgayKetThuc());

        holder.tvTitle.setText(title);
        holder.tvLocation.setText(location);
        holder.tvDate.setText(date);

        String imageUrl = item.getUrlHinhAnhChinh();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            String url = imageUrl.trim();

            // Backend thường trả về tên file (vd: tour_bana.jpg).
            // Static resources đang serve từ thư mục uploads/, trong đó ảnh tour nằm ở uploads/tour/...
            // Các màn khác cũng prefix "tour/" + filename (xem TourAdapter).
            if (!url.startsWith("http")) {
                // bỏ dấu '/' đầu nếu có để tránh baseUrl//path
                while (url.startsWith("/")) url = url.substring(1);

                // nếu chỉ là filename -> prefix tour/
                if (!url.contains("/")) {
                    url = "tour/" + url;
                }

                url = ApiClient.getFullImageUrl( url);
            }

            Glide.with(context)
                    .load(url)
                    .placeholder(R.drawable.nen)
                    .error(R.drawable.nen)
                    .timeout(60000)
                    .into(holder.ivImage);
        } else {
            holder.ivImage.setImageResource(R.drawable.nen);
        }

        // Nút Hủy: chỉ show ở tab Chờ xác nhận
        if (holder.btnCancel != null) {
            holder.btnCancel.setVisibility(showCancelButton ? View.VISIBLE : View.GONE);
            holder.btnCancel.setOnClickListener(v -> {
                if (cancelClickListener != null) cancelClickListener.onCancelClick(item);
            });
        }
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    private String formatDateRange(String start, String end) {
        String s = compactDate(start);
        String e = compactDate(end);
        if (s.isEmpty() && e.isEmpty()) return "";
        if (e.isEmpty()) return s;
        if (s.isEmpty()) return e;
        return s + " - " + e;
    }

    private String compactDate(String iso) {
        if (iso == null) return "";
        int tIdx = iso.indexOf('T');
        if (tIdx > 0) return iso.substring(0, tIdx);
        int spaceIdx = iso.indexOf(' ');
        if (spaceIdx > 0) return iso.substring(0, spaceIdx);
        return iso;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView ivImage;
        final TextView tvTitle;
        final TextView tvDate;
        final TextView tvLocation;
        final CheckBox cbChonTour;
        final MaterialButton btnCancel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_lich_trinh_image);
            tvTitle = itemView.findViewById(R.id.tv_lich_trinh_title);
            tvDate = itemView.findViewById(R.id.tv_lich_trinh_date);
            tvLocation = itemView.findViewById(R.id.tv_lich_trinh_location);
            cbChonTour = itemView.findViewById(R.id.cb_chon_tour);
            btnCancel = itemView.findViewById(R.id.btn_cancel_booking);

            if (cbChonTour != null) {
                cbChonTour.setVisibility(View.GONE);
            }
        }
    }
}

