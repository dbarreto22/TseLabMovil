package com.example.fakenews.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.example.fakenews.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service >>>>>>> ";

    public MyFirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
        super.onMessageReceived(remoteMessage);
        //Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getData());

        long[] pattern = {500,500,500,500,500};

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (remoteMessage.getData() != null) {

            Intent intent;
            intent = new Intent(this, MainActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            //NotificationManager notificationManager = new NotificationUtils(this).getManager();
            Notification notification = new Notification();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new NotificationCompat.Builder(this)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setContentTitle(remoteMessage.getData().get("title"))
                    .setContentText(remoteMessage.getData().get("body"))
                    .setChannelId("com.example.fakenews.ANDROID")
                    .setSmallIcon(com.google.firebase.R.drawable.notify_panel_notification_icon_bg)
                    .setAutoCancel(true)
                    .setVibrate(pattern)
                    .setLights(Color.BLUE, 1, 1)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .build();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    //Log.d(TAG, String.valueOf(notificationManager.getImportance()));

                //Log.d(TAG, "notificacion: " + notificationManager.getActiveNotifications().toString());
                }
            }

            NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
            manager.notify(new Random().nextInt(), notification);
            //manager.notify(0, notification);

            Log.d(TAG,notification.toString());
            Log.d(TAG, String.valueOf(manager.areNotificationsEnabled()));
            Log.d(TAG, String.valueOf(manager.getImportance()));



        }
    }
}
