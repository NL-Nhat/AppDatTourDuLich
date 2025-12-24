package com.example.apptravel.data.models; // Package của Android chứ không phải Backend

public class RegisterRequest {
    public String tenDangNhap;
    public String matKhau;
    public String hoTen;

    public String email;

    public RegisterRequest(String user, String pass, String name, String mail) {
        this.tenDangNhap = user;
        this.matKhau = pass;
        this.hoTen = name;
        this.email = mail;
    }
}