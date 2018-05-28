package com.playtowin.hungryusapps.spinnergame;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.playtowin.hungryusapps.spinnergame.Dialogs.InternetAlert;
import com.playtowin.hungryusapps.spinnergame.Utils.ConnectionDetector;

public class SplashActivity extends AppCompatActivity
{
    private static final String TAG = "SplashActivity";
    private Context mContext = SplashActivity.this;

    ConnectionDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d(TAG, "onCreate: ");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        detector = new ConnectionDetector(mContext);
        if(detector.isConnected())
        {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    startActivity(new Intent(mContext,MainActivity.class));
                    finish();
                }
            },2000);
        }//if
        else
        {


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("No Internet Connection");
            builder.setPositiveButton("Exit", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    finish();
                }
            });
            builder.create();
            builder.show();


        }//else




    }//onCreate
}//SplashActivity
