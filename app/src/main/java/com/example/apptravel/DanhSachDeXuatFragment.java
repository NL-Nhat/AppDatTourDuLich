package com.example.apptravel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptravel.adapters.LichTrinhYeuCauAdapter;
import com.example.apptravel.models.DateHeader;
import com.example.apptravel.models.DisplayableItem;
import com.example.apptravel.models.LichTrinhYeuCau;

import java.util.ArrayList;
import java.util.List;

public class DanhSachDeXuatFragment extends Fragment {
    private RecyclerView recyclerView;
    private LichTrinhYeuCauAdapter adapter;
    private List<DisplayableItem> displayableItemList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_danhsachdexuat, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_tours);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        taoDuLieuMau();

        adapter = new LichTrinhYeuCauAdapter(getContext(), displayableItemList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void taoDuLieuMau() {
        displayableItemList = new ArrayList<>();

        // Group 1
        displayableItemList.add(new DateHeader("12/10/2025"));
        displayableItemList.add(new LichTrinhYeuCau(R.drawable.da_nang, "Tour Du Lịch - Tham Quan, Check in tại Đà Nẵng City (1 Ngày 1 Đêm)", "Ngày 28 - Ngày 29/09/2025", "Đà Nẵng, Việt Nam", false));
        displayableItemList.add(new LichTrinhYeuCau(R.drawable.cao_bang, "Tour Du Lịch - Tham Quan, Cứu trợ tại Cao Bằng (2 Ngày 1 Đêm)", "Ngày 28 - Ngày 30/09/2025", "Cao Bằng, Việt Nam", true));

        // Group 2
        displayableItemList.add(new DateHeader("14/10/2025"));
        displayableItemList.add(new LichTrinhYeuCau(R.drawable.da_nang, "Tour Du Lịch - Tham Quan, Check in tại Đà Nẵng City (1 Ngày 1 Đêm)", "Ngày 28 - Ngày 29/09/2025", "Đà Nẵng, Việt Nam", false));
        displayableItemList.add(new LichTrinhYeuCau(R.drawable.cao_bang, "Tour Du Lịch - Tham Quan, Cứu trợ tại Cao Bằng (2 Ngày 1 Đêm)", "Ngày 28 - Ngày 30/09/2025", "Cao Bằng, Việt Nam", false));
    }
}
