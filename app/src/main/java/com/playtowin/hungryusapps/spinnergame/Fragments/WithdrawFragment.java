package com.playtowin.hungryusapps.spinnergame.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.playtowin.hungryusapps.spinnergame.Authentication.LoginActivity;
import com.playtowin.hungryusapps.spinnergame.R;
import com.playtowin.hungryusapps.spinnergame.Utils.BaseBackPressedListener;
import com.playtowin.hungryusapps.spinnergame.Utils.BottomNavigationViewHelper;
import com.playtowin.hungryusapps.spinnergame.Utils.FirebaseMethods;
import com.playtowin.hungryusapps.spinnergame.WithdrawActivity;
import com.playtowin.hungryusapps.spinnergame.WithdrawalMethodActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;


public class WithdrawFragment extends Fragment
{

    private static final String TAG = "WithdrawFragment";
    private final double MIN_LIMIT = .00006;
    private  double USD = 0;
    private int user_percentage;

    private int ACTIVITY_NUM = 2;

    public WithdrawFragment()
    {
        // Required empty public constructor
    }

    private TextView mTotalUsd;
    private CardView mWithdraw;
    private String user_score ="";
    private TextView mPercent;

    //firebase auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //firebase database
    DatabaseReference myRef;
    FirebaseMethods firebaseMethods;



    private void setupWidgets(View view)
    {
        myRef = FirebaseDatabase.getInstance().getReference();
        //mTotalCoins = view.findViewById(R.id.totalCoins);
        mTotalUsd = view.findViewById(R.id.totalUsd);
        mWithdraw = view.findViewById(R.id.withdraw_btn);
        firebaseMethods = new FirebaseMethods(getContext());
        mPercent = view.findViewById(R.id.user_percent);
    }//setupWidgets


    private void convertCoinsIntoPercent()
    {
       double percent =(USD/20)*100;
       user_percentage = (int)percent;
        Log.d(TAG, "convertCoinsIntoPercent: User Percentage: "+user_percentage);
        Log.d(TAG, "convertCoinsIntoPercent: Percentage of user payment : "+percent);

        String target = "You have reached "+user_percentage+"% of your payment thershold.";
        mPercent.setText(target);
    }//convertCoinsIntoPercent

    private void handleListenerOnWithdrawButton()
    {
       mWithdraw.setOnClickListener(new View.OnClickListener()
       {
           @Override
           public void onClick(View v)
           {
               try
               {
                   long points = Integer.parseInt(user_score);
                   if(points < 15000)
                   {//insuffient funds

                       Log.d(TAG, "onClick: Sorry You have Insufficient Funds");
//                       InsufficientFundDialog dialog = new InsufficientFundDialog();
//                       dialog.show(getFragmentManager(),"InsufficientFundDialog");
//                       dialog.setTargetFragment(WithdrawFragment.this,1);

                       android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
                       builder.setTitle("Sorry! you have insufficient funds");
                       builder.setMessage("Minimum 15,000 points required");
                       builder.setCancelable(false);
                       builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener()
                       {
                           @Override
                           public void onClick(DialogInterface dialog, int which)
                           {
                               dialog.cancel();

                           }//onClick
                       });

                       android.support.v7.app.AlertDialog alert = builder.create();
                       alert.show();

                   }//if
                   else
                   {//opt for withdrawal method
                       Log.d(TAG, "onClick: Navigating to Withdrawal Method !!");
                       Intent intent = new Intent( new Intent(getActivity(), WithdrawalMethodActivity.class));
                       intent.putExtra("user_score",String .valueOf(user_score));
                       startActivity(intent);
                       getActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

                   }//else
               }//try
               catch(Exception ex)
               {
                   Log.d(TAG, "handleListenerOnWithdrawButton: ERROR: "+ex.getStackTrace());
               }//catch
           }//onClick
       });
    }//handleListenerOnWithdrawButton

    //Responsible for Bottom Navigation View Setup
    private void setupBottomNavigationView(View view)
    {
        Log.d(TAG, "setupBottomNavigationView: setting Up Bottom Navigation View !");
        BottomNavigationViewEx bottomNavigationViewEx = view.findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(getContext(),getActivity(),bottomNavigationViewEx);
//
//        Menu menu = bottomNavigationViewEx.getMenu();
//        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
//        menuItem.setChecked(true);


        int selectedItemId = bottomNavigationViewEx.getSelectedItemId();
        MenuItem selectedItem = bottomNavigationViewEx.getMenu().findItem(selectedItemId);

    }//setupBottomNavigationView



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

        ((WithdrawActivity)getActivity()).setOnBackPressedListener(new BaseBackPressedListener(getActivity()));


        View view = inflater.inflate(R.layout.fragment_withdraw, container, false);

        setupFirebaseAuth();
        setupWidgets(view);
        handleListenerOnWithdrawButton();
        setupBottomNavigationView(view);

        return view;
    }

    private void convertUserScoreToUSD()
    {
        Log.d(TAG, "convertPointsToUSD: Converting Points to USD!");

        try
        {
            int points = Integer.parseInt(user_score);
            double usd = points * MIN_LIMIT;
            USD = usd;
            String usd_string = String.valueOf(usd);
            usd_string = "$"+usd_string.substring(0,5);
            Log.d(TAG, "convertPointsToUSD: Total USD: "+usd_string);

            convertCoinsIntoPercent();

            mTotalUsd.setText(usd_string);
        }//try
        catch(Exception ex)
        {
            Log.d(TAG, "convertPointsToUSD: IntegerParseException: "+ex.getStackTrace().toString());
        }//catch

    }//convertPointsToUSD

    private String getUserScoreFromBundle()
    {
        Log.d(TAG, "getUserScoreFromBundle: ");
        Bundle bundle = this.getArguments();
        if(bundle != null)
        {
            return bundle.getString("user_score");
        }
        else
        {
            return null;
        }
    }//getUserScoreFromBundle




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
        //checkCurrentUser(mAuth.getCurrentUser());
        //mTotalCoins.setText("wait...");
        user_score = getUserScoreFromBundle();
        //mTotalCoins.setText(user_score);
        convertUserScoreToUSD();

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
