package com.example.apptravel.data.models;

import java.io.Serializable;

public class Tour implements Serializable{

    private Integer maTour;
    private String tenTour;
    private String moTa;
    private Double giaNguoiLon;
    private Double giaTreEm;
    private String urlHinhAnhChinh;
    private Double diemDanhGiaTrungBinh;
    private Integer soLuongDanhGia;
    private String trangThai;
    private DiemDen diemDen;

    public Integer getMaTour() {
        return maTour;
    }

    public void setMaTour(Integer maTour) {
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

    public String getUrlHinhAnhChinh() {
        return urlHinhAnhChinh;
    }

    public void setUrlHinhAnhChinh(String urlHinhAnhChinh) {
        this.urlHinhAnhChinh = urlHinhAnhChinh;
    }

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
}
