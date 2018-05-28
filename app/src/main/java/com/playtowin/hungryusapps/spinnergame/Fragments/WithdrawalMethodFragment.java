package com.playtowin.hungryusapps.spinnergame.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.playtowin.hungryusapps.spinnergame.Authentication.LoginActivity;
import com.playtowin.hungryusapps.spinnergame.Dialogs.PaypalAccountDetails;
import com.playtowin.hungryusapps.spinnergame.Dialogs.PaytmAccountDetails;
import com.playtowin.hungryusapps.spinnergame.R;
import com.playtowin.hungryusapps.spinnergame.Utils.BaseBackPressedListener;
import com.playtowin.hungryusapps.spinnergame.Utils.FirebaseMethods;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.playtowin.hungryusapps.spinnergame.WithdrawalMethodActivity;


public class WithdrawalMethodFragment extends Fragment
{

    private static final String TAG = "HistoryFragment";

    //firebase auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //firebase database
    FirebaseMethods firebaseMethods;

    //widgets
    private TextView mTotalCoins;
    private ImageView mBackArrow;
    private RadioGroup mRadioButton;
    //vars
    private String user_score = "";


    private void setupWidgets(View view)
    {
        Log.d(TAG, "setupWidgets: ");
        mTotalCoins = view.findViewById(R.id.totalCoins);
        mBackArrow = view.findViewById(R.id.backArrow);
        firebaseMethods = new FirebaseMethods(getContext());
        mRadioButton = view.findViewById(R.id.radio_group);

    }//setupWidgets

    private void handleListenerOnBackArrow()
    {
        Log.d(TAG, "handleListenerOnBackArrow: ");
        mBackArrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "onClick: Navigating Back");
                getActivity().finish();
            }//onClick
        });//BackArrow

    }//handleListenerOnBackArrow

    public WithdrawalMethodFragment()
    {

    }

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
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        ((WithdrawalMethodActivity)getActivity()).setOnBackPressedListener(new BaseBackPressedListener(getActivity()));

        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_withdrawal_method, container, false);

       setupFirebaseAuth();
       setupWidgets(view);
       handleListenerOnBackArrow();
       handleListenerOnRadioGroup();

       return view;

    }//onCreateView

    private void handleListenerOnRadioGroup()
    {
        mRadioButton.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {

                int radioBtnID = group.getCheckedRadioButtonId();
                View radioB = group.findViewById(radioBtnID);
                int position = group.indexOfChild(radioB);

                Log.d(TAG, "onCheckedChanged: Position: "+position);
                
                if(position == 0)
                {
                    Log.d(TAG, "onCheckedChanged: Paytm");
                    PaytmAccountDetails dialog = new PaytmAccountDetails();
                    dialog.show(getFragmentManager(),"PaytmAccountDetails");
                    dialog.setTargetFragment(WithdrawalMethodFragment.this,1);

                }//if
                else if(position == 1)
                {
                    Log.d(TAG, "onCheckedChanged: Paypal");
                    PaypalAccountDetails dialog = new PaypalAccountDetails();
                    dialog.show(getFragmentManager(),"PaypalAccountDetails");
                    dialog.setTargetFragment(WithdrawalMethodFragment.this,1);

                }//else
            }
        });
    }//handleListenerOnRadioGroup


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
            Intent intent = new Intent(getActivity(), LoginActivity.class);
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
        user_score = getUserScoreFromBundle();
        mTotalCoins.setText(user_score);

    }//onStart

    @Override
    public void onStop()
    {
        Log.d(TAG, "onStop()");
        super.onStop();
        if(mAuthListener != null)
            mAuth.removeAuthStateListener(mAuthListener);

    }//onStart



}
