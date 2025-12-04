package com.example.apptravel.data.models;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TourDataManager {

    private static final Map<String, LichTrinhYeuCau> selectedToursMap = new LinkedHashMap<>();

    public static void setTourSelected(LichTrinhYeuCau tour, boolean isSelected) {
        if (isSelected) {
            selectedToursMap.put(tour.getTitle(), tour);
        } else {
            selectedToursMap.remove(tour.getTitle());
        }
    }

    public static List<LichTrinhYeuCau> getSelectedTours() {
        return new ArrayList<>(selectedToursMap.values());
    }

    public static boolean isTourSelected(LichTrinhYeuCau tour) {
        return selectedToursMap.containsKey(tour.getTitle());
    }
}
