package com.example.apptravel;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                //selectedFragment = new HomeFragment();
            } else if (itemId == R.id.nav_menu) {
                selectedFragment = new DanhSachTourFragment();
            } else if (itemId == R.id.nav_schedule) {
                //selectedFragment = new ScheduleFragment(); // Cần tạo lớp này
            } else if (itemId == R.id.nav_chat) {
                //selectedFragment = new ChatFragment(); // Cần tạo lớp này
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = new TrangCaNhanFragment();
            } else {

                return false;
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit();

            return true;
        });
    }
}