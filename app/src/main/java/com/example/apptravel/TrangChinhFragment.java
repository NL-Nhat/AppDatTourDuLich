package com.example.apptravel;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TrangChinhFragment extends Fragment {

    private TextView txtXemThem;
    private TextView txttien;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trangchinh, container, false);


        txtXemThem = view.findViewById(R.id.txtXemThem);
        txtXemThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("fragment_name", "DanhSachTourFragment");
                startActivity(intent);
            }
        });
        txttien = (TextView) view.findViewById(R.id.txtTien);

        return view;
    }
}
