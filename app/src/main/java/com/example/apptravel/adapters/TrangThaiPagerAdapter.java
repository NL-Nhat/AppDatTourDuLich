package com.example.apptravel.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.apptravel.TinhTrangListFragment;

/**
 * Adapter này cung cấp các Fragment cho mỗi tab trong ViewPager2.
 */
public class TrangThaiPagerAdapter extends FragmentStateAdapter {

    public TrangThaiPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return TinhTrangListFragment.newInstance("DA_DUYET");
            case 1:
                return TinhTrangListFragment.newInstance("CHO_XAC_NHAN");
            case 2:
                // Tab "Đã hủy"
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
