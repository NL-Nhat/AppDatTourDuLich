package com.example.apptravel.data.models;

import java.io.Serializable;
import java.util.List;

public class DiemDen implements Serializable {

    private Integer maDiemDen;

    private String tenDiemDen;
    private String thanhPho;
    private String moTa;
    private String urlHinhAnh;

    public Integer getMaDiemDen() {
        return maDiemDen;
    }

    public void setMaDiemDen(Integer maDiemDen) {
        this.maDiemDen = maDiemDen;
    }

    public String getTenDiemDen() {
        return tenDiemDen;
    }

    public void setTenDiemDen(String tenDiemDen) {
        this.tenDiemDen = tenDiemDen;
    }

    public String getThanhPho() {
        return thanhPho;
    }

    public void setThanhPho(String thanhPho) {
        this.thanhPho = thanhPho;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getUrlHinhAnh() {
        return urlHinhAnh;
    }

    public void setUrlHinhAnh(String urlHinhAnh) {
        this.urlHinhAnh = urlHinhAnh;
    }

    @Override
    public String toString() {
        return tenDiemDen;
    }
}
