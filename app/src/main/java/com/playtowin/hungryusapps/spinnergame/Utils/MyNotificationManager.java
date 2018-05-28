package com.playtowin.hungryusapps.spinnergame.Utils;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.playtowin.hungryusapps.spinnergame.MainActivity;
import com.playtowin.hungryusapps.spinnergame.R;

public class MyNotificationManager
{

    private Context mContext;
    private static MyNotificationManager myNotificationManager;


    private MyNotificationManager(Context context)
    {
        mContext = context;
    }//MyNotificationManager


    public static synchronized MyNotificationManager getNotificationInstance(Context context)
    {
        if(myNotificationManager == null)
            myNotificationManager = new MyNotificationManager(context);

        return myNotificationManager;
    }//getNotificationInstance

    public void displayNotification(String title,String body)
    {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext,Constants.CHANNEL_ID)
                                                .setSmallIcon(R.drawable.ic_notification_icon)
                                                .setContentTitle(title)
                                                .setContentText(body);

        Intent intent = new Intent(mContext, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext,0,
                                                            intent,PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(pendingIntent);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(defaultSoundUri);

        NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if(manager != null)
        {
            manager.notify(1,mBuilder.build());
        }//if


    }//displayNotification

}//MyNotificationManager
