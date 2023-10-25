package com.example.goclass;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private final static String BASE_URL = "http://ec2-43-202-167-120.ap-northeast-2.compute.amazonaws.com:3000";
    private static Retrofit retrofit = null;
    private RetrofitClient() {     }
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
