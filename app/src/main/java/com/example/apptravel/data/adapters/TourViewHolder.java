package com.example.apptravel.data.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptravel.R;

public class TourViewHolder extends RecyclerView.ViewHolder {
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
