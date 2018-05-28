package com.playtowin.hungryusapps.spinnergame;


import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MyApplication extends Application
{

    private static final String TAG = "MyApplication";

    @Override
    public void onCreate()
    {
        super.onCreate();

        printHashKey();
    }

    private void printHashKey()
    {
        // Add code to print out the key hash
        try
        {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.playtowin.hungryusapps.spinnergame",
                    PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures)
            {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }//for
        }//try
        catch (PackageManager.NameNotFoundException e)
        {
            Log.d(TAG, "prinHashKey: Exception: "+e.getMessage());
        }
        catch (NoSuchAlgorithmException e)
        {
            Log.d(TAG, "prinHashKey: Exception: "+e.getMessage());
        }
    }
}
