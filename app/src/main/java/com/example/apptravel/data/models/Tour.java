package com.example.apptravel.data.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class Tour implements Serializable {
    private int maTour;
    private String tenTour;
    private String moTa;
    private BigDecimal giaNguoiLon;
    private BigDecimal giaTreEm;
    private String urlHinhAnhChinh;
    private String trangThai;
    private DiemDen diemDen;
    
    // Bổ sung các trường đánh giá
    private Double diemDanhGiaTrungBinh;
    private Integer soLuongDanhGia;

    private List<LichKhoiHanh> lichKhoiHanhs;

    public Tour(int maTour, String tenTour, String urlHinhAnhChinh, BigDecimal giaNguoiLon, DiemDen diemDen) {
        this.maTour = maTour;
        this.tenTour = tenTour;
        this.urlHinhAnhChinh = urlHinhAnhChinh;
        this.giaNguoiLon = giaNguoiLon;
        this.diemDen = diemDen;
    }

    // === Getters & Setters ===

    public int getMaTour() {
        return maTour;
    }

    public void setMaTour(int maTour) {
        this.maTour = maTour;
    }

    public String getTenTour() {
        return tenTour;
    }

    public void setTenTour(String tenTour) {
        this.tenTour = tenTour;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public BigDecimal getGiaNguoiLon() {
        return giaNguoiLon;
    }

    public void setGiaNguoiLon(BigDecimal giaNguoiLon) {
        this.giaNguoiLon = giaNguoiLon;
    }

    public BigDecimal getGiaTreEm() {
        return giaTreEm;
    }

    public void setGiaTreEm(BigDecimal giaTreEm) {
        this.giaTreEm = giaTreEm;
    }

    public String getUrlHinhAnhChinh() {
        return urlHinhAnhChinh;
    }

    public void setUrlHinhAnhChinh(String urlHinhAnhChinh) {
        this.urlHinhAnhChinh = urlHinhAnhChinh;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public DiemDen getDiemDen() {
        return diemDen;
    }

    public void setDiemDen(DiemDen diemDen) {
        this.diemDen = diemDen;
    }
    
    // Getter/Setter cho đánh giá
    public Double getDiemDanhGiaTrungBinh() {
        return diemDanhGiaTrungBinh;
    }

    public void setDiemDanhGiaTrungBinh(Double diemDanhGiaTrungBinh) {
        this.diemDanhGiaTrungBinh = diemDanhGiaTrungBinh;
    }

    public Integer getSoLuongDanhGia() {
        return soLuongDanhGia;
    }

    public void setSoLuongDanhGia(Integer soLuongDanhGia) {
        this.soLuongDanhGia = soLuongDanhGia;
    }

    public List<LichKhoiHanh> getLichKhoiHanhs() {
        return lichKhoiHanhs;
    }

    public void setLichKhoiHanhs(List<LichKhoiHanh> lichKhoiHanhs) {
        this.lichKhoiHanhs = lichKhoiHanhs;
    }
}
