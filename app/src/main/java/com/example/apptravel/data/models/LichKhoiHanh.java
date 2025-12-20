package com.example.apptravel.data.models;

import java.time.LocalDateTime;

public class LichKhoiHanh {

    private Integer maLichKhoiHanh;
    private String ngayKhoiHanh;
    private String ngayKetThuc;
    private Integer soLuongKhachToiDa;
    private Integer soLuongKhachDaDat;
    private Tour tour;
    private NguoiDung huongDanVien;

    public Integer getMaLichKhoiHanh() {
        return maLichKhoiHanh;
    }

    public void setMaLichKhoiHanh(Integer maLichKhoiHanh) {
        this.maLichKhoiHanh = maLichKhoiHanh;
    }

    public String getNgayKhoiHanh() {
        return ngayKhoiHanh;
    }

    public void setNgayKhoiHanh(String ngayKhoiHanh) {
        this.ngayKhoiHanh = ngayKhoiHanh;
    }

    public String getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(String ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public Integer getSoLuongKhachToiDa() {
        return soLuongKhachToiDa;
    }

    public void setSoLuongKhachToiDa(Integer soLuongKhachToiDa) {
        this.soLuongKhachToiDa = soLuongKhachToiDa;
    }

    public Integer getSoLuongKhachDaDat() {
        return soLuongKhachDaDat;
    }

    public void setSoLuongKhachDaDat(Integer soLuongKhachDaDat) {
        this.soLuongKhachDaDat = soLuongKhachDaDat;
    }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public NguoiDung getHuongDanVien() {
        return huongDanVien;
    }

    public void setHuongDanVien(NguoiDung huongDanVien) {
        this.huongDanVien = huongDanVien;
    }
}
