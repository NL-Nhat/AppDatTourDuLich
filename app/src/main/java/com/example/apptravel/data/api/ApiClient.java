package com.example.apptravel.data.api;

import android.content.Context;
import android.util.Log;
import com.example.apptravel.util.QuanLyDangNhap;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;

public class ApiClient {

    private static Retrofit retrofit;
    // URL của Render bạn đã deploy thành công
    private static final String CLOUD_URL = "https://backend-apptravel-api.onrender.com/";

    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            synchronized (ApiClient.class) {
                if (retrofit == null) {

                    // Cấu hình OkHttpClient với Interceptor và Timeout
                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(60, TimeUnit.SECONDS) // Tăng timeout cho Render "thức dậy"
                            .readTimeout(60, TimeUnit.SECONDS)
                            .writeTimeout(60, TimeUnit.SECONDS)
                            .addInterceptor(chain -> {
                                QuanLyDangNhap session = new QuanLyDangNhap(context);
                                String token = session.LayToken();
                                Request.Builder builder = chain.request().newBuilder();

                                // Tự động thêm Token vào Header nếu đã đăng nhập
                                if (token != null && !token.isEmpty()) {
                                    builder.addHeader("Authorization", "Bearer " + token);
                                }
                                return chain.proceed(builder.build());
                            }).build();

                    retrofit = new Retrofit.Builder()
                            .baseUrl(CLOUD_URL)
                            .client(client)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }

    // Hàm tiện ích để lấy URL ảnh đầy đủ từ Cloud
    public static String getFullImageUrl(String relativePath) {
        if (relativePath == null) return "";
        return CLOUD_URL + relativePath;
    }
}