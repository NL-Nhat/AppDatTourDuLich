package com.example.apptravel.ui.fragments.admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.apptravel.ui.DangNhapActivity;
import com.example.apptravel.R;
import com.example.apptravel.ui.activitys.user.DoiMatKhauActivity;
import com.example.apptravel.ui.activitys.user.ThongTinCaNhanActivity;

public class TrangCaNhanAdminFragment extends Fragment {

    private LinearLayout menu_editInfo;
    private LinearLayout menu_doiMk;
    private LinearLayout menu_logout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trang_ca_nhan_admin, container, false);

        menu_editInfo = view.findViewById(R.id.menu_edit_info);
        menu_doiMk = view.findViewById(R.id.menu_doiMK);
        menu_logout = view.findViewById(R.id.menu_logout);


        menu_editInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ThongTinCaNhanActivity.class);
                startActivity(intent);
            }
        });

        menu_doiMk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DoiMatKhauActivity.class);
                startActivity(intent);
            }
        });

        menu_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DangNhapActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}