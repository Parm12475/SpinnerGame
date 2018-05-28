package com.playtowin.hungryusapps.spinnergame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.playtowin.hungryusapps.spinnergame.Fragments.WithdrawalMethodFragment;
import com.playtowin.hungryusapps.spinnergame.Interface.OnBackPressedListener;

public class WithdrawalMethodActivity extends AppCompatActivity
{
    private static final String TAG = "WithdrawalMethodActivit";
    private String user_score;

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
        setContentView(R.layout.activity_withdrawal_method);

        init();
    }

    private void init()
    {
        Intent intent = getIntent();

        if(intent.hasExtra("user_score"))
        {
            try
            {
                Log.d(TAG, "init: Inflating Spinner Fragment");
                WithdrawalMethodFragment fragment = new WithdrawalMethodFragment();

                user_score = intent.getStringExtra("user_score");
                Bundle bundle = new Bundle();
                bundle.putString("user_score",user_score);
                fragment.setArguments(bundle);


                android.support.v4.app.FragmentTransaction transaction = WithdrawalMethodActivity.this.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container,fragment);
                transaction.addToBackStack(getString(R.string.withdrawal_method_fragment));
                transaction.commit();
            }//try
            catch(Exception ex)
            {
                Log.d(TAG, "init: ERROR: "+ex.getMessage());
            }//catch
        }//if


    }//init


    @Override
    public void onBackPressed()
    {
        Log.d(TAG, "onBackPressed: "+onBackPressedListener);

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
