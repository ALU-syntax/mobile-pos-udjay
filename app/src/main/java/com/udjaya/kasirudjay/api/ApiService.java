package com.udjaya.kasirudjay.api;

import com.udjaya.kasirudjay.model.GetStruk;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    @GET("api-struk/{id}")
    Call<GetStruk> getStruk(@Path("id") String id);
}
