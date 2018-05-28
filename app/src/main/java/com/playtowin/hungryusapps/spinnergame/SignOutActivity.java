package com.playtowin.hungryusapps.spinnergame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.playtowin.hungryusapps.spinnergame.Interface.OnBackPressedListener;
import com.playtowin.hungryusapps.spinnergame.Utils.SignoutFragment;

public class SignOutActivity extends AppCompatActivity
{

    private static final String TAG = "SignOutActivity";

    protected OnBackPressedListener onBackPressedListener;

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener)
    {
        Log.d(TAG, "setOnBackPressedListener: ");
        this.onBackPressedListener = onBackPressedListener;
    }//setOnBackPressedListener


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_out);

        init();
    }//onCreate

    private void init()
    {
        SignoutFragment fragment = new SignoutFragment();
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container,fragment);
        transaction.addToBackStack(getString(R.string.fragment_signout));
        transaction.commit();
    }//init

    @Override
    public void onBackPressed()
    {
        Log.d(TAG, "onBackPressed: ");

        if (onBackPressedListener != null)
        {
            Log.d(TAG, "onBackPressed: #####");
            finish();
            onBackPressedListener.doBack();
        }
        else
            super.onBackPressed();

    }//onBackPressed
}
