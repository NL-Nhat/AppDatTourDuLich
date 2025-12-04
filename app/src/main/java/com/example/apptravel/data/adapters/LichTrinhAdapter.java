package com.example.apptravel.data.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptravel.R;
import com.example.apptravel.data.models.BookedTour;

import java.util.List;

public class LichTrinhAdapter extends RecyclerView.Adapter<LichTrinhAdapter.LichTrinhViewHolder> {

    private Context context;
    private List<BookedTour> bookedTourList;

    public LichTrinhAdapter(Context context, List<BookedTour> bookedTourList) {
        this.context = context;
        this.bookedTourList = bookedTourList;
    }

    @NonNull
    @Override
    public LichTrinhViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lich_trinh, parent, false);
        return new LichTrinhViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LichTrinhViewHolder holder, int position) {
        BookedTour bookedTour = bookedTourList.get(position);

        holder.imageView.setImageResource(bookedTour.getImageResId());
        holder.title.setText(bookedTour.getTitle());
        holder.date.setText(bookedTour.getDate());
        holder.location.setText(bookedTour.getLocation());
    }

    @Override
    public int getItemCount() {
        return bookedTourList.size();
    }

    public static class LichTrinhViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView title, date, location;

        public LichTrinhViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_lich_trinh_image);
            title = itemView.findViewById(R.id.tv_lich_trinh_title);
            date = itemView.findViewById(R.id.tv_lich_trinh_date);
            location = itemView.findViewById(R.id.tv_lich_trinh_location);
        }
    }
}
