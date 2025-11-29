package com.example.apptravel.fragments.user;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apptravel.R;
import com.example.apptravel.activitys.user.TourDetailActivity;
import com.example.apptravel.adapters.TourAdapter;
import com.example.apptravel.models.Tour;
import com.example.apptravel.models.TourData;

import java.util.List;

public class DanhSachTourFragment extends Fragment implements TourAdapter.OnTourClickListener {

    private RecyclerView recyclerView;
    private TourAdapter tourAdapter;
    private List<Tour> tourList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_danh_sach_tour, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_tours);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        tourList = TourData.getTourList();

        tourAdapter = new TourAdapter(getContext(), tourList, this);
        recyclerView.setAdapter(tourAdapter);

        return view;
    }

    @Override
    public void onTourClick(int position) {
        Intent intent = new Intent(getActivity(), TourDetailActivity.class);
        startActivity(intent);
    }
}
