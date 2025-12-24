package com.example.apptravel.data.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.apptravel.R;
import com.example.apptravel.data.api.ApiClient;
import com.example.apptravel.data.models.AdminBookingItem;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class BookingAdminAdapter extends RecyclerView.Adapter<BookingAdminAdapter.ViewHolder> {

    public interface OnDetailClickListener {
        void onDetailClick(AdminBookingItem item);
    }

    public interface OnConfirmClickListener {
        void onConfirmClick(AdminBookingItem item);
    }

    public interface OnCancelClickListener {
        void onCancelClick(AdminBookingItem item);
    }

    private final Context context;
    private final List<AdminBookingItem> bookings;
    private final OnDetailClickListener detailClickListener;
    private final OnConfirmClickListener confirmClickListener;
    private final OnCancelClickListener cancelClickListener;

    public BookingAdminAdapter(Context context,
                              List<AdminBookingItem> bookings,
                              OnDetailClickListener detailClickListener,
                              OnConfirmClickListener confirmClickListener,
                              OnCancelClickListener cancelClickListener) {
        this.context = context;
        this.bookings = bookings;
        this.detailClickListener = detailClickListener;
        this.confirmClickListener = confirmClickListener;
        this.cancelClickListener = cancelClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AdminBookingItem item = bookings.get(position);

        holder.customerName.setText(safe(item.getHoTen()));
        holder.customerPhone.setText(safe(item.getSoDienThoai()));
        holder.tourName.setText(safe(item.getTenTour()));

        holder.tourDate.setText(formatDateRange(item.getNgayKhoiHanh(), item.getNgayKetThuc()));

        int adults = item.getSoNguoiLon() != null ? item.getSoNguoiLon() : 0;
        int kids = item.getSoTreEm() != null ? item.getSoTreEm() : 0;
        holder.tourPeople.setText(adults + " người lớn + " + kids + " trẻ em");

        holder.bookingTotal.setText(formatMoney(item.getTongTien()));

        bindBookingStatus(holder, item.getTrangThaiDatTour());
        bindPaymentStatus(holder, item.getTrangThaiThanhToan());

        // Avatar
        String avatar = item.getAnhDaiDien();
        if (avatar != null && !avatar.isEmpty()) {
            String url = ApiClient.getFullImageUrl( avatar);
            Glide.with(context)
                    .load(url)
                    .placeholder(R.drawable.nen)
                    .error(R.drawable.nen)
                    .timeout(60000)
                    .into(holder.customerAvatar);
        } else {
            holder.customerAvatar.setImageResource(R.drawable.nen);
        }

        // Confirm: chỉ hiện khi Chờ xác nhận
        boolean canConfirm = item.getTrangThaiDatTour() != null && item.getTrangThaiDatTour().equalsIgnoreCase("ChoXacNhan");
        holder.btnConfirm.setVisibility(canConfirm ? View.VISIBLE : View.GONE);
        holder.btnConfirm.setOnClickListener(v -> {
            if (confirmClickListener != null) confirmClickListener.onConfirmClick(item);
        });

        // Cancel: ẩn nếu đã hủy
        boolean canCancel = item.getTrangThaiDatTour() == null || !item.getTrangThaiDatTour().equalsIgnoreCase("DaHuy");
        holder.btnCancel.setVisibility(canCancel ? View.VISIBLE : View.GONE);
        holder.btnCancel.setOnClickListener(v -> {
            if (cancelClickListener != null) cancelClickListener.onCancelClick(item);
        });

        holder.btnDetail.setOnClickListener(v -> {
            if (detailClickListener != null) detailClickListener.onDetailClick(item);
        });
    }

    private void bindBookingStatus(ViewHolder holder, String status) {
        String s = status == null ? "" : status;
        if (s.equalsIgnoreCase("ChoXacNhan")) {
            holder.bookingStatus.setText("Chờ xác nhận");
            holder.bookingStatus.setBackgroundResource(R.drawable.trangthai_tour_dong);
        } else if (s.equalsIgnoreCase("DaXacNhan")) {
            holder.bookingStatus.setText("Đã xác nhận");
            holder.bookingStatus.setBackgroundResource(R.drawable.trangthai_tour_mo);
        } else if (s.equalsIgnoreCase("DaHuy")) {
            holder.bookingStatus.setText("Đã hủy");
            holder.bookingStatus.setBackgroundResource(R.drawable.trangthai_tour_dong);
        } else {
            holder.bookingStatus.setText(s);
        }
    }

    private void bindPaymentStatus(ViewHolder holder, String status) {
        String s = status == null ? "" : status;
        if (s.equalsIgnoreCase("DaThanhToan")) {
            holder.paymentStatusIcon.setImageResource(R.drawable.ic_tick);
            holder.paymentStatus.setText("Đã thanh toán");
        } else {
            holder.paymentStatusIcon.setImageResource(R.drawable.ic_payment);
            holder.paymentStatus.setText(s.isEmpty() ? "Chưa thanh toán" : s);
        }
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

    private String formatMoney(Double amount) {
        if (amount == null) return "0 VND";
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        return nf.format(amount) + " VND";
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }

    @Override
    public int getItemCount() {
        return bookings == null ? 0 : bookings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView customerAvatar;
        final TextView customerName;
        final TextView customerPhone;
        final TextView bookingStatus;

        final TextView tourName;
        final TextView tourDate;
        final TextView tourPeople;

        final TextView bookingTotal;
        final ImageView paymentStatusIcon;
        final TextView paymentStatus;

        final Button btnDetail;
        final Button btnCancel;
        final Button btnConfirm;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            customerAvatar = itemView.findViewById(R.id.customer_avatar);
            customerName = itemView.findViewById(R.id.customer_name);
            customerPhone = itemView.findViewById(R.id.customer_phone);
            bookingStatus = itemView.findViewById(R.id.booking_status);

            tourName = itemView.findViewById(R.id.tour_name);
            tourDate = itemView.findViewById(R.id.tour_date);
            tourPeople = itemView.findViewById(R.id.tour_people);

            bookingTotal = itemView.findViewById(R.id.booking_total);
            paymentStatusIcon = itemView.findViewById(R.id.payment_status_icon);
            paymentStatus = itemView.findViewById(R.id.payment_status);

            btnDetail = itemView.findViewById(R.id.btn_view_detail);
            btnCancel = itemView.findViewById(R.id.btn_cancel);
            btnConfirm = itemView.findViewById(R.id.btn_confirm);
        }
    }
}
