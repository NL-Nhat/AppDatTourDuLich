package com.example.apptravel.ui.activitys.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.apptravel.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;

public class ThemTourActivity extends AppCompatActivity {

    private MaterialButton btn_them_lichKH;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_them_tour);

        btn_them_lichKH = findViewById(R.id.btn_them_lichKH);

        btn_them_lichKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
    }

    private void showDialog() {
        // A. Khởi tạo BottomSheetDialog
        BottomSheetDialog dialog = new BottomSheetDialog(this, com.google.android.material.R.style.Theme_Design_BottomSheetDialog);

        // B. Gán file giao diện XML
        dialog.setContentView(R.layout.dialog_themlichkhoihanh_admin);

        // C. Cấu hình để Dialog bung Full màn hình và hiển thị đúng Layout 0dp
        FrameLayout bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);

        if (bottomSheet != null) {
            // [QUAN TRỌNG] Set chiều cao MATCH_PARENT.
            // Nếu thiếu dòng này, ScrollView (0dp) sẽ bị co lại bằng 0, gây mất nội dung giữa.
            bottomSheet.getLayoutParams().height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;

            // Set trạng thái mở rộng toàn bộ
            BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            // (Tùy chọn) Chặn người dùng vuốt xuống để đóng dialog, bắt buộc phải bấm nút Hủy/Lưu
            behavior.setSkipCollapsed(true);
        }

        // 3. XỬ LÝ SỰ KIỆN NÚT HỦY
        // Tìm nút theo ID "btn_huy" trong file XML bạn vừa gửi
        MaterialButton btnHuy = dialog.findViewById(R.id.btn_huy);

        if (btnHuy != null) {
            btnHuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Đóng dialog, quay lại màn hình cũ
                    dialog.dismiss();
                }
            });
        }

        // D. Hiển thị lên
        dialog.show();
    }
}