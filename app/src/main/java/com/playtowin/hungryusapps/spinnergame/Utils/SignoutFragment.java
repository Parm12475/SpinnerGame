package com.playtowin.hungryusapps.spinnergame.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.playtowin.hungryusapps.spinnergame.Authentication.LoginActivity;
import com.playtowin.hungryusapps.spinnergame.R;
import com.playtowin.hungryusapps.spinnergame.SignOutActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SignoutFragment extends android.support.v4.app.Fragment
{
    private static final String TAG = "SignoutFragment";

    //firebase auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private ProgressBar mProgressBar;
    private TextView tvSignOut;
    private Button btnConfirmSignout;
    private ImageView mBackArrow;

    private ProgressDialog dialog;

    public SignoutFragment()
    {

        Log.d(TAG, "SignoutFragment: ");
    }



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        Log.d(TAG, "onCreateView: Inflating Sign Out Layout");

        ((SignOutActivity)getActivity()).setOnBackPressedListener(new BaseBackPressedListener(getActivity()));

        View view = inflater.inflate(R.layout.fragment_signout,container,false);

        tvSignOut = view.findViewById(R.id.tvConfirmSignOut);
        btnConfirmSignout = view.findViewById(R.id.btnConfirmSignout);
        mBackArrow = view.findViewById(R.id.backArrow);

        setupFirebaseAuth();

        mBackArrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().finish();
            }
        });
        btnConfirmSignout.setOnClickListener(new View.OnClickListener()
        {


            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "onClick: Attempting to sign out ");
                mAuth.signOut();

            }//onClick
        });//btnConfirmSignout.setOnClickListener
        return view;
    }


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

    //----------------------------------------------Firebase------------------------------------------------------------

    //set up the firebase auth object
    private void setupFirebaseAuth()
    {
        Log.d(TAG, "setupFirebaseAuth() -> Setting Up Firebase Auth ");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                FirebaseUser user = mAuth.getCurrentUser();
                if(user != null)
                {//user is signed in

                    Log.d(TAG, "onAuthStateChanged: Signed In Used ID : "+user.getUid());

                }//if
                else
                {//User is signed out
                    Log.d(TAG, "onAuthStateChanged: Signed Out");
                    Log.d(TAG, "onAuthStateChanged: Navigating back to Login Activity");
                    Intent intent = new Intent(getActivity(),LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }//else

            }//onAuthStateChanged
        };

    }//setupFirebaseAuth


    @Override
    public void onStart()
    {
        Log.d(TAG, "onStart() ");
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);


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
