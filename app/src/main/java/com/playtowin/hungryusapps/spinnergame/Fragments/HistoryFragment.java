package com.playtowin.hungryusapps.spinnergame.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.playtowin.hungryusapps.spinnergame.Authentication.LoginActivity;
import com.playtowin.hungryusapps.spinnergame.HistoryActivity;
import com.playtowin.hungryusapps.spinnergame.Models.UserSpins;
import com.playtowin.hungryusapps.spinnergame.R;
import com.playtowin.hungryusapps.spinnergame.Utils.BaseBackPressedListener;
import com.playtowin.hungryusapps.spinnergame.Utils.FirebaseMethods;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;


public class HistoryFragment extends Fragment
{

    private static final String TAG = "HistoryFragment";

    //firebase auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    //firebase database
    DatabaseReference myRef;
    FirebaseMethods firebaseMethods;

    FirebaseRecyclerAdapter<UserSpins,UserSpinViewHolder> FBRA;
    FirebaseRecyclerOptions<UserSpins> options;


    //widgets
    private ImageView mBackArrow;
    private RecyclerView mRecyclerView;
    private TextView mPoints;


    //vars
    private List<UserSpins> mList;  //data for recycler view
    private  String mUserScore = "";

    public HistoryFragment()
    {

    }


    private void setupWidgets(View view)
    {
        Log.d(TAG, "setupWidgets: Setting Widgets...");
        mBackArrow = view.findViewById(R.id.backArrow);
        mList = new ArrayList<>();
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mPoints = view.findViewById(R.id.points);
    }//setupWidgets

    private void handleListenerOnBackArrow()
    {
        Log.d(TAG, "handleListenerOnBackArrow: ");

        mBackArrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().finish();
            }//onClick
        });//setOnClickListener
    }//handleListenerOnBackArrow



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
        Log.d(TAG, "onCreateView: ");


        ((HistoryActivity)getActivity()).setOnBackPressedListener(new BaseBackPressedListener(getActivity()));

        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_history, container, false);
       setupFirebaseAuth();
       setupWidgets(view);
       mPoints.setText(mUserScore);
       handleListenerOnBackArrow();

        return view;
    }


    private void readUserEarningDetails()
    {
        Log.d(TAG, "readUserEarningDetails: ");
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        myRef = FirebaseDatabase.getInstance().getReference();
        Query query = myRef.child(getString(R.string.dbname_user_spins))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        options =
                new FirebaseRecyclerOptions.Builder<UserSpins>()
                        .setQuery(query,UserSpins.class)
                        .build();



        FBRA = new FirebaseRecyclerAdapter<UserSpins, UserSpinViewHolder>(options)
        {
            @Override
            public UserSpinViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
            {
                Log.d(TAG, "onCreateViewHolder: ");
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                View view = inflater.inflate(R.layout.recycler_view_layout,parent,false);
                return new UserSpinViewHolder(view);
            }//onCreateViewHolder

            @Override
            protected void onBindViewHolder(UserSpinViewHolder holder, int position, UserSpins model)
            {
                Log.d(TAG,"onBindViewHolder()");
                holder.mSource.setText(FBRA.getItem(  (FBRA.getItemCount() - 1) - position).getSource());
                holder.mDate.setText(FBRA.getItem((FBRA.getItemCount() - 1) - position).getDate());
                holder.mScore.setText(FBRA.getItem((FBRA.getItemCount() - 1)- position).getScore());

            }//onBindViewHolder


        };//FirebaseRecyclerAdapter



        FBRA.startListening();
        mRecyclerView.setAdapter(FBRA);

    }//readUserEarningDetails


    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

    }//onAttach

    @Override
    public void onDetach()
    {
        super.onDetach();

    }//onDetach



    public static class UserSpinViewHolder extends RecyclerView.ViewHolder
    {
        TextView mSource;
        TextView mDate;
        TextView mScore;

        public UserSpinViewHolder(View itemView)
        {
            super(itemView);
            mSource = itemView.findViewById(R.id.source);
            mDate = itemView.findViewById(R.id.date);
            mScore = itemView.findViewById(R.id.score);

        }//MessageViewHolder


    }//UserSpinViewHolder

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
        mUserScore = "wait...";
        mUserScore = getUserScoreFromBundle();
        mPoints.setText(mUserScore);
        readUserEarningDetails();


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
