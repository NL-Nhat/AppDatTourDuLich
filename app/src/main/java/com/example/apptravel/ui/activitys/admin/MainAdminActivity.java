package com.example.apptravel.ui.activitys.admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.apptravel.R;
import com.example.apptravel.ui.fragments.admin.BookingAdminFragment;
import com.example.apptravel.ui.fragments.admin.QuanLyTourAdminFragment;
import com.example.apptravel.ui.fragments.admin.TrangCaNhanAdminFragment;
import com.example.apptravel.ui.fragments.admin.TrangChuAdminFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainAdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav_admin);

        // Mặc định hiển thị TrangChuAdminFragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new TrangChuAdminFragment())
                .commit();

        // Xử lý navigation
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();
            if (itemId == R.id.nav_admin_dashboard) {
                selectedFragment = new TrangChuAdminFragment();
            } else if (itemId == R.id.nav_admin_tours) {
                selectedFragment = new QuanLyTourAdminFragment();
            } else if (itemId == R.id.nav_admin_bookings) {
                selectedFragment = new BookingAdminFragment();
            } else if (itemId == R.id.nav_admin_users) {
                Intent intent = new Intent(MainAdminActivity.this, QuanLyNguoiDungActivity.class);
                startActivity(intent);
            } else if (itemId == R.id.nav_admin_profile) {
                selectedFragment = new TrangCaNhanAdminFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }

            return true;
        });
    }
}