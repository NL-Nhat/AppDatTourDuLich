package com.example.apptravel.data.models;

import com.example.apptravel.R;

import java.util.ArrayList;
import java.util.List;

public class TourData {

    private static List<Tour> tourList;

    public static List<Tour> getTourList() {
        if (tourList == null) {
            tourList = new ArrayList<>();
            tourList.add(new Tour("Tour du lịch Trải nghiệm mọi thứ-\nĐà Nẵng", "490,000", 5.0f, R.drawable.da_nang, false, "Đà Nẵng, Việt Nam"));
            tourList.add(new Tour("Tour du lịch Hồ Gươm -\nHà Nội", "345,000", 4.8f, R.drawable.ha_noi, false, "Hà Nội, Việt Nam"));
            tourList.add(new Tour("Tour du lịch Ẩm thực -\nQuảng Ninh", "199,000", 4.9f, R.drawable.quang_ninh, false, "Quảng Ninh, Việt Nam"));
            tourList.add(new Tour("Tour du lịch -\nCao Bằng", "249,000", 4.7f, R.drawable.cao_bang, false, "Cao Bằng, Việt Nam"));
            tourList.add(new Tour("Tour du lịch Sơn Trà-\nĐà Nẵng", "239,000", 5.0f, R.drawable.da_nang, false, "Đà Nẵng, Việt Nam"));
            tourList.add(new Tour("Tour du lịch Tứ đảo -\nNha Trang", "150,000", 4.8f, R.drawable.ha_noi, false, "Nha Trang, Việt Nam"));
            tourList.add(new Tour("Tour du lịch Núi thần tài -\nĐà Nẵng", "145,000", 5.0f, R.drawable.da_nang, false, "Đà Nẵng, Việt Nam"));
            tourList.add(new Tour("Tour du lịch di tích lịch sử-\nQuảng Ngãi", "999,000", 4.8f, R.drawable.ha_noi, false, "Quảng Ngãi, Việt Nam"));
            tourList.add(new Tour("Tour du lịch vịnh Hạ Long -\nQuảng Ninh", "100,000", 4.9f, R.drawable.quang_ninh, false, "Quảng Ninh, Việt Nam"));
            tourList.add(new Tour("Tour du lịch và khám phá -\nCao Bằng", "289,000", 4.7f, R.drawable.cao_bang, false, "Cao Bằng, Việt Nam"));
            tourList.add(new Tour("Tour du lịch Bà Nà Hill -\nĐà Nẵng", "399,000", 5.0f, R.drawable.da_nang, false, "Đà Nẵng, Việt Nam"));
            tourList.add(new Tour("Tour du lịch Thiên đường-\nHà Nội", "678,000", 4.8f, R.drawable.ha_noi, false, "Hà Nội, Việt Nam"));
        }
        return tourList;
    }
}
