package com.playtowin.hungryusapps.spinnergame;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.playtowin.hungryusapps.spinnergame.Authentication.LoginActivity;
import com.playtowin.hungryusapps.spinnergame.Fragments.FeedbackFragment;
import com.playtowin.hungryusapps.spinnergame.Interface.OnBackPressedListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FeedbackActivity extends AppCompatActivity
{
    private static final String TAG = "FeedbackActivity";
    private Context mContext = FeedbackActivity.this;

    //firebase auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;



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
        setContentView(R.layout.activity_feedback);

        setupFirebaseAuth();

        init();

    }//onCreate

    private void init()
    {
        FeedbackFragment fragment = new FeedbackFragment();

        FragmentTransaction transaction = FeedbackActivity.this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container,fragment);
        transaction.addToBackStack(getString(R.string.fragment_feedback));
        transaction.commit();
    }//init




    /*----------------------------------------------Firebase------------------------------------------------------------ */


    //set up the firebase auth object
    private void setupFirebaseAuth()
    {
        Log.d(TAG, "setupFirebaseAuth()-> Setting Up Firebase Authentication");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                FirebaseUser user = mAuth.getCurrentUser();
                Log.d(TAG, "onAuthStateChanged: CurrentUser : "+user);
                Log.d(TAG, "onAuthStateChanged: ");
                //check if user is logged in
                checkCurrentUser(user);

                if(user != null)
                {//user is signed in
                    Log.d(TAG, "onAuthStateChanged() -> Signed In Used ID : "+user.getUid());
                }//if
                else
                {//User is signed out
                    Log.d(TAG, "onAuthStateChanged() -> Signed Out");
                }//else

            }//onAuthStateChanged
        };

    }//setupFirebaseAuth

    private void checkCurrentUser(FirebaseUser user)
    {
        Log.d(TAG, "checkCurrentUser() -> Checking if user is logged in ");
        if(user != null)
            Log.d(TAG, "checkCurrentUser() -> Current UserID : "+user.getUid());
        if(user == null)
        {
            Intent intent = new Intent(FeedbackActivity.this, LoginActivity.class);
            startActivity(intent);
        }//if
    }//checkCurrentUser


    @Override
    public void onStart()
    {
        Log.d(TAG, "onStart() ");
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        checkCurrentUser(mAuth.getCurrentUser());
//        user_score = getUserScoreFromBundle();

    }//onStart

    @Override
    public void onStop()
    {
        Log.d(TAG, "onStop()");
        super.onStop();
        if(mAuthListener != null)
            mAuth.removeAuthStateListener(mAuthListener);
    }//onStop


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




}//FeedbackActivity
