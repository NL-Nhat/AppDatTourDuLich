package com.example.apptravel.data.models;

public class ThanhToanRequest {

    private int maDatTour;
    private String phuongThuc;
    private Double soTien;

    public int getMaDatTour() {
        return maDatTour;
    }

    public void setMaDatTour(int maDatTour) {
        this.maDatTour = maDatTour;
    }

    public String getPhuongThuc() {
        return phuongThuc;
    }

    public void setPhuongThuc(String phuongThuc) {
        this.phuongThuc = phuongThuc;
    }

    public Double getSoTien() {
        return soTien;
    }

    public void setSoTien(Double soTien) {
        this.soTien = soTien;
    }
}
