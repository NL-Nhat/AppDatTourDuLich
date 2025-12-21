package com.example.apptravel.data.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.apptravel.ui.fragments.user.TinhTrangListFragment;

/**
 * PagerAdapter cho màn "Lịch sử đặt tour" (Đã lưu / Đã đặt / Đã hủy).
 * Tái sử dụng TinhTrangListFragment để load dữ liệu từ API.
 */
public class LichSuDatTourPagerAdapter extends FragmentStateAdapter {

    public LichSuDatTourPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                // Đã lưu -> Chờ xác nhận
                return TinhTrangListFragment.newInstance("CHO_XAC_NHAN");
            case 1:
                // Đã đặt -> Đã duyệt
                return TinhTrangListFragment.newInstance("DA_DUYET");
            case 2:
                // Đã hủy
                return TinhTrangListFragment.newInstance("DA_HUY");
            default:
                return new Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
