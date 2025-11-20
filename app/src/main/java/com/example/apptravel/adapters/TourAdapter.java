package com.example.apptravel.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptravel.R;
import com.example.apptravel.models.Tour;

import java.util.List;

public class TourAdapter extends RecyclerView.Adapter<TourAdapter.TourViewHolder> {

    private Context context;
    private List<Tour> tourList;
    private OnTourClickListener onTourClickListener;

    public interface OnTourClickListener {
        void onTourClick(int position);
    }

    public TourAdapter(Context context, List<Tour> tourList, OnTourClickListener onTourClickListener) {
        this.context = context;
        this.tourList = tourList;
        this.onTourClickListener = onTourClickListener;
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

        holder.tourTitle.setText(tour.getTitle());
        holder.tourPrice.setText(tour.getPrice());
        holder.tourRating.setText(String.valueOf(tour.getRating()));
        holder.tourImage.setImageResource(tour.getImageResId());

        holder.itemView.setOnClickListener(v -> {
            if (onTourClickListener != null) {
                onTourClickListener.onTourClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tourList.size();
    }

    public static class TourViewHolder extends RecyclerView.ViewHolder {
        ImageView tourImage, favoriteIcon;
        TextView tourTitle, tourPrice, tourRating;

        public TourViewHolder(@NonNull View itemView) {
            super(itemView);
            tourImage = itemView.findViewById(R.id.tour_image);
            favoriteIcon = itemView.findViewById(R.id.favorite_icon);
            tourTitle = itemView.findViewById(R.id.tour_title);
            tourPrice = itemView.findViewById(R.id.tour_price);
            tourRating = itemView.findViewById(R.id.tour_rating);
        }
    }
}
