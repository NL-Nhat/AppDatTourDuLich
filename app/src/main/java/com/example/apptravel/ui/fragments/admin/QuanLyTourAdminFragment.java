package com.example.apptravel.ui.fragments.admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apptravel.R;
import com.example.apptravel.ui.activitys.admin.ThemTourActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class QuanLyTourAdminFragment extends Fragment {

    private FloatingActionButton ic_them_tour;
    private RecyclerView recycler_tours;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quan_ly_tour_admin, container, false);

        ic_them_tour = view.findViewById(R.id.ic_them_tour);

        ic_them_tour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ThemTourActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}