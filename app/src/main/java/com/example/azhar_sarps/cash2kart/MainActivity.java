package com.example.azhar_sarps.cash2kart;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.azhar_sarps.cash2kart.Interfaces.CategoryInterface;
import com.example.azhar_sarps.cash2kart.Interfaces.CategoryListInterface;
import com.example.azhar_sarps.cash2kart.Interfaces.InsertToken;
import com.example.azhar_sarps.cash2kart.fragment.MainFragment;
import com.example.azhar_sarps.cash2kart.pojo.CategoryListPojo;
import com.example.azhar_sarps.cash2kart.pojo.FcmTokenPojo;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {


    private TabLayout tabLayout;
    //This is our viewPager
    private ViewPager viewPager;
    TextView tv_no_internet;
    Call<List<CategoryListPojo>> call;
    List<CategoryListPojo> arrayList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tv_no_internet = (TextView) findViewById(R.id.tv_no_internet);
        tabLayout.setSmoothScrollingEnabled(true);
        tabLayout.setScrollPosition(0, 0f, true);
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        viewPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        String fcm_id = FirebaseInstanceId.getInstance().getToken();
        System.out.println("TOKEN:-" + fcm_id);
        insertTokenn(fcm_id);
    }

    private void setupViewPager(final ViewPager viewPager) {


        Retrofit retrofit = new Retrofit.Builder().baseUrl(ConfigUtil.MAIN_URL).addConverterFactory(GsonConverterFactory.create()).build();
        CategoryListInterface request = retrofit.create(CategoryListInterface.class);
        call = request.getJson();
        System.out.println("call :- " + call);


        if (CheckNetwork.isInternetAvailable(getApplicationContext())) //returns true if internet available
        {


            arrayList = new ArrayList<>();
            call.enqueue(new Callback<List<CategoryListPojo>>() {

                @Override
                public void onResponse(Call<List<CategoryListPojo>> call, Response<List<CategoryListPojo>> response) {
                    arrayList = response.body();
                    ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
                    for (int i = 0; i < arrayList.size(); i++) {
                        adapter.addFrag(new MainFragment(arrayList.get(i).getCatDesc()), arrayList.get(i).getDisplayNames());
                    }
                    viewPager.setAdapter(adapter);
//                    for(CategoryListPojo categoryListPojo: arrayList){
//                        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//                        System.out.println("Cart Desc :- " + categoryListPojo.getCatDesc());
//                        adapter.addFrag(new MainFragment(categoryListPojo.getCatDesc()), categoryListPojo.getCatDesc());
//                    }
                }

                @Override
                public void onFailure(Call<List<CategoryListPojo>> call, Throwable t) {
                    System.out.println("t :- " + t);
                }
            });


        } else {
            viewPager.setVisibility(View.GONE);
            tv_no_internet.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }


    public void insertTokenn(String token) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ConfigUtil.MAIN_URL).addConverterFactory(GsonConverterFactory.create()).build();
        InsertToken request = retrofit.create(InsertToken.class);
        System.out.println("Imei :- " +  getIMEI(MainActivity.this));
        Call<FcmTokenPojo> call = request.insertToken(token, getIMEI(MainActivity.this));

        call.enqueue(new Callback<FcmTokenPojo>() {

            @Override
            public void onResponse(Call<FcmTokenPojo> call, Response<FcmTokenPojo> response) {
            }

            @Override
            public void onFailure(Call<FcmTokenPojo> call, Throwable t) {
                System.out.println("t :- " + t);
            }
        });
    }

    public String getIMEI(Activity activity) {
        TelephonyManager telephonyManager = (TelephonyManager) activity
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String shareSubText = "WhatsApp - The Great Chat App";
                String shareBodyText = "https://play.google.com/store/apps/details?id=com.whatsapp&hl=en";
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubText);
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
                startActivity(Intent.createChooser(shareIntent, "Share With"));
                return true;
            case R.id.rating:
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.whatsapp&hl=en");
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.whatsapp&hl=en")));
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub


        AlertDialog.Builder alertbox = new AlertDialog.Builder(MainActivity.this);
        alertbox.setTitle(getResources().getString(R.string.app_name));
        alertbox.setMessage("Do you want to close the app?");
        alertbox.setPositiveButton(getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        finish();


                    }
                });
        alertbox.setNegativeButton(getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });

        alertbox.show();
    }
}
