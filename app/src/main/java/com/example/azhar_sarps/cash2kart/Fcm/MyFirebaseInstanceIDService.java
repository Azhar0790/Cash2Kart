package com.example.azhar_sarps.cash2kart.Fcm;

/**
 * Created by azhar-sarps on 11-Jun-17.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

/**
 * Created by Belal on 5/27/2016.
 */


//Class extending FirebaseInstanceIdService
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    SharedPreferences prefs;
    String prefName = "report";

    @Override
    public void onTokenRefresh() {

        //Getting registration token


        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        Log.d(TAG, "Refreshed token: " + refreshedToken);
//        Toast.makeText(getApplicationContext(),refreshedToken,Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putString("newtoken", refreshedToken).apply();
        sendRegistrationToServer(refreshedToken);
        sharedPreferences.edit().putBoolean("sentTokenToServer", false).apply();
    }




    private void sendRegistrationToServer(String refreshedToken) {
        //You can implement this method to store the token on your server
        //Not required for current project
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        sharedPreferences.edit().putBoolean("sentTokenToServer", true).apply();
    }




    // Fetch token here






}