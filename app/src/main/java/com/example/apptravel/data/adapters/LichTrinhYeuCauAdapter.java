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
import com.example.apptravel.data.models.DateHeader;
import com.example.apptravel.data.models.DisplayableItem;
import com.example.apptravel.data.models.LichTrinhYeuCau;

import java.util.List;

public class LichTrinhYeuCauAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private final Context context;
    private final List<DisplayableItem> displayableItemList;

    public LichTrinhYeuCauAdapter(Context context, List<DisplayableItem> displayableItemList) {
        this.context = context;
        this.displayableItemList = displayableItemList;
    }

    @Override
    public int getItemViewType(int position) {
        if (displayableItemList.get(position) instanceof DateHeader) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_header_ngay, parent, false);
            return new HeaderViewHolder(view);
        } else { // TYPE_ITEM
            View view = LayoutInflater.from(context).inflate(R.layout.item_lich_trinh_yeu_cau, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_HEADER) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            DateHeader dateHeader = (DateHeader) displayableItemList.get(position);
            headerViewHolder.tvDateHeader.setText(dateHeader.getDate());
        } else { // TYPE_ITEM
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            LichTrinhYeuCau currentItem = (LichTrinhYeuCau) displayableItemList.get(position);

            itemViewHolder.ivImage.setImageResource(currentItem.getImageResId());
            itemViewHolder.tvTitle.setText(currentItem.getTitle());
            itemViewHolder.tvDate.setText(currentItem.getDate());
            itemViewHolder.tvLocation.setText(currentItem.getLocation());
            itemViewHolder.cbChonTour.setChecked(currentItem.isSelected());
        }
    }

    @Override
    public int getItemCount() {
        return displayableItemList.size();
    }

    // ViewHolder for Header
    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        final TextView tvDateHeader;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDateHeader = itemView.findViewById(R.id.tv_header_ngay);
        }
    }

    // ViewHolder for Item
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        final ImageView ivImage;
        final TextView tvTitle;
        final TextView tvDate;
        final TextView tvLocation;
        final CheckBox cbChonTour;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_lich_trinh_image);
            tvTitle = itemView.findViewById(R.id.tv_lich_trinh_title);
            tvDate = itemView.findViewById(R.id.tv_lich_trinh_date);
            tvLocation = itemView.findViewById(R.id.tv_lich_trinh_location);
            cbChonTour = itemView.findViewById(R.id.cb_chon_tour);
        }
    }
}