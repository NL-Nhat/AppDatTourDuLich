package com.example.apptravel.util;

import android.content.Context;
import android.content.SharedPreferences;

public class QuanLyDangNhap {

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public QuanLyDangNhap(Context context) {
        pref = context.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    // Lưu trạng thái đăng nhập
    public void LuuDangNhap(int maNguoiDung, Boolean login, String hoTen, String email, String anhDaiDien, String vaiTro){
        editor.putInt("maNguoiDung", maNguoiDung);
        editor.putBoolean("login", login);
        editor.putString("hoTen", hoTen);
        editor.putString("email", email);
        editor.putString("anhDaiDien", anhDaiDien);
        editor.putString("vaiTro", vaiTro);
        editor.apply();
    }

    // Kiểm tra đã đăng nhập chưa
    public boolean isLoggedIn(){
        return pref.getBoolean("login", false);
    }

    public String LayHoTen() {
        return pref.getString("hoTen", "");
    }

    public String LayEmail() {
        return pref.getString("email", "");
    }

    public String LayAnhDaiDien() {
        return pref.getString("anhDaiDien", "");
    }

    public String LayVaiTro() {
        return pref.getString("vaiTro", "");
    }
    public int LayMaNguoiDung() {
        return pref.getInt("maNguoiDung", 0);
    }

    // Đăng xuất
    public void DangXuat(){
        editor.clear();
        editor.apply();
    }
}
