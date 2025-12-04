package com.example.apptravel.data.api;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit;

    // Hàm lấy BASE_URL linh hoạt (máy ảo / máy thật)
    private static String getBaseUrl(Context context) {
        boolean isEmulator =
                Build.FINGERPRINT.contains("generic")
                        || Build.FINGERPRINT.contains("emulator")
                        || Build.MODEL.contains("Android SDK built for x86")
                        || Build.MANUFACTURER.contains("Genymotion")
                        || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                        || "google_sdk".equals(Build.PRODUCT);

        String EMULATOR_URL = "http://10.0.2.2:8080/";
        String DEVICE_URL = "http://192.168.1.20:8080/";

        Log.d("ApiClient", "isEmulator = " + isEmulator);

        return isEmulator ? EMULATOR_URL : DEVICE_URL;
    }



    // Hàm dùng để load ảnh (avatar, tour, hotel…)
    public static String getFullImageUrl(Context context, String relativePath) {
        return getBaseUrl(context) + relativePath;
    }

    public static Retrofit getClient(Context context) {
        String baseUrl = getBaseUrl(context);

        // Chuẩn hóa URL (đảm bảo có / cuối)
        if (!baseUrl.endsWith("/")) {
            baseUrl += "/";
        }

        if (retrofit == null || !retrofit.baseUrl().toString().equals(baseUrl)) {
            synchronized (ApiClient.class) {
                if (retrofit == null || !retrofit.baseUrl().toString().equals(baseUrl)) {
                    retrofit = new Retrofit.Builder()
                            .baseUrl(baseUrl)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }

}
