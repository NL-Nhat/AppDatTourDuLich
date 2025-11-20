package com.example.apptravel;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);

//        // Mặc định hiển thị HomeFragment
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.fragment_container, new HomeFragment())
//                .commit();
//
//        // Chọn fragment khi click vào tab
//        bottomNav.setOnItemSelectedListener(item -> {
//            Fragment selectedFragment;
//
//            switch (item.getItemId()) {
//                case R.id.nav_home:
//                    selectedFragment = new HomeFragment();
//                    break;
//                case R.id.nav_list:
//                    selectedFragment = new ListFragment();
//                    break;
//                case R.id.nav_schedule:
//                    selectedFragment = new ScheduleFragment();
//                    break;
//                case R.id.nav_favorite:
//                    selectedFragment = new FavoriteFragment();
//                    break;
//                case R.id.nav_profile:
//                    selectedFragment = new ProfileFragment();
//                    break;
//                default:
//                    selectedFragment = new HomeFragment();
//            }
//
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.fragment_container, selectedFragment)
//                    .commit();
//
//            return true;
//        });
    }
}