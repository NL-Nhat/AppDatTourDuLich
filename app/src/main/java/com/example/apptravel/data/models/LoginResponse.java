package com.example.apptravel.data.models;

import java.time.LocalDate;

public class LoginResponse {

    private String token;
    private int maNguoiDung;
    private String hoTen;
    private String email;
    private String vaiTro;
    private String anhDaiDien;
    private String soDienThoai;
    private String diaChi;
    private String gioiTinh;
    private String ngaySinh;

    public String getToken() { return token; }
    public int getMaNguoiDung() { return maNguoiDung; }
    public String getHoTen() { return hoTen; }
    public String getEmail() { return email; }
    public String getVaiTro() { return vaiTro; }
    public String getAnhDaiDien() { return anhDaiDien; }
    public String getSoDienThoai() { return soDienThoai; }
    public String getDiaChi() { return diaChi; }
    public String getGioiTinh() { return gioiTinh; }
    public String getNgaySinh() { return ngaySinh; }
}
