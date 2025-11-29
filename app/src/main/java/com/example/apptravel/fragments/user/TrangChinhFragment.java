package com.example.apptravel.fragments.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.apptravel.activitys.user.MainActivity;
import com.example.apptravel.R;

public class TrangChinhFragment extends Fragment {

    private TextView txtXemThem;
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

        return view;
    }
}
