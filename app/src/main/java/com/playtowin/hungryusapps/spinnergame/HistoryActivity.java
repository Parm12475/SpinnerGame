package com.playtowin.hungryusapps.spinnergame;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.playtowin.hungryusapps.spinnergame.Authentication.LoginActivity;
import com.playtowin.hungryusapps.spinnergame.Fragments.HistoryFragment;
import com.playtowin.hungryusapps.spinnergame.Interface.OnBackPressedListener;
import com.playtowin.hungryusapps.spinnergame.Models.UserSpins;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class HistoryActivity extends AppCompatActivity
{
    private static final String TAG = "HistoryActivity";
    private Context mContext = HistoryActivity.this;

    protected OnBackPressedListener onBackPressedListener;

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener)
    {
        Log.d(TAG, "setOnBackPressedListener: ");
        this.onBackPressedListener = onBackPressedListener;
    }//setOnBackPressedListener

    //firebase auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //firebase database
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        setupFirebaseAuth();
        init();

    }//onCreate

    private void init()
    {
        try
        {
            Intent intent = getIntent();
            String user_score  = intent.getStringExtra("user_score");
            Log.d(TAG, "init: User Score from MAINACTIVITY: "+user_score);

              if(intent.hasExtra("user_score"))
              {
                  Log.d(TAG, "init: Inflating Spinner Fragment");
                  HistoryFragment fragment = new HistoryFragment();

                  Bundle bundle = new Bundle();
                  bundle.putString("user_score",user_score);
                  fragment.setArguments(bundle);

                  android.support.v4.app.FragmentTransaction transaction = HistoryActivity.this.getSupportFragmentManager().beginTransaction();
                  transaction.replace(R.id.container,fragment);
                  transaction.addToBackStack(getString(R.string.history_fragment));
                  transaction.commit();
              }//if
        }//try
        catch(Exception ex)
        {
            Log.d(TAG, "init: ERROR: "+ex.getMessage());
        }//catch
    }//init

    private void readUserEarningDetails()
    {
        myRef = FirebaseDatabase.getInstance().getReference();

        Query query = myRef.child(getString(R.string.dbname_user_spins))
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        query.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren())
                {
                    String src = singleSnapshot.getValue(UserSpins.class).getSource();
                    String date = singleSnapshot.getValue(UserSpins.class).getDate();
                    String score = singleSnapshot.getValue(UserSpins.class).getScore();

                    Log.d(TAG, "onDataChange: "+src+" "+date+" "+score);
                }//for
            }//onDataChange

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }//onCancelled
        });//addValueEventListener
    }//readUserEarningDetails



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
            Intent intent = new Intent(mContext, LoginActivity.class);
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

    }//onStart

    @Override
    public void onStop()
    {
        Log.d(TAG, "onStop()");
        super.onStop();
        if(mAuthListener != null)
            mAuth.removeAuthStateListener(mAuthListener);

    }//onStart


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



}//HistoryActivity
