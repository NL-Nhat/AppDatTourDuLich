package com.example.apptravel.data.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.apptravel.R;
import com.example.apptravel.data.api.ApiClient;
import com.example.apptravel.data.api.ApiService;
import com.example.apptravel.data.models.Tour;
import com.example.apptravel.ui.activitys.admin.SuaTourActivity;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TourAdminAdapter extends RecyclerView.Adapter<TourAdminAdapter.ViewHolder> {
    private List<Tour> list;
    private Context context;

    public TourAdminAdapter(List<Tour> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tour_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tour tour = list.get(position);

        holder.tvTenTour.setText(tour.getTenTour());

        if (tour.getDiemDen() != null) {
            holder.tvLocation.setText(tour.getDiemDen().getTenDiemDen());
        } else {
            holder.tvLocation.setText("Chưa xác định");
        }

        holder.tvRating.setText("4.8");
        holder.tvBookings.setText("0 đơn đặt");

        if (tour.getGiaNguoiLon() != null) {
            NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
            holder.tvPrice.setText(formatter.format(tour.getGiaNguoiLon()) + " VND");
        }

        if ("DangMo".equals(tour.getTrangThai())) {
            holder.tvTrangThai.setText("Đang mở");
            holder.tvTrangThai.setBackgroundResource(R.drawable.trangthai_tour_mo);
        } else {
            holder.tvTrangThai.setText("Tạm dừng");
            holder.tvTrangThai.setBackgroundResource(R.drawable.trangthai_tour_dong);
        }

        // Tải hình ảnh
        String relativePath = tour.getUrlHinhAnhChinh();
        if (relativePath != null && !relativePath.isEmpty()) {
            String fullUrl = relativePath.startsWith("http")
                    ? relativePath
                    : ApiClient.getFullImageUrl(context, relativePath);

            Glide.with(context)
                    .load(fullUrl)
                    .placeholder(R.drawable.quang_ninh)
                    .error(R.drawable.quang_ninh)
                    .into(holder.imgTour);
        } else {
            holder.imgTour.setImageResource(R.drawable.quang_ninh);
        }

        // ================== THÊM SỰ KIỆN NÚT SỬA ==================
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, SuaTourActivity.class);
            // Gửi object Tour sang màn hình sửa
            // Lưu ý: Class Tour phải implements Serializable
            intent.putExtra("TOUR_OBJECT", tour);
            context.startActivity(intent);
        });
        // ==========================================================

        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xác nhận")
                    .setMessage("Bạn có chắc chắn muốn xóa tour này?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        deleteTourFromServer(tour.getMaTour(), position);
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    private void deleteTourFromServer(int maTour, int position) {
        ApiService apiService = ApiClient.getClient(context).create(ApiService.class);
        apiService.deleteTour(maTour).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    list.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, list.size());
                    Toast.makeText(context, "Xóa tour thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    String errorMsg = "Lỗi: " + response.code();
                    try {
                        if (response.errorBody() != null) {
                            String errorContent = response.errorBody().string();
                            errorMsg += " - " + errorContent;
                            Log.e("API_DELETE_ERROR", errorContent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgTour, btnEdit, btnDelete;
        TextView tvTenTour, tvTrangThai, tvRating, tvLocation, tvBookings, tvPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgTour = itemView.findViewById(R.id.tour_image);
            tvTenTour = itemView.findViewById(R.id.tour_name);
            tvTrangThai = itemView.findViewById(R.id.trangThaiTour);
            tvRating = itemView.findViewById(R.id.tour_rating);
            tvLocation = itemView.findViewById(R.id.tour_location);
            tvBookings = itemView.findViewById(R.id.tour_bookings);
            tvPrice = itemView.findViewById(R.id.tour_price);
            btnEdit = itemView.findViewById(R.id.btn_edit_tour);
            btnDelete = itemView.findViewById(R.id.btn_delete_tour);
        }
    }
}
