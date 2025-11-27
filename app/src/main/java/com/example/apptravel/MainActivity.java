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

        // Set default fragment to avoid blank screen on startup
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DanhSachTourFragment()).commit();
            bottomNav.setSelectedItemId(R.id.nav_menu);
        }

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                // HomeFragment is not created, using DanhSachTourFragment as a placeholder
                selectedFragment = new DanhSachTourFragment();
            } else if (itemId == R.id.nav_menu) {
                selectedFragment = new DanhSachTourFragment();
            } else if (itemId == R.id.nav_schedule) {
                selectedFragment = new LichTrinhFragment();
            } else if (itemId == R.id.nav_chat) { // This corresponds to 'Yêu thích' in the UI
                selectedFragment = new TaoTourTheoYeuCauFragment();
                // YeuThichFragment needs to be created
                // selectedFragment = new YeuThichFragment();
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = new TrangCaNhanFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
                return true;
            }

            return false; // Do not select the item if no fragment is associated
        });
    }
}