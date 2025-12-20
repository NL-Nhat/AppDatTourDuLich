package com.example.apptravel.ui.fragments.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptravel.R;
import com.example.apptravel.data.adapters.BookingAdminAdapter;
import com.example.apptravel.data.models.AdminBookingItem;
import com.example.apptravel.data.repository.AdminBookingRepository;
import com.example.apptravel.ui.activitys.admin.AdminBookingDetailActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingAdminFragment extends Fragment {

    private RecyclerView recyclerBookings;
    private BookingAdminAdapter adapter;
    private final List<AdminBookingItem> bookings = new ArrayList<>();

    private TabLayout tabLayout;

    private TextView statTotal;
    private TextView statPending;
    private TextView statSuccess;
    private TextView statCancelled;

    private AdminBookingRepository repo;

    private String currentStatus = null; // null = all
    private String currentQuery = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_admin, container, false);

        repo = new AdminBookingRepository(requireContext());

        recyclerBookings = view.findViewById(R.id.recycler_bookings);
        tabLayout = view.findViewById(R.id.tab_layout);

        statTotal = view.findViewById(R.id.stat_total_bookings);
        statPending = view.findViewById(R.id.stat_pending_bookings);
        statSuccess = view.findViewById(R.id.stat_success_bookings);
        statCancelled = view.findViewById(R.id.stat_cancelled_bookings);

        // Setup Recycler
        recyclerBookings.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BookingAdminAdapter(
                getContext(),
                bookings,
                item -> {
                    Intent intent = new Intent(getContext(), AdminBookingDetailActivity.class);
                    int id = item.getMaDatTour() != null ? item.getMaDatTour() : -1;
                    intent.putExtra(AdminBookingDetailActivity.EXTRA_MA_DAT_TOUR, id);
                    startActivity(intent);
                },
                this::confirmBooking,
                this::showCancelDialog
        );
        recyclerBookings.setAdapter(adapter);

        setupTabs();

        ImageView btnSearch = view.findViewById(R.id.btn_search);
        ImageView btnFilter = view.findViewById(R.id.btn_filter);
        if (btnSearch != null) {
            btnSearch.setOnClickListener(v -> showSearchDialog());
        }
        if (btnFilter != null) {
            btnFilter.setOnClickListener(v -> showFilterDialog());
        }

        loadBookings();

        return view;
    }

    private void setupTabs() {
        tabLayout.removeAllTabs();
        tabLayout.addTab(tabLayout.newTab().setText("Tất cả"));
        tabLayout.addTab(tabLayout.newTab().setText("Chờ xác nhận"));
        tabLayout.addTab(tabLayout.newTab().setText("Đã xác nhận"));
        tabLayout.addTab(tabLayout.newTab().setText("Đã hủy"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                if (pos == 0) currentStatus = null;
                else if (pos == 1) currentStatus = "ChoXacNhan";
                else if (pos == 2) currentStatus = "DaXacNhan";
                else if (pos == 3) currentStatus = "DaHuy";
                loadBookings();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                loadBookings();
            }
        });
    }

    private void loadBookings() {
        repo.getBookings(currentStatus, currentQuery).enqueue(new Callback<List<AdminBookingItem>>() {
            @Override
            public void onResponse(@NonNull Call<List<AdminBookingItem>> call, @NonNull Response<List<AdminBookingItem>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(getContext(), "Không tải được danh sách booking", Toast.LENGTH_SHORT).show();
                    return;
                }

                bookings.clear();
                bookings.addAll(response.body());
                adapter.notifyDataSetChanged();

                updateStats(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<AdminBookingItem>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showSearchDialog() {
        if (getContext() == null) return;

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_admin_booking_search, null);
        TextInputLayout til = dialogView.findViewById(R.id.til_search);
        TextInputEditText et = dialogView.findViewById(R.id.et_search);

        if (et != null && currentQuery != null) {
            et.setText(currentQuery);
        }

        AlertDialog dialog = new MaterialAlertDialogBuilder(getContext())
                .setTitle("Tìm kiếm đơn đặt tour")
                .setMessage("Nhập từ khóa (mã đơn, tên tour, họ tên, email, SĐT).")
                .setView(dialogView)
                .setNegativeButton("Đóng", (d, w) -> d.dismiss())
                .setNeutralButton("Xóa", null)
                .setPositiveButton("Tìm", null)
                .create();

        dialog.setOnShowListener(d -> {
            // Clear
            dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(v -> {
                currentQuery = null;
                if (et != null) et.setText("");
                til.setError(null);
                dialog.dismiss();
                loadBookings();
            });

            // Search
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                String q = et != null && et.getText() != null ? et.getText().toString().trim() : "";
                if (q.isEmpty()) {
                    til.setError("Vui lòng nhập từ khóa");
                    return;
                }
                til.setError(null);
                currentQuery = q;
                dialog.dismiss();
                loadBookings();
            });
        });

        dialog.show();
    }

    private void showFilterDialog() {
        if (getContext() == null) return;

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_admin_booking_filter, null);
        android.widget.RadioGroup rg = dialogView.findViewById(R.id.rg_status);
        android.widget.RadioButton rbAll = dialogView.findViewById(R.id.rb_all);
        android.widget.RadioButton rbPending = dialogView.findViewById(R.id.rb_pending);
        android.widget.RadioButton rbConfirmed = dialogView.findViewById(R.id.rb_confirmed);
        android.widget.RadioButton rbCancelled = dialogView.findViewById(R.id.rb_cancelled);

        // Preselect currentStatus
        if (currentStatus == null) {
            rbAll.setChecked(true);
        } else if (currentStatus.equalsIgnoreCase("ChoXacNhan")) {
            rbPending.setChecked(true);
        } else if (currentStatus.equalsIgnoreCase("DaXacNhan")) {
            rbConfirmed.setChecked(true);
        } else if (currentStatus.equalsIgnoreCase("DaHuy")) {
            rbCancelled.setChecked(true);
        } else {
            rbAll.setChecked(true);
        }

        AlertDialog dialog = new MaterialAlertDialogBuilder(getContext())
                .setTitle("Lọc đơn đặt tour")
                .setView(dialogView)
                .setNegativeButton("Đóng", (d, w) -> d.dismiss())
                .setNeutralButton("Xóa lọc", null)
                .setPositiveButton("Áp dụng", null)
                .create();

        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(v -> {
                // Reset status filter -> tab 0
                currentStatus = null;
                dialog.dismiss();
                selectTabForStatus(currentStatus);
            });

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                int checkedId = rg.getCheckedRadioButtonId();
                if (checkedId == R.id.rb_pending) currentStatus = "ChoXacNhan";
                else if (checkedId == R.id.rb_confirmed) currentStatus = "DaXacNhan";
                else if (checkedId == R.id.rb_cancelled) currentStatus = "DaHuy";
                else currentStatus = null;

                dialog.dismiss();
                selectTabForStatus(currentStatus);
            });
        });

        dialog.show();
    }

    private void selectTabForStatus(String status) {
        if (tabLayout == null) return;

        int pos = 0;
        if (status == null) pos = 0;
        else if (status.equalsIgnoreCase("ChoXacNhan")) pos = 1;
        else if (status.equalsIgnoreCase("DaXacNhan")) pos = 2;
        else if (status.equalsIgnoreCase("DaHuy")) pos = 3;

        TabLayout.Tab tab = tabLayout.getTabAt(pos);
        if (tab != null) {
            tab.select();
        } else {
            // fallback
            loadBookings();
        }
    }

    private void confirmBooking(AdminBookingItem item) {
        if (item == null || item.getMaDatTour() == null) return;
        int id = item.getMaDatTour();

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Xác nhận đơn")
                .setMessage("Bạn chắc chắn muốn xác nhận đơn #" + id + " ?")
                .setNegativeButton("Đóng", (d, w) -> d.dismiss())
                .setPositiveButton("Xác nhận", (d, w) -> {
                    repo.confirmBooking(id).enqueue(new retrofit2.Callback<Void>() {
                        @Override
                        public void onResponse(@NonNull retrofit2.Call<Void> call, @NonNull retrofit2.Response<Void> response) {
                            if (!response.isSuccessful()) {
                                Toast.makeText(getContext(), "Xác nhận thất bại (HTTP " + response.code() + ")", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Toast.makeText(getContext(), "Đã xác nhận đơn", Toast.LENGTH_SHORT).show();
                            loadBookings();
                        }

                        @Override
                        public void onFailure(@NonNull retrofit2.Call<Void> call, @NonNull Throwable t) {
                            Toast.makeText(getContext(), "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .show();
    }

    private void showCancelDialog(AdminBookingItem item) {
        if (getContext() == null || item == null || item.getMaDatTour() == null) return;

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_admin_booking_cancel, null);
        TextInputLayout til = dialogView.findViewById(R.id.til_cancel_reason);
        TextInputEditText et = dialogView.findViewById(R.id.et_cancel_reason);

        AlertDialog dialog = new MaterialAlertDialogBuilder(getContext())
                .setTitle("Hủy đơn")
                .setMessage("Vui lòng nhập lý do hủy.")
                .setView(dialogView)
                .setNegativeButton("Đóng", (d, w) -> d.dismiss())
                .setPositiveButton("Hủy đơn", null)
                .create();

        dialog.setOnShowListener(d -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String reason = et != null && et.getText() != null ? et.getText().toString().trim() : "";
            if (reason.isEmpty()) {
                til.setError("Vui lòng nhập lý do hủy");
                return;
            }
            til.setError(null);
            dialog.dismiss();
            callCancelBooking(item.getMaDatTour(), reason);
        }));

        dialog.show();
    }

    private void callCancelBooking(int maDatTour, String reason) {
        repo.cancelBooking(maDatTour, reason).enqueue(new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<Void> call, @NonNull retrofit2.Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "Hủy thất bại (HTTP " + response.code() + ")", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getContext(), "Đã hủy đơn", Toast.LENGTH_SHORT).show();
                loadBookings();
            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateStats(List<AdminBookingItem> list) {
        int total = list == null ? 0 : list.size();
        int pending = 0;
        int success = 0;
        int cancelled = 0;

        if (list != null) {
            for (AdminBookingItem b : list) {
                String s = b.getTrangThaiDatTour() == null ? "" : b.getTrangThaiDatTour();
                if (s.equalsIgnoreCase("ChoXacNhan")) pending++;
                else if (s.equalsIgnoreCase("DaXacNhan")) success++;
                else if (s.equalsIgnoreCase("DaHuy")) cancelled++;
            }
        }

        // Khi đang lọc theo status thì stat_total = số item của filter.
        // Tổng thực tế (tất cả status) muốn chuẩn hơn thì cần gọi API 1 lần status=null.
        statTotal.setText(String.valueOf(total));
        statPending.setText(String.valueOf(pending));
        statSuccess.setText(String.valueOf(success));
        statCancelled.setText(String.valueOf(cancelled));
    }
}
