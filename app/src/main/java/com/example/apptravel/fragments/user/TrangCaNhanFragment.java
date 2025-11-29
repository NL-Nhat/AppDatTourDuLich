package com.example.apptravel.fragments.user;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.apptravel.DangNhapActivity;
import com.example.apptravel.R;
import com.example.apptravel.activitys.user.CaiDatActivity;
import com.example.apptravel.activitys.user.ThongTinCaNhanActivity;

public class TrangCaNhanFragment extends Fragment {

    private LinearLayout userInfo;
    private LinearLayout logout;
    private LinearLayout caiDat;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trang_ca_nhan, container, false);

        userInfo = view.findViewById(R.id.user_info);
        logout = view.findViewById(R.id.logout);
        caiDat = view.findViewById(R.id.caiDat);

        userInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ThongTinCaNhanActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DangNhapActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        caiDat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CaiDatActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}