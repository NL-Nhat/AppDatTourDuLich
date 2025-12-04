package com.example.apptravel.data.models;

public class LoginRequest {

    private String tenDangNhap;
    private String matKhau;

    public LoginRequest(String tenDangNhap, String matKhau) {
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
    }
}
