package com.example.apptravel.data.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptravel.R;
import com.example.apptravel.data.models.Tour;

import java.util.List;

public class TourAdapter extends RecyclerView.Adapter<TourViewHolder> {

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

}
