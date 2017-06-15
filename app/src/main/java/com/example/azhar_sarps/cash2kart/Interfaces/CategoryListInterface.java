package com.example.azhar_sarps.cash2kart.Interfaces;

import com.example.azhar_sarps.cash2kart.pojo.CategoryListPojo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by azhar-sarps on 31-May-17.
 */

public interface CategoryListInterface {
    @GET("/catlist.php")
    Call<List<CategoryListPojo>> getJson();
}
