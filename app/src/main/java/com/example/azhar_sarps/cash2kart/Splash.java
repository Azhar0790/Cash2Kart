package com.example.azhar_sarps.cash2kart;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by azhar-sarps on 13-Jun-17.
 */

public class Splash extends AppCompatActivity {
    public final String[] PERMISSION_ALL = {
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.INTERNET
    };
    public final int PERMISSION_REQUEST_CODE = 100;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(Splash.this, PERMISSION_ALL, PERMISSION_REQUEST_CODE);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        },3000);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(Splash.this, "Permissions denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }
}
