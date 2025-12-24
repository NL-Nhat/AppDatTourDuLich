package com.example.apptravel.data.models;

import java.math.BigDecimal;
import java.util.List;

public class TourRequest {
    private String tenTour;
    private String moTa;
    private Double giaNguoiLon;
    private Double giaTreEm;
    private String trangThai;
    private int maDiemDen;
    private List<LichKhoiHanhDTO> lichKhoiHanhs;

    public static class LichKhoiHanhDTO {
        private int maLichKhoiHanh;
        private String ngayKhoiHanh;
        private String ngayKetThuc;
        private int soLuongKhachToiDa;
        private int maHDV;
        private String tenHDV;

        public LichKhoiHanhDTO(String ngayKH, String ngayKT, Integer soKhach) {
            this.ngayKhoiHanh = ngayKH;
            this.ngayKetThuc = ngayKT;
            this.soLuongKhachToiDa = (soKhach != null) ? soKhach : 0;
        }

        // --- Getters & Setters ---
        public Integer getMaLichKhoiHanh() { return maLichKhoiHanh; }
        public void setMaLichKhoiHanh(Integer maLichKhoiHanh) { this.maLichKhoiHanh = maLichKhoiHanh; }
        public String getNgayKhoiHanh() { return ngayKhoiHanh; }
        public void setNgayKhoiHanh(String ngayKhoiHanh) { this.ngayKhoiHanh = ngayKhoiHanh; }
        public String getNgayKetThuc() { return ngayKetThuc; }
        public void setNgayKetThuc(String ngayKetThuc) { this.ngayKetThuc = ngayKetThuc; }
        public Integer getSoLuongKhachToiDa() { return soLuongKhachToiDa; }
        public void setSoLuongKhachToiDa(Integer soLuongKhachToiDa) { this.soLuongKhachToiDa = soLuongKhachToiDa; }
        public Integer getMaHDV() { return maHDV; }
        public void setMaHDV(Integer maHDV) { this.maHDV = maHDV; }
        public String getTenHDV() { return tenHDV; }
        public void setTenHDV(String tenHDV) { this.tenHDV = tenHDV; }
    }
    
    // --- Getters & Setters cho TourRequest ---
    public String getTenTour() { return tenTour; }
    public void setTenTour(String tenTour) { this.tenTour = tenTour; }
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public List<LichKhoiHanhDTO> getLichKhoiHanhs() { return lichKhoiHanhs; }
    public void setLichKhoiHanhs(List<LichKhoiHanhDTO> lichKhoiHanhs) { this.lichKhoiHanhs = lichKhoiHanhs; }

    public Double getGiaNguoiLon() {
        return giaNguoiLon;
    }

    public void setGiaNguoiLon(Double giaNguoiLon) {
        this.giaNguoiLon = giaNguoiLon;
    }

    public Double getGiaTreEm() {
        return giaTreEm;
    }

    public void setGiaTreEm(Double giaTreEm) {
        this.giaTreEm = giaTreEm;
    }

    public int getMaDiemDen() {
        return maDiemDen;
    }

    public void setMaDiemDen(int maDiemDen) {
        this.maDiemDen = maDiemDen;
    }
}
