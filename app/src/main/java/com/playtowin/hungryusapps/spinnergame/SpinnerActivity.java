package com.playtowin.hungryusapps.spinnergame;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.playtowin.hungryusapps.spinnergame.Authentication.LoginActivity;
import com.playtowin.hungryusapps.spinnergame.Fragments.SpinnerFragment;
import com.playtowin.hungryusapps.spinnergame.Interface.OnBackPressedListener;
import com.playtowin.hungryusapps.spinnergame.Utils.FirebaseMethods;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;


public class SpinnerActivity extends AppCompatActivity
{
    private static final String TAG = "SpinnerActivity";
    private Context mContext = SpinnerActivity.this;
    public static final String APP_PNAME = "com.playtowin.hungryusapps.spinnergame";

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
    FirebaseMethods firebaseMethods;

    //Navigation Drawer
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    private TextView mBackButton;
    private TextView mName;
    private String user_name;


    private void setUpToolbar()
    {
        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.profileToolBar);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name,R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }//setUpToolbar

    private void setupNavigationDrawer()
    {
        navigationView = findViewById(R.id.navigation_menu);
        View header = navigationView.inflateHeaderView(R.layout.header);
        mName = header.findViewById(R.id.user_name);
        mName.setText(user_name);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
        switch (item.getItemId())
        {
            case R.id.nav_home:
                startActivity(new Intent(mContext,MainActivity.class));
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                break;
            case R.id.nav_rate_app:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                break;
            case R.id.nav_feedback:
                Intent intent = new Intent(mContext,FeedbackActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                break;
            case R.id.nav_contact_us:
                startActivity(new Intent(mContext,ContactUsActivity.class));
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                break;
            case R.id.nav_terms_conditions:
                startActivity(new Intent(SpinnerActivity.this,TermsConditionsActivity.class));
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                break;
            case R.id.nav_about:
                startActivity(new Intent(SpinnerActivity.this,AboutActivity.class));
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                break;
            case R.id.nav_logout:
                Intent intent5 = new Intent(mContext, SignOutActivity.class);
                mContext.startActivity(intent5);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                break;
        }//switch

        return false;

    }//onNavigationItemSelected
});

        setUpToolbar();
    }//setupNavigationDrawer

    private void handleListenerOnBackButton()
    {
        mBackButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }//handleListenerOnBackArraow





    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner);

        mBackButton = findViewById(R.id.backButton);

        setupFirebaseAuth();
        init();
        setupNavigationDrawer();
        setUpToolbar();
        handleListenerOnBackButton();
    }//onCreate


    private void init()
    {

        Intent intent = getIntent();

        if(intent.hasExtra("user_score"))
        {
            try
            {
                Log.d(TAG, "init: Inflating Spinner Fragment");
                SpinnerFragment fragment = new SpinnerFragment();

                String user_score = intent.getStringExtra("user_score");
                user_name = intent.getStringExtra("user_name");

                Bundle bundle = new Bundle();
                bundle.putString("user_score",user_score);
                fragment.setArguments(bundle);


                android.support.v4.app.FragmentTransaction transaction = SpinnerActivity.this.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container,fragment);
                transaction.addToBackStack(getString(R.string.spinner_fragment));
                transaction.commit();
            }//try
            catch(Exception ex)
            {
                Log.d(TAG, "init: ERROR: "+ex.getMessage());
            }//catch
        }//if


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
            Intent intent = new Intent(SpinnerActivity.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        }//if
    }//checkCurrentUser


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
    }//onStop

    @Override
    public void onBackPressed()
    {
        Log.d(TAG, "onBackPressed: "+onBackPressedListener);

        if (onBackPressedListener != null)
        {
            Log.d(TAG, "onBackPressed: #####");
            finish();
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            onBackPressedListener.doBack();
        }
        else
            super.onBackPressed();

    }//onBackPressed

}//SpinnerActivity
