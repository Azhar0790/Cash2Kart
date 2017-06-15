package com.example.azhar_sarps.cash2kart.Interfaces;

import com.example.azhar_sarps.cash2kart.pojo.CategoryCouponPojo;
import com.example.azhar_sarps.cash2kart.pojo.CategoryPojo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by azhar-sarps on 31-May-17.
 */

public interface CategoryInterface {
    @GET("/storelist.php")
    Call<CategoryPojo> getJson(@Query("cat_name") String cat_name);

}
