package com.example.apptravel.data.models;

import java.time.LocalDate;

public class BookingRequest {

    private int maNguoiDung;
    private int maLichKhoiHanh;
    private int soNguoiLon;
    private int soTreEm;
    private String hoTen;
    private String soDienThoai;
    private String gioiTinh;
    private String diaChi;
    private String ngaySinh;

    public int getMaNguoiDung() {
        return maNguoiDung;
    }

    public void setMaNguoiDung(int maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }

    public int getMaLichKhoiHanh() {
        return maLichKhoiHanh;
    }

    public void setMaLichKhoiHanh(int maLichKhoiHanh) {
        this.maLichKhoiHanh = maLichKhoiHanh;
    }

    public int getSoNguoiLon() {
        return soNguoiLon;
    }

    public void setSoNguoiLon(int soNguoiLon) {
        this.soNguoiLon = soNguoiLon;
    }

    public int getSoTreEm() {
        return soTreEm;
    }

    public void setSoTreEm(int soTreEm) {
        this.soTreEm = soTreEm;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }
}
