package com.example.apptravel.data.models;

import com.google.gson.annotations.SerializedName;

public class DatTourHistoryItem {

    @SerializedName("maDatTour")
    private int maDatTour;

    @SerializedName("soNguoiLon")
    private int soNguoiLon;

    @SerializedName("soTreEm")
    private int soTreEm;

    @SerializedName("tongTien")
    private double tongTien;

    @SerializedName("trangThaiDatTour")
    private String trangThaiDatTour;

    @SerializedName("trangThaiThanhToan")
    private String trangThaiThanhToan;

    @SerializedName("ngayDat")
    private String ngayDat;

    @SerializedName("ngayHuy")
    private String ngayHuy;

    @SerializedName("lyDoHuy")
    private String lyDoHuy;

    @SerializedName("maLichKhoiHanh")
    private Integer maLichKhoiHanh;

    @SerializedName("ngayKhoiHanh")
    private String ngayKhoiHanh;

    @SerializedName("ngayKetThuc")
    private String ngayKetThuc;

    @SerializedName("maTour")
    private Integer maTour;

    @SerializedName("tenTour")
    private String tenTour;

    @SerializedName("diaDiem")
    private String diaDiem;

    @SerializedName("urlHinhAnhChinh")
    private String urlHinhAnhChinh;

    public int getMaDatTour() {
        return maDatTour;
    }

    public int getSoNguoiLon() {
        return soNguoiLon;
    }

    public int getSoTreEm() {
        return soTreEm;
    }

    public double getTongTien() {
        return tongTien;
    }

    public String getTrangThaiDatTour() {
        return trangThaiDatTour;
    }

    public String getTrangThaiThanhToan() {
        return trangThaiThanhToan;
    }

    public String getNgayDat() {
        return ngayDat;
    }

    public String getNgayHuy() {
        return ngayHuy;
    }

    public String getLyDoHuy() {
        return lyDoHuy;
    }

    public Integer getMaLichKhoiHanh() {
        return maLichKhoiHanh;
    }

    public String getNgayKhoiHanh() {
        return ngayKhoiHanh;
    }

    public String getNgayKetThuc() {
        return ngayKetThuc;
    }

    public Integer getMaTour() {
        return maTour;
    }

    public String getTenTour() {
        return tenTour;
    }

    public String getDiaDiem() {
        return diaDiem;
    }

    public String getUrlHinhAnhChinh() {
        return urlHinhAnhChinh;
    }
}
