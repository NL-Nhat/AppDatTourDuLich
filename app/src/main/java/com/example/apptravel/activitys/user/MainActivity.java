package com.example.apptravel.activitys.user;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.Fragment;

import com.example.apptravel.R;
import com.example.apptravel.fragments.user.DanhSachTourFragment;
import com.example.apptravel.fragments.user.LichTrinhFragment;
import com.example.apptravel.fragments.user.TaoTourTheoYeuCauFragment;
import com.example.apptravel.fragments.user.TrangCaNhanFragment;
import com.example.apptravel.fragments.user.TrangChinhFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        String targetFragment = getIntent().getStringExtra("fragment_name");

        if (savedInstanceState == null) {
            Fragment initialFragment = null;
            int initialItemId = R.id.nav_home;

            if ("DanhSachTourFragment".equals(targetFragment)) {
                // Chuyển hướng từ nút "Xem thêm" ở trang chủ
                initialFragment = new DanhSachTourFragment();
                initialItemId = R.id.nav_menu;
            } else {
                initialFragment = new TrangChinhFragment();
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, initialFragment)
                    .commit();
            bottomNav.setSelectedItemId(initialItemId);
        }

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                selectedFragment = new TrangChinhFragment();
            } else if (itemId == R.id.nav_menu) {
                selectedFragment = new DanhSachTourFragment();
            } else if (itemId == R.id.nav_schedule) {
                selectedFragment = new LichTrinhFragment();
            } else if (itemId == R.id.nav_chat) {
                selectedFragment = new TaoTourTheoYeuCauFragment();
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = new TrangCaNhanFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
                return true;
            }

            return false;
        });
    }
}