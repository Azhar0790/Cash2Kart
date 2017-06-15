package com.example.azhar_sarps.cash2kart.Interfaces;

import com.example.azhar_sarps.cash2kart.pojo.FcmTokenPojo;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by azhar-sarps on 11-Jun-17.
 */

public interface InsertToken {

    @POST("/insertToken.php")
    @FormUrlEncoded
    Call<FcmTokenPojo> insertToken(@Field("token") String token,@Field("imei") String imei);
}
