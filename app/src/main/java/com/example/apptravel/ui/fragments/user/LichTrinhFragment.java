package com.example.apptravel.ui.fragments.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptravel.R;
import com.example.apptravel.data.adapters.LichTrinhAdapter;
import com.example.apptravel.data.models.BookedTour;
import com.example.apptravel.data.models.Tour;
import com.example.apptravel.data.models.TourData;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class LichTrinhFragment extends Fragment {

    private RecyclerView recyclerView;
    private LichTrinhAdapter adapter;
    private List<BookedTour> bookedTourList;
    private TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lich_trinh, container, false);

        tabLayout = view.findViewById(R.id.tab_layout);
        recyclerView = view.findViewById(R.id.recycler_view_lich_trinh);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Lấy dữ liệu gốc từ TourData
        bookedTourList = new ArrayList<>();
        List<Tour> allTours = TourData.getTourList();
    // Lấy 2 tour đầu tiên
        if (allTours.size() >= 2) {
            Tour tour1 = allTours.get(0);// Lấy tour
            bookedTourList.add(new BookedTour(tour1.getImageResId(), tour1.getTitle().replace("-\n", " "), "Ngày 28 - Ngày 29/09/2025", tour1.getLocation()));
            
            Tour tour2 = allTours.get(2); // Lấy tour
            bookedTourList.add(new BookedTour(tour2.getImageResId(), tour2.getTitle().replace("-\n", " "), "Ngày 18 - Ngày 20/09/2025", tour2.getLocation()));
        }

        adapter = new LichTrinhAdapter(getContext(), bookedTourList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabLayout.selectTab(tabLayout.getTabAt(1));
    }
}
