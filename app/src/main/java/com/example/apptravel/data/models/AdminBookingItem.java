package com.example.apptravel.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * Mirror of backend DTO: com.example.travelappapi.dto.AdminBookingItemDTO
 */
public class AdminBookingItem {

    @SerializedName("maDatTour")
    private Integer maDatTour;

    @SerializedName("soNguoiLon")
    private Integer soNguoiLon;

    @SerializedName("soTreEm")
    private Integer soTreEm;

    @SerializedName("tongTien")
    private Double tongTien;

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

    @SerializedName("maNguoiDung")
    private Integer maNguoiDung;

    @SerializedName("hoTen")
    private String hoTen;

    @SerializedName("email")
    private String email;

    @SerializedName("soDienThoai")
    private String soDienThoai;

    @SerializedName("anhDaiDien")
    private String anhDaiDien;

    public Integer getMaDatTour() {
        return maDatTour;
    }

    public Integer getSoNguoiLon() {
        return soNguoiLon;
    }

    public Integer getSoTreEm() {
        return soTreEm;
    }

    public Double getTongTien() {
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

    public Integer getMaNguoiDung() {
        return maNguoiDung;
    }

    public String getHoTen() {
        return hoTen;
    }

    public String getEmail() {
        return email;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public String getAnhDaiDien() {
        return anhDaiDien;
    }
}
