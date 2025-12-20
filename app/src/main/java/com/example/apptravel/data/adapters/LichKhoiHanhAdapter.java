package com.example.apptravel.data.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptravel.R;
import com.example.apptravel.data.models.LichKhoiHanh;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LichKhoiHanhAdapter extends RecyclerView.Adapter<LichKhoiHanhViewHolder>{

    private Context context;
    private List<LichKhoiHanh> list;
    // Biến lưu vị trí item được chọn
    private int selectedPosition = -1;
    private final SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    public LichKhoiHanhAdapter(Context context, List<LichKhoiHanh> list) {
        this.context = context;
        this.list = list;
    }

    // Phương thức để lấy lịch khởi hành đã chọn
    public LichKhoiHanh getSelectedSchedule() {
        if (selectedPosition != -1) {
            return list.get(selectedPosition);
        }
        return null;
    }

    @NonNull
    @Override
    public LichKhoiHanhViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lichkhoihanh, parent, false);
        return new LichKhoiHanhViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LichKhoiHanhViewHolder holder, int position) {
        LichKhoiHanh item = list.get(position);

        // Hiển thị trạng thái Checkbox dựa trên vị trí được chọn
        holder.checkbox.setChecked(position == selectedPosition);

        // Xử lý sự kiện click vào Checkbox hoặc toàn bộ Item
        View.OnClickListener clickListener = v -> {
            int previousSelected = selectedPosition;
            selectedPosition = holder.getAdapterPosition();

            // Chỉ cập nhật lại 2 item: cái vừa bỏ chọn và cái vừa được chọn để tối ưu hiệu năng
            notifyItemChanged(previousSelected);
            notifyItemChanged(selectedPosition);
        };
        holder.checkbox.setOnClickListener(clickListener);
        holder.itemView.setOnClickListener(clickListener);

        try{
            Date ngayDi = inputFormat.parse(item.getNgayKhoiHanh());
            Date ngayVe = inputFormat.parse(item.getNgayKetThuc());

            holder.txtDate.setText(
                    dateFormat.format(ngayDi) + " - " + dateFormat.format(ngayVe)
            );

            holder.txtTime.setText(timeFormat.format(ngayDi));

        }catch (Exception e){
            holder.txtDate.setText("—");
            holder.txtTime.setText("—");
        }

        // Kiểm tra null cho Hướng dẫn viên
        if (item.getHuongDanVien() != null) {
            holder.txtHDV.setText("HDV: " + item.getHuongDanVien().getHoTen());
        } else {
            holder.txtHDV.setText("HDV: Đang cập nhật");
        }

        holder.txtSlots.setText(item.getSoLuongKhachDaDat() + "/" + item.getSoLuongKhachToiDa() + " chỗ");

        if (item.getSoLuongKhachDaDat() >= item.getSoLuongKhachToiDa()) {
            holder.txtTrangThai.setText("Hết chỗ");
            holder.txtTrangThai.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        } else {
            holder.txtTrangThai.setText("Còn chỗ");
            holder.txtTrangThai.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        }
    }

    @Override
    public int getItemCount() { return list == null ? 0 : list.size(); }

}
