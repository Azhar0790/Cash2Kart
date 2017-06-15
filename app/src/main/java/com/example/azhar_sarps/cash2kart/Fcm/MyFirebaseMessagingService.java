package com.example.azhar_sarps.cash2kart.Fcm;

/**
 * Created by azhar-sarps on 11-Jun-17.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.example.azhar_sarps.cash2kart.MainActivity;

import com.example.azhar_sarps.cash2kart.R;
import com.example.azhar_sarps.cash2kart.WebviewActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by Belal on 5/27/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    String data;
    String msg;
    Bitmap remote_picture=null;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();


        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.v(TAG, "From: " + remoteMessage.getFrom());


        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.v(TAG, "Message data payload:- " + remoteMessage.getData());

            if (data.get("image_url").isEmpty()) {
                showNotification(data.get("message"), data.get("url"));
            } else {
                showNotification_withImage(data.get("message"), "https://cash2kart.com/upload/app_deals/" + data.get("image_url"), data.get("url"));
            }


            System.out.println("https://cash2kart.com/upload/app_deals/" + data.get("image_url"));

        }

        if (remoteMessage.getNotification() != null) {
            Log.v(TAG, "Message Notification Body:- " + remoteMessage.getNotification().getBody());
            System.out.println("Message :- " + remoteMessage.getNotification().getBody());
        }


    }

    private void showNotification_withImage(String message, String imageurl, String url) {


        if (!TextUtils.isEmpty(message)) {

            if (imageurl != null) {
                Intent intent = new Intent(this, WebviewActivity.class);
                intent.putExtra("url", url);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);

                NotificationCompat.BigPictureStyle s = new NotificationCompat.BigPictureStyle().bigPicture(getBitmapfromUrl(imageurl));
                s.setSummaryText(message);
//                NotificationCompat.BigPictureStyle notiStyle = new NotificationCompat.BigPictureStyle();
//                notiStyle.setSummaryText(intent.getExtras().getString(message));

                try {
                    remote_picture = BitmapFactory.decodeStream((InputStream) new URL(intent.getExtras().getString(imageurl)).getContent());
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                notiStyle.bigPicture(remote_picture);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.logo)
                        .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.logo))
                        .setContentTitle("Online shopping apps")
                        .setContentText(message)
                        .setStyle(s)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                        .setContentIntent(pendingIntent);

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(0, notificationBuilder.build());




            } else {
                Intent intent = new Intent(this, WebviewActivity.class);
                intent.putExtra("url", url);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);

                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.logo)
                        .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.logo))
                        .setContentTitle("Online shopping apps")
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                        .setContentIntent(pendingIntent);

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(0, notificationBuilder.build());
            }
        }
    }

    private void showNotification(String message, String url) {


        if (!TextUtils.isEmpty(message)) {


            Intent intent = new Intent(this, WebviewActivity.class);
            intent.putExtra("url", url);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.logo)
                    .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.logo))
                    .setContentTitle("Online shopping apps")
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, notificationBuilder.build());
        }
    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }
}

