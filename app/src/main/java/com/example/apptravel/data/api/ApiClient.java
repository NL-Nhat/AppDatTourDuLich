package com.example.apptravel.data.api;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.example.apptravel.util.QuanLyDangNhap;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit;

    // Hàm lấy BASE_URL linh hoạt (máy ảo / máy thật)
    private static String getBaseUrl(Context context) {
        boolean isEmulator =
                Build.FINGERPRINT.startsWith("generic")
                        || Build.FINGERPRINT.toLowerCase().contains("emulator")
                        || Build.HARDWARE.contains("ranchu")
                        || Build.HARDWARE.contains("goldfish");

        String EMULATOR_URL = "http://10.0.2.2:8080/";
        String DEVICE_URL = "http://192.168.1.87:8080/";

        Log.d("ApiClient", "isEmulator = " + isEmulator);

        return isEmulator ? EMULATOR_URL : DEVICE_URL;
    }


    // Hàm dùng để load ảnh (avatar, tour, hotel…)
    public static String getFullImageUrl(Context context, String relativePath) {
        return getBaseUrl(context) + relativePath;
    }

    public static Retrofit getClient(Context context) {
        String baseUrl = getBaseUrl(context);
        // Chuẩn hóa URL
        if (!baseUrl.endsWith("/")) baseUrl += "/";

        if (retrofit == null || !retrofit.baseUrl().toString().equals(baseUrl)) {
            synchronized (ApiClient.class) {
                if (retrofit == null || !retrofit.baseUrl().toString().equals(baseUrl)) {

                    // Giữ lại Interceptor từ code cũ để bảo mật
                    okhttp3.OkHttpClient client = new okhttp3.OkHttpClient.Builder()
                            .addInterceptor(chain -> {
                                okhttp3.Request originalRequest = chain.request();
                                if (originalRequest.header("No-Authentication") != null) {
                                    okhttp3.Request newRequest = originalRequest.newBuilder()
                                            .removeHeader("No-Authentication")
                                            .build();
                                    return chain.proceed(newRequest);
                                }
                                QuanLyDangNhap session = new QuanLyDangNhap(context);
                                String token = session.LayToken();
                                okhttp3.Request.Builder builder = chain.request().newBuilder();
                                if (token != null && !token.isEmpty()) {
                                    builder.addHeader("Authorization", "Bearer " + token);
                                }
                                return chain.proceed(builder.build());
                            }).build();

                    // Tạo Retrofit với đầy đủ tính năng
                    retrofit = new Retrofit.Builder()
                            .baseUrl(baseUrl)
                            .client(client) // Rất quan trọng
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }
}
