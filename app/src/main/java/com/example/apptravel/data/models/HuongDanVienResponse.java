package com.example.apptravel.data.models;

public class HuongDanVienResponse {
    private int maHuongDanVien;
    private String tenHuongDanVien;

    public int getMaHuongDanVien() {
        return maHuongDanVien;
    }

    public void setMaHuongDanVien(int maHuongDanVien) {
        this.maHuongDanVien = maHuongDanVien;
    }

    public String getTenHuongDanVien() {
        return tenHuongDanVien;
    }

    public void setTenHuongDanVien(String tenHuongDanVien) {
        this.tenHuongDanVien = tenHuongDanVien;
    }

    @Override
    public String toString() {
        return tenHuongDanVien; // ArrayAdapter sẽ dùng cái này để hiển thị text
    }
}
