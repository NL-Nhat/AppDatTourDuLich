package com.example.apptravel.data.models;

public class RegisterRequest {
    public String tenDangNhap;
    public String matKhau;
    public String hoTen;
    public String email;
    public String soDienThoai;
    public String diaChi;
    public String ngaySinh;
    public String vaiTro;
    public String gioiTinh;



    public RegisterRequest(String user, String pass, String name, String mail, String phone) {
        this.tenDangNhap = user;
        this.matKhau = pass;
        this.hoTen = name;
        this.email = mail;
        this.soDienThoai = phone;
    }
    public RegisterRequest(String user, String pass, String name, String mail,
                           String phone, String address, String birthday, String role, String gender) {
        this.tenDangNhap = user;
        this.matKhau = pass;
        this.hoTen = name;
        this.email = mail;
        this.soDienThoai = phone;
        this.diaChi = address;
        this.ngaySinh = birthday;
        this.vaiTro = role;
        this.gioiTinh = gender;

    }
}