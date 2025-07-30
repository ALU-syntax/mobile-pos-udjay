package com.udjaya.kasirudjay.api;

import com.udjaya.kasirudjay.model.Category;
import com.udjaya.kasirudjay.model.GetCategory;
import com.udjaya.kasirudjay.model.GetOpenBillStruk;
import com.udjaya.kasirudjay.model.notereceiptscheduler.GetActiveNoteReceiptScheduling;
import com.udjaya.kasirudjay.model.shiftorder.GetShiftOrderStruk;
import com.udjaya.kasirudjay.model.GetStruk;
import com.udjaya.kasirudjay.model.LogRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @GET("api-struk/{id}")
    Call<GetStruk> getStruk(@Path("id") String id);

    @GET("api-open-bill/{bill_id}")
    Call<GetOpenBillStruk> getOpenBillStrukOrder(@Path("bill_id") String id);

    @POST("api/logs")
    Call<Void> logError(@Body LogRequest logRequest);

    @GET("api-print-shift-detail/{petty_cash_id}")
    Call<GetShiftOrderStruk> getShiftOrderStruk(@Path("petty_cash_id") String id);

    @GET("get-all-category")
    Call<GetCategory> getAllCategory();

    @GET("getActiveNoteReceiptScheduling/{idOutlet}")
    Call<GetActiveNoteReceiptScheduling> getActiveNoteReceiptScheduling(@Path("idOutlet")String id);
}
