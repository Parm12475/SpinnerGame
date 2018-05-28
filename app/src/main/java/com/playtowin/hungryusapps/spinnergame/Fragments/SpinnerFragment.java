package com.playtowin.hungryusapps.spinnergame.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.playtowin.hungryusapps.spinnergame.AboutActivity;
import com.playtowin.hungryusapps.spinnergame.Authentication.LoginActivity;
import com.playtowin.hungryusapps.spinnergame.ContactUsActivity;
import com.playtowin.hungryusapps.spinnergame.FeedbackActivity;
import com.playtowin.hungryusapps.spinnergame.MainActivity;
import com.playtowin.hungryusapps.spinnergame.Models.User;
import com.playtowin.hungryusapps.spinnergame.R;
import com.playtowin.hungryusapps.spinnergame.SignOutActivity;
import com.playtowin.hungryusapps.spinnergame.SpinnerActivity;
import com.playtowin.hungryusapps.spinnergame.TermsConditionsActivity;
import com.playtowin.hungryusapps.spinnergame.Utils.BaseBackPressedListener;
import com.playtowin.hungryusapps.spinnergame.Utils.BottomNavigationViewHelper;
import com.playtowin.hungryusapps.spinnergame.Utils.FirebaseMethods;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.Random;


public class SpinnerFragment extends Fragment
{

    private static final String TAG = "SpinnerFragment";
    private InterstitialAd interstitialAd;
    public static final String APP_PNAME = "com.playtowin.hungryusapps.spinnergame" ;

    private int counter = 0;



    public SpinnerFragment()
    {
        // Required empty public constructor
    }

    private int ACTIVITY_NUM = 1;

    private Button spin_button;
    private ImageView ic_wheel;
    private TextView mBackButton;

    private AdView mAdview;

    private Random r;
    private int degree = 0,degree_old=0;

    private long prev_score = 0;
    private  long spin_score = 0;

    //bcoz there are 37 sectors on wheel (9.72 degrees each)
    private static final float FACTOR = 4.86f;

    //firebase auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    //firebase database
    DatabaseReference myRef;
    FirebaseMethods firebaseMethods;

    private void getPointsCount()
    {
        Log.d(TAG, "getPointsCount: Getting Points from DB");

        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null)
        {
            Query query = myRef.child(getString(R.string.dbname_users))
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            query.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    long points = dataSnapshot.getValue(User.class).getScore();

                }//onDataChange

                @Override
                public void onCancelled(DatabaseError databaseError)
                {

                }//onCancelled
            });
        }//if


    }//getCoinsCount

    private void setupWidgets(View view)
    {
        myRef = FirebaseDatabase.getInstance().getReference();

        spin_button = view.findViewById(R.id.spin_button);
        ic_wheel = view.findViewById(R.id.ic_wheel);
        firebaseMethods = new FirebaseMethods(getContext());
        mBackButton = view.findViewById(R.id.backButton);
        mAdview = view.findViewById(R.id.adView);
        r = new Random();
    }//setupWidgets

    private void requestBannerAd()
    {
        Log.d(TAG, "requestBannerAd: Requesting Banner Ad...");
        //Remove .addTestDevice(AdRequest.DEVICE_ID_EMULATOR) from below line to test add on real device
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);
    }//requestBannerAd

    private void handleListenerOnSpinButton()
    {
        spin_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                degree_old = degree % 360;
                degree = r.nextInt(3600) + 720;

                RotateAnimation rotate = new RotateAnimation(degree_old,degree,
                        RotateAnimation.RELATIVE_TO_SELF,0.5f,
                        RotateAnimation.RELATIVE_TO_SELF,0.5f);

                rotate.setDuration(3600);
                rotate.setFillAfter(true);
                rotate.setInterpolator(new DecelerateInterpolator());
                rotate.setAnimationListener(new Animation.AnimationListener()
                {
                    @Override
                    public void onAnimationStart(Animation animation)
                    {
                        counter = counter + 1;
                    }//onAnimationStart

                    @Override
                    public void onAnimationEnd(Animation animation)
                    {
                        //String score = String.valueOf(currentNumber(360 - (degree % 360)));

                        String score = String.valueOf(getCurrentSpinScore(360 - (degree % 360)));
                        Log.d(TAG, "onAnimationEnd: Current Score is: "+score);
                        try
                        {
                            spin_score = Integer.parseInt(score);
                            Log.d(TAG, "onAnimationEnd: Spin Score: "+spin_score);

                            //fetch user score from db
                            Query query = myRef.child(getString(R.string.dbname_users))
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());



                            query.addListenerForSingleValueEvent(new ValueEventListener()
                            {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot)
                                {
                                    //prev_score = dataSnapshot.getValue(User.class).getScore();
                                    String score = dataSnapshot.child(getString(R.string.field_score)).getValue().toString();
                                    prev_score = Long.parseLong(score);
                                    Log.d(TAG, "onDataChange: Previous Score: "+prev_score);

                                }//onDataChange

                                @Override
                                public void onCancelled(DatabaseError databaseError)
                                {

                                }//onCancelled
                            });//addListenerForSingleValueEvent
                            //add user spin info to user_spins db
                            firebaseMethods.addUserSpinInfo(spin_score,"Spin Bonus");


                        }//try
                        catch(Exception ex)
                        {
                            Log.d(TAG, "onAnimationEnd: ERROR: "+ex.getMessage());
                            Log.d(TAG, "onAnimationEnd: Reason: "+ex.getCause());
                        }//catch


                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Congratulations! You Scored "+score);
                        builder.setMessage("Claim Your Points");
                        builder.setCancelable(false);
                        builder.setPositiveButton("IGNORE", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.cancel();

                            }//onClick
                        });

                        builder.setNegativeButton("CLAIM", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                Toast.makeText(getContext(), "Spins Claimed !!", Toast.LENGTH_SHORT).show();

                                //update user score in db
                                long final_score = prev_score + spin_score;
                                firebaseMethods.updateUserScore(final_score);

                                Log.d(TAG, "onClick: ###Counter: "+counter);

                                //creating interstitial add
                                if(counter == 2)
                                {
                                    //load interstital add

                                    interstitialAd.loadAd( new AdRequest.Builder().build());
                                    if(interstitialAd.isLoaded())
                                    {
                                        Log.d(TAG, "onClick: Interstitial Add Loaded...");
                                        interstitialAd.show();

                                        Log.d(TAG, "onClick: Interstitail Add Shown");

                                    }//if

                                    interstitialAd.setAdListener(new AdListener()
                                    {
                                        @Override
                                        public void onAdClosed()
                                        {
                                            super.onAdClosed();

                                        }
                                    });

                                    counter = 0;
                                }//if
                                else
                                {
                                    //counter = counter + 1;
                                }//else

                                
                                    

                            }//onClick
                        });

                        AlertDialog alert = builder.create();
                        alert.show();

                    }//onAnimationEnd

                    @Override
                    public void onAnimationRepeat(Animation animation)
                    {

                    }//onAnimationRepeat
                });//setAnimationListener

                ic_wheel.startAnimation(rotate);
            }//onClick
        });//setOnClickListener

    }//handleSpinListener



    //Responsible for Bottom Navigation View Setup
    private void setupBottomNavigationView(View view)
    {
        Log.d(TAG, "setupBottomNavigationView: setting Up Bottom Navigation View !");
        BottomNavigationViewEx bottomNavigationViewEx = view.findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(getContext(),getActivity(),bottomNavigationViewEx);


        int selectedItemId = bottomNavigationViewEx.getSelectedItemId();
        MenuItem selectedItem = bottomNavigationViewEx.getMenu().findItem(selectedItemId);

    }//setupBottomNavigationView



    private String getCurrentSpinScore(int deg)
    {
        String text="";

        if(deg >= 0 && deg < 30)
        {
            text="40";
        }//if

        if(deg >= 30 && deg < 60)
        {
            text="12";
        }//if

        if(deg >= 60 && deg < 90)
        {
            text="5";
        }//if

        if(deg >= 90 && deg < 120)
        {
            text="10";
        }//if

        if(deg >= 120 && deg < 150)
        {
            text="20";
        }//if

        if(deg >= 150 && deg < 180)
        {
            text="50";
        }//if

        if(deg >= 180 && deg < 210)
        {
            text="1";
        }//if

        if(deg >= 210 && deg < 240)
        {
            text="2";
        }//if

        if(deg >= 240 && deg < 270)
        {
            text="15";
        }//if

        if(deg >= 270 && deg < 300)
        {
            text="25";
        }//if

        if(deg >= 300 && deg <330)
        {
            text="0";
        }//if

        if(deg >= 330 && deg < 360)
        {
            text="400";
        }//if

        return text;
    }//getCurrentSpinScore






    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment

        ((SpinnerActivity)getActivity()).setOnBackPressedListener(new BaseBackPressedListener(getActivity()));

        View view = inflater.inflate(R.layout.fragment_spinner, container, false);

        setupFirebaseAuth();
        setupWidgets(view);

        interstitialAd = new InterstitialAd(getContext());
        interstitialAd.setAdUnitId(getString(R.string.test_admob_interstitial_id));

        requestBannerAd();
        setupBottomNavigationView(view);
        handleListenerOnSpinButton();
        return view;

    }//onCreateView



    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

    }

    @Override
    public void onDetach()
    {
        super.onDetach();

    }



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
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        }//if
    }//checkCurrentUser


    @Override
    public void onStart()
    {
        Log.d(TAG, "onStart() ");
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
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

}
