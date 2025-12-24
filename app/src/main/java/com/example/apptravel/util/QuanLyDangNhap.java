package com.example.apptravel.util;

import android.content.Context;
import android.content.SharedPreferences;

public class QuanLyDangNhap {

    SharedPreferences pref;
    public QuanLyDangNhap(Context context) {
        pref = context.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
    }

    // Lưu trạng thái đăng nhập
    public void LuuDangNhap(String token, int maNguoiDung, boolean login, String hoTen, String email, String anhDaiDien,
                            String vaiTro, String soDienThoai, String diaChi, String gioiTinh, String ngaySinh) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("token", token);
        editor.putInt("maNguoiDung", maNguoiDung);
        editor.putBoolean("login", login);
        editor.putString("hoTen", hoTen);
        editor.putString("email", email);
        editor.putString("anhDaiDien", anhDaiDien);
        editor.putString("vaiTro", vaiTro);
        editor.putString("soDienThoai", soDienThoai);
        editor.putString("diaChi", diaChi);
        editor.putString("gioiTinh", gioiTinh);
        editor.putString("ngaySinh", ngaySinh);
        editor.commit();
    }


    public String LayToken() {
        return pref.getString("token", "");
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
    public void LuuAnhDaiDien(String tenFileAnh) {
        pref.edit().putString("anhDaiDien", tenFileAnh).apply();
    }
    public String LayVaiTro() {
        return pref.getString("vaiTro", "");
    }
    public int LayMaNguoiDung() {
        return pref.getInt("maNguoiDung", 0);
    }
    public String LaySoDienThoai() { return pref.getString("soDienThoai", ""); }
    public String LayDiaChi() { return pref.getString("diaChi", ""); }
    public String LayGioiTinh() { return pref.getString("gioiTinh", ""); }
    public String LayNgaySinh() { return pref.getString("ngaySinh", ""); }
    public String LayTrangThai() { return pref.getString("trangThai", ""); }



    // Đăng xuất
    public void DangXuat(){
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
}
