package com.example.azhar_sarps.cash2kart.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.azhar_sarps.cash2kart.CategoryAdapter;
import com.example.azhar_sarps.cash2kart.CategoryCouponAdapter;
import com.example.azhar_sarps.cash2kart.CheckNetwork;
import com.example.azhar_sarps.cash2kart.ConfigUtil;
import com.example.azhar_sarps.cash2kart.Interfaces.CategoryCouponInterface;
import com.example.azhar_sarps.cash2kart.Interfaces.CategoryInterface;
import com.example.azhar_sarps.cash2kart.MainActivity;
import com.example.azhar_sarps.cash2kart.R;
import com.example.azhar_sarps.cash2kart.pojo.CategoryCouponPojo;
import com.example.azhar_sarps.cash2kart.pojo.CategoryPojo;
import com.example.azhar_sarps.cash2kart.pojo.CouponMessage;
import com.example.azhar_sarps.cash2kart.pojo.Message;

import java.util.List;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by sarps-preeti on 5/25/2017.
 */

public class MainFragment extends Fragment {
Context context;
    private RecyclerView rv_category;
    private RecyclerView.Adapter mAdapter;
    private CategoryCouponAdapter categoryCouponAdapter;
    private RecyclerView.LayoutManager layoutManager;
    String category;
    List<Message> arrayList;
    List<CouponMessage> arrayList_cp;
    Call<CategoryPojo> call;
    Call<CategoryCouponPojo> call_cp;
    View view;
    SwipeRefreshLayout swipeRefreshLayout;
    fr.castorflex.android.circularprogressbar.CircularProgressBar circularProgressBar;

    public MainFragment() {
    }

    public MainFragment(String category) {
        this.category = category;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, viewGroup, false);
        init();
        circularProgressBar.setVisibility(View.VISIBLE);
        circularProgressBar.setIndeterminate(true);



        if (CheckNetwork.isInternetAvailable(getActivity())) //returns true if internet available
        {
            if(category.equals("coupon")) {
                Retrofit retrofit = new Retrofit.Builder().baseUrl(ConfigUtil.MAIN_URL).addConverterFactory(GsonConverterFactory.create()).build();
                CategoryCouponInterface request = retrofit.create(CategoryCouponInterface.class);
                call_cp = request.getJson_coupon("coupon");
                grid_coupon();

                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        refreshItems_grid_coupon();
                    }
                });

            }else if(category.equals("deals")){
                Retrofit retrofit = new Retrofit.Builder().baseUrl(ConfigUtil.MAIN_URL).addConverterFactory(GsonConverterFactory.create()).build();
                CategoryInterface request = retrofit.create(CategoryInterface.class);
                call = request.getJson(category);
                linear_deals();
                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // Refresh items
                        refreshItems_grid();
                    }
                });
            }else{
                Retrofit retrofit = new Retrofit.Builder().baseUrl(ConfigUtil.MAIN_URL).addConverterFactory(GsonConverterFactory.create()).build();
                CategoryInterface request = retrofit.create(CategoryInterface.class);
                call = request.getJson(category);
                grid();
                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // Refresh items
                        refreshItems_grid();
                    }
                });
            }

        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
        return view;

    }

    public void init() {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        rv_category = (RecyclerView) view.findViewById(R.id.rv_category);
        circularProgressBar = (CircularProgressBar) view.findViewById(R.id.cb_progress_bar);
    }

    void refreshItems_grid() {
        mAdapter.notifyDataSetChanged();;
        onItemsLoadComplete();
    }
    void refreshItems_grid_coupon() {
        categoryCouponAdapter.notifyDataSetChanged();
        onItemsLoadComplete();
    }
    void onItemsLoadComplete() {
        swipeRefreshLayout.setRefreshing(false);
    }
    public void grid() {
        call.enqueue(new Callback<CategoryPojo>() {

            @Override
            public void onResponse(Call<CategoryPojo> call, Response<CategoryPojo> response) {
                circularProgressBar.setVisibility(View.GONE);
                CategoryPojo pojo = response.body();
                arrayList = pojo.getMessage();
                layoutManager = new LinearLayoutManager(getActivity());
                rv_category.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                mAdapter = new CategoryAdapter(getActivity(), arrayList, category);
                rv_category.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<CategoryPojo> call, Throwable t) {
                circularProgressBar.setVisibility(View.GONE);

            }
        });
    }
    public void linear_deals() {
        call.enqueue(new Callback<CategoryPojo>() {

            @Override
            public void onResponse(Call<CategoryPojo> call, Response<CategoryPojo> response) {
                circularProgressBar.setVisibility(View.GONE);
                CategoryPojo pojo = response.body();
                arrayList = pojo.getMessage();
                layoutManager = new LinearLayoutManager(getActivity());
                rv_category.setLayoutManager(layoutManager);
                mAdapter = new CategoryAdapter(getActivity(), arrayList, "deals");
                rv_category.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<CategoryPojo> call, Throwable t) {
                circularProgressBar.setVisibility(View.GONE);

            }
        });
    }
    public void grid_coupon() {
        call_cp.enqueue(new Callback<CategoryCouponPojo>() {
            @Override
            public void onResponse(Call<CategoryCouponPojo> call, Response<CategoryCouponPojo> response) {
                circularProgressBar.setVisibility(View.GONE);
                CategoryCouponPojo pojo = response.body();
                arrayList_cp = pojo.getMessage();
                layoutManager = new LinearLayoutManager(getActivity());
                rv_category.setLayoutManager(layoutManager);
                categoryCouponAdapter = new CategoryCouponAdapter(getActivity(), arrayList_cp);
                rv_category.setAdapter(categoryCouponAdapter);
            }

            @Override
            public void onFailure(Call<CategoryCouponPojo> call, Throwable t) {
                circularProgressBar.setVisibility(View.GONE);
            }
        });
    }

}
