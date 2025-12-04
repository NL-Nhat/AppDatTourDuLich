package com.example.apptravel.ui.fragments.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.apptravel.R;
import com.google.android.material.navigation.NavigationView;

public class TaoTourTheoYeuCauFragment extends Fragment {

    private DrawerLayout drawerLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_taotourtheoyeucau, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        drawerLayout = (DrawerLayout) view.getRootView().findViewById(R.id.drawer_layout);

        ImageView ivBack = view.findViewById(R.id.iv_back);
        ImageView ivMenuToggle = view.findViewById(R.id.ic_menu_toggle);

        if (ivBack != null) {
            ivBack.setOnClickListener(v -> {
                FragmentManager fragmentManager = getParentFragmentManager();
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStack();
                } else {
                    requireActivity().getOnBackPressedDispatcher().onBackPressed();
                }
            });
        }

        if (ivMenuToggle != null && drawerLayout != null) {
            ivMenuToggle.setOnClickListener(v -> {
                drawerLayout.openDrawer(GravityCompat.END);
            });
        }

        NavigationView navigationView = view.findViewById(R.id.navigation_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(item -> {
                int itemId = item.getItemId();
                String message = "";

                if (itemId == R.id.nav_new_chat) {
                    message = "Chức năng: Đoạn chat mới";
                } else if (itemId == R.id.nav_suggestions) {
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new DanhSachDeXuatFragment())
                            .addToBackStack(null)
                            .commit();
                } else if (itemId == R.id.nav_status) {
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new TinhTrangXacNhanFragment())
                            .addToBackStack(null)
                            .commit();
                } else if (itemId == R.id.nav_history) {
                    message = "Chức năng: Lịch sử tra cứu";
                }
                showToast(message);

                drawerLayout.closeDrawer(GravityCompat.END);
                return true;
            });
        }
    }
    private void showToast(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}