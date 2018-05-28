package com.playtowin.hungryusapps.spinnergame.Utils;


import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ConnectionDetector
{
    private static final String TAG = "ConnectionDetector";
    public Context mContext ;

    public ConnectionDetector(Context mContext)
    {
        this.mContext = mContext;
    }

    public boolean isConnected()
    {
        Log.d(TAG, "isConnected: ");

        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Service.CONNECTIVITY_SERVICE);

        if(manager != null)
        {
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();

            if(networkInfo != null)
            {
                if(networkInfo.getState() == NetworkInfo.State.CONNECTED)
                {
                    return true;
                }//
            }//if


        }//if

        return false;

    }//isConnected


}//ConnectionDetector
