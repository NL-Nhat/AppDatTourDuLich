package com.example.apptravel.ui.activitys.user;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.Fragment;

import com.example.apptravel.R;
import com.example.apptravel.ui.DangNhapActivity;
import com.example.apptravel.ui.fragments.user.DanhSachTourFragment;
import com.example.apptravel.ui.fragments.user.LichTrinhFragment;
import com.example.apptravel.ui.fragments.user.TaoTourTheoYeuCauFragment;
import com.example.apptravel.ui.fragments.user.TrangCaNhanFragment;
import com.example.apptravel.ui.fragments.user.TrangChinhFragment;
import com.example.apptravel.util.QuanLyDangNhap;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private QuanLyDangNhap quanLyDangNhap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        quanLyDangNhap = new QuanLyDangNhap(this);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);

        if (savedInstanceState == null) {
            setupInitialFragment(bottomNav);
        }

        bottomNav.setOnItemSelectedListener(item -> {

            int id = item.getItemId();
            Fragment selected = null;

            if (id == R.id.nav_home) {
                selected = new TrangChinhFragment();

            } else if (id == R.id.nav_menu) {
                selected = new DanhSachTourFragment();

            } else if (id == R.id.nav_schedule) {
                selected = new LichTrinhFragment();

            } else if (id == R.id.nav_chat) {
                selected = new TaoTourTheoYeuCauFragment();

            } else if (id == R.id.nav_profile) {

                if (quanLyDangNhap.isLoggedIn()) {
                    selected = new TrangCaNhanFragment();
                } else {
                    startActivity(new Intent(MainActivity.this, DangNhapActivity.class));
                    return false; // Không thay đổi icon tab
                }
            }

            if (selected != null) {
                loadFragment(selected);
                return true;
            }

            return false;
        });
    }

    private void setupInitialFragment(BottomNavigationView bottomNav) {

        String fragmentName = getIntent().getStringExtra("fragment_name");

        Fragment initialFragment;
        int initialTabId = R.id.nav_home;

        if ("DanhSachTourFragment".equals(fragmentName)) {
            initialFragment = new DanhSachTourFragment();
            initialTabId = R.id.nav_menu;

        } else if ("TrangCaNhanFragment".equals(fragmentName)) {
            initialFragment = new TrangCaNhanFragment();
            initialTabId = R.id.nav_profile;

        } else {
            initialFragment = new TrangChinhFragment();
        }

        loadFragment(initialFragment);
        bottomNav.setSelectedItemId(initialTabId);
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
