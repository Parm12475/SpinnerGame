package com.playtowin.hungryusapps.spinnergame.Utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.playtowin.hungryusapps.spinnergame.MainActivity;
import com.playtowin.hungryusapps.spinnergame.R;


public class MessagingService extends FirebaseMessagingService
{
    private static final String TAG = "MessagingService";
    
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        Log.d(TAG, "onMessageReceived: ");

        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();

        Log.d(TAG, "onMessageReceived: Title: "+title);
        Log.d(TAG, "onMessageReceived: Message: "+body);

        MyNotificationManager.getNotificationInstance(getApplicationContext())
                            .displayNotification(title,body);


        //sendNotification(title,body);

    }//onMessageReceived


    @Override
    public void onDeletedMessages()
    {

    }//onDeletedMessages


    private void sendNotification(String title , String messageBody)
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = Constants.CHANNEL_ID;
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        android.support.v4.app.NotificationCompat.Builder builder =
                                                new android.support.v4.app.NotificationCompat.Builder(this,channelId)
                                                .setSmallIcon(R.drawable.ic_notification_icon)
                                                .setContentTitle(title)
                                                .setContentText(messageBody)
                                                .setSound(defaultSoundUri)
                                                .setAutoCancel(true)
                                                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, builder.build());
    }

}//MessagingService
