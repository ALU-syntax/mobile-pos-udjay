package com.udjaya.kasirudjay.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RClient {
    private static Retrofit retrofit;
//    private static  String BASE_URL = "https://udjaya.neidra.my.id";
    private static  String BASE_URL = "https://backoffice.uddjaya.com";


    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
