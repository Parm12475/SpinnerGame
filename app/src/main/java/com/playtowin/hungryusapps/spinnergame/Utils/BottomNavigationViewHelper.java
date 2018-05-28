package com.playtowin.hungryusapps.spinnergame.Utils;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.playtowin.hungryusapps.spinnergame.ContactUsActivity;
import com.playtowin.hungryusapps.spinnergame.MainActivity;
import com.playtowin.hungryusapps.spinnergame.R;
import com.playtowin.hungryusapps.spinnergame.SignOutActivity;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class BottomNavigationViewHelper
{

    private static final String TAG = "BottomNavigationViewHel";
    public static final String APP_NAME = "com.playtowin.hungryusapps.spinnergame" ;

    public static void setupNavigationView(BottomNavigationViewEx bottomNavigationViewEx)
    {
        Log.d(TAG, "setupNavigationView: BottomNavigationViewEx Object: ");
       try
       {
           Log.d(TAG, "setupNavigationView()");
           bottomNavigationViewEx.enableAnimation(false);
           bottomNavigationViewEx.enableItemShiftingMode(false);
           bottomNavigationViewEx.enableShiftingMode(false);
           bottomNavigationViewEx.setTextVisibility(true);

       }//try
        catch(Exception ex)
        {
            Log.d(TAG, "setupNavigationView: EXCEPTION: "+ex.getCause());
        }//catch
        //bottomNavigationViewEx.setItemTextColor(true);
    }//setupNavigationView

    public static void enableNavigation(final Context context, final Activity callingActivity, BottomNavigationViewEx bottomNavigationViewEx)
    {
        Log.d(TAG, "enableNavigation()");
        // use the unchecked color for first item

        bottomNavigationViewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.ic_home:
                        Intent intent1 = new Intent(context, MainActivity.class);
                        context.startActivity(intent1);
                        callingActivity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                        break;
                    case R.id.ic_share:
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        String shareBody = "Play and you can win unlimited prizes worth $1000";
                        String shareSub = "Free 5000 coins on aoo download." +
                                "Get it here: https://play.google.com/store/apps/details?id=com.playtowin.hungryusapps.spinnergame";
                        intent.putExtra(android.content.Intent.EXTRA_SUBJECT,shareBody);
                        intent.putExtra(android.content.Intent.EXTRA_TEXT,shareSub);
                        context.startActivity(Intent.createChooser(intent,"Share Using"));
                        callingActivity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                        break;
                    case R.id.ic_rate_us:
                        //context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_NAME)));
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.playtowin.hungryusapps.spinnergame")));
                        callingActivity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                        break;
                    case R.id.ic_contact_us:
                        Intent intent4 = new Intent(context, ContactUsActivity.class);
                        context.startActivity(intent4);
                        callingActivity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                        break;
                    case R.id.ic_logout:
                        Intent intent5 = new Intent(context, SignOutActivity.class);
                        context.startActivity(intent5);
                        callingActivity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                        break;

                }//switch

                return false;
            }//onNavigationItemSelected
        });//setOnNavigationItemSelectedListener
    }//enableNavigation
}//BottomNavigationViewHelper
